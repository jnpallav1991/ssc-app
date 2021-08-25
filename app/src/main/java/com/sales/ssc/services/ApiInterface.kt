package com.sales.ssc.services


import com.sales.ssc.response.*
import com.sales.ssc.utils.Constant
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST(Constant.SubUrl.GET_PRODUCTS)
    fun searchProduct(@Body searchProduct: SearchProduct): Call<ResponseSscBody>

    @POST(Constant.SubUrl.INSERT_PRODUCT_MASTER)
    fun insertProductMaster(@Body insertProduct: InsertProduct): Call<ResponseSscBody>

    @POST(Constant.SubUrl.USER_LOGIN)
    fun userLogin(@Body userLogin: UserLogin): Call<ResponseSscBody>

    @POST(Constant.SubUrl.INSERT_SALE_DETAILS)
    fun checkoutProducts(@Body productCheckoutRequest: ProductCheckoutRequest): Call<ResponseSuccessfulBody>

    @POST(Constant.SubUrl.SCAN_PRODUCT)
    fun scanProductByQrCode(@Body sendProduct: SendProduct): Call<ResponseSscBody>

    @POST(Constant.SubUrl.GET_SALES_PRODUCT_BY_DATE)
    fun getSalesProductByDate(@Body searchDate: SearchDate): Call<ResponseSscBody>

    @POST(Constant.SubUrl.GET_SALES_PRODUCT_BY_ID)
    fun getSalesProductByID(@Body searchId: SearchId): Call<ResponseSscBody>

    @POST(Constant.SubUrl.INSERT_RETURN)
    fun returnProducts(@Body productReturnRequest: ProductReturnRequest): Call<ResponseSuccessfulBody>

    @POST(Constant.SubUrl.GET_SALES_RETURN_BY_DATE)
    fun getSalesReturnByDate(@Body searchDate: SearchDate): Call<ResponseSscBody>

    @POST(Constant.SubUrl.GET_SALES_RETURN_BY_ID)
    fun getSalesReturnByID(@Body searchId: SearchId): Call<ResponseSscBody>

    /*@GET(Constant.SubUrl.GET_LEARN_LIST)
    fun getLearnList(): Call<ArrayList<LearnListResponse>>*/

    /* @POST(Constant.SubUrl.DEVICE)
     fun sendDeviceDetail(@Body device: Device): Call<Device>

     @POST(Constant.SubUrl.DOSES)
     fun sendDeviceDoses(@Body doses: Doses): Call<ResponseBody>

     @POST(Constant.SubUrl.ASTHMA_DIARIES)
     fun sendAsthmaDiaryRecord(@Body asthmaDiaryRecord: AsthmaDiaryRecord): Call<ResponseBody>*/

}