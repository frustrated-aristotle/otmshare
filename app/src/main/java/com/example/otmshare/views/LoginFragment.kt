package com.example.otmshare.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.otmshare.util.makeToast
import com.example.otmshare.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    //Firebase
    private lateinit var auth : FirebaseAuth
    //Binding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    //Login Variables
    var email = ""
    var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    var isLoginTrue : Boolean = false

    init {
        isLoginTrue = false
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser != null)
        {
            //We need to create new users in our user database.
                goTo(view)
        }
        binding.buttonSignup.setOnClickListener {
            getInputs()
            if(email!= null && password!= null)
            {
                createUser(it)
                //Toast.makeText(context, "Signed UP", Toast.LENGTH_SHORT).show()
            }
        }
        binding.buttonLogin.setOnClickListener{
            getInputs()
            if(email!= null && password!= null)
            {
                login(binding.buttonLogin)

            }
        }
    }
    var currentUser : String = ""
    val database = FirebaseFirestore.getInstance()
    private fun addUserIDToCategory() {

        val collectionRef = database.collection("User")
        val user = HashMap<String, String>()
        user.put("userID" , auth.currentUser!!.uid )
        user.put("savedSections", "")
        user.put("likedSections", "")
        user.put("email", auth.currentUser!!.email.toString())
        user.put("password", binding.passwordText.text.toString())
        collectionRef.add(user)
            .addOnCompleteListener {task ->
                if (task.isSuccessful)
                {
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{exception ->
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }

    }


    private fun login(it : View){

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful)
            {
                currentUser = auth.currentUser?.email.toString()
                goTo(it)
            }
        }.addOnFailureListener { exception->
            if (exception != null)
                Toast.makeText(it.context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getInputs()
    {
        email = binding.emailText.text.toString()
        password = binding.passwordText.text.toString()
    }

    private fun goTo(view : View)
    {
       val action = LoginFragmentDirections.actionLoginFragmentToAllSectionsFragment()
       Navigation.findNavController(view).navigate(action)
    }


    private fun createUser(it : View)
    {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    Toast.makeText(context, "Successfully Created", Toast.LENGTH_SHORT).show()
                    addUserIDToCategory()
                    goTo(it)
                    makeToast(auth.currentUser!!.uid, it)
                    println(auth.currentUser!!.uid)
                }
            }
            .addOnFailureListener{ exception ->
                    Toast.makeText(context, exception.localizedMessage,Toast.LENGTH_SHORT).show()
            }
    }
}