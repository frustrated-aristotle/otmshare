package com.example.otmshare.singleton

import com.example.otmshare.sections.Section
import com.example.otmshare.views.AllSectionsFragment
import com.example.otmshare.views.SavedSectionsFragment

object SectionSingleton {
    var allSectionsUpdated = mutableListOf<Section>()
    var savedSectionsUpdated = mutableListOf<Section>()

    var savedSectionsIDs = mutableListOf<String>()

    var isSectionUpdated : Boolean = false
    var isSavedSectionUpdated : Boolean = false

    var savedSectionsString :  String = ""
    var allSectionsFragment : AllSectionsFragment ? = null
    var savedSectionsFragment:  SavedSectionsFragment ? = null

    fun updateAllSections()
    {
        val newList = allSectionsFragment!!.recyclerViewAdapter.allSectionsList
        println("INSIDE OF UPDATEALLSECTIONS FROM SECTIONSIGNLETIN ${newList.get(0).id}")
        allSectionsUpdated = newList
    }

    fun initSavedSections(sections : String)
    {
        savedSectionsIDs = sections.split(".").toMutableList()
    }
    fun updateSavedSections()
    {
        //At the start of each run, we are getting the saved ones from cloud.
        //We should store them in our savedSectionsUpdated list.
        //When any changes happens to our list, we should modify the list.
        //If we add a new section, we should get the id and then add it to
    }

    var listX = mutableListOf<Section>()
    fun updateX()
    {
        for (section in allSectionsUpdated)
        {
            if (section.isSaveClicked)
            {
                listX.add(section)
            }
        }
    }

    fun updateSavedSections(id : Long) {
        for (section in allSectionsUpdated)
        {
            if (section.id == id)
            {
                println("Section id is ${section.id} and its value is ${section.isSaveClicked}")
            }
        }
    }

    fun addToList(loopsSectionID: Long, type: WantedStringType)
    {
        when(type)
        {
            WantedStringType.SAVED->
            {
                savedSectionsIDs.add(loopsSectionID.toString())
                for (section in allSectionsUpdated)
                {
                    if (section.id == loopsSectionID)
                    {
                        savedSectionsUpdated.add(section)
                        val index = allSectionsUpdated.indexOf(section)
                        allSectionsUpdated.get(index).isSaveClicked = true
                    }
                }
            }
            WantedStringType.LIKED->
            {

            }
            else->{

            }

        }
    }

    fun removeFromList(loopsSectionID: Long, type : WantedStringType)
    {
        when(type)
        {
            WantedStringType.SAVED ->
            {
                for (section in savedSectionsUpdated)
                {
                    if(section.id == loopsSectionID)
                    {
                        savedSectionsUpdated.remove(section)
                    }
                }
            }
            else->
            {

            }
        }
    }
    enum class WantedStringType
    {
        SAVED,
        LIKED
    }
}