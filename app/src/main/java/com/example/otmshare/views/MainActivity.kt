package com.example.otmshare.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.otmshare.adapters.ViewPagerAdapter
import com.example.otmshare.R
import com.example.otmshare.singleton.SectionSingleton
import com.example.otmshare.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var firstStart : Boolean = true
    var firstStartRight : Boolean = true
    var tabLayout : TabLayout ? = null
    var viewPager : ViewPager2 ? = null
    val name = "Main Activity Name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SectionSingleton.mainActivity = this


        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

    }

     fun swipers() {
        tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager = findViewById<ViewPager2>(R.id.viewPager)

        //We can create a new function that handles these.
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        viewPager!!.adapter = adapter

        tabLayout!!.visibility = View.VISIBLE
        viewPager!!.visibility = View.VISIBLE
        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Tüm Kesitler"
                }

                1 -> {
                    tab.text = "Kaydedilenler"
                }

                else -> {
                    tab.text = "Kýpslar"
                }
            }
        }.attach()

        viewPager!!.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // Saðdaki veya soldaki sayfaya geçildiðinde yapýlmasý gereken iþlemleri buraya yazabilirsiniz
                if (position > 0) //Right
                {
                    if (!firstStartRight)
                        SectionSingleton.savedSectionsFragment!!.getFromList()
                    else
                        firstStartRight = false

                } else if (position < viewPager!!.adapter?.itemCount?.minus(1) ?: 0) {
                    if (!firstStart)
                        SectionSingleton.allSectionsFragment!!.getFromList()
                    else
                        firstStart = false
                }
            }
        })


    }


    //This app is the otm app.
}