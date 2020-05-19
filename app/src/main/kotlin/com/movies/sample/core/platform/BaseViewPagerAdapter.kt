package com.movies.sample.core.platform

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class BaseViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
    private val mFragmentList = ArrayList<androidx.fragment.app.Fragment>()
    private val mTitleList = ArrayList<String>()

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragWithTitle(fragment: androidx.fragment.app.Fragment, title: String) {
        mFragmentList.add(fragment)
        mTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitleList[position]
    }
}