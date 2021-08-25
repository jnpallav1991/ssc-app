package com.sales.ssc.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sales.ssc.R
import com.sales.ssc.db.CartDetails
import com.sales.ssc.response.GetProducts
import com.sales.ssc.response.SendProduct
import com.sales.ssc.ui.scan.BottomSheetFragment
import com.sales.ssc.utils.LanguageHelper
import com.sales.ssc.utils.OnFragmentInteractionListener
import com.sales.ssc.utils.SharedPreferenceUtil
import com.sales.ssc.viewmodel.CartViewModel
import com.sales.ssc.viewmodel.ProductsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_scan.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


private const val PERMISSIONS_REQUEST_CODE = 10
private val PERMISSIONS_REQUIRED =
    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

class MainActivity : AppCompatActivity(), BottomSheetFragment.ItemClickListener,
    OnFragmentInteractionListener {

    private lateinit var navController: NavController
    private lateinit var cartViewModel: CartViewModel
    private var scanQrCode: Boolean = true
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var navGraph: NavGraph
    private lateinit var productsViewModel: ProductsViewModel

    companion object {

        var originalContext: Context? = null
        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun attachBaseContext(base: Context) {
        originalContext = base
        super.attachBaseContext(LanguageHelper.onAttach(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        firebaseAuth = FirebaseAuth.getInstance()

        //navController = findNavController(R.id.nav_host_fragment)
        val navHostFragment = nav_host_fragment as NavHostFragment
        val graphInflater = navHostFragment.navController.navInflater
        navGraph = graphInflater.inflate(R.navigation.mobile_navigation)
        navController = navHostFragment.navController

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)

        productsViewModel =
            ViewModelProviders.of(this).get(ProductsViewModel::class.java)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                // R.id.navigation_dashboard,
                R.id.navigation_product,
                R.id.navigation_scan,
                R.id.navigation_cart,
                R.id.navigation_sales,
                R.id.navigation_profile,
                R.id.navigation_return,
                R.id.navigation_summary
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottom_nav_view.setupWithNavController(navController)
        // Set up the ActionBar to stay in sync with the NavController
        //toolbar.setupWithNavController(navController,appBarConfiguration)

        if (hasPermissions(this)) {

        } else {
            // Request camera-related permissions
            requestPermissions(
                PERMISSIONS_REQUIRED,
                PERMISSIONS_REQUEST_CODE
            )
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.salesPrintFragment || destination.id == R.id.productsSearchFragment) {
                scanQrCode = false
                bottom_nav_view.visibility = View.VISIBLE
                invalidateOptionsMenu()
            } else if (destination.id == R.id.addProductFragment) {
                scanQrCode = false
                invalidateOptionsMenu()
                bottom_nav_view.visibility = View.GONE
            } else {
                scanQrCode = true
                bottom_nav_view.visibility = View.VISIBLE
                invalidateOptionsMenu()
            }
        }

        cartViewModel.getAllCart()!!.observe(this,
            Observer<List<CartDetails>?> { doseHistoryList ->
                if (doseHistoryList!!.isEmpty()) {

                    badgeCount(0)

                } else {

                    badgeCount(doseHistoryList.size)

                }

            })

        onStartActivity()
    }

    override fun badgeCount(count: Int) {
        setBadge(count)
    }

    override fun scanProductFromSearch(productCode: String) {
        onScanProduct(SendProduct(productCode))
    }

    private fun setBadge(count: Int) {

        if (count == 0) {
            bottom_nav_view.getBadge(R.id.navigation_cart)?.isVisible = false
        } else {
            bottom_nav_view.getBadge(R.id.navigation_cart)?.isVisible = true
            bottom_nav_view.getOrCreateBadge(R.id.navigation_cart).number = count
        }
    }

    private fun onStartActivity() {

        //val arrayList:ArrayList<String> = ArrayList()
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            //Toast.makeText(this,"SignIn first",Toast.LENGTH_SHORT).show()
            //navController.navigate(R.id.navigation_profile)
            bottom_nav_view.visibility = View.GONE
            scanQrCode = false

        } else {
            navGraph.startDestination = R.id.navigation_product
            bottom_nav_view.visibility = View.VISIBLE
            scanQrCode = true
            navController.graph = navGraph

        }
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        val scanItem = menu!!.findItem(R.id.navigation_scan)
        val languageMenu = menu!!.findItem(R.id.change_language)
        scanItem.isVisible = scanQrCode
        val language  = SharedPreferenceUtil(this).getString("SET_LANGUAGE","")
        if (language=="hi")
        {
            languageMenu.title = "English Language"
        }
        else
        {
            languageMenu.title = "Hindi Language"
        }

        return true

    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.navigation_scan -> {
            val intent = Intent(this, ScanActivity::class.java)
            startActivityForResult(intent, 1000)
            true
        }
        R.id.change_language -> {
            val language  = SharedPreferenceUtil(this).getString("SET_LANGUAGE","")
            when {
                language.isNullOrEmpty() -> {
                    LanguageHelper.setLanguage(this,"hi")
                    relaunch()
                    //invalidateOptionsMenu()
                }
                language=="hi" -> {
                    LanguageHelper.setLanguage(this,"en")
                    relaunch()
                    //invalidateOptionsMenu()
                }
                else -> {
                    LanguageHelper.setLanguage(this,"hi")
                    relaunch()
                   // invalidateOptionsMenu()
                }

            }
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1000 -> {
                if (data != null) {
                    val dataB = data.extras
                    val data1 = dataB!!.getString("data1")
                    //val cartDetails = Gson().fromJson(data1, CartDetails::class.java)
                    onScanProduct(SendProduct(data1))

                }
            }
        }
    }

    private fun onScanProduct(sendProduct: SendProduct) {
        productsViewModel.scanProduct(sendProduct)
            .observe(this, androidx.lifecycle.Observer { responseSscBody ->
                try {
                    if (responseSscBody != null) {
                        val type = object : TypeToken<ArrayList<GetProducts>>() {}.type
                        val l = Gson().toJson(responseSscBody.Data)
                        val productList = Gson().fromJson<ArrayList<GetProducts>>(l, type)

                        if (productList.isNotEmpty()) {
                            val product = productList[0]

                            val bottomSheetFragment = BottomSheetFragment.newInstance(product)
                            bottomSheetFragment.show(
                                supportFragmentManager,
                                bottomSheetFragment.tag
                            )
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            })
    }

    override fun onItemClick(getProducts: GetProducts) {

        doAsync {
            if (getProducts.ProductCode != null) {

                val cart = cartViewModel.getProductByCode(getProducts.ProductCode!!)
                if (cart == null) {

                    val cartDetails = CartDetails()
                    cartDetails.pCode = getProducts.ProductCode
                    cartDetails.pName = getProducts.ProductName
                    cartDetails.pPrice = getProducts.SellingPrice
                    cartDetails.pQuantity = getProducts.selectedQuantity
                    cartDetails.pTQuantity = getProducts.Quantity
                    cartDetails.pTotalPrice =
                        getProducts.selectedQuantity!! * getProducts.SellingPrice!!
                    cartViewModel.insert(cartDetails)
                } else {

                    cart.pQuantity = cart.pQuantity!! + getProducts.selectedQuantity!!
                    cart.pTotalPrice = cart.pQuantity!! * cart.pPrice!!

                    cartViewModel.updateProduct(
                        cart.pTotalPrice!!,
                        cart.pQuantity!!,
                        cart.id!!
                    )
                }
            }

            uiThread {
                //Toast.makeText(this@MainActivity, "Saved", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun signInSuccess() {
        onStartActivity()
    }

    private fun setAppLocale(localeCode: String) {
        val resources: Resources = resources
        val dm: DisplayMetrics = resources.displayMetrics
        val config: Configuration = resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(Locale(localeCode.toLowerCase()))
        } else {
            config.locale = Locale(localeCode.toLowerCase())
        }
        resources.updateConfiguration(config, dm)
    }

    fun relaunch() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        Runtime.getRuntime().exit(0)
        finish()
    }
}
