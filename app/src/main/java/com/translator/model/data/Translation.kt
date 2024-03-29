package com.translator.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Translation(@field:SerializedName("text") val translation: String? = "") : Parcelable