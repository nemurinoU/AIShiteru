package com.prototype.aishiteru

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.prototype.aishiteru.LoginActivity.User
import com.prototype.aishiteru.databinding.ActivityLoginBinding
import com.prototype.aishiteru.databinding.ActivitySignupBinding
import io.github.muddz.styleabletoast.StyleableToast

class SignupActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        */

        // This section focuses on account creation
        binding.btnNext.setOnClickListener{
            try {
                // get the data of the user
                val name = binding.registerUsername.getText().toString().split(" ")[0]
                val email = binding.registerEmail.getText().toString().trim()
                val pw = binding.registerPassword.getText().toString().trim()
                val cpw = binding.repeatPassword.getText().toString().trim()


                if (pw.equals(cpw, true)) {
                    // check if re-type pw was correct

                    // proceed with user creation
                    auth.createUserWithEmailAndPassword(email, pw)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                //val user = auth.currentUser

                                /*
                                val data : Map<String, String>  = HashMap<String, String>();
                                FirebaseFirestore.getInstance().collection("test").add(data);*/
                                // store the new google user in the database
                                val user = User(name, email)
                                storeUserData(user)

                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                StyleableToast.makeText(
                                    baseContext,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                    R.style.warningToast
                                ).show()
                            }
                        }
                } else {
                    // if not, throw error
                    StyleableToast.makeText(
                        baseContext,
                        getString(R.string.wrong_retype),
                        Toast.LENGTH_SHORT,
                        R.style.warningToast
                    ).show()
                }
            }
            catch (e: IllegalArgumentException) {
                StyleableToast.makeText(
                    baseContext,
                    getString(R.string.empty_logreg),
                    Toast.LENGTH_SHORT,
                    R.style.cautionToast
                ).show()
            }
            catch (e: Exception) {
                Log.d(TAG, e.toString())
            }
        }

    }

    public override fun onStart() {
        super.onStart()

        // check if user is already logged in
        // if they are, then just go to main activity
        val currentUser = auth.currentUser
        if (currentUser != null) {
            revealMain(currentUser.getEmail().toString())
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