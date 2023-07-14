package com.example.otmshare.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.otmshare.databinding.FragmentCreatingSectionsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.lang.Exception

class CreatingSections : Fragment() {


    private var _binding: FragmentCreatingSectionsBinding? = null
    private val binding get() = _binding!!

    //Firebase
    private val database = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreatingSectionsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    var seasonAndEpisode : String = ""
    var content : String = ""
    var url : String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSave.setOnClickListener {
            var hash = hashMapOf<String, Any>()
            getVars()
            hash.put("seasonAndEpisode", seasonAndEpisode)
            hash.put("content", content)
            hash.put("url", url)
            hash.put("id", getID())
            database.collection("Section").add(hash)
                .addOnCompleteListener {task ->
                    if (task.isSuccessful)
                    {
                        Toast.makeText(view.context, "Saved", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener{exception ->
                    Toast.makeText(view.context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        }
    }
    var idNum : Long = 0
    fun getID() : Long
    {
        val collectionRef = database.collection("Section")
        try {
            collectionRef
                .orderBy("id", Query.Direction.DESCENDING)
                .limit(1)
                .get().addOnSuccessListener { querySnapshot ->
                    if (querySnapshot != null && !querySnapshot.isEmpty) {
                        val highestIdDoc = querySnapshot.documents[0]
                        var highestID = highestIdDoc.get("id") as Long
                        highestID = highestID +1
                        idNum = highestID
                    }
                }
        }catch (e : Exception)
        {
            println("Exception : " + e)
        }
        return idNum
    }
    private fun getVars()
    {
        seasonAndEpisode = binding.txtEpisode.text.toString()
        content = binding.txtContent.text.toString()
        url = binding.txtURL.text.toString()
    }
}