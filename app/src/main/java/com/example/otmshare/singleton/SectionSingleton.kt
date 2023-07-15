package com.example.otmshare.singleton

import com.example.otmshare.sections.Section
import com.example.otmshare.views.AllSectionsFragment
import com.example.otmshare.views.SavedSectionsFragment

object SectionSingleton {
    var allSectionsUpdated = mutableListOf<Section>()

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


    var savedSectionsListSingleton = mutableListOf<Section>()
    fun updateSavedSections()
    {
        for (section in allSectionsUpdated)
        {
            if (section.isSaveClicked)
            {
                savedSectionsListSingleton.add(section)
            }
        }
    }
}