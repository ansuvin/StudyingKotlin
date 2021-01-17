package com.example.pra05_anonymoussns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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

    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this@FirstActivity, gso)

        btnSignIn.setOnClickListener {
            signIn()
        }
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        Log.e(TAG, "뭐라도 나와봐")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
                Log.e(TAG, account.email.toString())
            }catch (e: ApiException){
                Log.e(TAG, e.message.toString())
            }
        }
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { it ->
                if (it.isSuccessful){
                    showToast("성공?")
                    val user: FirebaseUser = auth.currentUser!!
                    Log.e(TAG, "성공?")
                    updateUI(user)
                } else {
                    showToast("실패")
                    Log.e(TAG, it.exception.toString())
                }
            }
    }

    fun updateUI(user : FirebaseUser) {
        if (user != null){
            val intent = Intent(this@FirstActivity, MainActivity::class.java)
            Log.e(TAG, "넘어간다ㅏㅏ")
            startActivity(intent)
            finish()
        }
    }

    fun showToast(str: String){
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }
    fun showToast(strId: Int){
        Toast.makeText(applicationContext, strId, Toast.LENGTH_SHORT).show()
    }
}