package br.com.psoa.megasena.megasenaapp.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.ABORT
import android.arch.persistence.room.Query


@Dao
interface UserDao {

    @Insert(onConflict = ABORT)
    fun insert(user: User)

    @Query("SELECT * FROM users WHERE email Is :email")
    fun load(email: String): User

    @Query("SELECT * FROM users")
    fun loadAll(): List<User>

}