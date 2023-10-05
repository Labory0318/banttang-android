package com.hproject.banttang.data.file.data_source.remote

import com.hproject.banttang.data.file.model.ImageData
import com.hproject.banttang.data.network.model.BanttangSingleResponse
import okhttp3.MultipartBody

interface IFileRemoteDataSource {

    suspend fun uploadImages(multipartFiles: List<MultipartBody.Part>) : Result<BanttangSingleResponse<ImageData>?>

}