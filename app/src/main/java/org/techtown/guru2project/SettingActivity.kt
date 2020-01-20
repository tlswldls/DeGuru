package org.techtown.guru2project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_setting.*
import org.techtown.guru2project.Fragment.*

class SettingActivity : AppCompatActivity() {
    private var firestore: FirebaseFirestore? = null
    private var email:String? = null
    private var name:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        //메인에서 사용자의 메일 데이터, 해야 할 일의 이름 데이터를 받아온다.
        email = intent.getStringExtra("mail")
        name = intent.getStringExtra("todo")

        //객체를 생성해 DB에 저장한다.
        var todo: Todo?
        todo = Todo(name, "", "", 0.0, 0.0, "", "", false)
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("$email")?.document("$name")
            ?.set(todo)?.addOnCompleteListener { task ->
                if(!task.isSuccessful){
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }

        //DB에 디폴트로 회원가입할 때 넣어주는 데이터가 있는지 확인
        firestore?.collection("$email")?.document("Enter what you have to do")
            ?.get()?.addOnCompleteListener { task ->
                if(task.isSuccessful){
                    //있으면 DB에서 삭제하기
                    firestore?.collection("$email")?.document("Enter what you have to do")?.delete()
                        ?.addOnCompleteListener { task->
                            if(!task.isSuccessful){
                                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }

        /* tabLayout, viewPager 설정 */
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DateFragment(), "Date")
        adapter.addFragment(LocationFragment(), "Location")
        adapter.addFragment(IndexFragment(), "Index")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)







        // tab 아이콘
//        tabLayout.getTabAt(0)?.setIcon(R.drawable.icon1)
//        tabLayout.getTabAt(1)?.setIcon(R.drawable.icon2)
//        tabLayout.getTabAt(2)?.setIcon(R.drawable.icon3)

        backBtn_menu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mail", email)
            startActivity(intent)
            finish()
        }

    }

    public fun getEmail():String {
        return email!!
    }

    public fun getName(): String {
        return name!!
    }
}
