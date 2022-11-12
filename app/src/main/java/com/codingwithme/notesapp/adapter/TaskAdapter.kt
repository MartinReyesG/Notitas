package com.codingwithme.notesapp.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codingwithme.notesapp.R
import com.codingwithme.notesapp.entities.Task
import kotlinx.android.synthetic.main.item_rv_task.view.*
import kotlin.collections.ArrayList

class TaskAdapter() :

    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    var listener:OnItemClickListener? = null
    var arrList = ArrayList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_task,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

    fun setData(arrTasksList: List<Task>){
        arrList = arrTasksList as ArrayList<Task>
    }

    fun setOnClickListener(listener1: OnItemClickListener){
        listener = listener1
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        holder.itemView.tvTitle.text = arrList[position].title
        holder.itemView.tvDesc.text = arrList[position].noteText
        holder.itemView.tvDateTime.text = arrList[position].dateTime

        if (arrList[position].color != null){
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor(arrList[position].color))
        }else{
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor(R.color.ColorLightBlack.toString()))
        }

        if (arrList[position].imgPath != null){
            holder.itemView.imgNote.setImageBitmap(BitmapFactory.decodeFile(arrList[position].imgPath))
            holder.itemView.imgNote.visibility = View.VISIBLE
        }else{
            holder.itemView.imgNote.visibility = View.GONE
        }

        if (arrList[position].webLink != ""){
            holder.itemView.tvWebLink.text = arrList[position].webLink
            holder.itemView.tvWebLink.visibility = View.VISIBLE
        }else{
            holder.itemView.tvWebLink.visibility = View.GONE
        }

        holder.itemView.cardView.setOnClickListener {
            listener!!.onClicked(arrList[position].id!!)
        }

    }

    class TaskViewHolder(view:View) : RecyclerView.ViewHolder(view){

    }


    interface OnItemClickListener{
        fun onClicked(taskId:Int)
    }

}