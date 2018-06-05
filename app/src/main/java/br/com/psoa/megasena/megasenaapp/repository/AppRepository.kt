package br.com.psoa.megasena.megasenaapp.repository

import android.app.Application
import br.com.psoa.megasena.megasenaapp.data.User
import br.com.psoa.megasena.megasenaapp.data.UserDao
import android.os.AsyncTask
import android.util.Log
import java.sql.SQLDataException


//@Singleton
class AppRepository {

    private var userDao: UserDao?


    constructor(application: Application) {
        val db = AppDatabase.getDatabase(application)
        userDao = db?.userDao()
    }

    fun load(email: String, userLoadListener: UserLoadListener) {
        LoadUserAsyncTask(userDao, userLoadListener).execute(email)
    }

    fun save(user: User, userInsertListener: UserInsertListener) {
        SaveUserAsyncTask(userDao, userInsertListener).execute(user)
    }

    companion object {
        private class SaveUserAsyncTask internal constructor(private val asyncTaskDao: UserDao?,
                                                             private val userInsertListener: UserInsertListener) :
                AsyncTask<User, Void, Void>() {

            override fun doInBackground(vararg params: User): Void? {
                try {
                    asyncTaskDao?.insert(params[0])
                    userInsertListener.onUserInsert(UserInsertListener.InsertStatusCode.OK)
                } catch (e: SQLDataException) {
                    Log.e("PSOA", e.localizedMessage, e)
                    userInsertListener.onUserInsert(UserInsertListener.InsertStatusCode.FAIL)
                } catch (e: Exception) {
                    Log.e("PSOA", e.localizedMessage, e)
                    userInsertListener.onUserInsert(UserInsertListener.InsertStatusCode.FAIL)
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
    }
}

/**
 * Used by provide a user from the repository
 *
 * @author: psoa
 *
 */
interface UserLoadListener {

    /**
     * Load user from data source, if exist
     */
    fun onUserLoad(user: User?)
}

interface UserInsertListener {
    fun onUserInsert(statusCode: InsertStatusCode)

    enum class InsertStatusCode {
        OK,
        FAIL
    }
}