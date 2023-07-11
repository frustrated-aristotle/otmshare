package com.example.otmshare.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.otmshare.R
import com.example.otmshare.Sections.Section
import com.example.otmshare.databinding.SectionFragmentRecyclerRowBinding

class SavedSectionsRecyclerRowAdapter(var savedSection : MutableList<Section>) :
    RecyclerView.Adapter<SavedSectionsRecyclerRowAdapter.SectionViewHolder>() {

    class SectionViewHolder(view : SectionFragmentRecyclerRowBinding) : ViewHolder(view.root)
    {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<SectionFragmentRecyclerRowBinding>(inflater, R.layout.fragment_saved_sections, parent, false)
        return SectionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
        return savedSection.size
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}