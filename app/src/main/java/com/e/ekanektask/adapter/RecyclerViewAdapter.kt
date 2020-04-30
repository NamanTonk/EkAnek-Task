package com.e.ekanektask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.ekanektask.R
import com.e.ekanektask.activity.MainActivity
import com.e.ekanektask.model.ImagesModel
import kotlinx.android.synthetic.main.image_item.view.*

class RecyclerViewAdapter(var list: ArrayList<ImagesModel.HitsBean>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var context: Context? = null
    var height = 0

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        context?.let {
            Glide.with(it).load(list[position].webformatURL).override(300, 300)
                .into(holder.itemView.imageView)

        }

    }
}