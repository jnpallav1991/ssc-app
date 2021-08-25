package com.sales.ssc.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CartDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartItem(
        cartDetails: CartDetails
    ): Long?

    @Query("SELECT * FROM CartDetails")
    fun getCartDetails(): LiveData<List<CartDetails>?>?

    @Query("SELECT * FROM CartDetails where pCode =:pCode")
    fun getCartDetailByPCode(pCode: String): CartDetails?

    @Query("DELETE FROM CartDetails where id = :id")
    fun deleteProduct(id: Long)

    @Query("DELETE FROM CartDetails")
    fun deleteAllProduct()

    @Query("update CartDetails set pTPrice=:productTotalPrice,pQuantity=:productQuantity where id=:id")
    fun updateCart(
        productTotalPrice: Double,
        productQuantity: Int,
        id: Long?
    )
}