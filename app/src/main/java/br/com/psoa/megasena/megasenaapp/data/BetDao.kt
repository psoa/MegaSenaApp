package br.com.psoa.megasena.megasenaapp.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.ABORT
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query


@Dao
interface BetDao {

    @Insert(onConflict = REPLACE)
    fun insert(bet: Bet)

    @Query("SELECT * FROM bets WHERE userId Is :userId")
    fun loadByUser(userId: String): List<Bet>


}