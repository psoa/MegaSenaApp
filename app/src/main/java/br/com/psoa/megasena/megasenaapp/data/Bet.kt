package br.com.psoa.megasena.megasenaapp.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import java.util.*


@Entity(tableName = "bets", indices = [(Index(value = "userId")), (Index(value = "lottery"))])

data class Bet(
        @PrimaryKey
        val id: String,
        @NonNull
        val dateOfBet: String, //YYYY-mm-dd
        @NonNull
        val numbers: String, //(n1,n2,n3)
        @NonNull
        val userId: String, //FIXME: Should be a foreign key (and will be)
        @NonNull
        val lottery: String //Concurso da mega (buscar uma boa traducao)
) {
    @Ignore
    constructor(dateOfBet: String,
                numbers: String,
                userId: String,
                lottery: String) : this(
            UUID.randomUUID().toString(),
            dateOfBet,
            numbers,
            userId,
            lottery)


}