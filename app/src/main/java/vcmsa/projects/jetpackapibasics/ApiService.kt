package vcmsa.projects.jetpackapibasics

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    //Retrieve a list of the loans from the API
    @GET("/loans")
    suspend fun getLoans():List<LoanResponse>

    @GET
    suspend fun createLoan(@Body loanRequest: LoanRequest)
    // @Body tells retrofit to use this object as the JSON request body

    // We now expect a Response object that contains no body (Unit)
    @DELETE("/Loans/{id}")
    suspend fun deleteLoan(@Path("id") loanID: Int): Response<Unit>
}

object RetrofitClient {
    private const val BASE_URL = "https://opsc.azurewebsites.net/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        retrofit.create(ApiService::class.java)
    }
}