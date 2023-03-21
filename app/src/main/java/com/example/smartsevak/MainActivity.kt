package com.example.smartsevak

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.gtappdevelopers.androidapplication.SliderAdapter
import com.smarteist.autoimageslider.SliderView

class MainActivity : AppCompatActivity() {
    // creating variable for slider view
    private lateinit var sliderView: SliderView

    // on below line creating variables for image urls.
    private var url1 =
        "https://developer.android.com/static/codelabs/basic-android-kotlin-compose-first-program/img/3bbebda874e6003b_960.png"
    private var url2 =
        "https://lh3.googleusercontent.com/GTmuiIZrppouc6hhdWiocybtRx1Tpbl52eYw4l-nAqHtHd4BpSMEqe-vGv7ZFiaHhG_l4v2m5Fdhapxw9aFLf28ErztHEv5WYIz5fA"
    private var url3 = "https://www.tutorialspoint.com/images/logo.png"

    private val footer = Footer(this)
    private val header = Header(this)
    private lateinit var reportButton: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        header.onClickHeader()
        footer.onCLickFooter()
        slider()
        inIt()
        reportButton.setOnClickListener {
            startActivity(Intent(this,ReportActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private fun inIt(){
        reportButton = findViewById(R.id.reportButton)
    }

    private fun slider() {
        //slider code:
        sliderView = findViewById(R.id.slider)
        // on below line creating variable for array list.
        val sliderDataArrayList: ArrayList<String> = ArrayList()
        // on below line adding urls in slider list.
        sliderDataArrayList.add(url1)
        sliderDataArrayList.add(url2)
        sliderDataArrayList.add(url3)
        // on below line initializing our adapter class by passing our list to it.
        val adapter = SliderAdapter(sliderDataArrayList)
        // on below line setting auto cycle direction for slider view from left to right.
        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        // on below line setting adapter for slider view.
        sliderView.setSliderAdapter(adapter)
        // on below line setting scroll time for slider view
        sliderView.scrollTimeInSec = 3
        // on below line setting auto cycle for slider view.
        sliderView.isAutoCycle = true
        // on below line setting start cycle for slider view.
        sliderView.startAutoCycle()
        //slider code end
    }
}