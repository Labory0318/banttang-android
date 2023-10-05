package com.hproject.banttang.data.file.data_source.remote.dummy

import com.hproject.banttang.data.file.data_source.remote.IFileRemoteDataSource
import com.hproject.banttang.data.file.model.ImageData
import com.hproject.banttang.data.network.model.BanttangBaseResponse
import com.hproject.banttang.data.network.model.BanttangSingleResponse
import com.hproject.banttang.data.user.model.UserInfoData
import okhttp3.MultipartBody

class FileDummyDataSource : IFileRemoteDataSource {

    override suspend fun uploadImages(multipartFiles: List<MultipartBody.Part>): Result<BanttangSingleResponse<ImageData>?> {
        val dummyUploadImagesData = BanttangSingleResponse(
            content = ImageData(
                images = ArrayList<String>().apply {
                    add("image")
                })
        )
        dummyUploadImagesData.meta?.result = BanttangBaseResponse.Meta.MetaResult.OK

        return Result.success(
            value = dummyUploadImagesData
        )
    }
}