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
import com.example.githubuserapi.viewmodel.FollowerViewModel
import kotlinx.android.synthetic.main.fragment_follower.*
import kotlinx.android.synthetic.main.fragment_follower.progressBar

class FollowerFragment : Fragment() {
    private lateinit var adapter: UserAdapter
    private lateinit var followerViewModel: FollowerViewModel

    companion object{
        const val ARG_USERNAME = "arg_username"

        fun newInstance(username: String?): FollowerFragment {
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var username: String? = ""
        arguments?.let {
            username = it.getString(ARG_USERNAME)
        }
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        recyclerViewFollowers.layoutManager = LinearLayoutManager(context)
        recyclerViewFollowers.adapter = adapter

        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel::class.java)
        followerViewModel.setFollowers(username!!)

        followerViewModel.getFollowers().observe(viewLifecycleOwner, Observer { userItems ->
            userItems?.let {
                adapter.setData(it)
                showLoading(false)
            }
        })
    }
    private fun showLoading(state: Boolean) {
        progressBar.visibility = if(state) View.VISIBLE else View.GONE
    }
}