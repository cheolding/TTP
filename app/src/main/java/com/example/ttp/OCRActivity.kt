package com.example.ttp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ttp.databinding.ActivityOcractivityBinding
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.googlecode.tesseract.android.TessBaseAPI
import org.w3c.dom.Text
import java.io.*
import java.text.SimpleDateFormat

class OCRActivity : AppCompatActivity() {
    // ViewBinding

    lateinit var binding : ActivityOcractivityBinding

    //-----------------------ocr
    lateinit var tess : TessBaseAPI //Tesseract API 객체 생성
    var dataPath : String = "" //데이터 경로 변수 선언
    private lateinit var resultTextView: TextView
    //------------------------oce end

    val PERMISSIONS = arrayOf(

        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE

    )
    val PERMISSIONS_REQUEST = 100

    private val BUTTON3 = 300
    private val BUTTON4 = 400
    private val BUTTON5 = 500

    private var photoUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOcractivityBinding.inflate(layoutInflater)
        var view =binding.root
        setContentView(view)
        resultTextView = findViewById(R.id.activityocr_tv_result)





        //-----------------------ocr end

        checkPermissions(PERMISSIONS, PERMISSIONS_REQUEST)






        binding.btn3.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            val photoFile = File(
                File("${filesDir}/image").apply{
                    if(!this.exists()){
                        this.mkdirs()
                    }
                },
                newJpgFileName()

            )
            photoUri = FileProvider.getUriForFile(
                this,
                "com.blacklog.takepicture.fileprovider",
                photoFile
            )
            takePictureIntent.resolveActivity(packageManager)?.also{
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePictureIntent, BUTTON3)
            }

        }
        binding.btn5.setOnClickListener {
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, newJpgFileName())
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/TakePicture")

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            photoUri?.path?.let { it1 -> Log.d("PhotoURI", it1) }
            takePictureIntent.resolveActivity(packageManager)?.also{
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePictureIntent, BUTTON3)
            }
        }
        binding.ocrButton.setOnClickListener{
            val imagePath = "${filesDir}/image/picture.jpg"
            val bitmap = BitmapFactory.decodeFile(imagePath)
            processImage(bitmap)
        }
        //---------------ocr---------
        dataPath = filesDir.toString()+ "/tesseract/" //언어데이터의 경로 미리 지정

        checkFile(File(dataPath+"tessdata/"),"kor") //사용할 언어파일의 이름 지정
        checkFile(File(dataPath+"tessdata/"),"eng")

        var lang : String = "kor+eng"
        tess = TessBaseAPI() //api준비
        tess.init(dataPath,lang) //해당 사용할 언어데이터로 초기화

    }



    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            when(requestCode) {

                BUTTON3, BUTTON4, BUTTON5 -> {
                    val imageBitmap = photoUri?.let { ImageDecoder.createSource(this.contentResolver, it) }
                    binding.imageView.setImageBitmap(imageBitmap?.let { ImageDecoder.decodeBitmap(it) })
                    Toast.makeText(this, photoUri?.path, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    /*@RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            when(requestCode) {

                BUTTON3, BUTTON4, BUTTON5 -> {
                    val imageBitmap = photoUri?.let { uri ->
                        val source = ImageDecoder.createSource(contentResolver, uri)
                        ImageDecoder.decodeBitmap(source)
                    }
                    binding.imageView.setImageBitmap(imageBitmap)
                    Toast.makeText(this, photoUri?.path, Toast.LENGTH_LONG).show()

                    if (imageBitmap != null) {
                        processImage(imageBitmap)
                    } else {
                        Toast.makeText(this, "이미지 로딩 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }*/



    /*private fun saveBitmapAsJPGFile(bitmap: Bitmap) {
        val path = File(filesDir, "image")
        if(!path.exists()){
            path.mkdirs()
        }
        val file = File(path, newJpgFileName())
        var imageFile: OutputStream? = null

        try{
            file.createNewFile()
            imageFile = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageFile)
            imageFile.close()
            Toast.makeText(this, file.absolutePath, Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            null
        }
    }*/
    private fun newJpgFileName() : String {
        val sdf = "picture"      /*SimpleDateFormat("yyyyMMdd_HHmmss")*/
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.jpg"
    }
    private fun checkPermissions(permissions: Array<String>, permissionsRequest: Int): Boolean {
        val permissionList : MutableList<String> = mutableListOf()
        for(permission in permissions){
            val result = ContextCompat.checkSelfPermission(this, permission)
            if(result != PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission)
            }
        }
        if(permissionList.isNotEmpty()){
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), PERMISSIONS_REQUEST)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(result in grantResults){
            if(result != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "권한 승인 부탁드립니다.", Toast.LENGTH_SHORT).show()
                finish()
                Toast.makeText(this, "승인성공", Toast.LENGTH_SHORT).show()
            }
        }
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
