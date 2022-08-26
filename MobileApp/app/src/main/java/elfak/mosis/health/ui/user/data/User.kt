package elfak.mosis.health.ui.user.data

import com.google.gson.annotations.SerializedName

data class User(@SerializedName("_Id")var _id: Int, @SerializedName("username")var username: String? = "",
                @SerializedName("age")var age: Int? = 0, @SerializedName("height")var height: Float?= 0.0f,
                @SerializedName("weight")var weight: Float = 0.0f, @SerializedName("gender")var gender: String? = "",
                @SerializedName("friends")var friends: MutableList<Int>?, @SerializedName("rank")var rank: Double? = 0.0,
                @SerializedName("imgSrc")var imgSrc: String? = "")
{

}
