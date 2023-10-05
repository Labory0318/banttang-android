package com.hproject.banttang.presentation.scene.join.terms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hproject.banttang.base.presentation.scene.AbstractBanttangBaseViewModel
import com.hproject.banttang.domain.common.define.TermsTypeDefine
import com.hproject.banttang.domain.common.define.TermsTypeDefine.PRIVACY_POLICY
import com.hproject.banttang.domain.common.define.TermsTypeDefine.GPS
import com.hproject.banttang.domain.common.define.TermsTypeDefine.SERVICE
import com.hproject.banttang.presentation.scene.join.argument.JoinArgument
import com.hproject.core.presentation.scene.AbstractBaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber


class JoinTermsFragmentViewModel @AssistedInject constructor(
    @Assisted private val joinArgument: JoinArgument,
): AbstractBanttangBaseViewModel() {
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            joinArgument: JoinArgument
        ): AbstractBaseViewModel.Factory<JoinTermsFragmentViewModel> = object : AbstractBaseViewModel.Factory<JoinTermsFragmentViewModel>() {
            override fun getViewModel(): JoinTermsFragmentViewModel {
                return assistedFactory.create(joinArgument = joinArgument)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(joinArgument: JoinArgument): JoinTermsFragmentViewModel
    }

    sealed class JoinTermsEvent {
        data class MoveTermsDetailFragment(
            val termsTypeDefine : TermsTypeDefine
        ) : JoinTermsEvent()

        data class MoveJoinProfileFragment(
            val joinArgument: JoinArgument
        ) : JoinTermsEvent()
    }

    // 모두 동의
    private val _isAllTermsAgree: MutableLiveData<Boolean> = MutableLiveData(false)
    val isAllTermsAgree: LiveData<Boolean> get() = _isAllTermsAgree

    // 서비스 이용약관
    private val _serviceTermsAgree: MutableLiveData<Boolean> = MutableLiveData(false)
    val serviceTermsAgree: LiveData<Boolean> get() = _serviceTermsAgree

    // 개인 정보
    private val _privacyPolicyAgree: MutableLiveData<Boolean> = MutableLiveData(false)
    val privacyPolicyAgree: LiveData<Boolean> get() = _privacyPolicyAgree

    // 위치 기반 서비스 이용약관
    private val _gpsTermsAgree: MutableLiveData<Boolean> = MutableLiveData(false)
    val gpsTermsAgree: LiveData<Boolean> get() = _gpsTermsAgree

    private fun checkIsAllTermsAgree() {
        val isChecked = _serviceTermsAgree.value == true &&
                _privacyPolicyAgree.value == true &&
                _gpsTermsAgree.value == true

        Timber.i("isChecked=$isChecked")

        _isAllTermsAgree.value = isChecked
    }

    fun onAllCheckboxClick() {
        val isChecked = (_isAllTermsAgree.value ?: false).not()
        Timber.i("isChecked=$isChecked")

        _isAllTermsAgree.value = isChecked
        _serviceTermsAgree.value = isChecked
        _privacyPolicyAgree.value = isChecked
        _gpsTermsAgree.value = isChecked
    }

    fun onTermsChanged(terms: TermsTypeDefine, isChecked: Boolean) {
        Timber.i("terms=$terms, isChecked=$isChecked")

        when (terms) {
            SERVICE -> _serviceTermsAgree.value = isChecked
            PRIVACY_POLICY -> _privacyPolicyAgree.value = isChecked
            GPS -> _gpsTermsAgree.value = isChecked
        }
        checkIsAllTermsAgree()
    }

    fun onTermsClick(terms: TermsTypeDefine) {
        Timber.i("terms=$terms")
        sendEventToView(event = JoinTermsEvent.MoveTermsDetailFragment(termsTypeDefine = terms))
    }

    fun onBackButtonClick(): Unit = navigateUp()

    fun onTermsAgreeButtonClick() {
        sendEventToView(event = JoinTermsEvent.MoveJoinProfileFragment(joinArgument = joinArgument))
    }

}