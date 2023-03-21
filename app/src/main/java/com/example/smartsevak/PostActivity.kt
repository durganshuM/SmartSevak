package com.example.smartsevak

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

class PostActivity : AppCompatActivity() {
    private lateinit var userName: String
    private lateinit var cancelPostButton: Button
    private lateinit var postButton: Button
    private lateinit var auth : FirebaseAuth
    private lateinit var postEditText: EditText
    private val db = Firebase.firestore
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var postProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        inIt()
        cancelPostButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        postButton.setOnClickListener {
            postProgressBar.visibility = View.VISIBLE
            val userMap = hashMapOf(
                "Date" to (LocalDate.now()).toString(),
                "Likes" to 0,
                "PostText" to (postEditText.text.toString()),
                "UserName" to userName
            )
            db.collection("userPost").document(uid).set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this,"Posted",Toast.LENGTH_SHORT).show()
                    postProgressBar.visibility = View.INVISIBLE
                    startActivity(Intent(this,MainActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed to post",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun inIt(){
        cancelPostButton = findViewById(R.id.cancelPostButton)
        postButton = findViewById(R.id.postButton)
        postEditText = findViewById(R.id.postEditText)
        postProgressBar = findViewById(R.id.postProgressBar)
        postProgressBar.visibility = View.INVISIBLE
        auth = FirebaseAuth.getInstance()
        val docRef = db.collection("user").document(uid)
        docRef.get()
            .addOnSuccessListener {document ->
                if(document != null){
                    userName = document.data!!["name"].toString()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to Retrieve Data", Toast.LENGTH_SHORT).show()
            }
    }
}