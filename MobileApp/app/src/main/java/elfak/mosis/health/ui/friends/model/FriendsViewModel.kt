package elfak.mosis.health.ui.friends.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elfak.mosis.health.ui.user.data.User

class FriendsViewModel : ViewModel() {

    private val _sendRequestState by lazy { MutableLiveData<SendRequestState>(SendRequestState.Idle)}
    var sendRequestState: LiveData<SendRequestState> = _sendRequestState

    private var _friends = MutableLiveData<MutableList<User>>()
    var friends: LiveData<MutableList<User>> = _friends

    fun getFriends(currentUserUid: String) {
        //TODO
    }

    fun addFriend(friendId: String) {
        //TODO
    }
}

sealed class SendRequestState {
    object Idle : SendRequestState()
    class Success(val message: String = "Successful"): SendRequestState()
    class FindGameError(val message: String? = null): SendRequestState()
}
