package org.techtown.guru2project

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        val REQUEST_CODE = 0
        val RESULT = "result"
        val EMAIL = "email"
        val PW = "password"
        val REQUEST_CODE_MENU = 1000
        val RESULT_TODO = "TODO"
        val RESULT_DATE = "DATE"
        val RESULT_PLACE = "PLACE"
        val RESULT_LAT = "LATITUDE"
        val RESULT_LONG = "LONGITUDE"
        val RESULT_INDEX = "INDEX"
    }
    private var firestore:FirebaseFirestore? = null
    var email:String = "User"

    private lateinit var adapter: TodoAdapter
    private val todoList = arrayListOf<Todo>(
        Todo(
            "위도, 경도 받아오기", "2020.01.17", "태능약국",
            "37.619087", "127.07819", "서울시 노원구", "pinkindex"
        ),
        Todo(
            "RecyclerView 완성하기", "2020.01.17", "태능약국",
            "37.619087", "127.07819", "경기도 부천시", "pinkindex"
        ),
        Todo(
            "firebase랑 연결하기", "2020.01.17", "태능약국",
            "37.619087", "127.07819", "경기도 남양주시", "pinkindex"
        ),
        Todo(
            "구루 이새끼 넘 힘들다", "2020.01.17", "태능약국",
            "37.619087", "127.07819", "서울시 노원구", "pinkindex"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(intent.hasExtra("mail")){
            email = intent.getStringExtra("mail")
            Toast.makeText(this, "환영합니다"+email+"님", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "로그인 정보를 가져오기에 실패했습니다.", Toast.LENGTH_LONG).show()
        }

        meueBrn_main2.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("mail", email)
            startActivityForResult(intent, REQUEST_CODE)
        }

        imageButton.setOnClickListener {
            if(!editText_main.text.isNullOrEmpty()){
                val intent = Intent(this, SettingActivity::class.java)
                intent.putExtra("todo", editText_main.text.toString())
                startActivity(intent)
            }else{
                Toast.makeText(this, "할 일을 추가해 주세요.", Toast.LENGTH_LONG).show()
            }
        }

        RecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TodoAdapter(this) {
            Toast.makeText(this, "Todo: ${it.todo}, Date: ${it.date}", Toast.LENGTH_SHORT).show()
        }
        adapter.setItems(todoList)
        RecyclerView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode != REQUEST_CODE) return
        val res = data?.getStringExtra(RESULT)
        if (res == "logout" || res == "delete") {
            //val intent = Intent(this, LoginActivity::class.java)
            //startActivity(intent)
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun createNewDoc(name:String){
        val document = name

        firestore = FirebaseFirestore.getInstance()
    }

}
