package com.example.smartsevak

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommunityActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postList: ArrayList<UserPost>
//    private lateinit var postLikeCountTV: TextView
//    private lateinit var postLikeButton: ImageButton
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        recyclerView = findViewById(R.id.communityRV)
        recyclerView.layoutManager = LinearLayoutManager(this)

        postList = arrayListOf()

        db = FirebaseFirestore.getInstance()

        db.collection("userPost").get()
            .addOnSuccessListener {
                if(!it.isEmpty){
                    for(data in it.documents){
                        val userPost: UserPost? = data.toObject(UserPost::class.java)
                        if (userPost != null) {
                            postList.add(userPost)
                        }
                    }
                    recyclerView.adapter = CommunityAdapter(postList)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

//    private fun inIt(){
//        postLikeButton = findViewById(R.id.postLikeButton)
//        postLikeCountTV = findViewById(R.id.postLikeCountTV)
//    }
}