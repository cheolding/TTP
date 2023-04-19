package com.example.ttp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.ttp.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding:ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)


        binding.button1.text = "하이"
        binding.button1.setBackgroundColor(Color.BLACK)
        binding.button1.setOnClickListener(listener1)
        binding.button1.setOnClickListener(listener2)

    }
    val listener1 = View.OnClickListener {
        binding.textView1.text = "첫 번째 버튼을 눌렀습니다."
    }
    val listener2 = View.OnClickListener {
        binding.textView1.text = "두 번째 버튼을 눌렀습니다."
    }
}