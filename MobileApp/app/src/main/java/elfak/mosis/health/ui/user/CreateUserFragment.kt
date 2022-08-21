package elfak.mosis.health.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentCreateUserBinding
import elfak.mosis.health.ui.user.data.User
import elfak.mosis.health.ui.user.model.AuthState
import elfak.mosis.health.ui.user.model.UserViewModel
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.firstTime

class CreateUserFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()

    private var _binding: FragmentCreateUserBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = SharedPreferencesHelper.customPreference(view.context, "First time")
        if(!prefs.firstTime){
            findNavController().navigate(R.id.action_CreateFragment_to_HomeFragment)
        }

        val inputUsername = binding.username
        val inputAge = binding.age
        val inputHeight = binding.height
        val inputWeight = binding.weight
        val inputGender = binding.gender

        binding.buttonSignup.setOnClickListener {
            val username: String = inputUsername.text.toString()
            val ageStr: String = inputAge.text.toString()
            val heightStr: String = inputHeight.text.toString()
            val weightStr: String = inputWeight.text.toString()
            val gender: String = inputGender.text.toString()

            if(username.isBlank() || gender.isBlank() || ageStr.isBlank() || heightStr.isBlank() || weightStr.isBlank())
            {
                Toast.makeText(
                    this.context,
                    "Please fill in all fields.",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                val user: User = User(8, username, ageStr.toInt(), heightStr.toFloat(), weightStr.toFloat(), gender)
                Log.i("USER", user.toString())
                userViewModel.createUser(view.context, user)
            }
        }

        val authStateObserver = Observer<AuthState>{ state ->
            if (state == AuthState.Success) {
                Toast.makeText(view.context, "User registered successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_CreateFragment_to_HomeFragment)
            }
            if (state is AuthState.AuthError) {
                Toast.makeText(view.context, state.message, Toast.LENGTH_SHORT).show()
            }
        }
        userViewModel.authState.observe(viewLifecycleOwner, authStateObserver)
    }

}