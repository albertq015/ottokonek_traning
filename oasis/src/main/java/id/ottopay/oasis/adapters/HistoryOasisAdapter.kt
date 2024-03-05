package id.ottopay.oasis.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.ottopay.oasis.ui.fragment.HistoryBatal
import id.ottopay.oasis.ui.fragment.HistoryPesanan
import id.ottopay.oasis.ui.fragment.HistorySelesai

class HistoryOasisAdapter (fm :FragmentManager, context: Context) : FragmentPagerAdapter(fm) {
    val  context = context
    private val pages = listOf(
            HistoryPesanan(),
            HistorySelesai(),
            HistoryBatal()
    )
    override fun getItem(position: Int): Fragment {
        return  pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> context.getString(com.otto.mart.R.string.proses_oasis)
            1 -> context.getString(com.otto.mart.R.string.selesai_oasis)
            else -> context.getString(com.otto.mart.R.string.dibatalkan_oasis)
        }
    }
}