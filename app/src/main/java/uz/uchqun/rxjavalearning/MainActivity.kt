package uz.uchqun.rxjavalearning

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import uz.uchqun.rxjavalearning.adapters.RecyclerAdapter
import uz.uchqun.rxjavalearning.models.Post
import uz.uchqun.rxjavalearning.network.ServiceGenerator
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerAdapter
    private val disposable: CompositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()

        getPostsObservable()
            ?.subscribeOn(Schedulers.io())
            ?.flatMap {
                getCommentsObservable(it)
            }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : Observer<Post?> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }


                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: ", e)
                }

                override fun onComplete() {}

                override fun onNext(t: Post) {
                    updatePost(t)
                }
            })

    }

    private fun updatePost(p: Post) {
        Observable
            .fromIterable(mAdapter.getPosts())
            .filter { post -> post.id === p.id }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Post> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onNext(post: Post) {
                    Log.d(
                        TAG,
                        "onNext: updating post: " + post.id + ", thread: " + Thread.currentThread().name
                    )
                    mAdapter.updatePost(post)
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: ", e)
                }

                override fun onComplete() {}
            })
    }


    private fun getCommentsObservable(post: Post): Observable<Post?>? {
        return ServiceGenerator.api
            .getComments(post.id)
            .map { comments ->
                val delay: Int = (Random.nextInt(5) + 1) * 1000 // sleep thread for x ms
                Thread.sleep(delay.toLong())
                Log.d(
                    TAG,
                    "apply: sleeping thread " + Thread.currentThread().name + " for " + delay.toString() + "ms"
                )
                post.comments = comments
                post
            }
            .subscribeOn(Schedulers.io())
    }

    private fun getPostsObservable(): Observable<Post?>? {
        return ServiceGenerator.api
            .getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { posts ->
                mAdapter.setPosts(posts)
                Observable.fromIterable(posts)
                    .subscribeOn(Schedulers.io())
            }
    }

    private fun initRecyclerView() {
        mRecyclerView = recycler_view
        mAdapter = RecyclerAdapter()
        mRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }
    }


}