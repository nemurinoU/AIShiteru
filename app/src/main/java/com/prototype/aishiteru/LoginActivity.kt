package com.prototype.aishiteru

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.prototype.aishiteru.classes.CastItem
import com.prototype.aishiteru.databinding.ActivityLoginBinding
import io.github.muddz.styleabletoast.StyleableToast

class LoginActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val reqCode:Int=123

    /***
     *  This function starts the activity with the sign in intent
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleSignInClient.signOut()
        auth.signOut()


        // this section is for logging in
        binding.btnNext.setOnClickListener{
            // get user data
            try {
                val email = binding.loginEmail.getText().toString().trim()
                val pw = binding.loginPassword.getText().toString().trim()

                auth.signInWithEmailAndPassword(email, pw)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            //val user = auth.currentUser
                            Log.d(TAG, "signInWithEmail:success")
                            revealMain(email)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            StyleableToast.makeText(
                                baseContext,
                                getString(R.string.wrong_login),
                                Toast.LENGTH_SHORT,
                                R.style.warningToast
                            ).show()
                        }
                    }
            }
            catch (e: IllegalArgumentException) {
                StyleableToast.makeText(
                    baseContext,
                    getString(R.string.empty_logreg),
                    Toast.LENGTH_SHORT,
                    R.style.cautionToast
                ).show()
                Log.d(TAG, e.toString())
            }
            catch (e: Exception) {
                StyleableToast.makeText(
                    baseContext,
                    getString(R.string.empty_logreg),
                    Toast.LENGTH_SHORT,
                    R.style.cautionToast
                ).show()
                Log.d(TAG, e.toString())
            }
        }

        // this section is for going to signup
        binding.toSignup.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.toGoogle.setOnClickListener{
            // Toast.makeText(this,"Logging In", Toast.LENGTH_SHORT).show()
            signInWithGoogle()
        }

        //Firebase.auth.signOut()
    }


    /***
     *  This function starts the activity with the sign in intent
     */
    public override fun onStart() {
        super.onStart()

        // check if user is already signed in
        // if they are, then just go to main
        val currentUser = auth.currentUser

        val gCreds = GoogleSignIn.getLastSignedInAccount(this)
        if(gCreds!=null){
            revealMain(gCreds.email.toString())
            //startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            revealMain(currentUser.email.toString())
            //startActivity(intent)
        }
    }

    /***
     *  This function starts the activity with the sign in intent
     */
    private fun signInWithGoogle(){
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, reqCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==reqCode){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException){
            //Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show()
            val toast = StyleableToast.makeText(
                baseContext,
                "Sign-in failed!",
                Toast.LENGTH_SHORT,
                R.style.warningToast
            )
        }
    }

    private fun updateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                //account.email.toString() <--- the user's email
                //account.displayName.toString()) <--- the user's name
                val intent = Intent(this, MainActivity::class.java)
                val user = User(account.displayName.toString().split(" ")[0],
                                account.email.toString())

                storeUserData(user) // store it in the database

                revealMain(account.email.toString())
                //startActivity(intent)
                finish()
            }
        }
    }

    private fun storeUserData(user: User) {
        val docRef = db.collection("users").document(user.userId) // Assuming email is unique

        // Option 1: Replace the entire document
        docRef.set(user)
            .addOnSuccessListener { Log.d("Firestore", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("Firestore", "Error writing document", e) }

        // Option 2: Merge data into the document (if it exists)
        docRef.set(user, SetOptions.merge())
            .addOnSuccessListener { Log.d("Firestore", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("Firestore", "Error writing document", e) }
    }

    private fun revealMain (uid : String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("SESSION_UID", uid)
        startActivity(intent)
    }

    // Your User data class
    data class User(val name: String, val userId: String)
}