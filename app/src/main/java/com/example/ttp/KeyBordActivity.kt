package com.example.ttp

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class KeyBordActivity : AppCompatActivity() {

    lateinit var dbHelper : DBHelper
    lateinit var database :SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_bord)

        //데이터베이스 관련 코드
        dbHelper = DBHelper(this, "newdb.db", null, 1)
        database = dbHelper.writableDatabase

        //문자 입력후 버튼 클릭시 이벤트 처리
        val button = findViewById<Button>(R.id.input_Button)
        button.setOnClickListener {

            val editText = findViewById<EditText>(R.id.inputText)
            val text = editText.text.toString()
            //db에 저장하는 코드
            var contentValues = ContentValues()
            contentValues.put("txt",text)
            database.insert("mytable",null,contentValues)


            //데이터베이스에 데이터가 들어갔는지 확인하는 로직
            val result = database.insert("mytable", null, contentValues)
            if (result == -1L) {
                // 데이터 삽입 실패 시
                Toast.makeText(this, "데이터 삽입 실패", Toast.LENGTH_SHORT).show()
            } else {
                // 데이터 삽입 성공 시
                Toast.makeText(this, "데이터 삽입 성공", Toast.LENGTH_SHORT).show()

                // 입력된 텍스트 값을 사용하는 로직을 추가해야 함
            }


            //입력후 화면이 이동하는 코드
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("input_text", text)
            startActivity(intent)
        }
    }
}