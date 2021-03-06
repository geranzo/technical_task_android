package com.geranzo.data.mapper

interface Mapper<in Input, out Output> {
    fun map(input: Input): Output
}