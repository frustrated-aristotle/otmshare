package com.example.otmshare.Singleton

import com.example.otmshare.Sections.Section
import com.example.otmshare.Views.AllSectionsFragment
import com.example.otmshare.Views.SavedSectionsFragment

object SectionSingleton {
    var allSections = mutableListOf<Section>()
    var allSectionsString = ""
    var allSectionsFragment : AllSectionsFragment ? = null
    var savedSectionsFragment:  SavedSectionsFragment ? = null
}