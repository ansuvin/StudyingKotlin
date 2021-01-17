package com.example.pra05_anonymoussns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    val TAG = "LoginActivity"

    // 로그인 방법들
    val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    // 비밀번호 정규식
    private val PASSWORD_PATTERN : Pattern = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$")

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_signIn.setOnClickListener {
            email = et_email.text.toString()
            password = et_password.text.toString()

            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        Toast.makeText(applicationContext, R.string.success_login, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, R.string.failed_login, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, it.exception.toString())
                    }
                }
        }

        btn_signUp.setOnClickListener {
            email = et_email.text.toString()
            password = et_password.text.toString()

            FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        Toast.makeText(applicationContext, R.string.success_signup, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, R.string.failed_signup, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, it.exception.toString())
                    }
                }
        }
    }


}