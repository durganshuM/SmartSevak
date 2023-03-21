package com.example.smartsevak

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {
    private val footer = Footer(this)
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private lateinit var signOutButton: Button
    private lateinit var profileNameTV: TextView
    private lateinit var profileAadhaarTV: TextView
    private lateinit var profilePhoneTV: TextView
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        inIt()
        fetchData()
        footer.onCLickFooter()
        signOutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    private fun inIt(){
        profileNameTV = findViewById(R.id.profilenameTextView)
        profileAadhaarTV = findViewById(R.id.profileAadhaarTextView)
        profilePhoneTV = findViewById(R.id.profilePhoneTextView)
        auth = FirebaseAuth.getInstance()
        signOutButton = findViewById(R.id.signOutButton)
    }

    private fun fetchData(){
        val docRef = db.collection("user").document(uid)
        docRef.get()
            .addOnSuccessListener {document ->
                if(document != null){
                    profileNameTV.text = document.data!!["name"].toString()
                    profileAadhaarTV.text = document.data!!["aadhaar"].toString()
                    profilePhoneTV.text = document.data!!["phone"].toString()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to Retrieve Data",Toast.LENGTH_SHORT).show()
            }
    }
}