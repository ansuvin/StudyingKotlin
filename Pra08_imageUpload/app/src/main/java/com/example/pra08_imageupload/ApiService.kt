package com.example.pra08_imageupload

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("/upload")
    fun postImage(
        @Part image: MultipartBody.Part,
        @Part("upload") name: RequestBody
    ): Call<ResponseBody>

//    @GET("/uploads/{number}")
//    @Streaming
//    fun getImage(
//            @Path("number") number: String
//    ): Call<ResponseBody>
}