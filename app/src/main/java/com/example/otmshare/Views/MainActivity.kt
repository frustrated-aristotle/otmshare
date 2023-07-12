package com.example.otmshare.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TableLayout
import android.widget.Toast
import androidx.core.view.MotionEventCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.otmshare.Adapters.ViewPagerAdapter
import com.example.otmshare.R
import com.example.otmshare.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(applicationContext, "context", Toast.LENGTH_SHORT).show()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        val adapter = ViewPagerAdapter(supportFragmentManager,lifecycle)

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "T�m Kesitler"
                }
                1 -> {
                    tab.text = "Kaydedilenler"
                }
                else ->
                {
                    tab.text = "K�pslar"
                }
            }
        }.attach()
    }

    //This app is the otm app.
}