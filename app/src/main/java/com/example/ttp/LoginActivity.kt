package com.example.ttp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.ttp.databinding.ActivityLoginBinding
import com.example.ttp.databinding.ActivityMainBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {
    //버튼 클릭시 화면을 바꾸기 위한 코드

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        fun kakaologin() {
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                }
                else if (token != null) {
                    Log.e(TAG, "로그인 성공 ${token.accessToken}")
                    val intent =Intent(this,MenuActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun kakaoLogout() {
            // 로그아웃
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("Hello", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                } else {
                    Log.i("Hello", "로그아웃 성공. SDK에서 토큰 삭제됨")
                    val intent =Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //callback을 위한 람다 함수
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오 로그인 실패", error)
                } else if (token != null) {
                    Log.i(TAG, "카카오 로그인 성공: ${token.accessToken}")
                    // 로그인 성공 시 처리할 코드 작성
                }
            }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        fun nokakao()
        {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("LOGIN", "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i("LOGIN", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }



        // 로그인 버튼 클릭 이벤트 처리
        val loginButton = findViewById<Button>(R.id.loginkakao)
        loginButton.setOnClickListener {
            kakaologin()
        }

        //로그아웃 버튼 클릭 이벤트 처리
        val logoutButton = findViewById<Button>(R.id.logoutkakao)
        logoutButton.setOnClickListener{
            kakaoLogout()
        }
    }

}