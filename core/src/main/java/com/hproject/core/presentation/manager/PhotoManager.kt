package com.hproject.core.presentation.manager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hproject.core.R
import com.hproject.core.presentation.extension.resize
import com.hproject.core.presentation.extension.rotate
import com.hproject.core.presentation.utils.PermissionUtils
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream


class PhotoManager(
    var onPhotoSelectedCallback: ((photo: ArrayList<Bitmap>) -> Unit)? = null,
    private val resize: Int = 4,
    private val selectMessage : String
) {
    companion object {
        /**
         * Bitmap to File
         *
         * @author hsjun
         * @since 2022/06/18
         **/
        fun convertBitmapToFile(
            context: Context,
            bitmap: Bitmap,
            filePath: String = "images",
            quality: Int = 100
        ): File? {
            val fileDirectory = File(context.filesDir, filePath)
            if (!fileDirectory.exists()) {
                val makeDirectoryResult = fileDirectory.mkdirs()
                Timber.i("makeDirectoryResult is $makeDirectoryResult")
            }
            val cacheFileName = "${fileDirectory.path}/photo-${System.currentTimeMillis()}.jpg"
            Timber.i("cacheFileName is $cacheFileName")
            var cacheFile: File? = null
            var out: FileOutputStream? = null
            try {
                cacheFile = File(cacheFileName)
                cacheFile.createNewFile()
                out = FileOutputStream(cacheFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
                Timber.i("fileSize is ${cacheFile.length()/1024}")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    out?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return cacheFile
        }
    }

    private var mContext: Context? = null

    private var mPhotoPickerLauncher: ActivityResultLauncher<Intent>? = null
    private var mPermissionLauncher: ActivityResultLauncher<Array<String>>? = null

    private var mCameraUri: Uri? = null

    /**
     * initialize photo picker & permission launcher
     * * need to call from onCreate or onStart
     *
     * @author hsjun
     * @since 2022/02/18
     **/
    fun init(
        activity: AppCompatActivity? = null,
        fragment: Fragment? = null
    ) {
        when {
            activity != null -> {
                mContext = activity

                mPhotoPickerLauncher = activity.registerForActivityResult(
                    // contract
                    ActivityResultContracts.StartActivityForResult(),
                    // callback method
                    this::onPhotoSelectedCallback
                )

                mPermissionLauncher = activity.registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions(),
                    this::onPermissionCallback
                )
            }
            fragment != null -> {
                mContext = fragment.context

                mPhotoPickerLauncher = fragment.registerForActivityResult(
                    // contract
                    ActivityResultContracts.StartActivityForResult(),
                    // callback method
                    this::onPhotoSelectedCallback
                )

                mPermissionLauncher = fragment.registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions(),
                    this::onPermissionCallback
                )
            }
        }
    }

    /**
     * open gallery
     * * callback is [onPhotoSelectedCallback]
     *
     * @author hsjun
     * @since 2022/02/18
     * @see onPhotoSelectedCallback
     **/
    fun openGallery(multiple: Boolean = false) {
        mPhotoPickerLauncher?.launch(Intent(Intent.ACTION_PICK).apply {
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple)
        }) ?: Timber.e("need to call init from onCreate")
    }

    /**
     * open camera & gallery picker
     *
     * @author hsjun
     * @since 2022/02/18
     **/
    fun openCameraAndGalleryChooser(multiple: Boolean = false) {
        mCameraUri = null

        // need to request camera permission
        if (!isCameraPermissionGranted()) {
            mPermissionLauncher?.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                )
                // exception handling: if permission launcher not initialized, open gallery
            ) ?: openGallery(multiple = multiple)

            return
        }

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            val fileName = "${System.currentTimeMillis()}.jpg"
            mCameraUri = Uri.fromFile(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName))
            putExtra(MediaStore.EXTRA_OUTPUT, mCameraUri)
        }

        val pickIntent = Intent(Intent.ACTION_PICK).apply {
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple)
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE)
        }

        val chooserIntent = Intent.createChooser(pickIntent, selectMessage).apply {
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(cameraIntent))
        }

        mPhotoPickerLauncher?.launch(chooserIntent) ?: Timber.e("need to call init from onCreate")
    }

    /**
     * photo selected callback from gallery or camera
     *
     * @author hsjun
     * @since 2022/02/18
     * @see openGallery
     **/
    private fun onPhotoSelectedCallback(result: ActivityResult) {
        Timber.i("code=${result.resultCode}, data=${result.data}")

        if (result.resultCode == Activity.RESULT_OK) {
            val resultData = result.data
            val multiSelectedPhotos = resultData?.clipData
            val singleSelectPhoto = resultData?.data
            val cameraPhoto = mCameraUri
            var photos: ArrayList<Bitmap>? = null

            mContext?.let { context ->
                when {
                    // multi select result
                    multiSelectedPhotos != null -> {
                        val itemCount = multiSelectedPhotos.itemCount
                        if (itemCount > 0) {
                            photos = arrayListOf()
                            for (index: Int in 0 until itemCount) {
                                multiSelectedPhotos.getItemAt(index)?.let { photo ->
                                    photo.uri.resize(context, resize)?.let { resizedBitmap ->
                                        resizedBitmap.rotate(context, photo.uri)?.let { rotateBitmap ->
                                            photos?.add(rotateBitmap)
                                        }
                                    }
                                }
                            }
                        } else {
                            Timber.w("selected item count is <= 0")
                        }
                    }
                    // single select result
                    singleSelectPhoto != null -> {
                        singleSelectPhoto.resize(context, resize)?.let { resizedBitmap ->
                            resizedBitmap.rotate(context, singleSelectPhoto)?.let { rotateBitmap ->
                                photos = arrayListOf(rotateBitmap)
                            }

                        }
                    }
                    cameraPhoto != null -> {
                        cameraPhoto.resize(context, resize)?.let { resizedBitmap ->
                            resizedBitmap.rotate(context, cameraPhoto)?.let { rotateBitmap ->
                                photos = arrayListOf(rotateBitmap)
                            }
                        }
                    }
                    else -> photos = null
                }
            }

            // notify gallery image pick result
            photos?.let {
                onPhotoSelectedCallback?.invoke(it)
            }
        }
    }

    /**
     * permission result callback
     *
     * @author hsjun
     * @since 2022/02/18
     **/
    private fun onPermissionCallback(result: Map<String, Boolean>) {
        Timber.i("result=$result")

        if (isCameraPermissionGranted()) {
            openCameraAndGalleryChooser()
        } else {
            openGallery()
        }
    }

    /**
     * check permission for open camera
     *
     * @author hsjun
     * @since 2022/02/18
     **/
    private fun isCameraPermissionGranted(): Boolean {
        return mContext?.let { context ->
            PermissionUtils.isAllPermissionGranted(
                context = context,
                permissions = arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        } ?: false
    }

}