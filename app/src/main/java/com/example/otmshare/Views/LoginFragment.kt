package com.example.otmshare.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.otmshare.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User

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
            println(currentUser.uid)
            goTo(view)
        }
        binding.buttonSignup.setOnClickListener {
            getInputs()
            if(email!= null && password!= null)
            {
                createUser()
                goTo(it)
            }
        }
        binding.buttonLogin.setOnClickListener{
            getInputs()
            if(email!= null && password!= null)
            {
                login()
                goTo(it)
            }
        }
    }

    private fun login() {

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful)
            {
                val currentUser = auth.currentUser?.email.toString()
            }
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


    private fun createUser()
    {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    println("Succesfull")}
            .addOnFailureListener{ exception ->
                println("Exception heyy: " + exception)
            }
    }
}