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

    private fun initLibrary() {
        ENMobileSDK.Builder()
            .with(context = applicationContext)
            .with(settings = ENSettings.Builder().build())
            .with(
                logger = ENLogger.Builder()
                    .with(ENLoggerConfig(true, ENLogger.VERBOSE))
                    .build()
            )
            //TODO QUI
            .with(
                mobileSdkConfig = ENMobileSdkConfig(
                    networkConfig = ENNetworkConfig(skipSSL = true),
                    certificateOwnerInfo = ENCertificateOwnerInfo(),
                    languageConfig = ENLanguageConfig(selectorVisible = true, languageEnabled = arrayListOf(ENLanguageType.en, ENLanguageType.el))
                )
            )
            .with(initializationCallback = this@MainActivity)
            .with(
                authConfig = ENAuthConfig(
                    "your licenseKey", "your server Url",
                    jwt = "your jwt optional if you set licensekey and serverUrl"
                )
            )
            .with(theme = ENDefaultTheme())
            .with(
                ENDigitalSignage.Builder()
                    .with(
                        digitalSignageConfig = ENDigitalSignageConfig(
                            baseUrl = "serverUrl", licenseCode = "licenseKey",
                            digitalSignageMediaConfig = ENDigitalSignageMediaConfig(
                                landPlaceholderAssetName = "fixed_land.jpeg", portPlaceholderAssetName = "fixed_portrait.jpeg",
                                localMediaContents = arrayListOf(
                                    ENLocalMedia(assetName = "fixed_land.jpeg", duration = 5000, ENDigitalSignageContentType.Image),
                                    ENLocalMedia(assetName = "fixed_land.jpeg", duration = 4000, ENDigitalSignageContentType.Image),
                                    ENLocalMedia(assetName = "fixed_land.jpeg", duration = 3000, ENDigitalSignageContentType.Image),
                                    ENLocalMedia(assetName = "fixed_portrait.jpeg", duration = 3000, ENDigitalSignageContentType.Image)
                                )
                            )
                        )
                    )
                    .build()
            )
            .build()
    }

    private fun initUI() {
        btnStart = findViewById(R.id.btnStart)
        btnStart.setOnClickListener(this)
        btnStart.isEnabled = initialized

        btnStartDs = findViewById(R.id.btnStartDs)
        btnStartDs.setOnClickListener(this)
        btnStartDs.isEnabled = initialized
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnStart -> {
                ENDigitalSignage.getInstance().start()
            }

            R.id.btnStartDs -> {
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

            else -> {}
        }
    }
}