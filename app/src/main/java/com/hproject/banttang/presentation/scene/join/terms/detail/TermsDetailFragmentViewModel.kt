package com.hproject.banttang.presentation.scene.join.terms.detail

import com.hproject.banttang.R
import com.hproject.banttang.base.presentation.scene.AbstractBanttangBaseViewModel
import com.hproject.banttang.domain.common.define.TermsTypeDefine
import com.hproject.core.presentation.scene.AbstractBaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class TermsDetailFragmentViewModel @AssistedInject constructor(
    @Assisted private val termsDetailArgument : TermsTypeDefine,
): AbstractBanttangBaseViewModel() {

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            termsTypeDefine: TermsTypeDefine
        ): AbstractBaseViewModel.Factory<TermsDetailFragmentViewModel> = object : AbstractBaseViewModel.Factory<TermsDetailFragmentViewModel>() {
            override fun getViewModel(): TermsDetailFragmentViewModel {
                return assistedFactory.create(termsTypeDefine = termsTypeDefine)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(termsTypeDefine: TermsTypeDefine): TermsDetailFragmentViewModel
    }

    fun getNavigationTitle() : Int =
         when(termsDetailArgument) {
            TermsTypeDefine.SERVICE -> R.string.service_terms
            TermsTypeDefine.PRIVACY_POLICY -> R.string.privacy_policy
            TermsTypeDefine.GPS -> R.string.gps_terms
        }

    fun onBackButtonClick(): Unit = navigateUp()

}