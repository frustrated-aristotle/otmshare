package com.example.otmshare.Views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otmshare.Adapters.SectionFragmentRecyclerRowAdapter
import com.example.otmshare.R
import com.example.otmshare.Sections.Section
import com.example.otmshare.databinding.FragmentAllSectionsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AllSectionsFragment : Fragment() {

    private val recyclerViewAdapter = SectionFragmentRecyclerRowAdapter(mutableListOf())

    private var _binding: FragmentAllSectionsBinding ? = null
    private val binding get() = _binding!!


    //Firebase
    private val database = FirebaseFirestore.getInstance()
    private lateinit var auth : FirebaseAuth

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
        auth = FirebaseAuth.getInstance()

        var sections = mutableListOf<Section>()
        val collectionRef = database.collection("Section")
        collectionRef.get()
            .addOnSuccessListener { documents ->
                for(document in documents)
                {
                    val content = document.get("content") as String
                    val seasonAndEpisode = document.get("seasonAndEpisode") as String
                    val url = document.get("url") as String
                    val docID = document.get("id") as Long
                    val section = Section(seasonAndEpisode,content,url,0,0, id = docID)
                    sections.add(section)
                    recyclerViewAdapter.sectionList = sections
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            }.addOnFailureListener { exception ->
            println("Error getting documents: " + exception)
        }
        binding.recyclerviewFragmentAllSections.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewFragmentAllSections.adapter = recyclerViewAdapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}