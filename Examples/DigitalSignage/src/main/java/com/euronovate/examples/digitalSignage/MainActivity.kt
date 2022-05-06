package com.euronovate.examples.digitalSignage

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.euronovate.auth.model.ENAuthConfig
import com.euronovate.digitalsignage.ENDigitalSignage
import com.euronovate.digitalsignage.extension.with
import com.euronovate.digitalsignage.model.ENDigitalSignageConfig
import com.euronovate.digitalsignage.model.ENDigitalSignageMediaConfig
import com.euronovate.digitalsignage.model.ENLocalMedia
import com.euronovate.digitalsignage.model.enums.ENDigitalSignageContentType
import com.euronovate.logger.ENLogger
import com.euronovate.logger.extension.with
import com.euronovate.logger.model.ENLanguageConfig
import com.euronovate.logger.model.ENLanguageType
import com.euronovate.logger.model.ENLoggerConfig
import com.euronovate.mobilesdk.ENMobileSDK
import com.euronovate.mobilesdk.callback.ENMobileInitializationCallback
import com.euronovate.mobilesdk.model.ENCertificateOwnerInfo
import com.euronovate.mobilesdk.model.ENMobileSDKResponse
import com.euronovate.mobilesdk.model.ENMobileSdkConfig
import com.euronovate.mobilesdk.model.ENNetworkConfig
import com.euronovate.mobilesdk.theme.ENDefaultTheme
import com.euronovate.mobilesdk.theme.with
import com.euronovate.utils.preferences.ENSettings
import com.euronovate.utils.preferences.with
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * Created by Lorenzo Sogliani on 15/12/2018
 * Copyright (c) 2020 Euronovate. All rights reserved.
 */
class MainActivity : AppCompatActivity(), CoroutineScope by MainScope(), View.OnClickListener, ENMobileInitializationCallback<String> {
    // Class private attributes **********************************************************************************************************************
    private lateinit var btnStart: Button
    private lateinit var btnStartDs: Button
    private var initialized = false

    // Class public functions ************************************************************************************************************************
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        initLibrary()
    }
    private fun initLibrary(){
        ENMobileSDK.Builder()
            .with(context = applicationContext)
            .with(settings = ENSettings.Builder().build())
            .with(logger = ENLogger.Builder()
                .with(ENLoggerConfig(true,ENLogger.VERBOSE))
                .build())
                //TODO QUI
            .with(mobileSdkConfig = ENMobileSdkConfig(networkConfig = ENNetworkConfig(skipSSL = true),
                certificateOwnerInfo = ENCertificateOwnerInfo(),
                languageConfig = ENLanguageConfig(selectorVisible = true,languageEnabled = arrayListOf(ENLanguageType.en,ENLanguageType.el))))
            .with(initializationCallback = this@MainActivity)
            .with(authConfig = ENAuthConfig("your licenseKey", "your server Url",
                jwt =  "eyJhbGciOiJSUzI1NiJ9.eyJlbmMiOnsiZGlwIjoiMTUxLjY4LjEzNi4xNjIiLCJwcmQiOiJ7XCJFTl9NQVBfQU5EX1NPRlRfU0VSVkVSXCI6e1wicHJzXCI6XCJFTlNFUlZcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxNjA3NzYxNyxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9BTkRfQklPTElCXCI6e1wicHJzXCI6XCJCSU9MSUJcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxNjA3NzYxNyxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9BTkRfRElHSVRTSUdOXCI6e1wicHJzXCI6XCJFTkRJR0lcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOntcImxpc3RCdW5kbGVJZFwiOlwiXCJ9LFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfSU9TX1NHTkJPWFwiOntcInByc1wiOlwiU0dOQk9YXCIsXCJwcmNcIjpcIk1BUElPU1wiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTYwNzc2MTcsXCJjc3RcIjpudWxsLFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfQU5EX1ZJRVdFUlwiOntcInByc1wiOlwiVklFV0VSXCIsXCJwcmNcIjpcIk1BUEFORFwiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTU0OTg1NDEsXCJjc3RcIjp7XCJsaXN0QnVuZGxlSWRcIjpcIlwifSxcImV4cHRrblwiOjAsXCJ1cHJcIjpbXCJSRUFERVJcIl19LFwiRU5fTUFQX0lPU19FTkxPR0dFUlwiOntcInByc1wiOlwiTE9HR0VSXCIsXCJwcmNcIjpcIk1BUElPU1wiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTI0MTQ3OTAsXCJjc3RcIjpudWxsLFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfSU9TX1BERk1ETFdSXCI6e1wicHJzXCI6XCJQREZNRExcIixcInByY1wiOlwiTUFQSU9TXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9JT1NfVklFV0VSXCI6e1wicHJzXCI6XCJWSUVXRVJcIixcInByY1wiOlwiTUFQSU9TXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxNTQ5ODU0MSxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9BTkRfUFVCU1VCXCI6e1wicHJzXCI6XCJQVUJTVUJcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9BTkRfRU5MT0dHRVJcIjp7XCJwcnNcIjpcIkxPR0dFUlwiLFwicHJjXCI6XCJNQVBBTkRcIixcImV4cHByZFwiOjI1MjY4NDk5MDAwMDAsXCJpc3NcIjoxNjMxNzEyNDE0NzkwLFwiY3N0XCI6bnVsbCxcImV4cHRrblwiOjAsXCJ1cHJcIjpbXCJSRUFERVJcIl19LFwiRU5fTUFQX0FORF9TR05CT1hcIjp7XCJwcnNcIjpcIlNHTkJPWFwiLFwicHJjXCI6XCJNQVBBTkRcIixcImV4cHByZFwiOjI1MjY4NDk5MDAwMDAsXCJpc3NcIjoxNjMxNzE2MDc3NjE3LFwiY3N0XCI6bnVsbCxcImV4cHRrblwiOjAsXCJ1cHJcIjpbXCJSRUFERVJcIl19LFwiRU5fTUFQX0lPU19TT0ZUX1NFUlZFUlwiOntcInByc1wiOlwiRU5TRVJWXCIsXCJwcmNcIjpcIk1BUElPU1wiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTYwNzc2MTcsXCJjc3RcIjpudWxsLFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfSU9TX1BVQlNVQlwiOntcInByc1wiOlwiUFVCU1VCXCIsXCJwcmNcIjpcIk1BUElPU1wiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTI0MTQ3OTAsXCJjc3RcIjpudWxsLFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfQU5EX1BERk1ETFdSXCI6e1wicHJzXCI6XCJQREZNRExcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9JT1NfRElHSVRTSUdOXCI6e1wicHJzXCI6XCJFTkRJR0lcIixcInByY1wiOlwiTUFQSU9TXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9JT1NfQklPTElCXCI6e1wicHJzXCI6XCJCSU9MSUJcIixcInByY1wiOlwiTUFQSU9TXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxNjA3NzYxNyxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfX0iLCJkaWQiOiJwb3N0bWFuVVVJRCIsImxjZCI6IntcInV1aWRcIjpcImFiYjNkNmNiLTZjN2EtNDVkZC1hOTAzLTgwOTliZGU0M2VjYVwiLFwidXNyXCI6bnVsbCxcIm93blwiOm51bGwsXCJjc3NcIjpcIkdSRUNJQVwiLFwiY3NjXCI6XCJOQkdYWFhcIn0iLCJ1c3IiOiJlbm1vYmFwcCJ9LCJqdGkiOiI2YzRiNDRlMS00YmIwLTRmMGQtOGVmYS02YjQ4M2JlZTAzYjkiLCJpYXQiOjE2MzE3MTkwNzUsImlzcyI6ImVuYXV0aCIsInN1YiI6ImFiYjNkNmNiLTZjN2EtNDVkZC1hOTAzLTgwOTliZGU0M2VjYSJ9.Ts4pFJKYu59dggc02sIIMJVZJ5zMa8PRC55RaXVLQbsO6X-2FLVsHHAckJX9Qs5nFWZQMUybBA9lO6v5e4mtdz2ZXo7PHn03WIJyiIRWPeCQQgFtt7Chah-y9iMRpn0MTkNrIgXCSpQ9qZpss-Lre3RTgr2F_SoAm0CRt8F52s9FrD6ENvSAXtRXC_mWwTRSkzubwHRC5w-bzKrS73YmIIO5Z3ynEDgtWz6A2VRfOOuxezroMbtK81i1e2cKDMUGwY4-ri8Q_UCfZ73RICvFdNlMZNhRTtDhSJ21c7cT9z9XE4SUeGILEUH-Rxx6ZuGfHYTccU8YgQ2o6PbK9mSS9g"))
            .with(theme = ENDefaultTheme())
            .with(ENDigitalSignage.Builder()
                .with(digitalSignageConfig = ENDigitalSignageConfig(baseUrl = "serverUrl", licenseCode = "licenseKey",
                    digitalSignageMediaConfig = ENDigitalSignageMediaConfig()))
                .build())
            .build()
    }

    private fun initUI(){
        btnStart = findViewById(R.id.btnStart)
        btnStart.setOnClickListener(this)
        btnStart.isEnabled = initialized

        btnStartDs = findViewById(R.id.btnStartDs)
        btnStartDs.setOnClickListener(this)
        btnStartDs.isEnabled = initialized
    }
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnStart ->{
                ENDigitalSignage.getInstance().start()
            }
            R.id.btnStartDs ->{
                ENDigitalSignage.getInstance().digitalSignageConfig.digitalSignageMediaConfig!!.localMediaContents = arrayListOf(
                    ENLocalMedia(assetName = "landscape_placeholder.png",duration = 5000, ENDigitalSignageContentType.Image),
                    ENLocalMedia(assetName = "landscape_placeholder.png",duration = 4000, ENDigitalSignageContentType.Image),
                    ENLocalMedia(assetName = "landscape_placeholder.png",duration = 3000, ENDigitalSignageContentType.Image))
                ENDigitalSignage.getInstance().start()
            }
        }
    }
    override fun didGetResponse(response: ENMobileSDKResponse<String>?) {
        when (response) {
            is ENMobileSDKResponse.error -> {
                //TODO
            }
            is ENMobileSDKResponse.success -> {
                initialized = true
                btnStart.isEnabled = initialized
                btnStartDs.isEnabled = initialized
            }
        }
    }
}