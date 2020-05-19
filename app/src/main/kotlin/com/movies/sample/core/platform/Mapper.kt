package com.movies.sample.core.platform

interface Mapper<I, O> {
    fun map(input: I): O
}