package elfak.mosis.health.ui.friends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import elfak.mosis.health.R
import elfak.mosis.health.databinding.FragmentFindFriendBinding
import elfak.mosis.health.ui.friends.model.FriendsViewModel
import elfak.mosis.health.ui.friends.model.SendRequestState

class FindFriendFragment : Fragment() {
    private var _binding: FragmentFindFriendBinding? = null
    private val binding get() = _binding!!

    private val friendsViewModel: FriendsViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFindFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSendRequest.setOnClickListener{
            val friendId = binding.friendId.text.toString()
            friendsViewModel.addFriend(friendId, view.context)

//            if(gameCode.length != 6){
//                Toast.makeText(requireContext(), "Game code must be 6 characters", Toast.LENGTH_SHORT).show()
//            }
//            else{
//                gameViewModel.getGame(gameCode)
//            }
        }

        val findGameObserver = Observer<SendRequestState> {state ->
            if(state is SendRequestState.Success){
                findNavController().navigate(R.id.action_FindFriendFragment_to_FriendsFragment)
                Toast.makeText(view.context, state.message, Toast.LENGTH_SHORT).show()
            }
            if(state is SendRequestState.SendRequestError){
                Toast.makeText(view.context, state.message, Toast.LENGTH_SHORT).show()
            }
        }

        friendsViewModel.sendRequestState.observe(viewLifecycleOwner, findGameObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        friendsViewModel.sendRequestState.removeObservers(viewLifecycleOwner)
    }

}