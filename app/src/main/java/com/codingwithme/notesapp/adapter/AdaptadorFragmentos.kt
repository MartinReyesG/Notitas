package com.codingwithme.notesapp.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.codingwithme.notesapp.HomeFragment
import com.codingwithme.notesapp.taskFragment

class AdaptadorFragmentos(fa:FragmentActivity) :FragmentStateAdapter(fa){

    companion object{
        private  const val ARG_OBJECT = "object"
    }

    //Se colcoa el numero el numero de paginas que se usaran en el view page
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        val fragment = HomeFragment()
        fragment.arguments = Bundle().apply { putInt(ARG_OBJECT,position+1) }
        return when(position){
            0-> {HomeFragment()}
            1-> {taskFragment()}
            else-> {HomeFragment()}
        }
    }
}