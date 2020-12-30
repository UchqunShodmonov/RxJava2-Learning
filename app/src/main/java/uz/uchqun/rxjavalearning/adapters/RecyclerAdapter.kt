package uz.uchqun.rxjavalearning.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.uchqun.rxjavalearning.R
import uz.uchqun.rxjavalearning.models.Post


class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    private val TAG = "RecyclerAdapter"

    private var posts = mutableListOf<Post>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_post_list_item,
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)

    }


    fun setPosts(posts: MutableList<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }
    fun getPosts():MutableList<Post>{
        return posts
    }

    fun updatePost(post: Post?) {
        posts[posts.indexOf(post)] = post!!
        notifyItemChanged(posts.indexOf(post))
    }

    override fun getItemCount(): Int = posts.size


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(post: Post) {
            title.text = post.title
            if (post.comments == null) {
                showProgressBar(true)
                numberComments.text = ""
            } else {
                showProgressBar(false)
                numberComments.text = post.comments.size.toString()
            }
        }

        private fun showProgressBar(showProgressBar: Boolean) {
            if (showProgressBar) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.GONE
        }

        val title: TextView = view.findViewById(R.id.title)
        val numberComments: TextView = view.findViewById(R.id.num_comments)
        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
    }

}