package com.vp.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vp.detail.DetailActivity
import com.vp.detail.database.AppDatabase
import com.vp.detail.entity.MovieEntity
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var myDataset: ArrayList<MovieEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        viewManager = LinearLayoutManager(this)
        viewAdapter = FavoriteListAdapter(myDataset)

        mRecyclerView = recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // launch the detail activity for the clicked favorite movie
        (viewAdapter as FavoriteListAdapter).onClickFunction = { imdbId ->
            val intent = Intent(this, DetailActivity::class.java)
            val builder = Uri.Builder()
            builder.appendQueryParameter("imdbID", imdbId)
            intent.data = builder.build()
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        setDataset()
    }

    private fun setDataset() {
        GlobalScope.launch(Dispatchers.IO) {
            myDataset.clear()
            myDataset.addAll(AppDatabase.getInstance(this@FavoriteActivity).movieDao().getAll())
            withContext(Dispatchers.Main) {
                mRecyclerView.adapter!!.notifyDataSetChanged()
            }
        }
    }
}
