package com.oquefazer.other

data class Resorce<out T>(val status: Status, val data : T?, val message : String?) {
    companion object {
        fun <T> success(data : T?) : Resorce<T> {
            return Resorce(Status.SUCCESS, data, null)
        }

        fun <T>error(msg : String, data: T?) :  Resorce<T> {
            return Resorce(Status.ERROR, data, msg)
        }

        fun <T>loading(data : T?) : Resorce<T> {
            return Resorce(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}