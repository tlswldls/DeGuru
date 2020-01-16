package org.techtown.guru2project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        getLoginData()

        joingBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        //로그인 버튼
        //logInBtn2.setOnClickListener {
        //    emailLogin()
        //}
    }

    private fun emailLogin(){
        if(emailEdit_login.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "이메일을 입력해 주세요.", Toast.LENGTH_LONG).show()
            return
        }
        if(pwEdit_login.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_LONG).show()
            return
        }

        auth?.signInWithEmailAndPassword(emailEdit_login.text.toString(), pwEdit_login.text.toString())
            ?.addOnCompleteListener { task ->
                if(task.isSuccessful){
                    saveLogInData()
                    val intent2 = Intent(this, MainActivity::class.java)
                    startActivity(intent2)
                }else{
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    pwEdit_login.setText("")
                }
            }
    }
    private fun saveLogInData(){
        val mail:String = emailEdit_login.text.toString()
        val pw:String = pwEdit_login.text.toString()

        val pref: SharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE)
        val ed: SharedPreferences.Editor = pref.edit()
        ed.putString("mail", mail)
        ed.putString("pw", pw)
        ed.apply()
    }

    private fun getLoginData(){
        val pref: SharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE)
        emailEdit_login.setText(pref.getString("mail", ""))
        pwEdit_login.setText(pref.getString("pw",""))
    }
}
