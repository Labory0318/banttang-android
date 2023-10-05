package com.hproject.banttang.data.file.data_source.remote.retrofit

import com.hproject.banttang.data.file.model.ImageData
import com.hproject.banttang.data.network.model.BanttangBaseResponse
import com.hproject.banttang.data.network.model.BanttangSingleResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface IFileRetrofitApi {

    companion object {
        private const val AWS_API = "/api/aws"
    }

    @Multipart
    @POST(AWS_API)
    fun uploadMultipartFiles(@Part multipartFiles: List<MultipartBody.Part>): Response<BanttangSingleResponse<ImageData>>

}