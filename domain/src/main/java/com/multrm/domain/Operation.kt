package com.multrm.domain

interface Operation<T> {
    fun <R: Any> map(mapper: Mapper<R>): R

    interface Mapper<R: Any> {

        fun <T> mapSuccess(data: T): R

        fun mapError(exception: Exception): R
    }

    data class Success<T>(val data: T): Operation<T> {
        override fun <R : Any> map(mapper: Mapper<R>): R {
            return mapper.mapSuccess(data)
        }
    }

    data class Failure<T>(val exception: Exception): Operation<T> {
        override fun <R : Any> map(mapper: Mapper<R>): R {
            return mapper.mapError(exception)
        }
    }
}