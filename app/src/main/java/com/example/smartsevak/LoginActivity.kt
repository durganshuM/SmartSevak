package com.example.smartsevak

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    lateinit var otpButton: Button
    lateinit var nameEditText: EditText
    lateinit var aadhaarEditText: EditText
    lateinit var phoneEditText: EditText
    lateinit var auth: FirebaseAuth
    lateinit var number: String
    lateinit var nameText: String
    lateinit var aadhaarNumber: String
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inIt()
        otpButton.setOnClickListener {
            number = phoneEditText.text.trim().toString()
            nameText = nameEditText.text.toString()
            aadhaarNumber = aadhaarEditText.text.toString()
            if (number.isNotEmpty()){
                if(number.length == 10){
                    number = "+91$number"

                    progressBar.visibility = View.VISIBLE

                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                }
                else
                    Toast.makeText(this,"Please Enter Valid Phone Number",Toast.LENGTH_SHORT).show()
            }

            else
                Toast.makeText(this,"Please Enter Phone Number",Toast.LENGTH_SHORT).show()
        }
    }

    private fun inIt() {
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE
        otpButton = findViewById(R.id.otpButton)
        nameEditText = findViewById(R.id.nameEditText)
        aadhaarEditText = findViewById(R.id.aadhaarEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        auth = FirebaseAuth.getInstance()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"Authentication Successful", Toast.LENGTH_SHORT).show()
                    sendToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    private fun sendToMain(){
        startActivity(Intent(this,MainActivity::class.java))
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.

            val intent = Intent(this@LoginActivity,OTPActivity::class.java)
            intent.putExtra("OTP",verificationId)
            intent.putExtra("resendToken", token)
            intent.putExtra("number", number)
            intent.putExtra("nameText", nameText)
            intent.putExtra("aadhaarNumber", aadhaarNumber)
            startActivity(intent)
            progressBar.visibility = View.INVISIBLE

            // Save verification ID and resending token so we can use them later
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}