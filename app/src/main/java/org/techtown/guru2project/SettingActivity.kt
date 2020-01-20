package org.techtown.guru2project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_date.*
import org.jetbrains.anko.toast
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

        //프래그먼트로 해야 할 일의 데이터를 넘긴다.


        //객체를 생성해 DB에 저장한다.
        var todo: Todo?
        todo = Todo(name, "", "", 0.0, 0.0, "", "", false)
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("$email")?.document("$name")
            ?.set(todo)?.addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "DB에 새로운 할 일을 추가했습니다.", Toast.LENGTH_LONG).show()
                }else{
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
