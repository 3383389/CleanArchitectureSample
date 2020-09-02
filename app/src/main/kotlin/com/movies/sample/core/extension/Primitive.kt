package com.movies.sample.core.extension

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt

fun Int.toDp() =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)

fun Float.toPx() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

fun Boolean?.falseIfNull() = this ?: false
