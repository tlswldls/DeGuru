package org.techtown.guru2project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object{
        val REQUEST_CODE = 0
        val RESULT = "result"
        val NAME = "name"
        val EMAIL = "email"
        val PW = "password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        meueBrn_main2.setOnClickListener{
            val intent = Intent(this, MenuActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        imageButton.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode!= REQUEST_CODE) return
        val res = data?.getStringExtra(RESULT)
        if(res== "logout" || res=="delete"){
            //val intent = Intent(this, LoginActivity::class.java)
            //startActivity(intent)
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
