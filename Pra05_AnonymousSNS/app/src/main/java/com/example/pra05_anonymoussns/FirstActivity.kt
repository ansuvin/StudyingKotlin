package com.example.pra05_anonymoussns

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_first.*

class FirstActivity : AppCompatActivity() {

    val TAG = "FirstActivity"

    private val googleSignInIntent by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        GoogleSignIn.getClient(this, gso).signInIntent
    }

    companion object {
        const val RESULT_CODE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        btnSignIn.setOnClickListener {
            startActivityForResult(googleSignInIntent, RESULT_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
}