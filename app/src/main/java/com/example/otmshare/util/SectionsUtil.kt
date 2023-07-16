package com.example.otmshare.util

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.otmshare.R
import com.example.otmshare.sections.Section
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text


fun saveSection(
    id: Long,
    view: View,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    cardView: CardView ?,
    currentSection: Section,
    saveCounter : TextView
) {
    if (currentSection.isSaveClicked == false)
    {
        currentSection.isSaveClicked = true
        view.background = view.context.getDrawable(R.drawable.baseline_turned_in_24)
        likeOrSaveImage(id, "savedSections", auth, db)
        modifyAmount(id.toInt(), db, 1, "saveCount",saveCounter)
    }
    else
    {
        currentSection.isSaveClicked = false
        view.background = view.context.getDrawable(R.drawable.baseline_turned_in_not_24)
        deleteLikedOrSavedImage(id, "savedSections", auth, db)
        modifyAmount(id.toInt(), db, -1, "saveCount",saveCounter)
        cardView.let {
            it?.visibility = View.GONE
        }
    }
}


fun likeSection(
    loopsSectionID: Long,
    view: View,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    section : Section,
    counter : TextView
)
{

    if (section.isLikeClicked == false) {
        section.isLikeClicked = true
        view.background = view.context.getDrawable(R.drawable.baseline_thumb_up_alt_24)
        likeOrSaveImage(loopsSectionID, "likedSections", auth, db)
        modifyAmount(loopsSectionID.toInt(), db, 1, "likeCount",counter)
        //SectionSingleton.addToList(loopsSectionID, SectionSingleton.WantedStringType.SAVED)
    }
    else
    {
        section.isLikeClicked = false
        view.background = view.context.getDrawable(R.drawable.baseline_thumb_up_off_alt_x)
        deleteLikedOrSavedImage(loopsSectionID, "likedSections", auth, db)
        modifyAmount(loopsSectionID.toInt(), db, -1, "likeCount",counter)

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
}


fun modifyAmount(sectionID: Int, database: FirebaseFirestore, addition : Int, modifyString : String, saveCounter: TextView)
{
    val sectionCollectionRef = database.collection("Section")
    sectionCollectionRef.get()
        .addOnSuccessListener { sections ->
            for (section in sections)
            {
                val docID = (section.get("id") as Long).toInt()
                if(docID == sectionID)
                {
                    val currentAmount = (section.get(modifyString) as Long).toInt()
                    val newAmount = currentAmount + addition
                    saveCounter.text = newAmount.toString()
                    val data = HashMap<String, Any>()
                    data.put(modifyString, newAmount)

                    sectionCollectionRef.document(section.id)
                        .update(data)
                        .addOnSuccessListener {
                            println("favouriteSections updated for document ${section.id}")
                        }
                        .addOnFailureListener { exception ->
                            println("Failed to update favouriteSections for document : $exception")
                        }
                }
            }
        }
}