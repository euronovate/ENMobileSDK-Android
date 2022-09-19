package com.euronovate.examples.multilayoutviewersignaturebox

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.euronovate.auth.model.ENAuthConfig
import com.euronovate.bio.ENBio
import com.euronovate.bio.extension.with
import com.euronovate.bio.model.enums.ENSignatureSourceType
import com.euronovate.logger.ENLogger
import com.euronovate.logger.extension.with
import com.euronovate.logger.model.ENLanguageConfig
import com.euronovate.logger.model.ENLanguageType
import com.euronovate.logger.model.ENLoggerConfig
import com.euronovate.mobilesdk.ENMobileSDK
import com.euronovate.mobilesdk.extensions.emitEvent
import com.euronovate.mobilesdk.model.*
import com.euronovate.mobilesdk.model.enums.ENDocumentSourceType
import com.euronovate.mobilesdk.model.enums.ENEventType
import com.euronovate.mobilesdk.model.events.ENSignDocument
import com.euronovate.mobilesdk.ui.dialog.ENDialog
import com.euronovate.mobilesdk.ui.dialog.ENDialogTextConfig
import com.euronovate.mobilesdk.ui.dialog.ENDialogType
import com.euronovate.pdfmiddleware.ENPdfMiddleware
import com.euronovate.pdfmiddleware.extension.with
import com.euronovate.signaturebox.ENSignatureBox
import com.euronovate.signaturebox.extension.with
import com.euronovate.signaturebox.model.ENSignatureBoxConfig
import com.euronovate.signaturebox.model.ENSignatureImageConfig
import com.euronovate.signaturebox.model.ENSignatureImageModeConfig
import com.euronovate.signaturebox.model.enums.ENSignatureContentMode
import com.euronovate.utils.preferences.ENSettings
import com.euronovate.utils.preferences.with
import com.euronovate.utils.util.ENBase64Utils
import com.euronovate.utils.util.ENFileUtils
import com.euronovate.viewer.ENViewer
import com.euronovate.viewer.extension.openDocument
import com.euronovate.viewer.extension.with
import com.euronovate.viewer.model.ENViewerConfig
import com.euronovate.viewer.model.enums.ENSignFieldPlaceholder
import com.euronovate.viewer.model.enums.ENViewerBarType
import com.euronovate.viewer.model.enums.ENViewerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.Runnable
import java.io.File

/**
 * Created by Lorenzo Sogliani on 15/12/2018
 * Copyright (c) 2020 Euronovate. All rights reserved.
 */
class MainActivity : AppCompatActivity(), CoroutineScope by MainScope(), View.OnClickListener {
    // Class private attributes **********************************************************************************************************************
    private lateinit var btnStartSimple: Button
    private lateinit var btnShareLastPdf: Button
    private lateinit var btnStartLastPdf: Button
    private lateinit var btnStartTheme1: Button

    // Class public functions ************************************************************************************************************************
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }
    private fun initUI(){
        btnStartSimple = findViewById(R.id.btnStartSimple)
        btnStartSimple.setOnClickListener(this)

        btnStartLastPdf = findViewById(R.id.btnStartLastPdf)
        btnStartLastPdf.setOnClickListener(this)

        btnShareLastPdf = findViewById(R.id.btnShareLastPdf)
        btnShareLastPdf.setOnClickListener(this)

        btnStartTheme1 = findViewById(R.id.btnStartTheme1)
        btnStartTheme1.setOnClickListener(this)
    }
    private fun showProgressDialog(title: String? = getString(R.string.info), content: String? = getString(
        R.string.please_wait
    )): Dialog {
        val dialogProgress = ENDialog.getInstance().dialog(activity = this, dialogType = ENDialogType.horizontalProgress,
            dialogTextConfig = ENDialogTextConfig(title = title, content = content))
        dialogProgress.show()
        return dialogProgress
    }
    var progressDialog: Dialog?=null
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnStartTheme1 ->{
                startDocument(ENViewerBarType.theme1,ENViewerType.theme1)
            }
            R.id.btnStartSimple->{
                startDocument(ENViewerBarType.simple,ENViewerType.simple)
            }
            R.id.btnShareLastPdf ->{
                val lastPdf = ENFileUtils.getLastModified(cacheDir.absolutePath)
                if(lastPdf != null && lastPdf.exists()){
                    val emailIntent = Intent(Intent.ACTION_SEND)
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pdf")
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                    emailIntent.putExtra(Intent.EXTRA_STREAM,
                        FileProvider.getUriForFile(this, "$packageName.provider", lastPdf))
                    emailIntent.setTypeAndNormalize(ENFileUtils.getMimeType(lastPdf.absolutePath))
                    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(Intent.createChooser(emailIntent, getString(R.string.send_with)))
                }
            }
            R.id.btnStartLastPdf ->{
                val lastPdf = ENFileUtils.getLastModified(cacheDir.absolutePath)
                if(lastPdf != null && lastPdf.exists()){
                    ENMobileSDK.emitEvent(ENEventType.viewDocument, ENSignDocument(documentType = ENDocumentSourceType.path(lastPdf.absolutePath)))
                }
            }
        }
    }
    var sdkInitialized: Boolean= false
    private fun startDocument(viewerBarType: ENViewerBarType, viewerType: ENViewerType){
        if(sdkInitialized){
            ENMobileSDK.free()
        }
        ENMobileSDK.Builder()
            .with(context = applicationContext)
            .with(settings = ENSettings.Builder().build())
            .with(logger = ENLogger.Builder()
                .with(ENLoggerConfig(true,ENLogger.VERBOSE))
                .build())
            .with(initializationCallback = {
                when (it) {
                    is ENMobileSDKResponse.error -> {
                        //TODO
                    }
                    is ENMobileSDKResponse.success -> {
                        sdkInitialized = true
                        progressDialog = showProgressDialog(title = getString(R.string.starting_signature),content = getString(
                            R.string.please_wait
                        ))
                        val pdfName = "Demo12_Verdi_PDFA1b.pdf"
                        val pdfFile = File(getExternalFilesDir(null)!!,pdfName)
                        ENFileUtils.copyAssetFileToInternalStorage(applicationContext.assets,getExternalFilesDir(null)!!,pdfName)
                        ENMobileSDK.getInstance().openDocument(documentBase64 = ENBase64Utils.toBase64(ENFileUtils.getBytes(pdfFile.inputStream())!!),
                            certPemBase64 = ENBase64Utils.toBase64(application.assets.open("encert.pem").readBytes()))
                        Thread(Runnable {
                            runOnUiThread {
                                progressDialog!!.dismiss()
                            }
                        }).start()
                    }
                    else -> {}
                }
            })
            .with(authConfig = ENAuthConfig("your licenseKey", "your server Url",
                jwt = "eyJhbGciOiJSUzI1NiJ9.eyJlbmMiOnsiZGlwIjoiMTUxLjY4LjEzNi4xNjIiLCJwcmQiOiJ7XCJFTl9NQVBfQU5EX1NPRlRfU0VSVkVSXCI6e1wicHJzXCI6XCJFTlNFUlZcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxNjA3NzYxNyxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9BTkRfQklPTElCXCI6e1wicHJzXCI6XCJCSU9MSUJcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxNjA3NzYxNyxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9BTkRfRElHSVRTSUdOXCI6e1wicHJzXCI6XCJFTkRJR0lcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOntcImxpc3RCdW5kbGVJZFwiOlwiXCJ9LFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfSU9TX1NHTkJPWFwiOntcInByc1wiOlwiU0dOQk9YXCIsXCJwcmNcIjpcIk1BUElPU1wiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTYwNzc2MTcsXCJjc3RcIjpudWxsLFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfQU5EX1ZJRVdFUlwiOntcInByc1wiOlwiVklFV0VSXCIsXCJwcmNcIjpcIk1BUEFORFwiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTU0OTg1NDEsXCJjc3RcIjp7XCJsaXN0QnVuZGxlSWRcIjpcIlwifSxcImV4cHRrblwiOjAsXCJ1cHJcIjpbXCJSRUFERVJcIl19LFwiRU5fTUFQX0lPU19FTkxPR0dFUlwiOntcInByc1wiOlwiTE9HR0VSXCIsXCJwcmNcIjpcIk1BUElPU1wiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTI0MTQ3OTAsXCJjc3RcIjpudWxsLFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfSU9TX1BERk1ETFdSXCI6e1wicHJzXCI6XCJQREZNRExcIixcInByY1wiOlwiTUFQSU9TXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9JT1NfVklFV0VSXCI6e1wicHJzXCI6XCJWSUVXRVJcIixcInByY1wiOlwiTUFQSU9TXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxNTQ5ODU0MSxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9BTkRfUFVCU1VCXCI6e1wicHJzXCI6XCJQVUJTVUJcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9BTkRfRU5MT0dHRVJcIjp7XCJwcnNcIjpcIkxPR0dFUlwiLFwicHJjXCI6XCJNQVBBTkRcIixcImV4cHByZFwiOjI1MjY4NDk5MDAwMDAsXCJpc3NcIjoxNjMxNzEyNDE0NzkwLFwiY3N0XCI6bnVsbCxcImV4cHRrblwiOjAsXCJ1cHJcIjpbXCJSRUFERVJcIl19LFwiRU5fTUFQX0FORF9TR05CT1hcIjp7XCJwcnNcIjpcIlNHTkJPWFwiLFwicHJjXCI6XCJNQVBBTkRcIixcImV4cHByZFwiOjI1MjY4NDk5MDAwMDAsXCJpc3NcIjoxNjMxNzE2MDc3NjE3LFwiY3N0XCI6bnVsbCxcImV4cHRrblwiOjAsXCJ1cHJcIjpbXCJSRUFERVJcIl19LFwiRU5fTUFQX0lPU19TT0ZUX1NFUlZFUlwiOntcInByc1wiOlwiRU5TRVJWXCIsXCJwcmNcIjpcIk1BUElPU1wiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTYwNzc2MTcsXCJjc3RcIjpudWxsLFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfSU9TX1BVQlNVQlwiOntcInByc1wiOlwiUFVCU1VCXCIsXCJwcmNcIjpcIk1BUElPU1wiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTI0MTQ3OTAsXCJjc3RcIjpudWxsLFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfQU5EX1BERk1ETFdSXCI6e1wicHJzXCI6XCJQREZNRExcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9JT1NfRElHSVRTSUdOXCI6e1wicHJzXCI6XCJFTkRJR0lcIixcInByY1wiOlwiTUFQSU9TXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9JT1NfQklPTElCXCI6e1wicHJzXCI6XCJCSU9MSUJcIixcInByY1wiOlwiTUFQSU9TXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxNjA3NzYxNyxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfX0iLCJkaWQiOiJwb3N0bWFuVVVJRCIsImxjZCI6IntcInV1aWRcIjpcImFiYjNkNmNiLTZjN2EtNDVkZC1hOTAzLTgwOTliZGU0M2VjYVwiLFwidXNyXCI6bnVsbCxcIm93blwiOm51bGwsXCJjc3NcIjpcIkdSRUNJQVwiLFwiY3NjXCI6XCJOQkdYWFhcIn0iLCJ1c3IiOiJlbm1vYmFwcCJ9LCJqdGkiOiI2YzRiNDRlMS00YmIwLTRmMGQtOGVmYS02YjQ4M2JlZTAzYjkiLCJpYXQiOjE2MzE3MTkwNzUsImlzcyI6ImVuYXV0aCIsInN1YiI6ImFiYjNkNmNiLTZjN2EtNDVkZC1hOTAzLTgwOTliZGU0M2VjYSJ9.Ts4pFJKYu59dggc02sIIMJVZJ5zMa8PRC55RaXVLQbsO6X-2FLVsHHAckJX9Qs5nFWZQMUybBA9lO6v5e4mtdz2ZXo7PHn03WIJyiIRWPeCQQgFtt7Chah-y9iMRpn0MTkNrIgXCSpQ9qZpss-Lre3RTgr2F_SoAm0CRt8F52s9FrD6ENvSAXtRXC_mWwTRSkzubwHRC5w-bzKrS73YmIIO5Z3ynEDgtWz6A2VRfOOuxezroMbtK81i1e2cKDMUGwY4-ri8Q_UCfZ73RICvFdNlMZNhRTtDhSJ21c7cT9z9XE4SUeGILEUH-Rxx6ZuGfHYTccU8YgQ2o6PbK9mSS9g"))
            .with(ENMobileSdkConfig(certificateOwnerInfo = ENCertificateOwnerInfo(),
                languageConfig = ENLanguageConfig(selectorVisible = true, languageEnabled = arrayListOf(ENLanguageType.en)), networkConfig = ENNetworkConfig()))
            .with(ENViewer.Builder()
                .with(ENViewerConfig(signFieldPlaceholder = ENSignFieldPlaceholder.defaultPlaceholder(), viewerType = viewerType,
                    viewerBarType = viewerBarType))
                .build())
            .with(ENPdfMiddleware.Builder().build())
            .with(ENSignatureBox.Builder()
                .with(signatureBoxConfig = ENSignatureBoxConfig(
                    signatureSourceType = ENSignatureSourceType.Any,
                    signatureImageConfig = ENSignatureImageConfig(useAlpha = true,
                        signatureContentMode = ENSignatureContentMode.keepFieldRatio,
                        signatureImageModeConfig = ENSignatureImageModeConfig.signatureSignerNameAndTimestamp(watermarkReservedHeight = 0.3f))))
                .build())
            .with(ENBio.Builder()
                .build())
            .build()
    }
}