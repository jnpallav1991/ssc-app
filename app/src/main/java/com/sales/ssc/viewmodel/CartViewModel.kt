package com.sales.ssc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sales.ssc.db.CartDetails
import com.sales.ssc.db.repository.CartDetailsRepository

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: CartDetailsRepository = CartDetailsRepository(application)
    private var allDoseEntry: LiveData<List<CartDetails>?>? = repository.getAllCart()

    fun insert(cartDetails: CartDetails): Long? {
        return repository.insert(cartDetails)
    }

    fun getAllCart(): LiveData<List<CartDetails>?>?
    {
        return allDoseEntry
    }

    fun getProductByCode(pCode: String): CartDetails? {
        return repository.getProductByCode(pCode)
    }

    fun updateProduct(pTotalPrice: Double, pQuantity: Int, id: Long) {
        return repository.updateProduct(pTotalPrice, pQuantity, id)
    }

    fun deleteProduct(id: Long) {
        return repository.deleteProduct(id)
    }

    fun deleteAllProduct() {
        return repository.deleteAllProduct()
    }

    /*fun getAsthmaDiaryInfoByDate(day: Date): AsthmaDiaryInfo? {
        return repository.getAsthmaDiaryInfoByDate(day)
    }

    fun updateAsthmaDiary(asthmaDiaryInfo: AsthmaDiaryInfo) {
        repository.updateAsthmaDiary(asthmaDiaryInfo)
    }

    fun updateDiary(cough: Int?, wheezing: Int?, nightTimeAwake: Int?, breathlessness: Int?, chestTightness: Int?, howsDay: Int?, additionalNote: String?, entrySynced: Boolean?, id: Long?) {
        repository.updateDiary(
            cough,
            wheezing,
            nightTimeAwake,
            breathlessness,
            chestTightness,
            howsDay,
            additionalNote,
            entrySynced,
            id
        )

    }


    fun getAsthmaDiaryListByMonth(startDate: Long, endDate: Long): List<AsthmaDiaryInfo?>? {
        return repository.getAsthmaDiaryList(startDate, endDate)

    }*/
}