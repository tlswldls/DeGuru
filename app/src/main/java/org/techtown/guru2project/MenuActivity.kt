package org.techtown.guru2project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        if(intent.hasExtra("mail")){
            useridText_menu.text = intent.getStringExtra("mail")
        }else{
            useridText_menu.text = "User"
        }

        logoutBtn_menu.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        deleteBtn_menu.setOnClickListener {
            //파이어베이스에서 사용자 계정 삭제하는 코드
            deleteID()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        backBtn_menu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mail", useridText_menu.text.toString())
            startActivity(intent)
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
