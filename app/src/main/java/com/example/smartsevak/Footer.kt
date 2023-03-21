package com.example.smartsevak

import android.app.Activity
import android.content.Intent
import android.widget.ImageButton

class Footer(private val activity: Activity) {


    fun onCLickFooter() {
        val qrButton: ImageButton = activity.findViewById(R.id.qrButton)
        val profileButton: ImageButton = activity.findViewById(R.id.profileButton)
        val dashButton: ImageButton = activity.findViewById(R.id.dashButton)
        val communityButton: ImageButton = activity.findViewById(R.id.commButton)

        val profileIntent = Intent(activity, ProfileActivity::class.java)
        val dashIntent = Intent(activity, MainActivity::class.java)
        val qrScanIntent = Intent(activity, QRActivity::class.java)
        val communitIntent = Intent(activity, CommunityActivity::class.java)

        qrButton.setOnClickListener {
            activity.startActivity(qrScanIntent)
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        profileButton.setOnClickListener {
            activity.startActivity(profileIntent)
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        dashButton.setOnClickListener {
            activity.startActivity(dashIntent)
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        communityButton.setOnClickListener {
            activity.startActivity(communitIntent)
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }
}