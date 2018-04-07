package com.gane.shoper.entity

data class BaseBean<T> (
        var code: Int = 0,
        var error: String? = null,
        var success: Boolean = true,
        var data: T? = null
)


data class BaseAryBean<T>(
        var code: Int = 0,
        var error: String? = null,
        var success: Boolean = true,
        var data: List<T>? = null
)