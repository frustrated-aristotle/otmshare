package com.example.otmshare.singleton

import com.example.otmshare.sections.Section
import com.example.otmshare.views.AllSectionsFragment
import com.example.otmshare.views.SavedSectionsFragment

object SectionSingleton {
    var allSections = mutableListOf<Section>()
    var allSectionsString = ""
    var allSectionsFragment : AllSectionsFragment ? = null
    var savedSectionsFragment:  SavedSectionsFragment ? = null
}