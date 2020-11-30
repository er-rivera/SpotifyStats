package com.erivera.apps.topcharts.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.erivera.apps.topcharts.models.domain.HomeTab
import com.jay.widget.StickyHeadersLinearLayoutManager
import kotlinx.android.synthetic.main.view_top_tab.view.*
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter


class PagerRecyclerViewAdapter<T> : BindingRecyclerViewAdapter<T>() {

    override fun onCreateBinding(
        inflater: LayoutInflater, @LayoutRes layoutId: Int,
        viewGroup: ViewGroup
    ): ViewDataBinding {
        val binding = super.onCreateBinding(inflater, layoutId, viewGroup)
        val context = binding.root.context
        binding.root.tabRecyclerView.adapter =
            TopListAdapter(context)
        binding.root.tabRecyclerView.layoutManager =
            StickyHeadersLinearLayoutManager<TopListAdapter>(context)
        Log.d(PagerRecyclerViewAdapter::class.java.name, "created binding: $binding")
        return binding
    }

    override fun onBindBinding(
        binding: ViewDataBinding,
        bindingVariable: Int, @LayoutRes layoutId: Int,
        position: Int,
        item: T
    ) {
        super.onBindBinding(binding, bindingVariable, layoutId, position, item)
        if (item is HomeTab) {
            item.list.value?.let {
                (binding.root.tabRecyclerView.adapter as? TopListAdapter)?.setList(it)
            }
        }
        Log.d(
            PagerRecyclerViewAdapter::class.java.name,
            "bound binding: $binding at position: $position"
        )
    }

    override fun setItems(items: MutableList<T>?) {
        super.setItems(items)
        Log.d("eresa", "asdds")
    }
}