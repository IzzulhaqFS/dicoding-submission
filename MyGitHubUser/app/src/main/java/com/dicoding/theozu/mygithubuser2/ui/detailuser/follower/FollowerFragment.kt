package com.dicoding.theozu.mygithubuser2.ui.detailuser.follower

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.theozu.mygithubuser2.adapter.ListUserAdapter
import com.dicoding.theozu.mygithubuser2.api.ItemsItem
import com.dicoding.theozu.mygithubuser2.databinding.FragmentFollowerBinding
import com.dicoding.theozu.mygithubuser2.ui.detailuser.DetailUserActivity

class FollowerFragment : Fragment() {

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(DetailUserActivity.EXTRA_USERNAME).toString()
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
            FollowersViewModel::class.java)

        binding.rvFollowers.setHasFixedSize(true)

        viewModel.setListFollowers(username)

        viewModel.listFollowers.observe(viewLifecycleOwner) {
            setListFollowers(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setListFollowers(listFollowers: List<ItemsItem>) {
        binding.apply {
            rvFollowers.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = ListUserAdapter(listFollowers)
            rvFollowers.adapter = adapter

            adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ItemsItem) {
                    val intentToDetail = Intent(requireActivity(), DetailUserActivity::class.java)
                    intentToDetail.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    intentToDetail.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    intentToDetail.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatarUrl)
                    startActivity(intentToDetail)
                }
            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.followersProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}