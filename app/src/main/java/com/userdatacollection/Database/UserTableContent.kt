package com.userdatacollection.Database

import android.provider.BaseColumns

object UserTableContent {

    class UserEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "users"
            val COLUMN_USER_ID = "userid"
            val COLUMN_PHOTO = "photo"
            val COLUMN_NAME = "name"
            val COLUMN_EMAIL = "email"
            val COLUMN_PHONE = "phone"
            val COLUMN_IMAGE = "image"
        }
    }
}