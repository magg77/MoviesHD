package com.maggiver.movieshd.homeMovie.data.provider.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class NowPlayingResponse(
    @SerializedName("dates")
    val dates: Dates,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
): Parcelable

@Parcelize
data class Dates(
    @SerializedName("maximum")
    val maximum: String,
    @SerializedName("minimum")
    val minimum: String
): Parcelable

@Parcelize
data class Result(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
): Parcelable


fun Result.toMovieCustom(): MovieCustom = MovieCustom(
    id = this.id,
    posterPath = this.posterPath,
    title = this.title,
    overview = this.overview,
    voteCount = this.voteCount,
    releaseDate = this.releaseDate,
    popularity = this.popularity,
    favoriteState = false
)

@Parcelize
data class MovieCustom(
    @SerializedName("id") val id: Int,
    @SerializedName("posterPath") val posterPath: String,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("voteCount") val voteCount: Int,
    @SerializedName("releaseDate") val releaseDate: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("favoriteState") var favoriteState: Boolean = false
) : Parcelable

@Parcelize
data class ListMovieCustom(
    val listMovie: List<MovieCustom> = listOf()
) : Parcelable
