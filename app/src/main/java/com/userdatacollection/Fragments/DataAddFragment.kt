package com.userdatacollection.Fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.userdatacollection.Database.DBHelper
import com.userdatacollection.Database.UserModel
import com.userdatacollection.R
import com.userdatacollection.Utils.Utils
import kotlinx.android.synthetic.main.data_add_fragment.*


class DataAddFragment : Fragment() {

    private lateinit var userDBHelper: DBHelper
    private var filepath: String? = ""
    private var image: Bitmap? = null
    private var listener: Communicate? = null

    companion object {
        private val IMAGE_PICK_CODE = 1000;
        private val PERMISSION_CODE = 1001;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.data_add_fragment, container,
            false
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDBHelper = DBHelper(requireContext())
        savedata.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (dataChecker())
                    addUser()
            }
        })

        user_photo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                permissionCall()
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Communicate) {
            listener = context
        } else throw RuntimeException(context.toString() + " must implement FragmentAListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun permissionCall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                pickImageFromGallery();
            }
        } else {
            pickImageFromGallery();
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            var targetUri: Uri = data?.data as Uri
            user_photo.setImageURI(targetUri)
            filepath = targetUri.toString()
            //var path = RealPathUtil.getRealPath(requireContext(), targetUri).toString()
            /*
            view?.let {
                Snackbar.make(it, filepath.toString(), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            }*/
            /*Picasso.get()
                .load(filepath.toString())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .resize(500, 500)
                .centerInside()
                .into(user_photo, object : Callback {
                    override fun onSuccess() {
                        Log.d("IMAGE", "success")
                    }

                    override fun onError(e: Exception?) {
                        Log.d("IMAGE", e!!.message.toString())
                    }
                })*/
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromGallery()
                } else {
                    showSettingsDialog()
                }
            }
        }
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(requireContext().resources.getString(R.string.permission_title))
        builder.setMessage(requireContext().resources.getString(R.string.permission_detail))
        builder.setPositiveButton(requireContext().resources.getString(R.string.btn_setting),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
                openSettings()
            })
        builder.setNegativeButton(requireContext().resources.getString(R.string.cancel),
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.setData(uri)
        startActivityForResult(intent, 101)
    }

    fun addUser() {
        var name = this.user_name.text.toString()
        var email = this.user_email.text.toString()
        var phone = this.user_phone.text.toString()
        var bitmap: Bitmap = (user_photo.drawable as BitmapDrawable).bitmap
        var usermodel =
            UserModel(
                photo = filepath.toString(),
                name = name,
                email = email,
                phone = phone,
                image = Utils.getBytes(bitmap)
            )
        userDBHelper.insertUser(usermodel)
        sendData(usermodel)
        clearView()
    }

    fun sendData(usermodel: UserModel) {
        listener?.sendData(usermodel)
        view?.let {
            Snackbar.make(
                it,
                requireContext().resources.getString(R.string.data_added),
                Snackbar.LENGTH_SHORT
            )
                .setAction("Action", null).show()
        }
    }

    fun clearView() {
        user_photo.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_launcher_foreground
            )
        )
        user_name.setText("")
        user_email.setText("")
        user_phone.setText("")
        filepath = ""
    }

    fun dataChecker(): Boolean {
        if (!(filepath.toString().trim().length > 0)) {
            view?.let {
                Snackbar.make(
                    it,
                    requireContext().resources.getString(R.string.data_error),
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("Action", null).show()
            }
            return false
        } else if (!(user_name.text.toString().trim().length > 0)) {
            view?.let {
                Snackbar.make(
                    it,
                    requireContext().resources.getString(R.string.data_error),
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("Action", null).show()
            }
            return false
        } else if (!(user_email.text.toString().trim().length > 0)) {
            view?.let {
                Snackbar.make(
                    it,
                    requireContext().resources.getString(R.string.data_error),
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("Action", null).show()
            }
            return false
        } else if (!(user_phone.text.toString().trim().length > 0)) {
            view?.let {
                Snackbar.make(
                    it,
                    requireContext().resources.getString(R.string.data_error),
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("Action", null).show()
            }
            return false
        } else
            return true
    }
}