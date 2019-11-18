package com.userdatacollection.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.userdatacollection.Adapter.UserDataAdapter
import com.userdatacollection.Database.DBHelper
import com.userdatacollection.Database.UserModel
import com.userdatacollection.R
import kotlinx.android.synthetic.main.data_list_fragment.view.*

class DataListFragment : Fragment() {

    private var dataitems = ArrayList<UserModel>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: UserDataAdapter
    private lateinit var userDBHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.data_list_fragment, container,
            false
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDBHelper = DBHelper(requireContext())
        linearLayoutManager = LinearLayoutManager(requireContext())
        view!!.data_list.layoutManager = linearLayoutManager
        dataitems = userDBHelper.readAllUsers()
        adapter = UserDataAdapter(dataitems)
        view!!.data_list.adapter = adapter
    }

    public fun addListItem(userModel: UserModel) {
        dataitems.add(userModel)
        adapter.notifyDataSetChanged()
    }
}