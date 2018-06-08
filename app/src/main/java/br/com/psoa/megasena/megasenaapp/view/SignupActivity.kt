package br.com.psoa.megasena.megasenaapp.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import br.com.psoa.megasena.megasenaapp.R
import br.com.psoa.megasena.megasenaapp.data.User
import br.com.psoa.megasena.megasenaapp.repository.AppRepository
import br.com.psoa.megasena.megasenaapp.repository.SaveListener

import kotlinx.android.synthetic.main.activity_signup.*

/**
 * Allow the register of new users
 *
 */
class SignupActivity : AppCompatActivity() {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mSignupTask: UserSignupTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        btSignup.setOnClickListener { signup() }
        lnLogin.setOnClickListener { openLoginActivity() }
    }

    private fun signup() {
        if (mSignupTask != null) {
            return
        }

        // Reset errors.
        etEmail.error = null
        etPassword.error = null
        etName.error = null

        // Store values at the time of the login attempt.
        val nameStr = etName.text.toString()
        val emailStr = etEmail.text.toString()
        val passwordStr = etPassword.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password
        if (!isPasswordValid(passwordStr)) {
            etPassword.error = getString(R.string.error_invalid_password)
            focusView = etPassword
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            etEmail.error = getString(R.string.error_field_required)
            focusView = etEmail
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            etEmail.error = getString(R.string.error_invalid_email)
            focusView = etEmail
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(nameStr)) {
            etName.error = getString(R.string.error_field_required)
            focusView = etName
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            mSignupTask = UserSignupTask(nameStr, emailStr, passwordStr)
            mSignupTask?.execute(null as Void?)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.trim().length >= 4
    }

    /**
     * Shows the progress UI and hides the signup form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(
                    android.R.integer.config_shortAnimTime).toLong()

            signup_form.visibility = if (show) View.GONE else View.VISIBLE
            signup_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            signup_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            signup_progress.visibility = if (show) View.VISIBLE else View.GONE
            signup_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            signup_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            signup_progress.visibility = if (show) View.VISIBLE else View.GONE
            signup_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    /**
     * Represents an asynchronous registration task used to sign up
     * the user.
     */
    inner class UserSignupTask internal constructor(private val mName: String,
                                                    private val mEmail: String,
                                                    private val mPassword: String) :
            AsyncTask<Void, Void, Void>(), SaveListener {

        private val repo = AppRepository(this@SignupActivity.application)
        private val user = User(mName, mEmail, mPassword)

        override fun onSave(statusCode: SaveListener.SaveStatusCode) {
            if (statusCode == SaveListener.SaveStatusCode.OK) {
                openLoginActivity()
            } else {
                //FIXME: Show a error message and keep in the register page
                Log.i("PSOA", "Unable to sign up")
                openLoginActivity()
            }
        }

        override fun doInBackground(vararg params: Void): Void? {

            repo.save(user, this)
            return null
        }

        override fun onCancelled() {
            mSignupTask = null
            showProgress(false)
        }
    }

    private fun openLoginActivity() {
        val intent = Intent(this@SignupActivity,
                LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
        finish()
    }

}
