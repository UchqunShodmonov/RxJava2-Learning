package uz.uchqun.rxjavalearning.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import uz.uchqun.rxjavalearning.models.Comment
import uz.uchqun.rxjavalearning.models.Post

interface RequestApi {

    @GET("posts")
    fun getPosts(): Observable<MutableList<Post>>

    @GET("/posts/{id}/comments")
    fun getComments(@Path("id") id: Int?): Observable<MutableList<Comment>>

}