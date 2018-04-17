package com.gane.smartcheck.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.gane.smartcheck.bean.LoginBean

@Database(entities = arrayOf(LoginBean::class), version = 1)
abstract class RoomDb : RoomDatabase() {

    companion object {
        private var roomDb: RoomDb? = null

        fun getInstance(appContext: Context): RoomDb {
            if (roomDb == null) {
                roomDb = Room.databaseBuilder(appContext, RoomDb::class.java, "smart_check.db")
                        .allowMainThreadQueries().build()
            }
            return roomDb!!
        }
    }

    abstract fun loginDao(): LoginDao

}

