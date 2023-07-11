package com.example.otmshare.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.otmshare.Adapters.SavedSectionsRecyclerRowAdapter
import com.example.otmshare.R

class SavedSectionsFragment : Fragment() {

    private val adapter = SavedSectionsRecyclerRowAdapter(mutableListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_sections, container, false)
    }
}