package com.erivera.apps.topcharts

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
/**
 * [RecyclerView.Adapter] that can display a [ColorInt]].
 * TODO: Replace the implementation with code for your data type.
 */
class ColorRecyclerViewAdapter(private var values: IntArray) :
    RecyclerView.Adapter<ColorRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_colors, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.colorImage.setBackgroundColor(item)
        holder.colorNumber.text = position.toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val colorImage: ImageView = view.findViewById(R.id.colorImage)
        val colorNumber: TextView = view.findViewById(R.id.colorNumber)

        override fun toString(): String {
            return super.toString() + " '" + colorNumber.text + "'"
        }
    }

    fun updateList(newList: IntArray) {
        values = newList
        this.notifyDataSetChanged()
    }
}