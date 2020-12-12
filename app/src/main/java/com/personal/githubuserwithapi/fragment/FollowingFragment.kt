package com.personal.githubuserwithapi.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.personal.githubuserwithapi.adapter.ListDataUserAdapter
import com.personal.githubuserwithapi.adapter.ListFollowingUserAdapter
import com.personal.githubuserwithapi.api.FollowingViewModel
import com.personal.githubuserwithapi.databinding.FragmentFollowingBinding
import com.personal.githubuserwithapi.model.Following
import com.personal.githubuserwithapi.model.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private var listFollowing: ArrayList<Following> = ArrayList()
    private lateinit var adapter: ListFollowingUserAdapter
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = ListFollowingUserAdapter(listFollowing)
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)

        val followingUser = activity!!.intent.getParcelableExtra<User>(EXTRA_DETAIL_USER) as User
        setRecyclerView()

        followingViewModel.setListFollowing(activity!!.applicationContext, followingUser.username.toString())
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

    companion object {

        const val EXTRA_DETAIL_USER = "extra_detail_user"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                FollowingFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}