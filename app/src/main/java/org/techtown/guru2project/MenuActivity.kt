package org.techtown.guru2project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
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

        deleteBtn_menu.setOnClickListener {
            //파이어베이스에서 사용자 계정 삭제하는 코드
            val intent = intent?:return@setOnClickListener
            intent.putExtra(MainActivity.RESULT, "logout")  //이거 로그아웃 딜리트로 바꿔야함
            setResult(MainActivity.REQUEST_CODE, intent)
            emailEdit_login.setText("")
            pwEdit_login.setText("")
            finish()
        }
    }

}
