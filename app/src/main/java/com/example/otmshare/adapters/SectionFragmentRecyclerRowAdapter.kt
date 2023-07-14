package com.example.otmshare.adapters

import android.annotation.SuppressLint
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
import com.example.otmshare.util.animateCardView
import com.example.otmshare.util.animateImages
import com.example.otmshare.util.initButtons
import com.example.otmshare.util.makeTitleFromEpisodeAndSeasonString
import com.example.otmshare.util.openSpotifyWithPodcast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SectionFragmentRecyclerRowAdapter(var allSectionsList : MutableList<Section>) : RecyclerView.Adapter<SectionFragmentRecyclerRowAdapter.SectionViewHolder>(){

    val auth = FirebaseAuth.getInstance()
    val database =FirebaseFirestore.getInstance()

    //CardView Components' Global Versions
    private lateinit var titleText : TextView
    private lateinit var contentText : TextView
    private lateinit var urlText : TextView
    private lateinit var cardView: CardView
    private lateinit var likeImage : ImageView
    private lateinit var  saveImage: ImageView
    //region Unused necessary ones
    class SectionViewHolder(var view: SectionFragmentRecyclerRowBinding) : ViewHolder(view.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<SectionFragmentRecyclerRowBinding>(inflater,R.layout.section_fragment_recycler_row, parent, false)
        return SectionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return allSectionsList.size
    }
    //endregion
    fun assignCardViewComponents(holder : SectionViewHolder, position : Int)
    {
        holder.view.titleText.text = makeTitleFromEpisodeAndSeasonString(allSectionsList[position].seasonAndEpisode)
        holder.view.contentText.text = allSectionsList[position].content
        holder.view.urlText.text = allSectionsList[position].url
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
            val podcastUrl = allSectionsList[position].url
            openSpotifyWithPodcast(podcastUrl, it)
        }
        likeImage.setOnClickListener{
            println("Click on like")
        }
        saveImage.setOnClickListener{
            println("Click on save")
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        println("Holder:  " + holder.itemId)
        assignCardViewComponents(holder, position)
        initliazeSetOnClickListeners(holder, position)

        animateImages(listOf(likeImage, saveImage),allSectionsList[position].id,allSectionsList,auth,database,null,allSectionsList[position])
        animateCardView(cardView,allSectionsList[position].id,allSectionsList,auth,database, position)

        initButtons(likeImage,saveImage, allSectionsList[position].id,auth,database, allSectionsList, allSectionsList[position] )
        println("---------------------------------------------------------------------")

    }

    fun printSections()
    {
        for (sec in allSectionsList)
        {
            println("OUTSIDE current section is ${sec.id} and its is save clicked value is ${sec.isSaveClicked} ")
        }
    }
/*
        animateImages(listOf(holder.view.likeImage, holder.view.saveImage),sectionList[position].id)
        animateCardView(holder.view.cardView2, position)
        initButtons(holder.view.likeImage,holder.view.saveImage,sectionList[position].id)
    //To set buttons background by getting if they are selected from cloud
    private fun initButtons(likeView : ImageView, saveView : ImageView, viewID : Long) {
        val userID = auth.currentUser!!.uid
        val collectionRef = database.collection("User")
        collectionRef.get()
            .addOnSuccessListener { documents ->
                for(document in documents)
                {
                   if (document.get("userID") == userID)
                   {
                       val likedSections = (document.get("likedSections")) as String
                       var savedSections = document.get("savedSections") as String
                       for (i in 0..likedSections.length-1)
                       {
                           if (i%2 == 0)
                           {
                               if (likedSections.get(i).toString() == viewID.toString())
                               {
                                   likeView.background = likeView.context.getDrawable(R.drawable.baseline_thumb_up_alt_24)
                                    //Change their selected boolean
                                   sectionList[viewID.toInt()].isLikeClicked = true
                               }
                           }
                       }
                       for(i in 0 .. savedSections.length-1)
                       {
                            if(savedSections.get(i).toString() == viewID.toString())
                            {
                                saveView.background = saveView.context.getDrawable(R.drawable.baseline_turned_in_24)
                                sectionList[viewID.toInt()].isSaveClicked = true;
                            }
                       }
                   }

                }
            }
    }

    //region Animations
    @SuppressLint("ClickableViewAccessibility")
    fun animateCardView(cardView : CardView, position: Int) {
        val c = android.graphics.Color.parseColor("#D3D2D2")
        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
            cardView,
            PropertyValuesHolder.ofFloat(View.SCALE_X, 0.9f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.9f)
        ).apply {
            duration = 100
            repeatCount = 0
            repeatMode = ObjectAnimator.REVERSE
        }
        cardView.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    // CardView dokunulduğunda animasyonu başlat
                    scaleDown.start()
                    cardView.setCardBackgroundColor(c)
                }
                MotionEvent.ACTION_CANCEL -> {
                    // CardView dokunuşu serbest bırakıldığında veya iptal edildiğinde animasyonu durdur
                    scaleDown.cancel()
                    cardView.scaleX = 1.0f
                    cardView.scaleY = 1.0f
                    cardView.setCardBackgroundColor(android.graphics.Color.WHITE)
                }
                MotionEvent.ACTION_UP ->{
                    scaleDown.cancel()
                    cardView.scaleX = 1.0f
                    cardView.scaleY = 1.0f
                    val podcastUrl = sectionList[position].url
                    openSpotifyWithPodcast(podcastUrl, cardView)
                    cardView.setCardBackgroundColor(android.graphics.Color.WHITE)
                }
            }
            true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun animateImages(imgViewList: List<ImageView>, id : Long)
    {
        var imgView : ImageView
        for (i in 0..1)
        {
            imgView = imgViewList[i]
            //Scale Down:
            val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                imgView,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0.9f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.9f)
            ).apply {
                duration = 100
                repeatCount = 0
                repeatMode = ObjectAnimator.REVERSE
            }
            //onTouchListener
            imgView.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        scaleDown.start()
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        scaleDown.cancel()
                        imgView.scaleX = 1.0f
                        imgView.scaleY = 1.0f
                    }
                    MotionEvent.ACTION_UP ->{
                        scaleDown.cancel()
                        imgView.scaleX = 1.0f
                        imgView.scaleY = 1.0f
                        if (i == 0) //like Img
                        {
                                if(sectionList[id.toInt()].isLikeClicked == false)
                                {
                                    sectionList[id.toInt()].isLikeClicked = true
                                    view.background = view.context.getDrawable(R.drawable.baseline_thumb_up_alt_24)
                                    likeOrSaveImage(id, "likedSections")
                                }
                                else
                                {
                                    sectionList[id.toInt()].isLikeClicked = false
                                    view.background = view.context.getDrawable(R.drawable.baseline_thumb_up_off_alt_x)
                                    deleteLikedOrSavedImage(id, "likedSections")
                                }
                        }
                        else
                        {
                            if(sectionList[id.toInt()].isSaveClicked == false)
                            {
                                sectionList[id.toInt()].isSaveClicked = true
                                view.background = view.context.getDrawable(R.drawable.baseline_turned_in_24)
                                likeOrSaveImage(id, "savedSections")
                            }
                            else
                            {
                                sectionList[id.toInt()].isSaveClicked = false
                                view.background = view.context.getDrawable(R.drawable.baseline_turned_in_not_24)
                                deleteLikedOrSavedImage(id, "savedSections")
                            }
                        }
                    }
                }
                true
            }
        }
    }

    //endregion

    private fun likeOrSaveImage(id: Long, str : String) {
        val currentUser = auth.currentUser
        val currentUserID = currentUser!!.uid
        val collectionRef = database.collection("User")
        collectionRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document.get("userID") == currentUserID)
                    {
                        val documentId = document.id
                        val favSections = document.get(str) as String
                        var updatedFavSections = ""
                        val intId : Int = id.toInt()
                        if(!favSections.contains(intId.toString()))
                        {
                            if(favSections.length > 0)
                            {
                                updatedFavSections = favSections +"."+intId
                            }
                            else
                            {
                                updatedFavSections = favSections + intId
                            }

                            val data = HashMap<String, Any>()
                            data.put(str, updatedFavSections)
                            collectionRef.document(documentId)
                                .update(data)
                                .addOnSuccessListener {
                                        println("favouriteSections updated for document $documentId")
                                    }
                                .addOnFailureListener { exception ->
                                        println("Failed to update favouriteSections for document $documentId: $exception")
                                    }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                println(exception)
            }
    }

    private fun deleteLikedOrSavedImage(id: Long, str : String)
    {
        val currentUser = auth.currentUser
        val currentUserID = currentUser!!.uid
        val collectionRef = database.collection("User")
        collectionRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document.get("userID") == currentUserID)
                    {
                        val documentId = document.id
                        val favSections = document.get(str) as String
                        val intId : Int = id.toInt()
                        var updatedFavSections = delete(favSections, id.toString())
                        val data = HashMap<String, Any>()
                        data.put(str, updatedFavSections)
                        collectionRef.document(documentId)
                            .update(data)
                            .addOnSuccessListener {
                                println("favouriteSections updated for document $documentId")
                            }
                            .addOnFailureListener { exception ->
                                println("Failed to update favouriteSections for document $documentId: $exception")
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                println(exception)
            }
    }

    fun delete(currentString : String, stringToRemove : String) : String
    {
        var updatedString = ""
        for (i in 0..currentString.length)
        {
            if (currentString.contains(stringToRemove))
            {
                val length = stringToRemove.length
                val startingIndex = currentString.indexOf(stringToRemove)
                val endingIndex =  startingIndex + length

                println("Starting index is ${startingIndex} EndingIndex is ${endingIndex} Length${length}")
                if (currentString.length > startingIndex + length)
                {
                    updatedString= currentString.removeRange(startingIndex, endingIndex+1)
                }
                else
                {
                    updatedString= currentString.removeRange(startingIndex, endingIndex)
                }
                println("b: " + updatedString)
            }
        }
        return updatedString
    }*/
}