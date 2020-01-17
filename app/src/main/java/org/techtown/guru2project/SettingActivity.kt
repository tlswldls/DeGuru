package org.techtown.guru2project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_date.*
import org.techtown.guru2project.Fragment.*

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        /* tabLayout, viewPager 설정 */
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DateFragment(), "Date")
        adapter.addFragment(LocationFragment(), "Location")
        adapter.addFragment(IndexFragment(), "Index")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        // tab 아이콘
//        tabLayout.getTabAt(0)?.setIcon(R.drawable.icon1)
//        tabLayout.getTabAt(1)?.setIcon(R.drawable.icon2)
//        tabLayout.getTabAt(2)?.setIcon(R.drawable.icon3)

        backBtn_menu.setOnClickListener {
            finish()
        }


    }
}
