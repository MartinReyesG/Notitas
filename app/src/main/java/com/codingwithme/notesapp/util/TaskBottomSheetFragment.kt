package com.codingwithme.notesapp.util

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.codingwithme.notesapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_task_bottom_sheet.*

class TaskBottomSheetFragment : BottomSheetDialogFragment() {
    var selectedColor = "#171C26"


    companion object {
        var taskId = -1
        fun newInstance(id:Int): TaskBottomSheetFragment{
            val args = Bundle()
            val fragment = TaskBottomSheetFragment()
            fragment.arguments = args
            taskId = id
            return fragment
        }
    }
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_task_bottom_sheet,null)
        dialog.setContentView(view)

        val param = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams

        val behavior = param.behavior

        if (behavior is BottomSheetBehavior<*>){
            behavior.setBottomSheetCallback(object  : BottomSheetBehavior.BottomSheetCallback(){
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    TODO("Not yet implemented")
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    var state = ""
                    when (newState) {
                        BottomSheetBehavior.STATE_DRAGGING -> {
                            state = "DRAGGING"
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                            state = "SETTLING"
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            state = "EXPANDED"
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            state = "COLLAPSED"
                        }

                        BottomSheetBehavior.STATE_HIDDEN -> {
                            state = "HIDDEN"
                            dismiss()
                            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        }

                    }
                }

            })


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes_bottom_sheet,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (taskId != -1){
            layoutDeleteTask.visibility = View.VISIBLE
        }else{
            layoutDeleteTask.visibility = View.GONE
        }
        setListener()
    }

    private fun setListener(){
        fTask1.setOnClickListener {

            imgTask1.setImageResource(R.drawable.ic_tick)
            imgTask2.setImageResource(0)
            imgTask4.setImageResource(0)
            imgTask5.setImageResource(0)
            imgTask6.setImageResource(0)
            imgTask7.setImageResource(0)
            selectedColor = "#4e33ff"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","Blue")
            intent.putExtra("selectedColor",selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }

        fTask2.setOnClickListener {
            imgTask1.setImageResource(0)
            imgTask2.setImageResource(R.drawable.ic_tick)
            imgTask4.setImageResource(0)
            imgTask5.setImageResource(0)
            imgTask6.setImageResource(0)
            imgTask7.setImageResource(0)
            selectedColor = "#ffd633"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","Yellow")
            intent.putExtra("selectedColor",selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }

        fTask4.setOnClickListener {
            imgTask1.setImageResource(0)
            imgTask2.setImageResource(0)
            imgTask4.setImageResource(R.drawable.ic_tick)
            imgTask5.setImageResource(0)
            imgTask6.setImageResource(0)
            imgTask7.setImageResource(0)
            selectedColor = "#ae3b76"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","Purple")
            intent.putExtra("selectedColor",selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }

        fTask5.setOnClickListener {
            imgTask1.setImageResource(0)
            imgTask2.setImageResource(0)
            imgTask4.setImageResource(0)
            imgTask5.setImageResource(R.drawable.ic_tick)
            imgTask6.setImageResource(0)
            imgTask7.setImageResource(0)
            selectedColor = "#0aebaf"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","Green")
            intent.putExtra("selectedColor",selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }

        fTask6.setOnClickListener {

            imgTask1.setImageResource(0)
            imgTask2.setImageResource(0)
            imgTask4.setImageResource(0)
            imgTask5.setImageResource(0)
            imgTask6.setImageResource(R.drawable.ic_tick)
            imgTask7.setImageResource(0)
            selectedColor = "#ff7746"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","Orange")
            intent.putExtra("selectedColor",selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }

        fTask7.setOnClickListener {
            imgTask1.setImageResource(0)
            imgTask2.setImageResource(0)
            imgTask4.setImageResource(0)
            imgTask5.setImageResource(0)
            imgTask6.setImageResource(0)
            imgTask7.setImageResource(R.drawable.ic_tick)
            selectedColor = "#202734"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","Black")
            intent.putExtra("selectedColor",selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }

        layoutImage.setOnClickListener{
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","Image")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }
        layoutWebUrl.setOnClickListener{
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","WebUrl")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }
        layoutDeleteTask.setOnClickListener {
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","DeleteTask")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }




    }


}