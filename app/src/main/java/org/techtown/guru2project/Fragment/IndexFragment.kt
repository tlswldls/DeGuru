package org.techtown.guru2project.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_index.*
import org.techtown.guru2project.R
import org.techtown.guru2project.Todo
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class IndexFragment : Fragment() {
    var indexColor: String = "color"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_index, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 클릭이벤트 리스너 추가
        setIndex1.setOnClickListener {
            indexColor = "setIndex1"
        }
        setIndex2.setOnClickListener {
            indexColor = "setIndex2"
        }
        setIndex3.setOnClickListener {
            indexColor = "setIndex3"
        }
        setIndex4.setOnClickListener {
            indexColor = "setIndex4"
        }
        setIndex5.setOnClickListener {
            indexColor = "setIndex5"
        }
        btnSetIndex.setOnClickListener {
            textView3.setText("$indexColor 로 설정")
        }
    }
}