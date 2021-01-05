package com.personal.githubuserwithapi.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.personal.githubuserwithapi.adapter.ListFollowingUserAdapter
import com.personal.githubuserwithapi.api.FollowingViewModel
import com.personal.githubuserwithapi.databinding.FragmentFollowingBinding
import com.personal.githubuserwithapi.entity.Following
import com.personal.githubuserwithapi.entity.User

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private var listFollowing: ArrayList<Following> = ArrayList()
    private lateinit var adapter: ListFollowingUserAdapter
    private lateinit var followingViewModel: FollowingViewModel

    companion object {
        const val EXTRA_DETAIL_USER = "extra_detail_user"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = ListFollowingUserAdapter(listFollowing)
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)

        val followingUser = requireActivity().intent.getParcelableExtra<User>(EXTRA_DETAIL_USER) as User
        setRecyclerView()

        followingViewModel.setListFollowing(requireActivity().applicationContext, followingUser.username.toString())
        showLoading(true)
        viewModel(adapter)

        return view
    }

    private fun setRecyclerView() {
        binding.recyclerViewFollowing.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewFollowing.setHasFixedSize(true)
        binding.recyclerViewFollowing.adapter = adapter
    }

    private fun viewModel(adapter: ListFollowingUserAdapter) {
        followingViewModel.getListFollowing().observe(viewLifecycleOwner, { listUser ->
            if (listUser != null) {
                adapter.setDataFollowing(listUser)
                showLoading(false)
                binding.recyclerViewFollowing.visibility = View.VISIBLE
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        }
        else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}