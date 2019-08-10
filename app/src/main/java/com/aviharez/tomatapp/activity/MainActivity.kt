package com.aviharez.tomatapp.activity

import android.graphics.Typeface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aviharez.tomatapp.R
import com.aviharez.tomatapp.fragment.AccountFragment
import com.aviharez.tomatapp.fragment.SmartCatFragment
import com.aviharez.tomatapp.fragment.TomatFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ID_TOMAT = 1
        private const val ID_SC = 2
        private const val ID_CONFIG = 3
//        private const val ID_NOTIFICATION = 4
//        private const val ID_ACCOUNT = 5
    }

//    @SuppressLint("NewApi")
//    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    override fun attachBaseContext(newBase: Context?) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            super.attachBaseContext(MyContextWrapper.wrap(newBase, "fa"))
//        } else {
//            super.attachBaseContext(newBase)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragment(TomatFragment())
        bottomNavigation.show(ID_TOMAT, true)

        //tv_selected.typeface = Typeface.createFromAsset(assets, "fonts/SourceSansPro-Regular.ttf")

        bottomNavigation.add(MeowBottomNavigation.Model(ID_TOMAT, R.drawable.ic_home))
        bottomNavigation.add(MeowBottomNavigation.Model(ID_SC, R.drawable.ic_explore))
        bottomNavigation.add(MeowBottomNavigation.Model(ID_CONFIG, R.drawable.ic_account))
//        bottomNavigation.add(MeowBottomNavigation.Model(ID_NOTIFICATION, R.drawable.ic_notification))
//        bottomNavigation.add(MeowBottomNavigation.Model(ID_ACCOUNT, R.drawable.ic_account))

        //bottomNavigation.setCount(ID_NOTIFICATION, "115")

//        bottomNavigation.setOnShowListener {
//            when (it.id) {
//                ID_TOMAT -> initFragment(TomatFragment())
//                ID_SC -> initFragment(SmartCatFragment())
//                ID_CONFIG -> initFragment(AccountFragment())
////                ID_NOTIFICATION -> "NOTIFICATION"
////                ID_ACCOUNT -> "ACCOUNT"
//                else -> Toast.makeText(applicationContext, "Something when wrong", Toast.LENGTH_SHORT).show()
//            }
//        }

        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                ID_TOMAT -> initFragment(TomatFragment())
                ID_SC -> initFragment(SmartCatFragment())
                ID_CONFIG -> initFragment(AccountFragment())
//                ID_NOTIFICATION -> "NOTIFICATION"
//                ID_ACCOUNT -> "ACCOUNT"
                else -> Toast.makeText(applicationContext, "Something when wrong", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun initFragment(classFragment: androidx.fragment.app.Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameFragment, classFragment)
        transaction.commit()
    }

}