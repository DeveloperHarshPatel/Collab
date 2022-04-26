package com.developer_harshpatel.demo.ui.main.adapter

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developer_harshpatel.demo.R
import com.developer_harshpatel.demo.data.model.History
import com.developer_harshpatel.demo.ui.base.App
import kotlinx.android.synthetic.main.adapter_history.view.*

class HistoryAdapter(
    private val historyList: ArrayList<History>,
    val playClickInterface: PlayClickInterface
) : RecyclerView.Adapter<HistoryAdapter.DataViewHolder>() {

    lateinit var mediaPlayer: MediaPlayer

    var playingPosition: Int = -1

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(history: History) {
            itemView.tvTime.text = history.timeStamp
            if (history.isAudio) {
                Glide.with(itemView.imgAvatar.context)
                    .load(R.drawable.ic_baseline_call_24)
                    .centerCrop()
                    .into(itemView.imgAvatar)
            } else {
                Glide.with(itemView.imgAvatar.context)
                    .load(R.drawable.ic_baseline_videocam_24)
                    .centerCrop()
                    .into(itemView.imgAvatar)
            }

            if (playingPosition == historyList.indexOf(history)) {
                Glide.with(itemView.imgPlayPause.context)
                    .load(R.drawable.ic_pause)
                    .centerCrop()
                    .into(itemView.imgPlayPause)
            } else {
                Glide.with(itemView.imgPlayPause.context)
                    .load(R.drawable.ic_play)
                    .centerCrop()
                    .into(itemView.imgPlayPause)
            }

            itemView.cardPlay.setOnClickListener {
                if (history.isAudio) {
                    val audioUrl = history.filePath
                    if (playingPosition != historyList.indexOf(history)) {
                        playingPosition = historyList.indexOf(history)
                        notifyDataSetChanged()
                        Glide.with(itemView.imgPlayPause.context)
                            .load(R.drawable.ic_pause)
                            .centerCrop()
                            .into(itemView.imgPlayPause)
                        stopPlayer()
                        playAudio(audioUrl)
                    } else {
                        Glide.with(itemView.imgPlayPause.context)
                            .load(R.drawable.ic_play)
                            .centerCrop()
                            .into(itemView.imgPlayPause)
                        App.showToast("Audio has been paused")
                        stopPlayer()
                    }
                } else {
                    playingPosition = -1
                    notifyDataSetChanged()
                    stopPlayer()
                    playClickInterface.onPlayIconClick(history)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_history, parent,
                false
            )
        )

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(historyList[position])

    fun addData(list: List<History>) {
        historyList.addAll(list)
    }

    interface PlayClickInterface {
        // creating a method for click
        // action on audio image view.
        fun onPlayIconClick(history: History)
    }

    private fun stopPlayer() {
        try {
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()

        } catch (x: Exception) {
            App.log(""+x.message)
        }

    }

    private fun playAudio(audioUrl: String) {

        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
        )

        try {
            // below line is use to set our url to our media player.
            mediaPlayer.setDataSource(audioUrl)
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener(
                MediaPlayer.OnPreparedListener { mp ->
                    // This line is used to play the music
                    mp.start()
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // below line is use to display a toast message.
        App.showToast("Audio started playing")
    }

    fun onStop() {
        stopPlayer()
    }
}