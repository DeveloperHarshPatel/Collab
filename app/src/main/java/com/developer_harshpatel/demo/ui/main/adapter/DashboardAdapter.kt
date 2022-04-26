package com.developer_harshpatel.demo.ui.main.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developer_harshpatel.demo.R
import com.developer_harshpatel.demo.data.model.User
import com.developer_harshpatel.demo.ui.main.view.AudioRecordActivity
import kotlinx.android.synthetic.main.adapter_dashboard.view.*

class DashboardAdapter(
    private val users: ArrayList<User>,
    val itemClickInterface: ItemClickInterface,
    val audioClickInterface: AudioClickInterface,
    val videoClickInterface: VideoClickInterface
) : RecyclerView.Adapter<DashboardAdapter.DataViewHolder>() {

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(user: User) {
            itemView.tvUserName.text = user.first_name + "" + user.last_name
            itemView.tvUserEmail.text = user.email
            Glide.with(itemView.imgAvatar.context)
                .load(user.avatar)
                .circleCrop()
                .into(itemView.imgAvatar)

            itemView.setOnClickListener {
                itemClickInterface.onItemClick(user)
            }
            itemView.cardAudio.setOnClickListener {
                audioClickInterface.onAudioIconClick(user)
            }
            itemView.cardVideo.setOnClickListener {
                videoClickInterface.onVideoIconClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_dashboard, parent,
                false
            )
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(users[position])

    fun addData(list: List<User>) {
        users.addAll(list)
    }

    interface AudioClickInterface {
        // creating a method for click
        // action on audio image view.
        fun onAudioIconClick(user: User)
    }

    interface VideoClickInterface {
        // creating a method for click
        // action on video card view.
        fun onVideoIconClick(user: User)
    }

    interface ItemClickInterface {
        // creating a method for click action
        // on recycler view item for updating it.
        fun onItemClick(user: User)
    }

}