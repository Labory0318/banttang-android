package com.hproject.banttang.presentation.scene.join.profile

import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hproject.banttang.R
import com.hproject.banttang.databinding.JoinProfileFragmentBinding
import com.hproject.core.presentation.binding_adapter.ImageViewBindingAdapter.loadUrlAsync
import com.hproject.core.presentation.manager.PhotoManager
import com.hproject.core.presentation.scene.AbstractBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JoinProfileFragment : AbstractBaseFragment<JoinProfileFragmentBinding, JoinProfileFragmentViewModel>(inflate = JoinProfileFragmentBinding::inflate) {

    private val navArgs: JoinProfileFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: JoinProfileFragmentViewModel.Factory

    override val viewModel: JoinProfileFragmentViewModel by viewModels {
        JoinProfileFragmentViewModel.provideFactory(viewModelFactory, navArgs.joinArgument)
    }


    override fun onInitView() {
        setBinding()
        setUerProfileImage()
        setPhotoManager()
    }

    override fun onRegisterViewModelObserver() {
    }

    override fun onReceivedViewModelEvent(event: Any) {
        when (event) {
            is JoinProfileFragmentViewModel.JoinProfileEvent.MoveToMainPage -> {
//                getBaseActivity()?.startActivity(
//                        activity = MainActivity::class.java,
//                        needFinishCurrent = true
//                    )
            }
            is JoinProfileFragmentViewModel.JoinProfileEvent.OpenImageMenu -> {
                photoManager.openCameraAndGalleryChooser(multiple = false)
            }
        }
    }

    private lateinit var photoManager: PhotoManager

    private fun setBinding(){
        with(binding){
            joinProfileViewModel = viewModel
            lifecycleOwner  = this@JoinProfileFragment
        }
    }
    private fun setPhotoManager(){
        photoManager = PhotoManager(
            selectMessage = getString(R.string.select_image),
            onPhotoSelectedCallback = { images ->
            if (images.isNotEmpty()) {
                Glide.with(this)
                    .load(images.firstOrNull())
                    .centerCrop()
                    .circleCrop()
                    .into(binding.profileImageButton)
            }
            viewModel.onPhotoSelectedCallback(images = images)
        })
        photoManager.init(fragment = this)
    }

    private fun setUerProfileImage(){
        if (!navArgs.joinArgument.imageUrl.isNullOrEmpty()) {
            binding.profileImageButton.loadUrlAsync(
                url = navArgs.joinArgument.imageUrl,
                placeholder = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_profile_large_placeholder),
                circleCrop = true
            )
        }
    }

}