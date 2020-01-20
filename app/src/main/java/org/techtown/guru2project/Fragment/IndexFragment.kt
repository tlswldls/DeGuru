package org.techtown.guru2project.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_index.*
import org.techtown.guru2project.R
import org.techtown.guru2project.SettingActivity
import org.techtown.guru2project.Todo
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class IndexFragment : Fragment() {
    var indexColor: String = "color"
    private var firestore: FirebaseFirestore? = null
    var email:String = ""
    var todoName:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_index, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val extra:Bundle? = this.arguments

        val act = activity as SettingActivity
        email = act.getEmail()
        todoName = act.getName()

        firestore = FirebaseFirestore.getInstance()

        // 클릭이벤트 리스너 추가
        setIndex1.setOnClickListener {
            indexColor = "indexbar1"
            setIndexColor()
        }
        setIndex2.setOnClickListener {
            indexColor = "indexbar2"
            setIndexColor()
        }
        setIndex3.setOnClickListener {
            indexColor = "indexbar3"
            setIndexColor()
        }
        setIndex4.setOnClickListener {
            indexColor = "indexbar4"
            setIndexColor()
        }
        setIndex5.setOnClickListener {
            indexColor = "indexbar5"
            setIndexColor()
        }
    }

    private fun setIndexColor(){
        var map = mutableMapOf<String, Any>()
        map["index"] = indexColor
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("$email")?.document("$todoName")?.update(map)
            ?.addOnCompleteListener { task ->
                if(!task.isSuccessful){
                    indexColor = task.exception?.message.toString()
                }
            }

    }
}