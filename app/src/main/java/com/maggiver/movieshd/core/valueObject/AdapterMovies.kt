package com.maggiver.movieshd.core.valueObject

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maggiver.movieshd.core.utils.BaseViewHolder
import com.maggiver.movieshd.core.utils.Constants
import com.maggiver.movieshd.databinding.MovieCardViewBinding
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieCustom

class AdapterMovies(
    private val context: Context,
    private val moviesList: List<MovieCustom>,
    private val onItemClickListener : (MovieCustom) -> Unit
): RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = MovieCardViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return MainViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MainViewHolder -> holder.bind(moviesList[position], position)
        }
    }

    inner class MainViewHolder(private val binding: MovieCardViewBinding) :
        BaseViewHolder<MovieCustom>(binding.root) {

        override fun bind(item: MovieCustom, position: Int) = with(binding) {

            Glide.with(context).load("${Constants.IMG_MOVIE_DB_COVER}${item.posterPath}")
                .into(imvMovie)

            tvTitleMovie.text = item.title
            tvDescripShort.text = item.overview.takeIf { it.isNotBlank() } ?: "Sin descripción disponible."
            binding.contentCardView.setOnClickListener {
                onItemClickListener(moviesList[position])
            }
        }
    }



}