package com.codingwithme.notesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codingwithme.notesapp.adapter.TaskAdapter
import com.codingwithme.notesapp.database.NotesDatabase
import com.codingwithme.notesapp.database.TaskDatabase
import com.codingwithme.notesapp.databinding.FragmentCreateTaskBinding
import com.codingwithme.notesapp.databinding.FragmentTaskBinding
import com.codingwithme.notesapp.entities.Notes
import com.codingwithme.notesapp.entities.Task
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class taskFragment : BaseFragment() {

    var arrtask = ArrayList<Task>()
    var taskAdapter: TaskAdapter = TaskAdapter()
    private var _binding: FragmentTaskBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            taskFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.recycler?.setHasFixedSize(true)

        _binding?.recycler?.layoutManager = StaggeredGridLayoutManager(1,
            StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {
                var task = TaskDatabase.getDatabase(it).taskDao().getAllTask()
                taskAdapter!!.setData(task)
                arrtask = task as ArrayList<Task>
                _binding?.recycler?.adapter = taskAdapter
            }
        }

    taskAdapter!!.setOnClickListener(onClicked)


        _binding?.fabBtnCreateNote?.setOnClickListener {
            replaceFragment(CreateTaskFragment.newInstance(),false)
        }

        _binding?.searchView?.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                var tempArr = ArrayList<Task>()

                for (arr in arrtask){
                    if (arr.title!!.toLowerCase(Locale.getDefault()).contains(p0.toString())){
                        tempArr.add(arr)
                    }
                }

                taskAdapter.setData(tempArr)
                taskAdapter.notifyDataSetChanged()
                return true
            }

        })


    }


    private val onClicked = object : TaskAdapter.OnItemClickListener{
        override fun onClicked(taskId: Int) {


            var fragment :Fragment
            var bundle = Bundle()
            bundle.putInt("taskId",taskId)
            fragment = CreateTaskFragment.newInstance()
            fragment.arguments = bundle

            replaceFragment(fragment,false)
        }

    }


    fun replaceFragment(fragment:Fragment, istransition:Boolean){
        val fragmentTransition = activity!!.supportFragmentManager.beginTransaction()

        if (istransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.replace(R.id.frame_layout,fragment).addToBackStack(fragment.javaClass.simpleName).commit()
    }
}