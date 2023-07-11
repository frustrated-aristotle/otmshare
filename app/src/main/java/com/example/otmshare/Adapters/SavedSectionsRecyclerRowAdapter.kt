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


    class SectionViewHolder(var view : SectionFragmentRecyclerRowBinding) : ViewHolder(view.root)
    {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<SectionFragmentRecyclerRowBinding>(inflater, R.layout.section_fragment_recycler_row, parent, false)
        return SectionViewHolder(view)
    }

    override fun getItemCount(): Int {

        return savedSection.size
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        //region Initializing CardView components
        holder.view.titleText.text = makeItATitle(savedSection[position].seasonAndEpisode)
        holder.view.contentText.text = savedSection[position].content
        holder.view.urlText.text = savedSection[position].url
        //endregion
    }
    fun makeItATitle(str : String) : String
    {
        val numberParts = str.split(".")

        val title = "Sezon ${numberParts[0]} - Bölüm ${numberParts[1]}  |  ${numberParts[2]}"

        return title
    }
}