package com.userdatacollection.Adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.userdatacollection.Database.UserModel
import com.userdatacollection.R
import com.userdatacollection.Utils.Utils
import kotlinx.android.synthetic.main.data_item_row.view.*

class UserDataAdapter(private val userData: ArrayList<UserModel>) :
    RecyclerView.Adapter<UserDataAdapter.UserDataHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            UserDataAdapter.UserDataHolder {
        val inflatedView = parent.inflate(R.layout.data_item_row, false)
        return UserDataHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return userData.size
    }

    override fun onBindViewHolder(holder: UserDataAdapter.UserDataHolder, position: Int) {
        val user = userData[position]
        holder.bindUserData(user)

    }

    class UserDataHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var userModel: UserModel? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

        fun bindUserData(userModel: UserModel) {
            this.userModel = userModel
            view.userImage.setImageBitmap(Utils.getImage(userModel.image))
            view.userName.text = userModel.name
            view.userEmail.text = userModel.email
            view.userPhone.text = userModel.phone
        }
    }

}