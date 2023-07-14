package com.example.otmshare.util

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.example.otmshare.sections.Section
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("ClickableViewAccessibility")
fun animateCardView(cardView : CardView, id : Long, sectionList : MutableList<Section>, auth : FirebaseAuth, db : FirebaseFirestore, position : Int) {
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
                val podcastUrl = sectionList[position].url
                openSpotifyWithPodcast(podcastUrl, cardView)
                cardView.setCardBackgroundColor(android.graphics.Color.WHITE)
            }
        }
        true
    }
}

@SuppressLint("ClickableViewAccessibility")
fun animateImages(
    imgViewList: List<ImageView>,
    loopsSectionID: Long,
    sectionList: MutableList<Section>,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    //isSavedSectionsFragment : Boolean,
    cardView: CardView ?,
    section : Section
)
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
                    if (i == 0)//Like Image
                    {
                        likeSection(sectionList, loopsSectionID, view, auth, db, section)
                    }
                    else//Save Image
                    {
                        saveSection(sectionList, loopsSectionID, view, auth, db, cardView,section)
                    }
                }
            }
            true
        }
    }
}
