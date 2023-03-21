package com.example.smartsevak

import android.app.Activity
import android.content.Intent
import android.widget.ImageButton

class Header (private val activity: Activity) {
    fun onClickHeader(){
        val addButton: ImageButton = activity.findViewById(R.id.addButton)
        val bellButton: ImageButton = activity.findViewById(R.id.bellButton)

        addButton.setOnClickListener {
            activity.startActivity(Intent(this.activity,PostActivity::class.java))
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }
}