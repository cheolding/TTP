package com.example.ttp

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.AuthCodeClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import java.lang.reflect.Modifier as Modifier1

class GlobalApplication:Application() {
    override fun onCreate() {

        //카카오 키를 위한 클래스 어플리케이션을 상속받음
        super.onCreate()
        KakaoSdk.init(this, "82aba16b4eb3bb4c5a5a42b88870cfe9")


    }

}
