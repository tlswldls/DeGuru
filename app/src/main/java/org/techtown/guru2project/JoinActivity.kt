package org.techtown.guru2project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private var firestore:FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = FirebaseAuth.getInstance()

        joinBtn_join.setOnClickListener{
            createAndLoginEmail()
        }

        loginBtn_join.setOnClickListener {
            finish()
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

        val emailTxt = emailEdit_join.text.toString()
        val pw = pwEdit_join.text.toString()
        auth?.createUserWithEmailAndPassword(emailTxt, pw)
            ?.addOnCompleteListener { task->
                if(task.isSuccessful){
                    //회원 가입이 성공한 경우
                    createDB(emailTxt)
                }else{
                    //회원 가입에 실패한 경우
                    Toast.makeText(this, "회원가입 실패. 아이디를 확인해 주세요.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun createDB(email:String){
        val todo = Todo("Enter what you have to do", "날짜", "장소의 이름", "장소의 위도", "장소의 경도", "간략 주소", "인덱스 컬러" ,false)

        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("$email")?.document("Enter what you have to do")
            ?.set(todo)?.addOnCompleteListener{task->
                if(task.isSuccessful){
                    Toast.makeText(this, "Created DB", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }
}
