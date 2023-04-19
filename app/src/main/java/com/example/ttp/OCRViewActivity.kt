package com.example.ttp

import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.*
import android.Manifest
import android.os.Environment
import android.provider.MediaStore
import java.text.SimpleDateFormat


class OCRViewActivity : AppCompatActivity() {

    lateinit var tess : TessBaseAPI //Tesseract API 객체 생성
    var dataPath : String = "" //데이터 경로 변수 선언
    private lateinit var resultTextView: TextView// Permisisons

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocrview)

        resultTextView = findViewById(R.id.activityocrview_tv_result)

        dataPath = filesDir.toString()+ "/tesseract/" //언어데이터의 경로 미리 지정

        checkFile(File(dataPath+"tessdata/"),"kor") //사용할 언어파일의 이름 지정
        checkFile(File(dataPath+"tessdata/"),"eng")

        var lang : String = "kor+eng"
        tess = TessBaseAPI() //api준비
        tess.init(dataPath,lang) //해당 사용할 언어데이터로 초기화

        processImage(BitmapFactory.decodeResource(resources,R.drawable.koreantest)) //이미지 가공후 텍스트뷰에 띄우기


       /* if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " DESC"
            )
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val imagePath = cursor.getString(columnIndex)
                cursor.close()
                val file = File(imagePath)
                val bitmap = BitmapFactory.decodeFile(file.path)
                processImage(bitmap)
            } else {
                Toast.makeText(applicationContext, "No image found", Toast.LENGTH_SHORT).show()
            }
        }*/



    }







    //----------------------------ocr start-----------------------------------
    fun processImage(bitmap : Bitmap){
        Toast.makeText(applicationContext,"잠시 기다려 주세요",Toast.LENGTH_SHORT).show()
        var ocrResult : String? = null;
        tess.setImage(bitmap)
        ocrResult = tess.utF8Text
        resultTextView.text = ocrResult
    }
    fun copyFile(lang : String){
        try{
            //언어데이타파일의 위치
            var filePath : String = dataPath+"/tessdata/"+lang+".traineddata"

            //AssetManager를 사용하기 위한 객체 생성
            var assetManager : AssetManager = getAssets();

            //byte 스트림을 읽기 쓰기용으로 열기
            var inputStream : InputStream = assetManager.open("tessdata/"+lang+".traineddata")
            var outStream : OutputStream = FileOutputStream(filePath)


            //위에 적어둔 파일 경로쪽으로 해당 바이트코드 파일을 복사한다.
            var buffer : ByteArray = ByteArray(1024)

            var read : Int = 0
            read = inputStream.read(buffer)
            while(read!=-1){
                outStream.write(buffer,0,read)
                read = inputStream.read(buffer)
            }
            outStream.flush()
            outStream.close()
            inputStream.close()

        }catch(e : FileNotFoundException){
            Log.v("오류발생",e.toString())
        }catch (e : IOException)
        {
            Log.v("오류발생",e.toString())
        }
    }
    fun checkFile(dir : File, lang : String){

        //파일의 존재여부 확인 후 내부로 복사
        if(!dir.exists()&&dir.mkdirs()){
            copyFile(lang)
        }

        if(dir.exists()){
            var datafilePath : String = dataPath+"/tessdata/"+lang+".traineddata"
            var dataFile : File = File(datafilePath)
            if(!dataFile.exists()){
                copyFile(lang)
            }
        }

    }

    //----------------------------ocr end-----------------------------------


}