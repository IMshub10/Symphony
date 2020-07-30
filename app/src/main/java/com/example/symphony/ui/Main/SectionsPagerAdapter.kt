package com.example.symphony.ui.Main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.symphony.Fragments.chat_fragment
import com.example.symphony.Fragments.status_fragment


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = chat_fragment.newInstance("ChatFragment", "1")
            1 -> fragment = status_fragment.newInstance("StatusFragment", "2")
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "CHATS"
            1 -> "STATUS"
            else -> ""
        }
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}