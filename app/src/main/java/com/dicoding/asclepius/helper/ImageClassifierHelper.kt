package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.dicoding.asclepius.R
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    private var confidenceThreshold: Float = 0.1f,
    private var maxOutputResults: Int = 1,
    private val tfliteModelName: String = "cancer_classification.tflite",
    private val context: Context,
    private val resultListener: ResultListener?
) {
    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    interface ResultListener {
        fun onError(error: String)
        fun onSucces(results: List<Classifications>?, interfaceTime: Long)
    }

    companion object {
        const val TAG = "ImageClassifierHelper"
    }

    private fun setupImageClassifier() {
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(confidenceThreshold)
            .setMaxResults(maxOutputResults)
            .setBaseOptions(BaseOptions.builder().setNumThreads(4).build())
            .build()

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context, tfliteModelName, options
            )
        } catch (e: IllegalStateException) {
            resultListener?.onError(context.getString(R.string.classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        val contentResolver = context.contentResolver
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, imageUri).copy(Bitmap.Config.ARGB_8888, true)
        }

        bitmap?.let {
            val imageProcessor = ImageProcessor.Builder().build()
            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(it))
            val startTime = SystemClock.uptimeMillis()
            val results = imageClassifier?.classify(tensorImage)
            val inferenceTime = SystemClock.uptimeMillis() - startTime

            resultListener?.onSucces(results, inferenceTime)
        } ?: run {
            resultListener?.onError(context.getString(R.string.classifier_failed))
        }
    }
}
