package br.com.psoa.megasena.megasenaapp.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import java.util.*


@Entity(tableName = "users", indices = [Index(value = "email", unique = true)])

data class User(
        @PrimaryKey
        val id: String,
        @NonNull
        val name: String,
        @NonNull
        val email: String,
        @NonNull
        val password: String
) {
    @Ignore
    constructor(name: String,
                email: String,
                password: String) : this(UUID.randomUUID().toString(), name, email, password)
}