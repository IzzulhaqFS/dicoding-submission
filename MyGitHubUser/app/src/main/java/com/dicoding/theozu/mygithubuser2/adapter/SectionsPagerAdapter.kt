package com.dicoding.theozu.mygithubuser2.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.theozu.mygithubuser2.ui.detailuser.follower.FollowerFragment
import com.dicoding.theozu.mygithubuser2.ui.detailuser.following.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, data: Bundle) : FragmentStateAdapter(activity) {

    private var fragmentBundle: Bundle = data

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowerFragment()
            1 -> fragment = FollowingFragment()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }
}