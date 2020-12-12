package com.personal.githubuserwithapi.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.personal.githubuserwithapi.R
import com.personal.githubuserwithapi.adapter.ListFollowersUserAdapter
import com.personal.githubuserwithapi.adapter.ListFollowingUserAdapter
import com.personal.githubuserwithapi.api.FollowersViewModel
import com.personal.githubuserwithapi.api.FollowingViewModel
import com.personal.githubuserwithapi.databinding.FragmentFollowersBinding
import com.personal.githubuserwithapi.databinding.FragmentFollowingBinding
import com.personal.githubuserwithapi.model.Followers
import com.personal.githubuserwithapi.model.Following
import com.personal.githubuserwithapi.model.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowersFragment : Fragment() {
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

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    private var listFollowers: ArrayList<Followers> = ArrayList()
    private lateinit var adapter: ListFollowersUserAdapter
    private lateinit var followersViewModel: FollowersViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = ListFollowersUserAdapter(listFollowers)
        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowersViewModel::class.java)

        val followersUser = activity!!.intent.getParcelableExtra<User>(EXTRA_DETAIL_USER) as User
        setRecyclerView()

        followersViewModel.setListFollowers(activity!!.applicationContext, followersUser.username.toString())
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

    companion object {

        const val EXTRA_DETAIL_USER = "extra_detail_user"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FollowersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}