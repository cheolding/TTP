package com.example.ttp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.ttp.databinding.ActivityMainBinding
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient



class MainActivity : AppCompatActivity() {
    //버튼 클릭시 화면을 바꾸기 위한 코드
    private lateinit var binding: ActivityMainBinding
    var toggle: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //버튼 클릭시 화면을 바꾸기 위한 코드
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //화면을 바꾸는 코드 함수 intent 사용

        binding.startButton.setOnClickListener ({
          val intent =Intent(this,LoginActivity::class.java)
            startActivity(intent)
        })
    }
}

