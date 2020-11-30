package com.erivera.apps.topcharts.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.erivera.apps.topcharts.R
import com.erivera.apps.topcharts.models.domain.Artist
import com.erivera.apps.topcharts.models.domain.Song
import com.erivera.apps.topcharts.models.domain.TopListHeader
import com.erivera.apps.topcharts.models.domain.TopListItem
import com.jay.widget.StickyHeaders
import kotlinx.android.synthetic.main.view_top_artist.view.*
import kotlinx.android.synthetic.main.view_top_header.view.*
import kotlinx.android.synthetic.main.view_top_song.view.*


class TopListAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    StickyHeaders {
    companion object {
        private const val TYPE_HEADER = 1
        private const val TYPE_ARTIST = 2
        private const val TYPE_SONGS = 3
        private const val TYPE_GENRES = 4
        private const val TYPE_UNKNOWN = -1
    }


    private var list: List<TopListItem> = mutableListOf()

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_HEADER -> {
                (holder as HeaderViewHolder).setHeaderDetails(list[position])
            }
            TYPE_ARTIST -> {
                (holder as ArtistViewHolder).setArtistDetails(list[position])
            }
            TYPE_SONGS -> {
                (holder as SongViewHolder).setSongDetails(list[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_HEADER -> {
                LayoutInflater.from(context).inflate(R.layout.view_top_header, parent, false)?.let {
                    return HeaderViewHolder(it)
                }
            }
            TYPE_ARTIST -> {
                LayoutInflater.from(context).inflate(R.layout.view_top_artist, parent, false)?.let {
                    return ArtistViewHolder(it)
                }
            }
            TYPE_SONGS -> {
                LayoutInflater.from(context).inflate(R.layout.view_top_song, parent, false)?.let {
                    return SongViewHolder(it)
                }
            }
            TYPE_GENRES -> {
                return HeaderViewHolder(parent.rootView)
            }
            else -> {
                return HeaderViewHolder(parent.rootView)
            }
        }
        return HeaderViewHolder(parent.rootView)
    }

    private fun openSpotify(uri: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(uri)
        startActivity(context, intent, null)
    }

    inner class SongViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition < list.size) {
                    list.getOrNull(adapterPosition)?.let { item ->
                        if (item is Song) {
                            openSpotify(item.uri)
                        }
                    }
                }
            }
        }

        fun setSongDetails(topListItem: TopListItem) {
            if (topListItem is Song) {
                if (topListItem.photoUrl.isEmpty()) {
                    view.itemPhotoCardView.visibility = View.GONE
                } else {
                    view.itemPhotoCardView.visibility = View.VISIBLE
                    view.itemPhoto?.let {
                        Glide.with(context)
                            .load(topListItem.photoUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(it)
                    }
                }
                view.itemTitle.text = topListItem.titleName
                view.itemPosition.text = topListItem.position.toString()
                view.itemDescription.text = topListItem.description
            }
        }
    }

    inner class ArtistViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition < list.size) {
                    list.getOrNull(adapterPosition)?.let { item ->
                        if (item is Artist) {
                            openSpotify(item.uri)
                        }
                    }
                }
            }
        }

        fun setArtistDetails(topListItem: TopListItem) {
            if (topListItem is Artist) {
                if (topListItem.photoUrl.isEmpty()) {
                    view.artistPhotoCardView.visibility = View.GONE
                } else {
                    view.artistPhotoCardView.visibility = View.VISIBLE
                    view.artistPhoto?.let {
                        Glide.with(context)
                            .load(topListItem.photoUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(it)
                    }
                }
                view.artistName.text = topListItem.name
                view.artistPosition.text = topListItem.position.toString()
            }
        }
    }

    inner class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun setHeaderDetails(topListItem: TopListItem) {
            if (topListItem is TopListHeader) {
                view.headerName.text = topListItem.title
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is TopListHeader -> {
                TYPE_HEADER
            }
            is Artist -> {
                TYPE_ARTIST
            }
            is Song -> {
                TYPE_SONGS
            }
            else -> {
                TYPE_UNKNOWN
            }
        }
    }

    override fun isStickyHeader(position: Int): Boolean = list[position] is TopListHeader

    fun setList(list: List<TopListItem>) {
        this.list = list
        notifyDataSetChanged()
    }
}