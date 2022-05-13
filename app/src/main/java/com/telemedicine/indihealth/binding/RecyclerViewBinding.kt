package id.co.pradiptapaa.icare.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("rvAdapter")
fun bindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
    view.adapter = adapter
}

@BindingAdapter("setNestedScrollingEnabled")
fun setNestedScrollingEnabled(view: RecyclerView, boolean: Boolean) {
    view.isNestedScrollingEnabled = boolean
}

