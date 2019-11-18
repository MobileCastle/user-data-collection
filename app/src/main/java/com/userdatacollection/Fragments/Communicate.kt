package com.userdatacollection.Fragments

import com.userdatacollection.Database.UserModel

interface Communicate {
    fun sendData(usermodel: UserModel)
}