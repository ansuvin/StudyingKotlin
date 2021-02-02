package com.example.pra08_imageupload

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("/upload")
    fun postImage(
        @Part image: MultipartBody.Part,
        @Part("upload") name: RequestBody
    ): Call<ResponseBody>
}