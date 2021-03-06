package com.example.githubuserapi.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi.R
import com.example.githubuserapi.adapter.UserAdapter
import com.example.githubuserapi.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {
    private lateinit var adapter: UserAdapter
    private lateinit var followingViewModel: FollowingViewModel

    companion object{
        const val ARG_USERNAME = "arg_username"

        fun newInstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var username: String? = ""
        arguments?.let {
            username = it.getString(ARG_USERNAME)
        }
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        recyclerViewFollowing.layoutManager = LinearLayoutManager(context)
        recyclerViewFollowing.adapter = adapter

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowingViewModel::class.java)
        followingViewModel.setFollowing(username!!)

        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer { userItems ->
            userItems?.let {
                adapter.setData(it)
                showLoading(false)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    private fun showLoading(state: Boolean) {
        progressBar.visibility = if(state) View.VISIBLE else View.GONE
    }
}