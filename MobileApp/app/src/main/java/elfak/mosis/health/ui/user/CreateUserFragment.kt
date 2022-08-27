package elfak.mosis.health.ui.user

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentCreateUserBinding
import elfak.mosis.health.ui.heartrate.FetchingState
import elfak.mosis.health.ui.user.data.User
import elfak.mosis.health.ui.user.model.AuthState
import elfak.mosis.health.ui.user.model.UserViewModel
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper._id
import elfak.mosis.health.utils.helpers.SharedPreferencesHelper.firstTime
import java.util.concurrent.Executors

class CreateUserFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private var gender: String = "m"

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
        //findNavController().navigate(R.id.action_CreateFragment_to_HomeFragment)
        if(!prefs.firstTime){
            userViewModel.getUser(view.context, prefs._id)

            val fetchingUserObserver = Observer<FetchingState>{ state ->
                if (state == FetchingState.Success) {
                    Toast.makeText(view.context, "User fetched", Toast.LENGTH_SHORT).show()

                    val navView: NavigationView? = activity?.findViewById(R.id.nav_view) ?: null
                    if(navView != null) {
                        val headerView: View = navView.getHeaderView(0)
                        val usernameHeader: TextView = headerView.findViewById(R.id.textViewUsername)

                        val headerImgProfile: ImageView = headerView.findViewById(R.id.imageViewProfile)
                        usernameHeader.text = userViewModel.currentUser!!.username

                        Glide.with(headerView).load(userViewModel.currentUser!!.imgSrc).into(headerImgProfile)


                    }

                    findNavController().navigate(R.id.action_CreateFragment_to_HomeFragment)
                }
                if (state is FetchingState.FetchingError) {
                    Toast.makeText(view.context, state.message, Toast.LENGTH_SHORT).show()
                }
            }

            userViewModel.fetchingUserState.observe(viewLifecycleOwner, fetchingUserObserver)
        }

        val inputUsername = binding.username
        val inputAge = binding.age
        val inputHeight = binding.height
        val inputWeight = binding.weight

        binding.radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                Log.i("RADIO", checkedId.toString())
                if(checkedId == R.id.radio_f)
                    gender = "f"
                else if(checkedId == R.id.radio_m)
                    gender = "m"
            }
        )



        binding.buttonSignup.setOnClickListener {
            val username: String = inputUsername.text.toString()
            val ageStr: String = inputAge.text.toString()
            val heightStr: String = inputHeight.text.toString()
            val weightStr: String = inputWeight.text.toString()


            if(username.isBlank() || gender.isBlank() || ageStr.isBlank() || heightStr.isBlank() || weightStr.isBlank())
            {
                Toast.makeText(
                    this.context,
                    "Please fill in all fields.",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                val user: User = User(8, username, ageStr.toInt(), heightStr.toFloat(), weightStr.toFloat(), gender, mutableListOf())
                Log.i("USER", user.toString())
                userViewModel.createUser(view.context, user)
            }
        }

        val authStateObserver = Observer<AuthState>{ state ->
            if (state == AuthState.Success) {
                Toast.makeText(view.context, "User registered successfully", Toast.LENGTH_SHORT).show()
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result

                    // Log and toast
                    val msg = token.toString()
                    Log.d("Firebase", msg)
                    Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
                    userViewModel.addFcm(view.context, msg)
                })

                findNavController().navigate(R.id.action_CreateFragment_to_WelcomeFragment)
            }
            if (state is AuthState.AuthError) {
                Toast.makeText(view.context, state.message, Toast.LENGTH_SHORT).show()
            }
        }
        userViewModel.authState.observe(viewLifecycleOwner, authStateObserver)
    }

    public fun onRadioButtonClicked(view: View)
    {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_m ->
                    if (checked) {
                        gender = "m"
                    }
                R.id.radio_f ->
                    if (checked) {
                        gender = "f"
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userViewModel.fetchingUserState.removeObservers(viewLifecycleOwner)
        userViewModel.authState.removeObservers(viewLifecycleOwner)
    }
}