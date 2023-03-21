package com.example.smartsevak

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {
    lateinit var otpEditText: EditText
    private lateinit var signupButton: Button
    lateinit var resendTextView: TextView
    lateinit var auth: FirebaseAuth
    private lateinit var otp: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    lateinit var phoneNumber: String
    lateinit var nameText: String
    lateinit var aadhaarText: String
    lateinit var otpProgressBar: ProgressBar
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)

        otp = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        phoneNumber = intent.getStringExtra("number").toString()
        nameText = intent.getStringExtra("nameText").toString()
        aadhaarText = intent.getStringExtra("aadhaarNumber").toString()

        inIt()

        otpProgressBar.visibility = View.INVISIBLE

        signupButton.setOnClickListener {
            val typedOTP = otpEditText.text.toString()

            if(typedOTP.isNotEmpty()){
                if (typedOTP.length == 6){
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(otp,typedOTP)
                    otpProgressBar.visibility = View.VISIBLE
                    signInWithPhoneAuthCredential(credential)
                }
                else{
                    Toast.makeText(this,"Enter Valid OTP",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Enter OTP",Toast.LENGTH_SHORT).show()
            }
        }

        resendTextView.setOnClickListener {
            resendOTP()
            resendOTPVisibility()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    otpProgressBar.visibility = View.INVISIBLE
//                    Toast.makeText(this,"Authentication Successful", Toast.LENGTH_SHORT).show()
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    val userMap = hashMapOf(
                        "name" to nameText,
                        "aadhaar" to aadhaarText,
                        "phone" to phoneNumber
                    )
                    db.collection("user").document(userId).set(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this,"User Registered Successfully",Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this,"User Registration Failed",Toast.LENGTH_SHORT).show()
                        }
                    sendToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this,"Incorrect OTP",Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }

    private fun sendToMain(){
        startActivity(Intent(this,MainActivity::class.java))
    }

    private fun resendOTPVisibility(){
        otpEditText.setText("")
        resendTextView.visibility = View.INVISIBLE
        resendTextView.isEnabled = false

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            resendTextView.visibility = View.VISIBLE
            resendTextView.isEnabled = true
        }, 60000)
    }

    private fun resendOTP(){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
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

            otp = verificationId
            resendToken = token

            // Save verification ID and resending token so we can use them later
        }
    }

    private fun inIt(){
        otpEditText = findViewById(R.id.otpEditText)
        signupButton = findViewById(R.id.signupButton)
        resendTextView = findViewById(R.id.resendTextView)
        auth = FirebaseAuth.getInstance()
        otpProgressBar = findViewById(R.id.otpProgressBar)
    }
}