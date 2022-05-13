package id.co.pradiptapaa.icare.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@BindingAdapter(value = ["adapter","tabLayout"],requireAll = true )
fun bindVp2Adapter(view: ViewPager2, adapter: RecyclerView.Adapter<*>?,tabLayout: TabLayout) {
    view.adapter = adapter
    TabLayoutMediator(tabLayout, view) { _, _ ->
        //Some implementation
    }.attach()
}

