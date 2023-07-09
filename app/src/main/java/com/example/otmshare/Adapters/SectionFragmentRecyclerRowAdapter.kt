package com.example.otmshare.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.otmshare.R
import com.example.otmshare.databinding.SectionFragmentRecyclerRowBinding

class SectionFragmentRecyclerRowAdapter(var sectionList : List<String>) : RecyclerView.Adapter<SectionFragmentRecyclerRowAdapter.SectionViewHolder>(){

    var a : List<Int> = listOf(3,5,2,4,1)
    class SectionViewHolder(var view: SectionFragmentRecyclerRowBinding) : ViewHolder(view.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<SectionFragmentRecyclerRowBinding>(inflater,R.layout.section_fragment_recycler_row, parent, false)
        return SectionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.view.likeImage.setOnClickListener{
            println("Click on like")
        }
        holder.view.saveImage.setOnClickListener{
            println("Click on save")
        }
        //holder.view.listener = this!!
    }
}