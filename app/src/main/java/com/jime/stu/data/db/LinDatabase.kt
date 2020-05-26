package com.jime.stu.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.blankj.utilcode.util.Utils
import com.jime.stu.data.db.dao.HomeDao
import com.jime.stu.data.db.migration.MIGRATION
import com.jime.stu.network.entity.BannerBean
import com.jime.stu.network.entity.HomeListBean

/**
 *   @auther : Aleyn
 *   time   : 2020/03/12
 */
@Database(entities = [HomeListBean::class, BannerBean::class], version = 2, exportSchema = false)
abstract class LinDatabase : RoomDatabase() {

    abstract fun homeLocaData(): HomeDao


    companion object {
        fun getInstanse() = SingletonHolder.INSTANCE
    }

    private object SingletonHolder {
        val INSTANCE = Room.databaseBuilder(
                Utils.getApp(),
                LinDatabase::class.java,
                "lin_db"
            )
            .addMigrations(MIGRATION.MIGRATION_1_2)
            .build()
    }
}