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
import com.google.firebase.ktx.Firebase
import com.prototype.aishiteru.databinding.ActivityLoginBinding
import com.prototype.aishiteru.databinding.ActivitySignupBinding
import io.github.muddz.styleabletoast.StyleableToast

class SignupActivity : AppCompatActivity() {
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
                                val user = auth.currentUser

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}