package elfak.mosis.health.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentCreateUserBinding
import elfak.mosis.health.databinding.FragmentWelcomeBinding
import elfak.mosis.health.ui.user.model.UserViewModel

class WelcomeFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewHealthID.text = userViewModel.currentUser?._id.toString()
        binding.buttonGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_WelcomeFragment_to_HomeFragment)
        }
    }

}