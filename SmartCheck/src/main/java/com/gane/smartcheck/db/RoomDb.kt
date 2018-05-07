package com.gane.smartcheck.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.gane.smartcheck.bean.LoginBean
import com.gane.smartcheck.bean.ProductBean

@Database(entities = arrayOf(LoginBean::class, ProductBean::class), version = 4)
abstract class RoomDb : RoomDatabase() {

    companion object {
        private var roomDb: RoomDb? = null

        fun getInstance(appContext: Context): RoomDb {
            if (roomDb == null) {
                roomDb = Room.databaseBuilder(appContext, RoomDb::class.java, "smart_check.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries().build()
            }
            return roomDb!!
        }
    }

    abstract fun loginDao(): LoginDao
    abstract fun productDao(): ProductDao

}




