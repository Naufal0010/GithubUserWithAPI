package com.exercise.consumerapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.exercise.consumerapp.adapter.ListFollowersUserAdapter
import com.exercise.consumerapp.api.FollowersViewModel
import com.exercise.consumerapp.databinding.FragmentFollowersBinding
import com.exercise.consumerapp.model.Followers
import com.exercise.consumerapp.model.User

class FollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    private var listFollowers: ArrayList<Followers> = ArrayList()
    private lateinit var adapter: ListFollowersUserAdapter
    private lateinit var followersViewModel: FollowersViewModel

    companion object {
        const val EXTRA_DETAIL_USER = "extra_detail_user"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = ListFollowersUserAdapter(listFollowers)
        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowersViewModel::class.java)

        val followersUser = requireActivity().intent.getParcelableExtra<User>(EXTRA_DETAIL_USER) as User
        setRecyclerView()

        followersViewModel.setListFollowers(requireActivity().applicationContext, followersUser.username.toString())
        showLoading(true)
        viewModel(adapter)

        return view
    }

    private fun setRecyclerView() {
        binding.recyclerViewFollowers.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewFollowers.setHasFixedSize(true)
        binding.recyclerViewFollowers.adapter = adapter
    }

    private fun viewModel(adapter: ListFollowersUserAdapter) {
        followersViewModel.getListFollowers().observe(viewLifecycleOwner, { listUser ->
            if (listUser != null) {
                adapter.setDataFollowers(listUser)
                showLoading(false)
                binding.recyclerViewFollowers.visibility = View.VISIBLE
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
}