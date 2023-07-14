package com.example.otmshare.util

import android.widget.ImageView
import com.example.otmshare.R
import com.example.otmshare.sections.Section
import com.example.otmshare.singleton.SectionSingleton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//userDocument is a single user document in User collection of our database.
//Each user document has 
//email, likedSections, savedSections, password, userID which is given at the creation of an account.
//Loops section id is the id that the section lists current position on run.
//if the onbindViewHolder is at the start, then this id is the first member of list's id.
fun initButtons(likeView : ImageView, saveView : ImageView, loopsSectionID : Long, auth: FirebaseAuth, database : FirebaseFirestore, sectionsList : MutableList<Section>, currentSection : Section
) {
    val currentUserID = auth.currentUser!!.uid
    val userCollection = database.collection("User")
    userCollection.get()
        .addOnSuccessListener { documents ->
            for(userDocument in documents)
            {
                if (userDocument.get("userID") == currentUserID)
                {
                    val likedSectionsIDs = splitSectionStrings(userDocument.get("likedSections") as String)
                    val savedSectionsIDs = splitSectionStrings(userDocument.get("savedSections") as String)

                    if(savedSectionsIDs.contains(loopsSectionID.toString()))
                    {
                        saveView.background = saveView.context.getDrawable(R.drawable.baseline_turned_in_24)
                        currentSection.isSaveClicked = true
                    }
/*
                    for (section in savedSectionsIDs)
                    {
                        if (section == loopsSectionID.toString())
                        {
                            saveView.background = saveView.context.getDrawable(R.drawable.baseline_turned_in_24)

                        }
                    }
                    for (i in 0..likedSectionsIDs.size-1)
                    {
                        initLikeButton(likedSectionsIDs, i, loopsSectionID, likeView, sectionsList)
                    }
                    for(i in 0 .. savedSectionsIDs.size-1)
                    {
                        initSaveButton(savedSectionsIDs, i, loopsSectionID, saveView, sectionsList)
                    }*/
                }
            }
        }
    SectionSingleton.allSectionsFragment!!.recyclerViewAdapter.printSections()
}

private fun initSaveButton(
    savedSectionsIDs: List<String>,
    i: Int,
    loopsSectionID: Long,
    saveView: ImageView,
    sectionsList: MutableList<Section>
) {
    if (savedSectionsIDs.get(i) == loopsSectionID.toString()) {
        saveView.background = saveView.context.getDrawable(R.drawable.baseline_turned_in_24)
        for (section in sectionsList)
        {
            if(section.id == loopsSectionID)
                section.isSaveClicked = true
        }
    }
}

private fun initLikeButton(
    likedSectionsIDs: List<String>,
    i: Int,
    loopsSectionID: Long,
    likeView: ImageView,
    sectionsList: MutableList<Section>
) {
    if (likedSectionsIDs.get(i) == loopsSectionID.toString()) {
        likeView.background = likeView.context.getDrawable(R.drawable.baseline_thumb_up_alt_24)
        //Change their selected boolean
        //val indexInSectionList =
        for (section in sectionsList)
        {
            if(section.id == loopsSectionID)
                section.isLikeClicked = true
        }
        //sectionsList[loopsSectionID.toInt()].isLikeClicked = true
    }
}

fun splitSectionStrings(sectionString : String) : MutableList<String>
{
    val splittedSections = sectionString.split(".")

    return splittedSections.toMutableList()
}
