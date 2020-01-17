package org.techtown.guru2project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    //이거 필요 없어요 메인 액티비티에서 받아옵니다.
    companion object{
        val REQUEST_CODE = 0
        val EMAIL = "email"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        //이거도 수정
        useridText_menu.text = EMAIL

        logoutBtn_menu.setOnClickListener {
            val intent = intent?:return@setOnClickListener
            intent.putExtra(MainActivity.RESULT, "logout")
            setResult(MainActivity.REQUEST_CODE, intent)
            finish()
        }

        deleteBtn_menu.setOnClickListener {
            //파이어베이스에서 사용자 계정 삭제하는 코드
            deleteID()
            val intent = intent?:return@setOnClickListener
            intent.putExtra(MainActivity.RESULT, "delete")
            setResult(MainActivity.REQUEST_CODE, intent)
            finish()
        }

        backBtn_menu.setOnClickListener {
            val intent = intent?:return@setOnClickListener
            intent.putExtra(MainActivity.RESULT, "result")
            setResult(MainActivity.REQUEST_CODE, intent)
            finish()
        }
    }

    private fun deleteID(){
        FirebaseAuth.getInstance().currentUser!!.delete(). addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(this, "탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
            }else{
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

}
