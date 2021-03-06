package com.example.pra05_anonymoussns

import android.content.Intent
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
                        if (FirebaseAuth.getInstance().currentUser?.isEmailVerified!!){
                            showToast("인증받은 사용자 입니다.")
                            val intent = Intent(this@LoginActivity, PhoneAuthActivity::class.java)
                            startActivity(intent)
                        } else {
                            showToast("인증받지 않은 사용자 입니다.")
                        }
                    } else {
                        showToast(R.string.failed_login)
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
                        emailAuthentication()
                        showToast(R.string.success_signup)
                    } else {
                        showToast(R.string.failed_signup)
                        Log.e(TAG, it.exception.toString())
                    }
                }
        }
    }

    fun emailAuthentication(){
        FirebaseAuth.getInstance().currentUser
            ?.sendEmailVerification()
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("이메일로 들어가 인증을 진행해 주세요")
                } else {
                    showToast("실패")
                    Log.e(TAG, "err: ${it.exception.toString()}")
                }
            }
    }

    // 이메일 유효성 검사
    private fun isValidEmail(): Boolean {
        if(email.isEmpty()){
            showToast("이메일이 비었습니다.")
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            showToast("이메일 형식이 아닙니다.")
            return false
        }
        return true
    }

    // 비밀번호 유효성 검사
    private fun isValidPasswd(): Boolean {
        if (password.isEmpty()) {
            showToast("비밀번호가 공백입니다.")
            return false
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            showToast("비밀번호 형식이 아닙니다.")
            return false
        }
        return true
    }

    fun showToast(str: String){
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }
    fun showToast(strId: Int){
        Toast.makeText(applicationContext, strId, Toast.LENGTH_SHORT).show()
    }

}