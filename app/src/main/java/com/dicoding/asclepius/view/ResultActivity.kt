package com.dicoding.asclepius.view

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.data.ClassificationData
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<ClassificationData>(
                EXTRA_RESULT,
                ClassificationData::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<ClassificationData>(EXTRA_RESULT)
        }

        result?.let {
            binding.resultImage.setImageURI(it.imageUri)
            binding.tvAnalyze.text = "${it.label}"
            binding.tvConfidence.text = "${it.confidenceScore}"
        }
    }

    companion object {
        const val EXTRA_RESULT = "extra_result"
    }
}
