package com.example.otmshare.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otmshare.adapters.SectionFragmentRecyclerRowAdapter
import com.example.otmshare.sections.Section
import com.example.otmshare.singleton.SectionSingleton
import com.example.otmshare.databinding.FragmentAllSectionsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.lang.Exception


class AllSectionsFragment : Fragment() {

    val recyclerViewAdapter = SectionFragmentRecyclerRowAdapter(mutableListOf())

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

        println(this.id)
        getFromCloud()
    }

    fun getFromCloud() {
        val sections = mutableListOf<Section>()



        val collectionRef = database.collection("Section")
        collectionRef.get()
            .addOnSuccessListener { documents ->


                for (document in documents) {
                    //changeSaveAndLikeCounts(collectionRef, document)
                    val content = document.get("content") as String
                    val seasonAndEpisode = document.get("seasonAndEpisode") as String
                    val url = document.get("url") as String
                    val docID = document.get("id") as Long
                    val likeCount = (document.get("likeCount") as Long).toInt()
                    val saveCount = (document.get("saveCount") as Long).toInt()
                    val section = Section(seasonAndEpisode, content, url,saveCount,likeCount ,id = docID)
                    sections.add(section)
                    recyclerViewAdapter.allSectionsList = sections
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            }.addOnFailureListener { exception ->
                println("Error getting documents: " + exception)
            }
        binding.recyclerviewFragmentAllSections.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewFragmentAllSections.adapter = recyclerViewAdapter
    }

    private fun changeSaveAndLikeCounts(
        collectionRef: CollectionReference,
        document: QueryDocumentSnapshot
    ) {
        var saveCountHash = HashMap<String, Any>()
        saveCountHash.put("saveCount", 0)
        saveCountHash.put("likeCount", 0)
        collectionRef.document(document.id).update(saveCountHash)
            .addOnSuccessListener {
                println("favouriteSections updated for document ${document.id}")
            }
            .addOnFailureListener {
                println("FAILURE : " + it.localizedMessage)
            }
    }

    fun getFromList()
    {
        SectionSingleton.updateAllSections()
        recyclerViewAdapter.allSectionsList = SectionSingleton.allSectionsUpdated
        recyclerViewAdapter.notifyDataSetChanged()
        binding.recyclerviewFragmentAllSections.adapter = recyclerViewAdapter
    }
    fun test()
    {
        println(recyclerViewAdapter.allSectionsList)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}