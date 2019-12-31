package com.erivera.apps.topcharts

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
import com.erivera.apps.topcharts.models.domain.Artist
import com.erivera.apps.topcharts.models.domain.TopListHeader
import com.erivera.apps.topcharts.models.domain.TopListItem
import com.jay.widget.StickyHeaders
import kotlinx.android.synthetic.main.view_top_header.view.*
import kotlinx.android.synthetic.main.view_top_item.view.*


class TopListAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    StickyHeaders {
    private val TYPE_HEADER = 1
    private val TYPE_ITEM = 2
    private var list: List<TopListItem> = mutableListOf()

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_HEADER) {
            (holder as HeaderViewHolder).setHeaderDetails(list[position])
        } else {
            (holder as ArtistViewHolder).setArtistDetails(list[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            LayoutInflater.from(context).inflate(R.layout.view_top_header, parent, false)?.let {
                return HeaderViewHolder(it)
            }
        } else {
            LayoutInflater.from(context).inflate(R.layout.view_top_item, parent, false)?.let {
                return ArtistViewHolder(it)
            }
        }
        return HeaderViewHolder(parent.rootView)
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

        private fun openSpotify(uri: String) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(uri)
            startActivity(context, intent, null)
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
        return if (list[position] is TopListHeader) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    override fun isStickyHeader(position: Int): Boolean = list[position] is TopListHeader

    fun setList(list: List<TopListItem>) {
        this.list = list
        notifyDataSetChanged()
    }
}