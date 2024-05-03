package com.nutapos.testandroid2.presentation.photo

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.nutapos.testandroid2.R
import com.nutapos.testandroid2.databinding.ActivityTakePhotoBinding
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class TakePhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTakePhotoBinding

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var cameraSelector: CameraSelector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startCamera()
        with(binding) {
            toggleFlash.setOnClickListener {
                setFlash(camera)
            }
            capture.setOnClickListener {
                takePhoto()
            }
        }
    }

    private fun startCamera() {
        val listenableFuture = ProcessCameraProvider.getInstance(this)
        listenableFuture.addListener({
            cameraProvider = listenableFuture.get()
            bindCameraUserCase()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUserCase() {
        val aspectRatio = aspectRatio(binding.cameraPreview.width, binding.cameraPreview.height)
        val rotation = binding.cameraPreview.display.rotation
        val resolutionSelector = ResolutionSelector.Builder()
            .setAspectRatioStrategy(
                AspectRatioStrategy(aspectRatio, AspectRatioStrategy.FALLBACK_RULE_AUTO)
            )
            .build()

        val preview = Preview.Builder()
            .setResolutionSelector(resolutionSelector)
            .setTargetRotation(rotation)
            .build()
            .also {
                it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setResolutionSelector(resolutionSelector)
            .setTargetRotation(rotation)
            .build()

        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height) / min(width, height)
        return if (abs(previewRatio - (4.0 / 3.0)) <= abs(previewRatio - (16.0 / 9.0)))
            AspectRatio.RATIO_4_3
        else AspectRatio.RATIO_16_9
    }

    private fun setFlash(camera: Camera) {
        if (camera.cameraInfo.hasFlashUnit()) {
            if (camera.cameraInfo.torchState.value == 0) {
                camera.cameraControl.enableTorch(true)
                binding.toggleFlash.setImageResource(R.drawable.ic_baseline_flash_off)
                return
            }
            camera.cameraControl.enableTorch(false)
            binding.toggleFlash.setImageResource(R.drawable.ic_baseline_flash_on)
            return
        }
        Toast.makeText(this, "Flash tidak tersedia", Toast.LENGTH_SHORT).show()
        binding.toggleFlash.isEnabled = false
    }

    private fun takePhoto() {
        val name = SimpleDateFormat("yyyMMddHHmmss", Locale("in", "ID"))
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$name.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "images/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Bukti")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                else MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object: ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent().apply {
                        putExtra(RESULT_IMAGE_CAPTURE, output.savedUri.toString())
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@TakePhotoActivity, exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    companion object {
        const val RESULT_IMAGE_CAPTURE = "result_image_capture"
    }
}