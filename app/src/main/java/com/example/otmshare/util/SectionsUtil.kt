package com.example.otmshare.util

import android.opengl.Visibility
import android.view.View
import androidx.cardview.widget.CardView
import com.example.otmshare.R
import com.example.otmshare.sections.Section
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore




fun saveSection(
    sectionList: MutableList<Section>,
    id: Long,
    view: View,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    cardView: CardView ?,
    currentSection: Section
) {
    if (currentSection.isSaveClicked == false)
    {
        currentSection.isSaveClicked = true
        view.background = view.context.getDrawable(R.drawable.baseline_turned_in_24)
        likeOrSaveImage(id, "savedSections", auth, db)
    }
    else
    {
        currentSection.isSaveClicked = false
        view.background = view.context.getDrawable(R.drawable.baseline_turned_in_not_24)
        deleteLikedOrSavedImage(id, "savedSections", auth, db)
        cardView.let {
            it?.visibility = View.GONE
        }
    }
    //extracted(sectionList, view, id, auth, db, cardView)
    for (s in sectionList)
        println("AFTER DUDEEEEE ${s.id} : ${s.isSaveClicked}")

}

private fun extracted(
    sectionList: MutableList<Section>,
    view: View,
    id: Long,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    cardView: CardView?
) {
    for (section in sectionList) {
        if (section.isSaveClicked == false) {
            section.isSaveClicked = true
            view.background = view.context.getDrawable(R.drawable.baseline_turned_in_24)
            likeOrSaveImage(id, "savedSections", auth, db)
        } else {
            section.isSaveClicked = false
            view.background = view.context.getDrawable(R.drawable.baseline_turned_in_not_24)
            deleteLikedOrSavedImage(id, "savedSections", auth, db)
            cardView.let {
                it?.visibility = View.GONE
            }
        }
    }
}

fun likeSection(
    sectionList: MutableList<Section>,
    loopsSectionID: Long,
    view: View,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    section : Section
)
{
    if (section.isLikeClicked == false) {
        section.isLikeClicked = true

        view.background = view.context.getDrawable(R.drawable.baseline_thumb_up_alt_24)
        likeOrSaveImage(loopsSectionID, "likedSections", auth, db)
    }
    else
    {
        section.isLikeClicked = false
        view.background = view.context.getDrawable(R.drawable.baseline_thumb_up_off_alt_x)
        deleteLikedOrSavedImage(loopsSectionID, "likedSections", auth, db)
    }
}

fun likeOrSaveImage(loopsSectionID: Long, wantedString : String, auth : FirebaseAuth, database : FirebaseFirestore) {
    val currentUser = auth.currentUser
    val currentUserID = currentUser!!.uid
    val collectionRef = database.collection("User")

    collectionRef.get()
        .addOnSuccessListener { documents ->
            for (userDocument in documents) {
                if (userDocument.get("userID") == currentUserID)
                {
                    val documentId = userDocument.id
                    val initSections = userDocument.get(wantedString) as String
                    var updatedSections = ""
                    val retypedLoopsSectionID : Int = loopsSectionID.toInt()
                    if(!initSections.contains(retypedLoopsSectionID.toString()))
                    {
                        if(initSections.length > 0)
                        {
                            updatedSections = initSections +"."+retypedLoopsSectionID
                        }
                        else
                        {
                            updatedSections = initSections + retypedLoopsSectionID
                        }

                        val data = HashMap<String, Any>()
                        data.put(wantedString, updatedSections)
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
fun deleteLikedOrSavedImage(
    loopsSectionID: Long,
    wantedString: String,
    auth: FirebaseAuth,
    database: FirebaseFirestore
)
{
    val currentUser = auth.currentUser
    val currentUserID = currentUser!!.uid
    val collectionRef = database.collection("User")
    collectionRef.get()
        .addOnSuccessListener { documents ->
            for (userDocument in documents) {
                if (userDocument.get("userID") == currentUserID)
                {
                    val documentId = userDocument.id
                    val initSections = userDocument.get(wantedString) as String
                    var updatedFavSections = delete(initSections, loopsSectionID.toString())
                    val data = HashMap<String, Any>()
                    data.put(wantedString, updatedFavSections)
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

fun delete(initSections : String, stringToRemove : String) : String
{
    var updatedString = ""
    var initSectionsList = splitSectionStrings(initSections)
    //Remove the stringToRemove from our initSectionsList
    for (i in initSectionsList)
    {
        if( i == stringToRemove)
        {
            val removeIndex = initSectionsList.indexOf(i)
            initSectionsList.removeAt(removeIndex)
            break
        }
    }
    //Make a string from sections left in our sections list.
    for (i in initSectionsList)
    {
        if(initSectionsList.indexOf(i) == 0)
        {
            updatedString = updatedString + i
        }
        else
        {
            updatedString = updatedString  + "." + i
        }
    }
    return updatedString
    /*for (i in 0..initSectionsList.size)
    {
        if (initSections.contains(stringToRemove))
        {
            val length = stringToRemove.length
            val startingIndex = initSections.indexOf(stringToRemove)
            val endingIndex =  startingIndex + length

            println("Starting index is ${startingIndex} EndingIndex is ${endingIndex} Length${length}")
            if (initSections.length > startingIndex + length)
            {
                updatedString= initSections.removeRange(startingIndex, endingIndex+1)
            }
            else
            {
                updatedString= initSections.removeRange(startingIndex, endingIndex)
            }
            println("b: " + updatedString)
        }
    }
    return updatedString*/
}
