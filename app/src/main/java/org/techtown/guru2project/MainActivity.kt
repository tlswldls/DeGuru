package org.techtown.guru2project

import android.content.Intent
import android.os.Bundle
import android.text.style.StrikethroughSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    private var adapter: TodoAdapter? = null
    private var firestore:FirebaseFirestore? = null

    companion object {
        val REQUEST_CODE = 0
        val RESULT = "result"
    }

    var email:String = "User"
    val mStrikeThrough = StrikethroughSpan()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* 요일, 날짜 설정 */
        val current = LocalDateTime.now()
        val formatterDate = DateTimeFormatter.ofPattern("MM.dd", Locale.KOREA)
        val formattedDate= current.format(formatterDate)
        val formatterDay = DateTimeFormatter.ofPattern("EEEE", Locale.KOREA)
        val formattedDay = current.format(formatterDay)
        dayText_main.text = formattedDay
        dateText_main2.text = formattedDate


        /* 로그인 정보 가져오기 */
        if(intent.hasExtra("mail")){
            email = intent.getStringExtra("mail")
        }else{
            Toast.makeText(this, "로그인 정보를 가져오기에 실패했습니다.", Toast.LENGTH_LONG).show()
        }

        meueBrn_main2.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("mail", email)
            startActivity(intent)
            finish()
        }

        imageButton.setOnClickListener {
            if(!editText_main.text.isNullOrEmpty()){
                val intent = Intent(this, SettingActivity::class.java)
                intent.putExtra("todo", editText_main.text.toString())
                intent.putExtra("mail", email)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "할 일을 추가해 주세요.", Toast.LENGTH_LONG).show()
            }
        }

        /* recyclerview 추가 */
        RecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TodoAdapter(this) {
            //Toast.makeText(this, "Todo: ${it.todo}, Date: ${it.date}", Toast.LENGTH_SHORT).show()
        }
        RecyclerView.adapter = adapter

        viewDatabase()
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

    private fun viewDatabase() {
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("$email")?.get()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (dc in task.result!!.documents) {
                        var todo = dc.toObject(Todo::class.java)
                        adapter?.addItem(todo!!)
                    }
                    adapter?.notifyDataSetChanged()
                }
                else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }
}
