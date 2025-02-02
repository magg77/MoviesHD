package com.maggiver.movieshd.core.valueObject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maggiver.movieshd.R
import com.maggiver.movieshd.core.utils.BaseViewHolder
import com.maggiver.movieshd.core.utils.Constants
import com.maggiver.movieshd.databinding.MovieCardViewBinding
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieCustom

class AdapterMovies(
    private val context: Context,
    private val moviesList: List<Any>,
    private val onItemClickListener: (MovieCustom) -> Unit
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(context)
        return when (viewType) {

            VIEW_TYPE_HEADER -> {
                val view = inflater.inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }

            VIEW_TYPE_MOVIE -> {
                val itemBinding = MovieCardViewBinding.inflate(inflater, parent, false)
                MainViewHolder(itemBinding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = moviesList.size


    override fun getItemViewType(position: Int): Int {
        return if (moviesList[position] is String) VIEW_TYPE_HEADER else VIEW_TYPE_MOVIE
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(moviesList[position] as String, position)
            is MainViewHolder -> holder.bind(moviesList[position] as MovieCustom, position)
        }
    }

    // ViewHolder para el Header
    inner class HeaderViewHolder(view: View) : BaseViewHolder<String>(view) {
        private val title: TextView = view.findViewById(R.id.headerTitle)
        override fun bind(item: String, position: Int) {
            title.text = item
        }
    }

    inner class MainViewHolder(private val binding: MovieCardViewBinding) :
        BaseViewHolder<MovieCustom>(binding.root) {

        override fun bind(item: MovieCustom, position: Int) = with(binding) {

            Glide.with(context)
                .load("${Constants.IMG_MOVIE_DB_COVER}${item.posterPath}")
                .into(imvMovie)

            tvTitleMovie.text = item.title
            tvDescripShort.text =
                item.overview.takeIf { it.isNotBlank() } ?: "Sin descripción disponible."

            binding.contentCardView.setOnClickListener {
                onItemClickListener(item)
            }

        }
        
    }

    companion object {
        const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_MOVIE = 1
    }

}