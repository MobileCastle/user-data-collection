package com.userdatacollection.Adapter

import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.userdatacollection.Database.UserModel
import com.userdatacollection.R
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
            Picasso.get()
                .load(Uri.parse(userModel.photo.toString()))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .resize(500, 500)
                .centerInside()
                .into(view.userImage, object : Callback {
                    override fun onSuccess() {
                        Log.d("IMAGE", "success")
                    }

                    override fun onError(e: Exception?) {
                        Log.d("IMAGE", e!!.message.toString())
                    }
                })
            view.userName.text = userModel.name
            view.userEmail.text = userModel.email
            view.userPhone.text = userModel.photo
        }
    }

}