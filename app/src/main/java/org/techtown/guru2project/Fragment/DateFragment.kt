package org.techtown.guru2project.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_date.*
import org.techtown.guru2project.R
import org.techtown.guru2project.SettingActivity
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
lateinit var calendar: Calendar
class DateFragment : Fragment() {
    private var firestore: FirebaseFirestore? = null

    var email:String = ""
    var name:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val act = activity as SettingActivity
        email = act.getEmail()
        name = act.getName()

        btnSet.setOnClickListener {
            val date = date_picker.year.toString() +" / "+ (date_picker.month+1).toString() + " / " + date_picker.dayOfMonth.toString()
            setDate(date)
        }
    }

    private fun setDate(date: String){
        var map = mutableMapOf<String, Any>()
        map["date"] = date
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("$email")?.document("$name")?.update(map)
            ?.addOnCompleteListener { task ->
                if(!task.isSuccessful){
                    textView.text = task.exception?.message.toString()
                }
            }

    }

}