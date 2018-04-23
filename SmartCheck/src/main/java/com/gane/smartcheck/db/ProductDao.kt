package com.gane.smartcheck.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.gane.smartcheck.bean.ProductBean

@Dao
interface ProductDao {

    @Query("SELECT * FROM t_product")
    fun getProductList() : List<ProductBean>

    @Insert
    fun insert(data: List<ProductBean>)
}