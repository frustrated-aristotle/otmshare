package com.example.otmshare.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.otmshare.sections.Section
import com.example.otmshare.singleton.SectionSingleton
import com.example.otmshare.views.AllSectionsFragment
import com.example.otmshare.views.SavedSectionsFragment

class ViewPagerAdapter(fragmentManager : FragmentManager , lifecycle : Lifecycle):FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        return when(position)
        {
            0->{
                val frag= AllSectionsFragment()
                SectionSingleton.allSectionsFragment = frag
                frag
            }
            1->{
                val frag = SavedSectionsFragment()
                SectionSingleton.savedSectionsFragment = frag
                frag
            }
            else->{
                return Fragment()
            }
        }
    }
}