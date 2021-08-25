package com.sales.ssc.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.sales.ssc.R
import com.sales.ssc.ui.scan.LumaListener
import com.sales.ssc.utils.LanguageHelper
import kotlinx.android.synthetic.main.activity_scan.*
import kotlinx.android.synthetic.main.camera_ui_container.*
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class ScanActivity : AppCompatActivity(), LumaListener {

    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService

    companion object {
        private const val TAG = "ScanActivity"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LanguageHelper.onAttach(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        cameraExecutor = Executors.newSingleThreadExecutor()
        view_finder.post {

            updateCameraUi()
            startCamera()
        }
    }


    private fun updateCameraUi() {

        // Inflate a new view containing all UI for controlling the camera
        View.inflate(this, R.layout.camera_ui_container, container)
        camera_close_button.setOnClickListener {
            // Shut down our background executor
            cameraExecutor.shutdown()
            finish()
        }
    }

    private fun startCamera() {

        // Build the viewfinder use case
        // val preview = Preview(previewConfig)
        // Preview
        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also { view_finder.display.getRealMetrics(it) }
        Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")
        val rotation = view_finder.display.rotation

        // Bind the CameraProvider to the LifeCycleOwner
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {

            // CameraProvider
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation
                .setTargetRotation(rotation)
                .build()

            // Attach the viewfinder's surface provider to preview use case
            preview.setSurfaceProvider(view_finder.previewSurfaceProvider)

            // ImageCapture
            val imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                // We request aspect ratio but no resolution to match preview config, but letting
                // CameraX optimize for whatever specific resolution best fits our use cases
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)
                .build()

            // ImageAnalysis
            val imageAnalyzer = ImageAnalysis.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer(this, this@ScanActivity))
                }

            // Must unbind the use-cases before rebinding them
            cameraProvider.unbindAll()

            try {
                // A variable number of use-cases can be passed here -
                // camera provides access to CameraControl & CameraInfo
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private class LuminosityAnalyzer(val context: Activity, val listener: LumaListener? = null) :
        ImageAnalysis.Analyzer {
        private var lastAnalyzedTimestamp = 0L
        private val frameRateWindow = 8
        private val frameTimestamps = ArrayDeque<Long>(5)
        var framesPerSecond: Double = -1.0
            private set
        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }

        /**
         * Get the angle by which an image must be rotated given the device's current
         * orientation.
         */
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Throws(CameraAccessException::class)
        private fun getRotationCompensation(activity: Activity): Int {
            // Get the device's current rotation relative to its "native" orientation.
            // Then, from the ORIENTATIONS table, look up the angle the image must be
            // rotated to compensate for the device's rotation.
            val deviceRotation = activity.windowManager.defaultDisplay.rotation
            var rotationCompensation = ORIENTATIONS.get(deviceRotation)

            // On most devices, the sensor orientation is 90 degrees, but for some
            // devices it is 270 degrees. For devices with a sensor orientation of
            // 270, rotate the image an additional 180 ((270 + 270) % 360) degrees.
            val cameraManager = activity.getSystemService(CAMERA_SERVICE) as CameraManager
            for (cameraId in cameraManager.cameraIdList) {
                val sensorOrientation = cameraManager
                    .getCameraCharacteristics(cameraId)
                    .get(CameraCharacteristics.SENSOR_ORIENTATION)!!
                rotationCompensation = (rotationCompensation + sensorOrientation + 270) % 360
            }

            // Return the corresponding FirebaseVisionImageMetadata rotation value.
            val result: Int
            when (rotationCompensation) {
                0 -> result = FirebaseVisionImageMetadata.ROTATION_0
                90 -> result = FirebaseVisionImageMetadata.ROTATION_90
                180 -> result = FirebaseVisionImageMetadata.ROTATION_180
                270 -> result = FirebaseVisionImageMetadata.ROTATION_270
                else -> {
                    result = FirebaseVisionImageMetadata.ROTATION_0
                    Log.e(TAG, "Bad rotation value: $rotationCompensation")
                }
            }
            return result
        }


        private fun degreesToFirebaseRotation(degrees: Int): Int = when (degrees) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
        }

        /**
         * Helper extension function used to extract a byte array from an
         * image plane buffer
         */
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {

            // Calculate the average luma no more often than every second
            // Keep track of frames analyzed
            val currentTime = System.currentTimeMillis()
            frameTimestamps.push(currentTime)

            // Compute the FPS using a moving average
            while (frameTimestamps.size >= frameRateWindow) frameTimestamps.removeLast()
            val timestampFirst = frameTimestamps.peekFirst() ?: currentTime
            val timestampLast = frameTimestamps.peekLast() ?: currentTime
            framesPerSecond = 2.0 / ((timestampFirst - timestampLast) /
                    frameTimestamps.size.coerceAtLeast(1).toDouble()) * 2000.0

            // Analysis could take an arbitrarily long amount of time
            // Since we are running in a different thread, it won't stall other use cases

            lastAnalyzedTimestamp = frameTimestamps.first

            val c = getRotationCompensation(context)
            Log.d("scan barcode 1 ", "barcode")

            val mediaImage = image.image
            if (mediaImage != null) {
                val imageVision = FirebaseVisionImage.fromMediaImage(mediaImage, c)
                // Call all listeners with new value
                listener!!.scanBarcode(imageVision)
            }

            image.close()
        }
    }


    override fun scanBarcode(imageVision: FirebaseVisionImage) {
        sendToBarcodeScan(imageVision)
    }

    private fun sendToBarcodeScan(imageVision: FirebaseVisionImage) {

        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(
                FirebaseVisionBarcode.FORMAT_QR_CODE,
                FirebaseVisionBarcode.FORMAT_AZTEC
            )
            .build()

        val visionDetector = FirebaseVision.getInstance()
            .getVisionBarcodeDetector(options)

        visionDetector.detectInImage(imageVision)
            .addOnSuccessListener { barcodes ->
                // Task completed successfully
                // ...
                for (barcode in barcodes) {


                    val bounds = barcode.boundingBox
                    val corners = barcode.cornerPoints

                    val rawValue = barcode.rawValue

                    val valueType = barcode.valueType

                    try {
                        Log.d("scan barcode Value", rawValue)
                        Log.d("scan barcode Value", "" + valueType)

                        val intent = Intent()
                        //intent.putExtra("data",cartDetails)
                        intent.putExtra("data1", rawValue)
                        setResult(Activity.RESULT_OK, intent)
                        cameraExecutor.shutdown()
                        finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
            .addOnFailureListener {
                // Task failed with an exception
                // ...
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}
