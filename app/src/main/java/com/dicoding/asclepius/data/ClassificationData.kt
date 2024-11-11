package com.dicoding.asclepius.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ClassificationData(
    val label: String,
    val confidenceScore: String,
    val imageUri: Uri
) : Parcelable
