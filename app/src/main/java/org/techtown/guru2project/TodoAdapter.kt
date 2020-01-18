package org.techtown.guru2project

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.todo_item.view.*
import java.util.*

class TodoAdapter (val context: Context, val itemCheck: (Todo) -> Unit)
    : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {
    private var items = ArrayList<Todo>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val itemView: View = inflater.inflate(R.layout.todo_item, viewGroup, false)
        return ViewHolder(itemView, itemCheck)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: Todo = items[position]
        viewHolder.setItem(item)
    }

    override fun getItemCount(): Int {
        return items.count()
    }


    fun setItems(items: ArrayList<Todo>) {
        this.items = items
    }

    fun addItem(item: Todo) {
        items.add(item)
    }

    inner class ViewHolder(itemView: View, itemCheck: (Todo) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        fun setItem(item: Todo) {
            val resourceId = context.resources.getIdentifier(
                item.index,
                "drawable",
                context.packageName
            )
            if (resourceId in 0..1) {
                itemView.imageView?.setImageResource(R.drawable.pinkindex)
            }
            else {
                itemView.imageView?.setImageResource(resourceId)
            }
            var isStriked: Boolean = false
            itemView.tvTodo.text = item.todo
            itemView.setOnClickListener(){
                // 할 일 완료 후 취소선 넣기
                var length: Int = item.todo.length
                var SS: SpannableString = SpannableString(item.todo)
                if(isStriked == false) {
                    SS.setSpan(StrikethroughSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    itemView.tvTodo.text = SS
                    isStriked = true
                }
                else if(isStriked == true) {
                    SS.removeSpan(StrikethroughSpan())
                    itemView.tvTodo.text = SS
                    isStriked = false
                }
            }
        }
    }
}
