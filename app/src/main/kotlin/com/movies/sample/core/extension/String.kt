package com.movies.sample.core.extension

fun String.Companion.empty() = ""

fun String.toNumericString() = this.filter { it.isDigit() }
