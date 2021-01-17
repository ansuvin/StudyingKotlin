package com.example.pra05_anonymoussns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_phone_auth.*
import java.util.concurrent.TimeUnit

class PhoneAuthActivity : AppCompatActivity() {

    val TAG = "PhoneAuthActivity"

    var phoneNumber = ""
    var authNum = ""

    var storedVerificationId = ""
    var resendToken : PhoneAuthProvider.ForceResendingToken? = null

    private lateinit var firebaseAuthSettings: FirebaseAuthSettings
    private lateinit var auth : FirebaseAuth
    private lateinit var credential: PhoneAuthCredential

    private val callbacks by lazy {
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(verifycationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                // SMS를 보냄
                showToast("인증코드가 전송되었습니다. 60초 이내에 입력해주세요!")
                storedVerificationId = verifycationId
                resendToken = token

                Log.e(TAG, "verifycationId: $verifycationId, token: $token")
                et_authNum.isEnabled = true
                tv_againNum.isEnabled = true
                btn_auth.isEnabled = true
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                showToast("인증코드가 전송되었습니다. 60초 이내에 입력해주세요!!!!!")
                authNum = p0.smsCode.toString()
                Log.e(TAG, "authNum: $authNum")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast("인증 실패")
                Log.e(TAG, p0.message.toString())
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("ko")

        firebaseAuthSettings = FirebaseAuth.getInstance().firebaseAuthSettings

        btn_req.setOnClickListener {
            phoneNumber = et_phone.text.toString()

            val options = PhoneAuthOptions.newBuilder()
                .setPhoneNumber("+82$phoneNumber")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build()

            if(!phoneNumber.isEmpty()){
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }

        btn_auth.setOnClickListener {
            // 인증 객체를 만들기
            credential = PhoneAuthProvider.getCredential(storedVerificationId, et_authNum.text.toString())
            auth.signInWithCredential(credential)
                .addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        showToast("인증성공입니다.")
                        val intent = Intent(this@PhoneAuthActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.e(TAG, it.exception.toString())
                        showToast("안됨")
                    }
                }
        }
    }

    fun showToast(str: String){
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }
}