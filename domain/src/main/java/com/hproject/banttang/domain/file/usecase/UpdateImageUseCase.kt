package com.hproject.banttang.domain.file.usecase

import com.hproject.banttang.domain.file.repository.IFileRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UpdateImageUseCase @Inject constructor(
    private val repository : IFileRepository
){
    suspend operator fun invoke(requestParam: List<MultipartBody.Part>) = repository.uploadImages(requestParam = requestParam)

}