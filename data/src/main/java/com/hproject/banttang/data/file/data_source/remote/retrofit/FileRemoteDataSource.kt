package com.hproject.banttang.data.file.data_source.remote.retrofit

import com.hproject.banttang.data.file.data_source.remote.IFileRemoteDataSource
import com.hproject.banttang.data.file.model.ImageData
import com.hproject.banttang.data.network.model.BanttangSingleResponse
import com.hproject.core.data.data_source.remote.retrofit.AbstractBaseRetrofitRemoteDataSource
import okhttp3.MultipartBody
import javax.inject.Inject

class FileRemoteDataSource @Inject constructor(
    private val fileApi : IFileRetrofitApi
) : AbstractBaseRetrofitRemoteDataSource(), IFileRemoteDataSource{

    override suspend fun uploadImages(multipartFiles: List<MultipartBody.Part>): Result<BanttangSingleResponse<ImageData>?> =
        runWithHandlingResult { fileApi.uploadMultipartFiles(multipartFiles = multipartFiles) }

}