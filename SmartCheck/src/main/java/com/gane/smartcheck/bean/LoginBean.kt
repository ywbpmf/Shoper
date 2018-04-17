package com.gane.smartcheck.bean

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "t_login")
data class LoginBean(
        @PrimaryKey var userno: String,
        var name: String? = null,
        var state: Int = -1,
        var shopno: String = ""
) {
    constructor() : this("-1")
}