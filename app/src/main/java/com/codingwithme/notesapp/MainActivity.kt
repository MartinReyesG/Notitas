package com.codingwithme.notesapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.codingwithme.notesapp.adapter.AdaptadorFragmentos
import com.codingwithme.notesapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    private val adaptadorFragmentos by lazy { AdaptadorFragmentos(this) }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ActivityMain
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.viewfragment.adapter=adaptadorFragmentos
        val tabLayoutMediator = TabLayoutMediator(binding.tabPrincipal,binding.viewfragment,
            TabLayoutMediator.TabConfigurationStrategy{
                tab, position -> when(position+1){
            1->{
                tab.text="Notas"
            }
            2->{
                tab.text="Tareas"
            }
        }
        })
        tabLayoutMediator.attach()


    }
}