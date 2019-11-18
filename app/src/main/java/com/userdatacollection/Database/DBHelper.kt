package com.userdatacollection.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertUser(user: UserModel): Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(UserTableContent.UserEntry.COLUMN_PHOTO, user.photo)
        values.put(UserTableContent.UserEntry.COLUMN_NAME, user.name)
        values.put(UserTableContent.UserEntry.COLUMN_EMAIL, user.email)
        values.put(UserTableContent.UserEntry.COLUMN_PHONE, user.phone)

        val newRowId = db.insert(UserTableContent.UserEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteUser(userid: String): Boolean {
        val db = writableDatabase
        val selection = UserTableContent.UserEntry.COLUMN_USER_ID + " LIKE ?"
        val selectionArgs = arrayOf(userid)
        db.delete(UserTableContent.UserEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun updateUser(user: UserModel): Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(UserTableContent.UserEntry.COLUMN_USER_ID, user.userid)
        values.put(UserTableContent.UserEntry.COLUMN_PHOTO, user.photo)
        values.put(UserTableContent.UserEntry.COLUMN_NAME, user.name)
        values.put(UserTableContent.UserEntry.COLUMN_EMAIL, user.email)
        values.put(UserTableContent.UserEntry.COLUMN_PHONE, user.phone)

        db.update(
            UserTableContent.UserEntry.TABLE_NAME,
            values,
            "ID = ?",
            arrayOf("" + user.userid)
        )

        return true
    }

    fun readUser(userid: Int): ArrayList<UserModel> {
        val users = ArrayList<UserModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(
                "select * from "
                        + UserTableContent.UserEntry.TABLE_NAME
                        + " WHERE " + UserTableContent.UserEntry.COLUMN_USER_ID
                        + "='" + userid + "'", null
            )
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var photo: String
        var name: String
        var email: String
        var phone: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                photo =
                    cursor.getString(cursor.getColumnIndex(UserTableContent.UserEntry.COLUMN_PHOTO))
                name =
                    cursor.getString(cursor.getColumnIndex(UserTableContent.UserEntry.COLUMN_NAME))
                email =
                    cursor.getString(cursor.getColumnIndex(UserTableContent.UserEntry.COLUMN_EMAIL))
                phone =
                    cursor.getString(cursor.getColumnIndex(UserTableContent.UserEntry.COLUMN_PHONE))

                users.add(UserModel(userid, photo, name, email, phone))
                cursor.moveToNext()
            }
        }
        return users
    }

    fun readAllUsers(): ArrayList<UserModel> {
        val users = ArrayList<UserModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(
                "select * from "
                        + UserTableContent.UserEntry.TABLE_NAME, null
            )
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var userid: Int
        var photo: String
        var name: String
        var email: String
        var phone: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                userid =
                    cursor.getInt(cursor.getColumnIndex(UserTableContent.UserEntry.COLUMN_USER_ID))
                photo =
                    cursor.getString(cursor.getColumnIndex(UserTableContent.UserEntry.COLUMN_PHOTO))
                name =
                    cursor.getString(cursor.getColumnIndex(UserTableContent.UserEntry.COLUMN_NAME))
                email =
                    cursor.getString(cursor.getColumnIndex(UserTableContent.UserEntry.COLUMN_EMAIL))
                phone =
                    cursor.getString(cursor.getColumnIndex(UserTableContent.UserEntry.COLUMN_PHONE))

                users.add(UserModel(userid, photo, name, email, phone))
                cursor.moveToNext()
            }
        }
        return users
    }

    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "UserData.db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserTableContent.UserEntry.TABLE_NAME + " (" +
                    UserTableContent.UserEntry.COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    UserTableContent.UserEntry.COLUMN_PHOTO + " TEXT," +
                    UserTableContent.UserEntry.COLUMN_NAME + " TEXT," +
                    UserTableContent.UserEntry.COLUMN_EMAIL + " TEXT," +
                    UserTableContent.UserEntry.COLUMN_PHONE + " TEXT)"

        private val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserTableContent.UserEntry.TABLE_NAME
    }

}