package com.education.android.myquizappmain

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.education.android.myquizappmain.utils.AuthUtils
import com.education.android.myquizappmain.utils.DisplayUtils
import com.education.android.myquizappmain.utils.NetworkUtils
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.stephentuso.welcome.WelcomeActivity
import com.stephentuso.welcome.WelcomeHelper
import java.util.*
import kotlin.properties.Delegates

private const val REQUEST_SIGN_UP = 0
private const val REQUEST_SIGN_IN = 1
private const val REQUEST_FORGOT_PASSWORD = 2
private const val REQUEST_SIGN_UP_NAME = 3
private const val PASSWORD_MINIMAL_LENGTH = 8

class AuthActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authEmail: String
    private lateinit var authPassword: String

    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var usernameLayout: TextInputLayout

    private lateinit var signinButton: MaterialButton
    private lateinit var signupButton: MaterialButton
    private lateinit var backButton: MaterialButton
    private lateinit var forgotPasswordButton: TextView

    private var isAuthorized by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        showWelcomeScreen()

        firebaseAuth = FirebaseAuth.getInstance()

        emailLayout = findViewById(R.id.email_layout)
        passwordLayout = findViewById(R.id.password_layout)
        usernameLayout = findViewById(R.id.username_layout)

        signinButton = findViewById(R.id.sign_in_button)
        signupButton = findViewById(R.id.sign_up_button)
        backButton = findViewById(R.id.back_button)
        forgotPasswordButton = findViewById(R.id.forgot_password_button)

        signinButton.setOnClickListener { onSignIn() }
        signupButton.setOnClickListener { onSignUp() }
        backButton.setOnClickListener { onBack() }
        forgotPasswordButton.setOnClickListener { onForgotPassword() }
    }

    override fun onBackPressed() {
        if (isAuthorized) {
            super.onBackPressed()
        }
    }

    private fun authSignIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                authorizeUser()
            } else {
                displayAuthError(task)
            }
        }
    }

    private fun authSignUp(email: String, password: String, username: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = firebaseAuth.currentUser
                if (user != null) {
                    val profileUpdates: UserProfileChangeRequest =
                        UserProfileChangeRequest.Builder().setDisplayName(username).build()
                    user.updateProfile(profileUpdates)
                    authorizeUser()
                }
            } else {
                onBack()
                displayAuthError(task)
            }
        }
    }

    private fun resetPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                DisplayUtils.showToast(
                    this,
                    getString(R.string.auth_restore_password) + " " + email
                )
            } else {
                displayAuthError(task)
            }
        }
    }

    private fun displayAuthError(task: Task<*>) {
        try {
            val e = task.exception
            if (e != null) throw e
        } catch (e: FirebaseAuthInvalidUserException) {
            emailLayout.error = getString(R.string.email_not_exist)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            passwordLayout.error = getString(R.string.wrong_password)
        } catch (e: FirebaseAuthUserCollisionException) {
            emailLayout.error = getString(R.string.email_already_exist)
        } catch (e: FirebaseTooManyRequestsException) {
            DisplayUtils.showToast(this, R.string.too_many_attempts)
        } catch (e: Exception) {
            DisplayUtils.showToast(this, R.string.network_not_available)
        }
    }

    private fun showWelcomeScreen() {
        val welcomeScreen = WelcomeHelper(this, WelcomeActivity::class.java)
        welcomeScreen.forceShow()
    }

    private fun getFieldText(parent: TextInputLayout): String {
        val field: EditText? = parent.editText
        return field?.text?.toString()?.trim()?.toLowerCase(Locale.ROOT) ?: ""
    }

    private fun isAuthCorrect(requestCode: Int): Boolean {
        DisplayUtils.hideKeyboard(this)
        if (NetworkUtils.isNetworkAvailable(this)) {
            if (requestCode == REQUEST_SIGN_UP ||
                requestCode == REQUEST_SIGN_IN || requestCode == REQUEST_FORGOT_PASSWORD) {
                authEmail = getFieldText(emailLayout)
                authPassword = getFieldText(passwordLayout)
                if (AuthUtils.isValidEmail(authEmail)) {
                    emailLayout.error = null
                    if (requestCode == REQUEST_FORGOT_PASSWORD || AuthUtils.isValidPassword(
                            authPassword, PASSWORD_MINIMAL_LENGTH)) {
                        passwordLayout.error = null
                        return true
                    } else {
                        passwordLayout.error = getString(R.string.password_is_too_short)
                    }
                } else {
                    emailLayout.error = getString(R.string.wrong_email)
                }
            } else if (requestCode == REQUEST_SIGN_UP_NAME) {
                return true
            }
        } else {
            DisplayUtils.showToast(this, R.string.network_not_available)
        }

        return false
    }

    private fun updateNameField(isVisible: Boolean) {
        if (isVisible) {
            emailLayout.visibility = View.GONE
            passwordLayout.visibility = View.GONE
            signinButton.visibility = View.GONE
            forgotPasswordButton.visibility = View.GONE
            usernameLayout.visibility = View.VISIBLE
            backButton.visibility = View.VISIBLE
        } else {
            emailLayout.visibility = View.VISIBLE
            passwordLayout.visibility = View.VISIBLE
            signinButton.visibility = View.VISIBLE
            forgotPasswordButton.visibility = View.VISIBLE
            usernameLayout.visibility = View.GONE
            backButton.visibility = View.GONE
        }
    }

    private fun onSignUp() {
        if (usernameLayout.visibility == View.GONE) {
            if (isAuthCorrect(REQUEST_SIGN_UP)) {
                updateNameField(true)
            }
        } else {
            val authUsername = getFieldText(usernameLayout).trim { it <= ' ' }
            if (isAuthCorrect(REQUEST_SIGN_UP_NAME) && authUsername.isNotEmpty()) {
                authSignUp(authEmail, authPassword, authUsername)
            } else {
                usernameLayout.error = getString(R.string.wrong_name)
            }
        }
    }

    private fun onSignIn() {
        if (isAuthCorrect(REQUEST_SIGN_IN)) {
            authSignIn(authEmail, authPassword)
        }
    }

    private fun onForgotPassword() {
        if (isAuthCorrect(REQUEST_FORGOT_PASSWORD)) {
            resetPassword(authEmail)
        }
    }

    private fun onBack() {
        DisplayUtils.hideKeyboard(this)
        updateNameField(false)
    }

    private fun authorizeUser() {
        isAuthorized = true
        onBackPressed()
    }

}