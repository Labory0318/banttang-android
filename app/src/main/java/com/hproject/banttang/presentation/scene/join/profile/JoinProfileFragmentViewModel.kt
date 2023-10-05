package com.hproject.banttang.presentation.scene.join.profile

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hproject.banttang.BanttangApplication
import com.hproject.banttang.R
import com.hproject.banttang.base.presentation.scene.AbstractBanttangBaseViewModel
import com.hproject.banttang.domain.file.usecase.UpdateImageUseCase
import com.hproject.banttang.domain.user.entity.JoinResponse
import com.hproject.banttang.domain.user.entity.LoginResponse
import com.hproject.banttang.domain.user.request_param.JoinRequestParam
import com.hproject.banttang.domain.user.request_param.LoginRequestParam
import com.hproject.banttang.domain.user.request_param.UpdateProfileRequestParam
import com.hproject.banttang.domain.user.usecase.JoinUseCase
import com.hproject.banttang.domain.user.usecase.LoginUseCase
import com.hproject.banttang.domain.user.usecase.UpdateProfileUseCase
import com.hproject.banttang.presentation.scene.join.argument.JoinArgument
import com.hproject.core.domain.authorization.ITokenManager
import com.hproject.core.presentation.manager.PhotoManager
import com.hproject.core.presentation.scene.AbstractBaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
class JoinProfileFragmentViewModel @AssistedInject constructor(
    @Assisted private val joinArgument: JoinArgument,
    private val application :Application,
    private val tokenManager: ITokenManager,
    private val loginUseCase: LoginUseCase,
    private val joinUseCase: JoinUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val updateImageUseCase: UpdateImageUseCase
    ): AbstractBanttangBaseViewModel() {

    companion object {

        private const val FORM_DATA_FILE_NAME = "multipartFiles"
        private const val QUALITY = 80

        fun provideFactory(
            assistedFactory: Factory,
            joinArgument: JoinArgument
        ): AbstractBaseViewModel.Factory<JoinProfileFragmentViewModel> = object : AbstractBaseViewModel.Factory<JoinProfileFragmentViewModel>() {
            override fun getViewModel(): JoinProfileFragmentViewModel {
                return assistedFactory.create(joinArgument = joinArgument)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(joinArgument: JoinArgument): JoinProfileFragmentViewModel
    }

    sealed class JoinProfileEvent {
        object OpenImageMenu: JoinProfileEvent()
        object MoveToMainPage : JoinProfileEvent()
    }

    // 선택된 프로필 이미지
    private var selectedProfileImage: Bitmap? = null

    // 이름
    val name: MutableLiveData<String> = MutableLiveData(joinArgument.name)

    private fun join() {
        val name = name.value
        val imageUrl = joinArgument.imageUrl

        if (name.isNullOrEmpty()) {
            sendShowToastEvent(R.string.name_input_request)
            return
        }

        joinArgument.name = name
        joinArgument.imageUrl = imageUrl

        showScreenBlockProgress()

        viewModelScope.launch {
            joinUseCase.invoke(
                requestParam = JoinRequestParam(
                    provider = joinArgument.provider,
                    providerAccessToken = joinArgument.providerAccessToken,
                    userKey = joinArgument.userKey,
                    name = joinArgument.name,
                    imageUrl =joinArgument.imageUrl
                )
            ).onSuccess { response ->
                when (response.result) {
                    JoinResponse.Result.JOIN_SUCCESS -> {
                        login()
                    }
                    JoinResponse.Result.DUPLICATED_JOINING -> {
                        sendShowToastEvent(R.string.error_duplicate_join)
                    }
                    JoinResponse.Result.FAIL -> {
                        sendShowToastEvent(R.string.error_common)
                    }
                }
            }.onFailure {
                sendShowToastEvent(it.message?:application.getString(R.string.error_common))
            }.also {
                hideScreenBlockProgress()
            }
        }
    }

    /**
     * 로그인
     *
     * @author hsjun
     * @since 2022-06-28
     **/
    private fun login() {
        val fcmToken = tokenManager.getFcmToken()
        val accessToken = tokenManager.getAccessToken()
        if (fcmToken.isNullOrEmpty() && accessToken.isNullOrEmpty()) {
            Timber.e("fcmToken && accessToken is null or empty")
            return
        }
        showScreenBlockProgress()
        viewModelScope.launch {
            loginUseCase(
                requestParam = LoginRequestParam(
                    provider = joinArgument.provider,
                    providerAccessToken = accessToken!!,
                    fcmToken = fcmToken!!
                )
            ).onSuccess { response ->
                when (response.result) {
                    LoginResponse.Result.LOGIN_SUCCESS -> {
                        tokenManager.setAccessToken(response.data?.accessToken)
                        tokenManager.setRefreshToken(response.data?.refreshToken)
                        if (tokenManager?.isValidToken() == true) {
                            val selectedProfileImage = selectedProfileImage
                            if (selectedProfileImage != null) {
                                uploadImage(image = selectedProfileImage)
                            }
                            else {
                                sendEventToView(event = JoinProfileEvent.MoveToMainPage)
                            }
                        } else {
                            sendShowToastEvent(R.string.error_common)
                        }
                    }
                    LoginResponse.Result.AUTHENTICATION_FAIL,
                    LoginResponse.Result.USER_NOT_FOUND,
                    LoginResponse.Result.FAIL -> {
                        sendShowToastEvent(R.string.error_network)
                    }
                }
            }.onFailure {
                sendShowToastEvent(it.message?:application.getString(R.string.error_common))
            }.also {
                hideScreenBlockProgress()
            }
        }
    }

    /**
     * 프로필 이미지 업로드
     *
     * @author hsjun
     * @since 2022/06/28
     **/
    private fun uploadImage(image: Bitmap) {
        showScreenBlockProgress()
        viewModelScope.launch {
            updateImageUseCase.invoke(
                requestParam = getMultipart(images = arrayListOf(image))
            ).onSuccess {
                it.data?.images?.firstOrNull()?.let { imageUrl ->
                    updateProfile(imageUrl = imageUrl)
                }
            }.onFailure {
                sendEventToView(event = JoinProfileEvent.MoveToMainPage)
            }
        }
    }

    private fun getMultipart(images: ArrayList<Bitmap>) : ArrayList<MultipartBody.Part> =
        ArrayList<MultipartBody.Part>().apply {
            images.forEach { image ->
                val bitmapFile = PhotoManager.convertBitmapToFile(
                    context = application.applicationContext,
                    bitmap = image,
                    quality = QUALITY
                )
                bitmapFile?.let {
                    val fileName = bitmapFile.name
                    val requestFile: RequestBody = bitmapFile.asRequestBody(MultipartBody.FORM)
                    val body = MultipartBody.Part.createFormData(FORM_DATA_FILE_NAME, fileName, requestFile)
                    add(body)
                }
            }
        }


    private fun updateProfile(imageUrl: String) {
        val nickname = name.value
        if (nickname.isNullOrEmpty()) {
            Timber.e("invalid user nickname!!!")
            return
        }

        showScreenBlockProgress()

        viewModelScope.launch {
            updateProfileUseCase.invoke(
                requestParam = UpdateProfileRequestParam(
                    imageUrl = imageUrl,
                    nickname = nickname
                )
            ).onSuccess {
                hideScreenBlockProgress()
                sendEventToView(event = JoinProfileEvent.MoveToMainPage)
            }
        }
    }

    fun onPickImagesButtonClick() {
        sendEventToView(event = JoinProfileEvent.OpenImageMenu)
    }

    fun onPhotoSelectedCallback(images: ArrayList<Bitmap>) {
        if (images.isNotEmpty()) {
            selectedProfileImage = images.firstOrNull()
        }
    }
    fun onBackButtonClick(): Unit = navigateUp()

    fun onJoinButtonClick() {
        join()
    }

}