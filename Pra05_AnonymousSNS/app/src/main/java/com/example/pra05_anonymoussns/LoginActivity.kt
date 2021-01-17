package com.example.pra05_anonymoussns

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
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
            login()
        }

        btn_signUp.setOnClickListener {
            signUp()
        }
    }

    fun login() {
        email = et_email.text.toString()
        password = et_password.text.toString()

        if (isValidEmail() && isValidPasswd()){
            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            R.string.success_login,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            R.string.failed_login,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(TAG, it.exception.toString())
                    }
                }
        }
    }

    fun signUp() {
        email = et_email.text.toString()
        password = et_password.text.toString()

        if (isValidEmail() && isValidPasswd()){
            FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            R.string.success_signup,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            R.string.failed_signup,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(TAG, it.exception.toString())
                    }
                }
        }
    }

    // 이메일 유효성 검사
    private fun isValidEmail(): Boolean {
        if(email.isEmpty()){
            Toast.makeText(applicationContext, "이메일이 비어있습니다.", Toast.LENGTH_SHORT).show()
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(applicationContext, "이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // 비밀번호 유효성 검사
    private fun isValidPasswd(): Boolean {
        if (password.isEmpty()) {
            Toast.makeText(applicationContext, "비밀번호가 공백입니다.", Toast.LENGTH_SHORT).show()
            return false
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            Toast.makeText(applicationContext, "비밀번호 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}