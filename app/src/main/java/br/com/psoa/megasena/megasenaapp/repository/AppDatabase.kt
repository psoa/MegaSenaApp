package br.com.psoa.megasena.megasenaapp.repository

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import br.com.psoa.megasena.megasenaapp.data.User
import br.com.psoa.megasena.megasenaapp.data.UserDao
import android.arch.persistence.room.Room


@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        private var _instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (_instance == null) {
                synchronized(AppDatabase::class.java) {
                    if (_instance == null) {
                        _instance = Room.databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java,
                                "megasena_database")
                                .build();
                    }
                }
            }
            return _instance
        }

        fun destroyInstance() {
            _instance = null
        }
    }

}