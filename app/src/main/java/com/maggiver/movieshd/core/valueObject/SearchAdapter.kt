package com.maggiver.movieshd.core.valueObject

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maggiver.movieshd.R
import com.maggiver.movieshd.core.utils.BaseViewHolder
import com.maggiver.movieshd.core.utils.Constants
import com.maggiver.movieshd.databinding.MovieSearchBinding
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieSearchCustom


class SearchAdapter(
    private val context: Context,
    private val itemClickListener: OnMovieSearchClickListener
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var movielist = listOf<MovieSearchCustom>()

    interface OnMovieSearchClickListener {
        fun onMovieSearchCustom(movieSearchCustom: MovieSearchCustom, position: Int)
    }

    fun setMovielList(movieList: List<MovieSearchCustom>) {
        this.movielist = movieList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = MovieSearchBinding.inflate(LayoutInflater.from(context), parent, false)

        val holder = MainViewHolder(itemBinding)

        itemBinding.root.setOnClickListener {
            val position = holder.adapterPosition.takeIf { it != NO_POSITION } ?: return@setOnClickListener
            itemClickListener.onMovieSearchCustom(movielist[position], position)
        }

        return holder
    }

    override fun getItemCount(): Int {
        val count = movielist.size
        return count
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MainViewHolder -> holder.bind(movielist[position], position)
        }
    }

    private inner class MainViewHolder(
        val binding: MovieSearchBinding
    ) : BaseViewHolder<MovieSearchCustom>(binding.root) {

        override fun bind(item: MovieSearchCustom, position: Int) = with(binding) {

            val posterUrl = item.posterPath.let { "${Constants.IMG_MOVIE_DB_COVER}$it" } ?: ""

            Glide.with(context)
                .load(posterUrl.ifEmpty { R.drawable.baseline_adb_24 })
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.baseline_add_a_photo_24)
                        .error(R.drawable.baseline_adb_24)
                )
                .into(binding.imgMovie)

            txtTitulo.text = item.title
            txtDescripcion.text = item.overview.takeIf { it.isNotBlank() } ?: "Sin descripción disponible."
        }


    }
}