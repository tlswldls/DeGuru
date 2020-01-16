package org.techtown.guru2project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = FirebaseAuth.getInstance()

        loginBtn_join.setOnClickListener{
            createAndLoginEmail()
        }
    }

    private fun createAndLoginEmail(){
        if(emailEdit_join.text.toString().isNullOrEmpty() || pwEdit_join.text.toString().isNullOrEmpty() || editText.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "이메일과 패스워드를 입력해 주세요.", Toast.LENGTH_LONG).show()
            return
        }
        if(pwEdit_join.text.toString() != editText.text.toString()){
            Toast.makeText(this, "패스워드를 확인해 주세요", Toast.LENGTH_LONG).show()
            pwEdit_join.setText("")
            editText.setText("")
            return
        }

        auth?.createUserWithEmailAndPassword(emailEdit_join.text.toString(), pwEdit_join.text.toString())
            ?.addOnCompleteListener { task->
                if(task.isSuccessful){
                    Toast.makeText(this, "회원가입 성공. 환영합니다.", Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }
}
