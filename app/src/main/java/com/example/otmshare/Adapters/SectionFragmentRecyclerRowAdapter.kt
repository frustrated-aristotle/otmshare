package com.example.otmshare.Adapters

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.otmshare.R
import com.example.otmshare.Sections.Section
import com.example.otmshare.databinding.SectionFragmentRecyclerRowBinding

class SectionFragmentRecyclerRowAdapter(var sectionList : MutableList<Section>) : RecyclerView.Adapter<SectionFragmentRecyclerRowAdapter.SectionViewHolder>(){

    var a : List<Int> = listOf(3,5,2,4,1)
    class SectionViewHolder(var view: SectionFragmentRecyclerRowBinding) : ViewHolder(view.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<SectionFragmentRecyclerRowBinding>(inflater,R.layout.section_fragment_recycler_row, parent, false)
        return SectionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.view.titleText.text = makeItATitle(sectionList[position].seasonAndEpisode)
        holder.view.contentText.text = sectionList[position].content
        holder.view.urlText.text = sectionList[position].url
        holder.view.cardView2.setOnClickListener {
            //val podcastUrl = "https://open.spotify.com/episode/0ZYEwsnHKhCRIJsXnK1RYY?si=877509381e2e41ab&t=460"
            val podcastUrl = sectionList[position].url
            openSpotifyPodcast(podcastUrl, it)
        }
        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
            holder.view.cardView2,
            PropertyValuesHolder.ofFloat(View.SCALE_X, 0.9f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.9f)
        ).apply {
            duration = 100
            repeatCount = 0
            repeatMode = ObjectAnimator.REVERSE
        }
        val c = android.graphics.Color.parseColor("#D3D2D2")
        holder.view.cardView2.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    // CardView dokunulduğunda animasyonu başlat
                    scaleDown.start()
                    holder.view.cardView2.setCardBackgroundColor(c)
                }
                MotionEvent.ACTION_CANCEL -> {
                    // CardView dokunuşu serbest bırakıldığında veya iptal edildiğinde animasyonu durdur
                    scaleDown.cancel()
                    holder.view.cardView2.scaleX = 1.0f
                    holder.view.cardView2.scaleY = 1.0f
                    holder.view.cardView2.setCardBackgroundColor(android.graphics.Color.WHITE)
                }
                MotionEvent.ACTION_UP ->{
                    scaleDown.cancel()
                    holder.view.cardView2.scaleX = 1.0f
                    holder.view.cardView2.scaleY = 1.0f
                    val podcastUrl = sectionList[position].url
                    openSpotifyPodcast(podcastUrl, holder.view.cardView2)
                    holder.view.cardView2.setCardBackgroundColor(android.graphics.Color.WHITE)
                }
            }
            true
        }
        holder.view.likeImage.setOnClickListener{
            println("Click on like")
        }
        holder.view.saveImage.setOnClickListener{
            println("Click on save")
        }
        //holder.view.listener = this!!
    }
    fun openSpotifyPodcast(url: String, view : View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage("com.spotify.music") // Spotify uygulamasını belirtmek için
        startActivity(view.context,intent,null)
    }
    fun makeItATitle(str : String) : String
    {
        val numberParts = str.split(".")

        val title = "Sezon ${numberParts[0]} - Bölüm ${numberParts[1]}  |  ${numberParts[2]}"

        return title
    }
}