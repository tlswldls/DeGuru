package org.techtown.guru2project.Fragment

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_date.*
import org.techtown.guru2project.R
import org.techtown.guru2project.SettingActivity
import java.text.DateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
lateinit var calendar: Calendar
class DateFragment : Fragment() {

    //var email:String = mailAdr.text.toString()
    //var todo:String = todoName.text.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSet.setOnClickListener {
            val date = date_picker.year.toString() +" / "+ date_picker.month+1.toString() + " / " + date_picker.dayOfMonth.toString()
            textView.text = date


        }
    }
}