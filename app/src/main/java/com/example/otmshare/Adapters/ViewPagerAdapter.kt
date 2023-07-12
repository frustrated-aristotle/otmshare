package com.example.otmshare.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.otmshare.Views.AllSectionsFragment
import com.example.otmshare.Views.SavedSectionsFragment

class ViewPagerAdapter(fragmentManager : FragmentManager , lifecycle : Lifecycle):FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position)
        {
            0->{
                AllSectionsFragment()
            }
            1->{
                SavedSectionsFragment()
            }
            else->{
                return Fragment()

            }
        }
    }
}