package br.com.psoa.megasena.megasenaapp.repository

import android.app.Application
import br.com.psoa.megasena.megasenaapp.data.User
import br.com.psoa.megasena.megasenaapp.data.UserDao
import android.os.AsyncTask
import android.util.Log
import br.com.psoa.megasena.megasenaapp.data.Bet
import br.com.psoa.megasena.megasenaapp.data.BetDao
import java.sql.SQLDataException

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

    fun save(user: User, listener: SaveListener) {
        SaveUserAsyncTask(userDao, listener).execute(user)
    }


    fun load(userId: String, userBetLoadListener: UserBetLoadListener) {
        LoadUserBetAsyncTask(betDao, userBetLoadListener).execute(userId)
    }

    fun save(bet: Bet, statusCode: SaveListener) {
        SaveBetAsyncTask(betDao, statusCode).execute(bet)
    }

    fun delete(bet: Bet, listener: DeleteListener) {
        DeleteBetAsyncTask(betDao, listener).execute(bet)
    }


    companion object {
        private class SaveUserAsyncTask internal constructor(private val asyncTaskDao: UserDao?,
                                                             private val insertListener: SaveListener) :
                AsyncTask<User, Void, Void>() {

            override fun doInBackground(vararg params: User): Void? {
                try {
                    asyncTaskDao?.insert(params[0])
                    insertListener.onSave(SaveListener.SaveStatusCode.OK)
                } catch (e: SQLDataException) {
                    Log.e("ROOM", e.localizedMessage, e)
                    insertListener.onSave(SaveListener.SaveStatusCode.FAIL)
                } catch (e: Exception) {
                    Log.e("ROOM", e.localizedMessage, e)
                    insertListener.onSave(SaveListener.SaveStatusCode.FAIL)
                }
                return null
            }
        }
        private class SaveBetAsyncTask internal constructor(private val asyncTaskDao: BetDao?,
                                                             private val insertListener: SaveListener) :
                AsyncTask<Bet, Void, Void>() {

            override fun doInBackground(vararg params: Bet): Void? {
                try {
                    asyncTaskDao?.insert(params[0])
                    insertListener.onSave(SaveListener.SaveStatusCode.OK)
                } catch (e: SQLDataException) {
                    Log.e("ROOM", e.localizedMessage, e)
                    insertListener.onSave(SaveListener.SaveStatusCode.FAIL)
                } catch (e: Exception) {
                    Log.e("ROOM", e.localizedMessage, e)
                    insertListener.onSave(SaveListener.SaveStatusCode.FAIL)
                }
                return null
            }
        }

        private class DeleteBetAsyncTask internal constructor(private val asyncTaskDao: BetDao?,
                                                            private val listener: DeleteListener) :
                AsyncTask<Bet, Void, Void>() {

            override fun doInBackground(vararg params: Bet): Void? {
                try {
                    asyncTaskDao?.delete(params[0])
                    listener.onDelete(DeleteListener.DeleteStatusCode.OK)
                } catch (e: SQLDataException) {
                    Log.e("ROOM", e.localizedMessage, e)
                    listener.onDelete(DeleteListener.DeleteStatusCode.FAIL)
                } catch (e: Exception) {
                    Log.e("ROOM", e.localizedMessage, e)
                    listener.onDelete(DeleteListener.DeleteStatusCode.FAIL)
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
                    Log.e("ROOM", e.localizedMessage, e)
                } catch (e: Exception) {
                    Log.e("ROOM", e.localizedMessage, e)
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
                    Log.e("ROOM", e.localizedMessage, e)
                } catch (e: Exception) {
                    Log.e("ROOM", e.localizedMessage, e)
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

interface SaveListener {
    fun onSave(statusCode: SaveStatusCode)

    enum class SaveStatusCode {
        OK,
        FAIL
    }
}

interface DeleteListener {
    fun onDelete(statusCode: DeleteStatusCode)

    enum class DeleteStatusCode {
        OK,
        FAIL
    }
}

interface UserBetLoadListener {
    fun onUserBetLoad(bets: List<Bet>?)
}