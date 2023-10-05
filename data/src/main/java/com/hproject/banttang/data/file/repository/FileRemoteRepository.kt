package com.hproject.banttang.data.file.repository

import com.hproject.banttang.data.file.data_source.remote.IFileRemoteDataSource
import com.hproject.banttang.data.file.model.mapper.toDomainEntity
import com.hproject.banttang.data.network.model.BanttangBaseResponse
import com.hproject.banttang.data.user.model.mapper.toDomainEntity
import com.hproject.banttang.domain.file.entity.Image
import com.hproject.banttang.domain.file.entity.ImageResponse
import com.hproject.banttang.domain.file.repository.IFileRepository
import com.hproject.banttang.domain.user.entity.UserInfoResponse
import com.hproject.core.extension.toEnumOrNull
import okhttp3.MultipartBody
import javax.inject.Inject

class FileRemoteRepository @Inject constructor(
    private val remoteDatasource : IFileRemoteDataSource
): IFileRepository {
    override suspend fun uploadImages(requestParam: List<MultipartBody.Part>): Result<ImageResponse<Image>> =
        remoteDatasource.uploadImages(requestParam).map {
            if(it?.meta?.result == BanttangBaseResponse.Meta.MetaResult.OK){
                ImageResponse(
                    result = ImageResponse.Result.GET_IMAGE_SUCCESS,
                    data = it.content?.toDomainEntity()
                )
            }else{
                ImageResponse(
                    result = it?.meta?.errorCode?.replace("-", "_")?.uppercase().toEnumOrNull<ImageResponse.Result>()
                )
            }
        }
}
