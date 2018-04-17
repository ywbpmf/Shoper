package com.gane.smartcheck.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.gane.smartcheck.bean.LoginBean

@Dao
interface LoginDao {

    @Query("SELECT * FROM t_login LIMIT 1")
    fun getLoginData() : LoginBean?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLoginData(loginBean: LoginBean)

}