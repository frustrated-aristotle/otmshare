package com.example.otmshare.Adapters

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.GONE
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.otmshare.R
import com.example.otmshare.Sections.Section
import com.example.otmshare.Singleton.SectionSingleton
import com.example.otmshare.databinding.SectionFragmentRecyclerRowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SavedSectionsRecyclerRowAdapter(var savedSection : MutableList<Section>) :
    RecyclerView.Adapter<SavedSectionsRecyclerRowAdapter.SectionViewHolder>() {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseFirestore.getInstance()
    var savedSections : String = ""

    class SectionViewHolder(var view : SectionFragmentRecyclerRowBinding) : ViewHolder(view.root)
    {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<SectionFragmentRecyclerRowBinding>(inflater, R.layout.section_fragment_recycler_row, parent, false)
        return SectionViewHolder(view)
    }

    override fun getItemCount(): Int {

        return savedSection.size
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        //region Initializing CardView components
        holder.view.titleText.text = makeItATitle(savedSection[position].seasonAndEpisode)
        holder.view.contentText.text = savedSection[position].content
        holder.view.urlText.text = savedSection[position].url
        //endregion
        //region setOnClickListeners cardView2, likeImage, saveImage
        holder.view.cardView2.setOnClickListener {
            //val podcastUrl = "https://open.spotify.com/episode/0ZYEwsnHKhCRIJsXnK1RYY?si=877509381e2e41ab&t=460"
            val podcastUrl = savedSection[position].url
            openSpotifyPodcast(podcastUrl, it)
        }

        holder.view.likeImage.setOnClickListener{
            println("Click on like")
        }
        holder.view.saveImage.setOnClickListener{
            println("Click on save")
        }
        //endregion
        animateImages(listOf(holder.view.likeImage, holder.view.saveImage),savedSection[position].id,holder.view.cardView2)
        animateCardView(holder.view.cardView2, position)
        initButtons(holder.view.likeImage,holder.view.saveImage,savedSection[position].id)
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun animateImages(imgViewList: List<ImageView>, id : Long, cardView: CardView)
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
                        val index = savedSections.indexOf(id.toString()) / 2

                        if (i == 0) //like Img
                        {
                            //if(savedSection[id.toInt()].isLikeClicked == false)
                            if(savedSection[index].isLikeClicked == false)
                            {
                                savedSection[index].isLikeClicked = true
                                view.background = view.context.getDrawable(R.drawable.baseline_thumb_up_alt_24)
                                likeOrSaveImage(index.toLong()+1, "likedSections")
                            }
                            else
                            {
                                savedSection[index].isLikeClicked = false
                                view.background = view.context.getDrawable(R.drawable.baseline_thumb_up_off_alt_x)
                                deleteLikedOrSavedImage(id, "likedSections")
                            }
                        }
                        else
                        {

                            if(savedSection[index].isSaveClicked == false)
                            {
                                savedSection[index].isSaveClicked = true
                                view.background = view.context.getDrawable(R.drawable.baseline_turned_in_24)
                                likeOrSaveImage(index.toLong()+1, "savedSections")
                            }
                            else
                            {
                                savedSection[index].isSaveClicked = false
                                view.background = view.context.getDrawable(R.drawable.baseline_turned_in_not_24)
                                deleteLikedOrSavedImage(id, "savedSections")
                                cardView.visibility = GONE
                            }
                        }
                    }
                }
                true
            }
        }
    }
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

    var tempSection = mutableListOf<Section>()
    private fun deleteLikedOrSavedImage(id: Long, str : String)
    {
        tempSection = savedSection
        val currentUser = auth.currentUser
        val currentUserID = currentUser!!.uid
        val collectionRef = database.collection("User")
        collectionRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document.get("userID") == currentUserID)
                    {
                        val index = savedSections.indexOf(id.toString()) / 2

                        val documentId = document.id
                        val favSections = document.get(str) as String
                        val intId : Int = index.toInt()
                        var updatedFavSections = delete(favSections, id.toString())
                        val data = HashMap<String, Any>()
                        data.put(str, updatedFavSections)
                        collectionRef.document(documentId)
                            .update(data)
                            .addOnSuccessListener {
                                for (sec in savedSection)
                                {
                                    println("Saved Section size" + savedSection.size)

                                    if(sec.id == id)
                                    {
                                        println("id is: " + id)
                                        //This id is the section's id.
                                        val sectionIndex = tempSection.indexOf(sec)
                                        println("SIZE " + tempSection.size)
                                        //println("before " +allSections.size)
                                        //val allSectIndex= SectionSingleton.allSections.indexOf()
                                        tempSection.removeAt(sectionIndex)
                                        println("SIZE " + tempSection.size)
                                        /*SectionSingleton.allSections.removeAt(allSectIndex)
                                        println("SIZE " + SectionSingleton.allSections.size)
                                        //println("after : " + allSections.size.toString())*/
                                    }
                                }

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
    }
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
                    // CardView dokunuldu�unda animasyonu ba�lat
                    scaleDown.start()
                    cardView.setCardBackgroundColor(c)
                }
                MotionEvent.ACTION_CANCEL -> {
                    // CardView dokunu�u serbest b�rak�ld���nda veya iptal edildi�inde animasyonu durdur
                    scaleDown.cancel()
                    cardView.scaleX = 1.0f
                    cardView.scaleY = 1.0f
                    cardView.setCardBackgroundColor(android.graphics.Color.WHITE)
                }
                MotionEvent.ACTION_UP ->{
                    scaleDown.cancel()
                    cardView.scaleX = 1.0f
                    cardView.scaleY = 1.0f
                    val podcastUrl = savedSection[position].url
                    openSpotifyPodcast(podcastUrl, cardView)
                    cardView.setCardBackgroundColor(android.graphics.Color.WHITE)
                }
            }
            true
        }
    }

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
                        savedSections = document.get("savedSections") as String
                        for (i in 0..likedSections.length-1)
                        {
                            if (i%2 == 0)
                            {
                                if (likedSections.get(i).toString() == viewID.toString())
                                {
                                    likeView.background = likeView.context.getDrawable(R.drawable.baseline_thumb_up_alt_24)
                                    //Change their selected boolean
                                    val index = savedSections.indexOf(viewID.toString()) / 2

                                    savedSection[index].isLikeClicked = true
                                }
                            }
                        }
                        for(i in 0 .. savedSections.length-1)
                        {
                            if(savedSections.get(i).toString() == viewID.toString())
                            {
                                saveView.background = saveView.context.getDrawable(R.drawable.baseline_turned_in_24)
                                val index = savedSections.indexOf(viewID.toString()) / 2
                                savedSection[index].isSaveClicked = true;
                            }
                        }
                    }

                }
            }
    }




    private fun openSpotifyPodcast(url: String, view : View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage("com.spotify.music") // Spotify uygulamas�n� belirtmek i�in
        ContextCompat.startActivity(view.context, intent, null)
    }
    private fun makeItATitle(str : String) : String
    {
        val numberParts = str.split(".")

        val title = "Sezon ${numberParts[0]} - B�l�m ${numberParts[1]}  |  ${numberParts[2]}"

        return title
    }
}