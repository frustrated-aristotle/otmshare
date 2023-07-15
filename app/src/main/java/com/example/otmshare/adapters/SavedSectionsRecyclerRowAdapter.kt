package com.example.otmshare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.otmshare.R
import com.example.otmshare.sections.Section
import com.example.otmshare.databinding.SectionFragmentRecyclerRowBinding
import com.example.otmshare.singleton.SectionSingleton
import com.example.otmshare.util.makeTitleFromEpisodeAndSeasonString
import com.example.otmshare.util.openSpotifyWithPodcast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.otmshare.util.animateCardView
import com.example.otmshare.util.animateImages
import com.example.otmshare.util.initButtons

class SavedSectionsRecyclerRowAdapter(var savedSectionsList : MutableList<Section>) :
    RecyclerView.Adapter<SavedSectionsRecyclerRowAdapter.SectionViewHolder>() {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseFirestore.getInstance()
    var savedSections : String = ""

    //CardView Components' Global Versions
    private lateinit var titleText : TextView
    private lateinit var contentText : TextView
    private lateinit var urlText : TextView
    private lateinit var cardView: CardView
    private lateinit var likeImage : ImageView
    private lateinit var  saveImage: ImageView

    //region Unnecessary parts
    class SectionViewHolder(var view : SectionFragmentRecyclerRowBinding) : ViewHolder(view.root)
    {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<SectionFragmentRecyclerRowBinding>(inflater, R.layout.section_fragment_recycler_row, parent, false)
        return SectionViewHolder(view)
    }

    override fun getItemCount(): Int {

        return savedSectionsList.size
    }
    //endregion

    fun assignCardViewComponents(holder : SectionViewHolder, position : Int)
    {
        holder.view.titleText.text = makeTitleFromEpisodeAndSeasonString(savedSectionsList[position].seasonAndEpisode)
        holder.view.contentText.text = savedSectionsList[position].content
        holder.view.urlText.text = savedSectionsList[position].url
        titleText = holder.view.titleText
        contentText = holder.view.contentText
        urlText = holder.view.urlText
    }
    fun initliazeSetOnClickListeners(holder : SectionViewHolder, position : Int)
    {
        cardView = holder.view.cardView2
        likeImage = holder.view.likeImage
        saveImage = holder.view.saveImage
        cardView.setOnClickListener {
            //val podcastUrl = "https://open.spotify.com/episode/0ZYEwsnHKhCRIJsXnK1RYY?si=877509381e2e41ab&t=460"
            val podcastUrl = savedSectionsList[position].url
            openSpotifyWithPodcast(podcastUrl, it)
        }
        likeImage.setOnClickListener{
            println("Click on like")
        }
        saveImage.setOnClickListener{
            println("Click on save")
        }
    }
    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {

        assignCardViewComponents(holder, position)
        initliazeSetOnClickListeners(holder, position)

        animateImages(listOf(likeImage, saveImage),savedSectionsList[position].id,auth,database, cardView,savedSectionsList[position],position)
        animateCardView(cardView,savedSectionsList[position].id,savedSectionsList,auth,database, position)
        initButtons(likeImage,saveImage, savedSectionsList[position].id,auth,database,  savedSectionsList[position],false)
    }
}