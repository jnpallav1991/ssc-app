package com.sales.ssc.db.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Query
import com.sales.ssc.db.CartDetails
import com.sales.ssc.db.CartDetailsDao
import com.sales.ssc.db.SscDatabase

class CartDetailsRepository(application: Application) {
    private var cartDetailsDao: CartDetailsDao

    private var cartDetailList: LiveData<List<CartDetails>?>? = null

    init {
        val database: SscDatabase = SscDatabase.getDatabase(
            application.applicationContext
        )
        cartDetailsDao = database.cartDetailsDao()
        cartDetailList = cartDetailsDao.getCartDetails()
    }

    fun insert(cartDetails: CartDetails): Long? {
        return cartDetailsDao.insertCartItem(cartDetails)
    }

    fun getProductByCode(pCode: String): CartDetails? {
        return cartDetailsDao.getCartDetailByPCode(pCode)
    }

    fun updateProduct(pTotalPrice:Double,pQuantity:Int,id:Long) {
        return cartDetailsDao.updateCart(pTotalPrice,pQuantity,id)
    }

    fun deleteProduct(id: Long) {
        return cartDetailsDao.deleteProduct(id)
    }

    fun deleteAllProduct() {
        return cartDetailsDao.deleteAllProduct()
    }

    fun getAllCart(): LiveData<List<CartDetails>?>?
    {
        return cartDetailList
    }


}