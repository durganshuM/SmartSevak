package com.gtappdevelopers.androidapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.smartsevak.R
import com.smarteist.autoimageslider.SliderViewAdapter


class SliderAdapter(private val sliderList: List<String>) :
    SliderViewAdapter<SliderAdapter.SliderViewHolder>() {

    // creating  a class for slider view holder
    class SliderViewHolder(ItemView: View) : SliderViewAdapter.ViewHolder(ItemView) {
        // on below line creating and initializing variable for slider image view with unique id.
        val sliderIV: ImageView = itemView.findViewById(R.id.idIVSliderItem)
    }


    // below method is use to return the count for the size of slider list
    override fun getCount(): Int {
        return sliderList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapter.SliderViewHolder {
        // on below line we are creating a variable to inflate the layout file which we have created.
        val itemView: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.slider_item, null)
        // on below line we are passing that view to view holder class.
        return SliderViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapter.SliderViewHolder?, position: Int) {
        // on below line we are loading an image from image url using Glide library and displaying that image in
        // our slider image view
        Glide.with(viewHolder!!.itemView).load(sliderList.get(position)).fitCenter()
            .into(viewHolder.sliderIV)
    }
}