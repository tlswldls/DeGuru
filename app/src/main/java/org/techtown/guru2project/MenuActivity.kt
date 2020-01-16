package org.techtown.guru2project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        logoutBtn_menu.setOnClickListener {
            val intent = intent?:return@setOnClickListener
            intent.putExtra(MainActivity.RESULT, "logout")
            setResult(MainActivity.REQUEST_CODE, intent)
            finish()
        }
    }

}
