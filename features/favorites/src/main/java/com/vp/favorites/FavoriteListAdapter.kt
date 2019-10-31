package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.detail.entity.MovieEntity
import kotlinx.android.synthetic.main.row_favorite.view.*

class FavoriteListAdapter(private val myDataset: List<MovieEntity>) :
        RecyclerView.Adapter<FavoriteListAdapter.FavoriteListViewHolder>() {

    var onClickFunction: (imdbId: String?) -> Unit = { Unit }

    inner class FavoriteListViewHolder(val constraintLayout: ConstraintLayout) : RecyclerView.ViewHolder(constraintLayout), View.OnClickListener {

        init {
            constraintLayout.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onClickFunction(myDataset[adapterPosition].imdbId)
        }

    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): FavoriteListViewHolder {
        // create a new view
        val constraintLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_favorite, parent, false) as ConstraintLayout
        return FavoriteListViewHolder(constraintLayout)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(listViewHolder: FavoriteListViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        listViewHolder.constraintLayout.title.text = myDataset[position].title
        Glide
                .with(listViewHolder.constraintLayout.poster)
                .load(myDataset[position].poster)
                .into(listViewHolder.constraintLayout.poster)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}
