package com.example.doosen.network

import com.example.doosen.model.*
import retrofit2.Response
import retrofit2.http.*



interface ApiServiceRetrofit {

    @FormUrlEncoded
    @POST("realms/dev/protocol/openid-connect/token")
    suspend fun loginDosen(
        @Field("grant_type") grantType: String = "password",
        @Field("client_id") clientId: String = "setoran-mobile-dev",
        @Field("client_secret") clientSecret: String = "aqJp3xnXKudgC7RMOshEQP7ZoVKWzoSl",
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("scope") scope: String = "openid profile email"
    ): LoginResponse

    @GET("dosen/pa-saya")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<UserInfoResponse>


    @GET("mahasiswa/setoran/{nim}")
    suspend fun getDetailMahasiswa(
        @Header("Authorization") token: String,
        @Path("nim") nim: String
    ): Response<DetailMahasiswaResponse>

    @POST("mahasiswa/setoran/{nim}")
    suspend fun simpanSetoran(
        @Header("Authorization") token: String,
        @Path("nim") nim: String,
        @Body body: SimpanSetoranRequest
    ): Response<SimpanSetoranResponse>

    @HTTP(method = "DELETE", path = "mahasiswa/setoran/{nim}", hasBody = true)
    suspend fun hapusSetoran(
        @Header("Authorization") token: String,
        @Path("nim") nim: String,
        @Body body: HapusSetoranRequest
    ): Response<SimpanSetoranResponse>


    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("refresh_token") refreshToken: String
    ): TokenResponse

}
