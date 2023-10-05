package com.hproject.banttang.domain.file.repository

import android.graphics.Bitmap
import com.hproject.banttang.domain.file.entity.Image
import com.hproject.banttang.domain.file.entity.ImageResponse
import okhttp3.MultipartBody

interface IFileRepository {

    suspend fun uploadImages(requestParam: List<MultipartBody.Part>) : Result<ImageResponse<Image>>

}