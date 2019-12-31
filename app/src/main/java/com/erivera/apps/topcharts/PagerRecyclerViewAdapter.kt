package com.erivera.apps.topcharts

import android.util.Log
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.erivera.apps.topcharts.models.domain.HomeTab
import com.erivera.apps.topcharts.models.domain.TopListItem
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
        binding.root.tabRecyclerView.adapter = TopListAdapter(context)
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
        if (item is HomeTab) {
            item.list.value?.let {
                (binding.root.tabRecyclerView.adapter as? TopListAdapter)?.setList(it)
            }
        }
        super.onBindBinding(binding, bindingVariable, layoutId, position, item)
        Log.d(
            PagerRecyclerViewAdapter::class.java.name,
            "bound binding: $binding at position: $position"
        )
    }
}