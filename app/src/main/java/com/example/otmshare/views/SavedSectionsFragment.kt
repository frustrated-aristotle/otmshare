package com.example.otmshare.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otmshare.adapters.SavedSectionsRecyclerRowAdapter
import com.example.otmshare.sections.Section
import com.example.otmshare.databinding.FragmentSavedSectionsBinding
import com.example.otmshare.singleton.SectionSingleton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SavedSectionsFragment : Fragment() {
    val sections = mutableListOf<Section>()
    private val adapter = SavedSectionsRecyclerRowAdapter(mutableListOf())
    //binding
    private var _binding : FragmentSavedSectionsBinding ? = null
    private val binding get() = _binding!!

    //db
    private val database = FirebaseFirestore.getInstance()

    val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSavedSectionsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_saved_sections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerviewFragmentAllSections.layoutManager = LinearLayoutManager(context)

        adapter.savedSectionsList = sections
        //Display just saved ones.
        val collectionRef =  database.collection("User")
        collectionRef.get()
            .addOnSuccessListener { documents ->
                for(document in documents)
                {
                    val userID = auth.currentUser!!.uid;
                    if(document.get("userID") == userID)
                    {
                        val savedSectionsString : String= document.get("savedSections") as String
                        SectionSingleton.initSavedSections(savedSectionsString)
                        val collectionRefForSection = database.collection("Section")
                        collectionRefForSection.get().addOnSuccessListener { documents ->
                            for (doc in documents)
                            {

                                println(doc.id)
                                if (savedSectionsString.contains(doc.get("id").toString()))
                                {
                                    val content = doc.get("content") as String
                                    val seasonAndEpisode = doc.get("seasonAndEpisode") as String
                                    val url = doc.get("url") as String
                                    val docID = doc.get("id") as Long
                                    val saveCount = (doc.get("saveCount") as Long).toInt()
                                    val likeCount = (doc.get("likeCount") as Long).toInt()
                                    val section = Section(seasonAndEpisode,content,url,saveCount,likeCount, id = docID)
                                    sections.add(section)
                                    adapter.savedSectionsList = sections
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                println("Error getting documents: " + exception)
            }
        binding.recyclerviewFragmentAllSections.adapter = adapter
    }

    fun getFromList()
    {
        SectionSingleton.savedSectionsListSingleton.clear()
        SectionSingleton.updateSavedSections()
        adapter.savedSectionsList = SectionSingleton.savedSectionsListSingleton
        adapter.notifyDataSetChanged()
        binding.recyclerviewFragmentAllSections.adapter = adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
