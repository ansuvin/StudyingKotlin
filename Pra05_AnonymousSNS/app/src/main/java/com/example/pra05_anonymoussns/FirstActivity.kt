package com.example.pra05_anonymoussns

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.internal.Utility
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_first.*
import java.security.MessageDigest

class FirstActivity : AppCompatActivity() {

    val TAG = "FirstActivity"

    var loginMode =""

    // region GoogleLogin
    private val googleSignInIntent by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        GoogleSignIn.getClient(this, gso).signInIntent
    }

    companion object {
        const val RESULT_CODE = 10
    }
    //endregion

    // region FaceBookLogin
    lateinit var callbackManager : CallbackManager
    lateinit var auth: FirebaseAuth
    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        btnGoogleLogin.setOnClickListener {
            loginMode="Google"
            startActivityForResult(googleSignInIntent, RESULT_CODE)
        }

        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        btnFacebookLogin.setOnClickListener { loginMode = "FaceBook" }
        btnFacebookLogin.setReadPermissions("email")
        btnFacebookLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Log.e(TAG, "facebook: 성공 $result.")
                handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {
                Log.e(TAG, "cancel")
            }

            override fun onError(error: FacebookException?) {
                Log.e(TAG, "error $error")
                errorText.text = error.toString()
            }

        })

    }

    // region GoogleLogin


    private fun firebaseLogin(googleAccont: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(googleAccont.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Log.e(TAG, it.result?.user?.displayName.toString())
                    val intent = Intent(this@FirstActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e(TAG, "에러닷")
                }
            }
    }
    //endregion

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (loginMode) {
            "Google" -> {
                if (resultCode == Activity.RESULT_OK && requestCode == RESULT_CODE) {
                    val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                    result?.let {
                        if (it.isSuccess) {
                            it.signInAccount?.displayName //이름
                            it.signInAccount?.email //이메일
                            Log.e("Value", it.signInAccount?.email!!)
                            // 기타 등등
                            if (result.isSuccess) {
                                firebaseLogin(result.signInAccount!!)
                            } else {

                            }
                        } else  {
                            Log.e("Value", "error")
                            // 에러 처리
                        }
                    }
                }
            }

            "FaceBook" -> {
                callbackManager?.onActivityResult(requestCode, resultCode, data)
                showToast("$loginMode: onActivityResult")
            }

            else -> { }
        }

    }

    private fun handleFacebookAccessToken(token: AccessToken?) {
        Log.e(TAG, "handler: $token")
        val credential = FacebookAuthProvider.getCredential(token!!.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { it ->
                if (it.isSuccessful){
                    Log.e(TAG, "성공입니다~")
                    val intent = Intent(this@FirstActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e(TAG, "실패다 ${it.exception}")
                }
            }


    }



    fun showToast(str: String){
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }
    fun showToast(strId: Int){
        Toast.makeText(applicationContext, strId, Toast.LENGTH_SHORT).show()
    }
}