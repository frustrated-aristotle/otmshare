package com.example.otmshare.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.otmshare.Sections.Section
import com.example.otmshare.Singleton.SectionSingleton
import com.example.otmshare.Views.AllSectionsFragment
import com.example.otmshare.Views.SavedSectionsFragment

class ViewPagerAdapter(fragmentManager : FragmentManager , lifecycle : Lifecycle):FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }
    var allsectionsFragment : AllSectionsFragment ? = null
    var savedSectFrag : SavedSectionsFragment ? = null

    var allSections = mutableListOf<Section>()
    override fun createFragment(position: Int): Fragment {
        return when(position)
        {
            0->{
                val frag= AllSectionsFragment()
                allsectionsFragment = frag
                SectionSingleton.allSectionsFragment = frag
                frag
            }
            1->{
                val frag = SavedSectionsFragment()
                savedSectFrag = frag
                SectionSingleton.savedSectionsFragment = frag
                frag
            }
            else->{
                return Fragment()
            }
        }
    }

    //!!
    override fun onViewDetachedFromWindow(holder: FragmentViewHolder) {
        super.onViewDetachedFromWindow(holder)
       // allSections = allsectionsFragment!!.recyclerViewAdapter.sectionList
        println("allsections ${allsectionsFragment!!.id} saved ${savedSectFrag!!.id}")
        //allsectionsFragment!!.getFromList()
        allsectionsFragment!!.test()
        //allsectionsFragment!!.getFromCloud()
    }
}