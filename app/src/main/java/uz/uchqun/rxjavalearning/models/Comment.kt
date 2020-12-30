package uz.uchqun.rxjavalearning.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Comment(
    @Expose
    @SerializedName("postId")
    val postId: Int?,

    @Expose
    @SerializedName("id")
    val id: Int?,

    @Expose
    @SerializedName("name")
    val name: String?,

    @Expose
    @SerializedName("email")
    val email: String?,

    @Expose
    @SerializedName("body")
    val body: String?
):Serializable