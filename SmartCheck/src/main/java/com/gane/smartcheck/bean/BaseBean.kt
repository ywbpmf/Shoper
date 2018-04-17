package com.gane.smartcheck.bean

data class BaseBean<T> (
        var code: Int = 0,
        var success: Boolean = false,
        var error: String? = null,
        var data: T
)