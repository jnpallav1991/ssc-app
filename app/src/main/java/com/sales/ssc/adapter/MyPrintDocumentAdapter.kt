package com.sales.ssc.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.pdf.PrintedPdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.res.ResourcesCompat
import com.sales.ssc.R
import com.sales.ssc.response.ResponseByDate
import com.sales.ssc.response.ResponseById
import com.sales.ssc.utils.Constant
import java.io.FileOutputStream
import java.io.IOException


class MyPrintDocumentAdapter(
    val context: Context,
    val doseEntryList: ArrayList<ResponseById>?,
    val responseByDate: ResponseByDate?
) : PrintDocumentAdapter() {

    var myPdfDocument: PdfDocument? = null
    var pageHeight = 0
    var pageWidth = 0

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback?,
        metadata: Bundle?
    ) {

        myPdfDocument = PrintedPdfDocument(context, newAttributes!!)

        pageHeight = newAttributes.mediaSize!!.heightMils / 1000 * 72
        pageWidth = newAttributes.mediaSize!!.widthMils / 1000 * 72
        if (cancellationSignal!!.isCanceled) {
            callback!!.onLayoutCancelled()
            return
        }
        val builder = PrintDocumentInfo.Builder(Constant.PDF_NAME)
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .setPageCount(1)

        val info = builder.build()
        callback!!.onLayoutFinished(info, true)
    }

    override fun onWrite(
        pageRanges: Array<out PageRange>?,
        destination: ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback?
    ) {
        val newPage = PageInfo.Builder(
            pageWidth,
            pageHeight, 1
        ).create()

        val page = myPdfDocument!!.startPage(newPage)
        if (cancellationSignal!!.isCanceled) {
            callback!!.onWriteCancelled()
            myPdfDocument!!.close()
            myPdfDocument = null
            return
        }
        drawPage(page)
        myPdfDocument!!.finishPage(page)

        try {
            myPdfDocument!!.writeTo(
                FileOutputStream(
                    destination!!.fileDescriptor
                )
            )
        } catch (e: IOException) {
            callback!!.onWriteFailed(e.toString())
            return
        } finally {
            myPdfDocument!!.close()
            myPdfDocument = null
        }

        callback!!.onWriteFinished(pageRanges)
    }

    private fun drawBoldText(text:String,paint : Paint,canvas :Canvas,x_axis:Float,y_axis:Float)
    {
        val customTypeface = ResourcesCompat.getFont(context, R.font.quicksand_medium)
        paint.typeface = customTypeface
        canvas.drawText(
            text,
            x_axis, y_axis, paint
        )
    }
    private fun drawDefaultText(text:String,paint : Paint,canvas :Canvas,x_axis:Float,y_axis:Float)
    {
        val customTypeface = ResourcesCompat.getFont(context, R.font.quicksand)
        paint.typeface = customTypeface
        canvas.drawText(
            text,
            x_axis, y_axis, paint
        )
    }

    private fun drawHorizontalLine(paint : Paint,canvas :Canvas,startX:Float,  startY:Float,  stopX:Float,  stopY:Float)
    {
        val customTypeface = ResourcesCompat.getFont(context, R.font.quicksand_bold)
        paint.typeface = customTypeface
        canvas.drawLine(startX, startY, stopX, stopY, paint)
    }

    private fun drawPage(page: PdfDocument.Page) {
        val canvas = page.canvas
        var titleBaseLine = 25

        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 10f

        var x_axis = canvas.width / 2
        val start_x_axis = 12F
        //estimate start
        x_axis -= 20
        drawBoldText("Estimate",paint,canvas,x_axis.toFloat(),titleBaseLine.toFloat())

        drawHorizontalLine(paint,canvas,x_axis.toFloat(), 25F + 2F, (x_axis + 41).toFloat(), 25F + 2F)

        // stop

        //start
        paint.textSize = 9f
        titleBaseLine += 18

        drawBoldText("Name : ",paint,canvas,start_x_axis,titleBaseLine.toFloat())

        drawDefaultText("${responseByDate?.CustomerName.toString()} ${responseByDate?.City.toString()}",paint,canvas,45F,titleBaseLine.toFloat())

        titleBaseLine += 18

        drawBoldText("Phone : ",paint,canvas,start_x_axis,titleBaseLine.toFloat())

        drawDefaultText(responseByDate?.MobileNumber.toString(),paint,canvas,45F,titleBaseLine.toFloat())

        titleBaseLine += 18

        drawBoldText("Date : ",paint,canvas,start_x_axis,titleBaseLine.toFloat())




        if (responseByDate!!.isSalesResponse!!) {

            drawDefaultText(responseByDate.printSellingDate(),paint,canvas,45F,titleBaseLine.toFloat())
       } else {

            drawDefaultText(responseByDate.printReturningDate(),paint,canvas,45F,titleBaseLine.toFloat())
        }
        //end
        //start
        titleBaseLine += 4
        val endXAxis = canvas.width - 10
        canvas.drawLine(
            start_x_axis,
            titleBaseLine.toFloat(),
            endXAxis.toFloat(),
            titleBaseLine.toFloat(),
            paint
        )
        //end

        //start
        titleBaseLine += 10
        paint.textSize = 8f

        drawBoldText("Particular",paint,canvas,start_x_axis,titleBaseLine.toFloat())
        drawBoldText("Qty",paint,canvas,150F,titleBaseLine.toFloat())
        drawBoldText("Rate",paint,canvas,180F,titleBaseLine.toFloat())
        drawBoldText("Amount",paint,canvas,230F,titleBaseLine.toFloat())

        //end

        //start
        titleBaseLine += 4
        canvas.drawLine(
            start_x_axis,
            titleBaseLine.toFloat(),
            endXAxis.toFloat(),
            titleBaseLine.toFloat(),
            paint
        )
        //end

        //set All Items here

        titleBaseLine += 14
        paint.textSize = 7f


        if (doseEntryList != null) {
            for (products in doseEntryList) {
                val mTextPaint = TextPaint()
                mTextPaint.textSize = 7f
                val customTypeface = ResourcesCompat.getFont(context, R.font.quicksand)
                mTextPaint.typeface = customTypeface

                val textS = products.ProductName
                val builder =
                    StaticLayout.Builder.obtain(
                        textS,
                        0,
                        textS.length,
                        mTextPaint,
                        135
                    )
                        .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                        .setLineSpacing(0.0f, 01.0f)
                        .setIncludePad(false)
                        .setMaxLines(0)
                canvas.save()
                val myStaticLayout = builder.build()
                canvas.translate(start_x_axis, titleBaseLine.toFloat()-6)
                myStaticLayout.draw(canvas)
                canvas.restore()

                drawDefaultText("${products.Quantity}",paint,canvas,154F,titleBaseLine.toFloat())

                val singlePrice =products.Price/products.Quantity

                drawDefaultText("${singlePrice}",paint,canvas,181F,titleBaseLine.toFloat())

                //val total
                drawDefaultText("${products.Price}",paint,canvas,231F,titleBaseLine.toFloat())

                titleBaseLine += 20

            }
        }

        //start
        paint.textSize = 10f
        var endYAxis = canvas.height - 80
        canvas.drawLine(start_x_axis, endYAxis.toFloat(), endXAxis.toFloat(), endYAxis.toFloat(), paint)
        //end
        endYAxis += 12
        drawBoldText("TOTAL",paint,canvas,start_x_axis,endYAxis.toFloat())

        drawBoldText(responseByDate!!.TotalPrice.toString(),paint,canvas,230F,endYAxis.toFloat())
        //start
        endYAxis+= 5
        canvas.drawLine(start_x_axis, endYAxis.toFloat(), endXAxis.toFloat(), endYAxis.toFloat(), paint)
        //end

    }

}