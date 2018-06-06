package br.com.psoa.megasena.megasenaapp.repository

import android.app.Application
import br.com.psoa.megasena.megasenaapp.data.User
import br.com.psoa.megasena.megasenaapp.data.UserDao
import android.os.AsyncTask
import android.util.Log
import br.com.psoa.megasena.megasenaapp.data.Bet
import br.com.psoa.megasena.megasenaapp.data.BetDao
import java.sql.SQLDataException


//@Singleton
class AppRepository {

    private var userDao: UserDao?
    private var betDao: BetDao?


    constructor(application: Application) {
        val db = AppDatabase.getDatabase(application)
        userDao = db?.userDao()
        betDao = db?.betDao()
    }

    fun load(email: String, userLoadListener: UserLoadListener) {
        LoadUserAsyncTask(userDao, userLoadListener).execute(email)
    }

    fun save(user: User, InsertListener: InsertListener) {
        SaveUserAsyncTask(userDao, InsertListener).execute(user)
    }


    fun load(userId: String, userBetLoadListener: UserBetLoadListener) {
        LoadUserBetAsyncTask(betDao, userBetLoadListener).execute(userId)
    }

    fun save(bet: Bet, InsertListener: InsertListener) {
        SaveBetAsyncTask(betDao, InsertListener).execute(bet)
    }

    companion object {
        private class SaveUserAsyncTask internal constructor(private val asyncTaskDao: UserDao?,
                                                             private val insertListener: InsertListener) :
                AsyncTask<User, Void, Void>() {

            override fun doInBackground(vararg params: User): Void? {
                try {
                    asyncTaskDao?.insert(params[0])
                    insertListener.onUserInsert(InsertListener.InsertStatusCode.OK)
                } catch (e: SQLDataException) {
                    Log.e("PSOA", e.localizedMessage, e)
                    insertListener.onUserInsert(InsertListener.InsertStatusCode.FAIL)
                } catch (e: Exception) {
                    Log.e("PSOA", e.localizedMessage, e)
                    insertListener.onUserInsert(InsertListener.InsertStatusCode.FAIL)
                }
                return null
            }
        }
        private class SaveBetAsyncTask internal constructor(private val asyncTaskDao: BetDao?,
                                                             private val insertListener: InsertListener) :
                AsyncTask<Bet, Void, Void>() {

            override fun doInBackground(vararg params: Bet): Void? {
                try {
                    asyncTaskDao?.insert(params[0])
                    insertListener.onUserInsert(InsertListener.InsertStatusCode.OK)
                } catch (e: SQLDataException) {
                    Log.e("PSOA", e.localizedMessage, e)
                    insertListener.onUserInsert(InsertListener.InsertStatusCode.FAIL)
                } catch (e: Exception) {
                    Log.e("PSOA", e.localizedMessage, e)
                    insertListener.onUserInsert(InsertListener.InsertStatusCode.FAIL)
                }
                return null
            }
        }

        private class LoadUserAsyncTask internal constructor(
                private val asyncTaskDao: UserDao?
                , private val userLoadListener: UserLoadListener) :
                AsyncTask<String, Void, User>() {

            override fun doInBackground(vararg params: String): User? {
                try {
                    return asyncTaskDao?.load(params[0])
                } catch (e: SQLDataException) {
                    Log.e("PSOA", e.localizedMessage, e)
                } catch (e: Exception) {
                    Log.e("PSOA", e.localizedMessage, e)
                }
                return null
            }

            override fun onPostExecute(result: User?) {
                userLoadListener.onUserLoad(result)

            }
        }
        private class LoadUserBetAsyncTask internal constructor(
                private val asyncTaskDao: BetDao?
                , private val userBetLoadListener: UserBetLoadListener) :
                AsyncTask<String, Void, List<Bet>>() {

            override fun doInBackground(vararg params: String): List<Bet>? {
                try {
                    return asyncTaskDao?.loadByUser(params[0])
                } catch (e: SQLDataException) {
                    Log.e("PSOA", e.localizedMessage, e)
                } catch (e: Exception) {
                    Log.e("PSOA", e.localizedMessage, e)
                }
                return null
            }

            override fun onPostExecute(result: List<Bet>?) {
                userBetLoadListener.onUserBetLoad(result)

            }
        }
    }
}

interface UserLoadListener {

    fun onUserLoad(user: User?)
}

interface InsertListener {
    fun onUserInsert(statusCode: InsertStatusCode)

    enum class InsertStatusCode {
        OK,
        FAIL
    }
}

interface UserBetLoadListener {
    fun onUserBetLoad(bets: List<Bet>?)
}