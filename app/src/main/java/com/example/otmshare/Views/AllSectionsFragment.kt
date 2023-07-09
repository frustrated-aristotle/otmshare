package com.example.otmshare.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otmshare.Adapters.SectionFragmentRecyclerRowAdapter
import com.example.otmshare.R
import com.example.otmshare.databinding.FragmentAllSectionsBinding


class AllSectionsFragment : Fragment() {

    private val recyclerViewAdapter = SectionFragmentRecyclerRowAdapter(listOf())

    private var _binding: FragmentAllSectionsBinding ? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_all_sections, container, false)
        _binding = FragmentAllSectionsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sectionList = listOf("Öğe 1", "Öğe 2", "Öğe 3") // Örnek bir öğe listesi
        recyclerViewAdapter.sectionList = sectionList
        recyclerViewAdapter.notifyDataSetChanged()
        binding.recyclerviewFragmentAllSections.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewFragmentAllSections.adapter = recyclerViewAdapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}