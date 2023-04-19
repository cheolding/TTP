package com.example.ttp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ttp.databinding.ActivityMainBinding

import com.example.ttp.databinding.ActivityMenuBinding


class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)


        //화면이동 관련 코드
        binding = ActivityMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.keybordButton.setOnClickListener {
            val intent = Intent(this, KeyBordActivity::class.java)
            startActivity(intent)
        }

        binding.cameraButton.setOnClickListener {
            val intent = Intent(this, OCRActivity::class.java)
            startActivity(intent)
        }
        binding.settingButton.setOnClickListener {
            val intent = Intent(this, OCRViewActivity::class.java)
            startActivity(intent)
        }
        binding.imsiButton.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

    }
}