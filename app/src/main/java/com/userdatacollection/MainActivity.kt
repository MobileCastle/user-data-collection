package com.userdatacollection

import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentTransaction
import com.userdatacollection.Database.UserModel
import com.userdatacollection.Fragments.Communicate
import com.userdatacollection.Fragments.DataAddFragment
import com.userdatacollection.Fragments.DataListFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), Communicate {
    override fun sendData(usermodel: UserModel) {
        mDataListFragment?.addListItem(usermodel)
    }

    private val fragmentManager = supportFragmentManager
    private var isForword: Boolean = false
    private var mDataAddFragment: DataAddFragment? = null
    private var mDataListFragment: DataListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mDataAddFragment = DataAddFragment()
        mDataListFragment = DataListFragment()

        fab.setOnClickListener { view ->
            if (!isForword)
                animRotateForword()
            else
                animRotateBackword()
        }

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                .add(R.id.viewfragment, mDataListFragment!!, "mDataListFragment")
                .commit()
        }
    }

    private fun animRotateForword() {
        isForword = true
        val interpolator = OvershootInterpolator()
        ViewCompat.animate(fab).rotation(135f).withLayer().setDuration(300)
            .setInterpolator(interpolator).start()

        val frag = fragmentManager.findFragmentByTag("mDataAddFragment")
        if (frag == null) {
            fragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.viewfragment, mDataAddFragment!!, "mDataAddFragment")
                .addToBackStack("mDataAddFragment")
                .commit()
        }
    }

    private fun animRotateBackword() {
        isForword = false
        val interpolator = OvershootInterpolator()
        ViewCompat.animate(fab).rotation(0f).withLayer().setDuration(300)
            .setInterpolator(interpolator).start()
        if (fragmentManager.backStackEntryCount > 0)
            fragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0)
            animRotateBackword()
        else
            super.onBackPressed()
    }
}
