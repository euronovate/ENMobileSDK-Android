package com.euronovate.demoviewersignature

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
import com.euronovate.bio.model.enum.ENSignatureSourceType
import com.euronovate.logger.ENLogger
import com.euronovate.logger.extension.with
import com.euronovate.logger.model.ENLoggerConfig
import com.euronovate.mobilesdk.ENMobileSDK
import com.euronovate.mobilesdk.callback.ENMobileInitializationCallback
import com.euronovate.mobilesdk.model.ENCertificateOwnerInfo
import com.euronovate.mobilesdk.model.ENMobileSDKResponse
import com.euronovate.mobilesdk.model.ENMobileSdkConfig
import com.euronovate.mobilesdk.ui.dialog.ENDialog
import com.euronovate.mobilesdk.ui.dialog.ENDialogTextConfig
import com.euronovate.mobilesdk.ui.dialog.ENDialogType
import com.euronovate.pdfmiddleware.ENPdfMiddleware
import com.euronovate.pdfmiddleware.extension.with
import com.euronovate.signaturebox.ENSignatureBox
import com.euronovate.signaturebox.extension.with
import com.euronovate.signaturebox.model.ENSignatureBoxConfig
import com.euronovate.signaturebox.model.ENSignatureImageConfig
import com.euronovate.utils.preferences.ENSettings
import com.euronovate.utils.preferences.with
import com.euronovate.utils.util.ENBase64Utils
import com.euronovate.utils.util.ENFileUtils
import com.euronovate.viewer.ENViewer
import com.euronovate.viewer.extension.openDocument
import com.euronovate.viewer.extension.with
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.Runnable
import java.io.File

/**
 * Created by Lorenzo Sogliani on 15/12/2018
 * Copyright (c) 2020 Euronovate. All rights reserved.
 */
class MainActivity : AppCompatActivity(), CoroutineScope by MainScope(), View.OnClickListener, ENMobileInitializationCallback<String> {
    // Class private attributes **********************************************************************************************************************
    private lateinit var btnStart: Button
    private lateinit var btnShareLastPdf: Button
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
            .with(settings = ENSettings.Builder().with(applicationContext).build())
            .with(logger = ENLogger.Builder()
                .with(applicationContext)
                .with(ENLoggerConfig(true,ENLogger.VERBOSE))
                .build())
            .with(applicationContext)
            .with(initializationCallback = this@MainActivity)
            .with(authConfig = ENAuthConfig("5ee4e1d4-eac2-41fd-b79e-b1dcafdbfcde", "https://dev.euronovate.com:9490",
                jwt = "eyJhbGciOiJSUzI1NiJ9.eyJlbmMiOnsiZGlwIjoiMTUxLjY4LjEzNi4xNjIiLCJwcmQiOiJ7XCJFTl9NQVBfQU5EX1NPRlRfU0VSVkVSXCI6e1wicHJzXCI6XCJFTlNFUlZcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxNjA3NzYxNyxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9BTkRfQklPTElCXCI6e1wicHJzXCI6XCJCSU9MSUJcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxNjA3NzYxNyxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9BTkRfRElHSVRTSUdOXCI6e1wicHJzXCI6XCJFTkRJR0lcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOntcImxpc3RCdW5kbGVJZFwiOlwiXCJ9LFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfSU9TX1NHTkJPWFwiOntcInByc1wiOlwiU0dOQk9YXCIsXCJwcmNcIjpcIk1BUElPU1wiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTYwNzc2MTcsXCJjc3RcIjpudWxsLFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfQU5EX1ZJRVdFUlwiOntcInByc1wiOlwiVklFV0VSXCIsXCJwcmNcIjpcIk1BUEFORFwiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTU0OTg1NDEsXCJjc3RcIjp7XCJsaXN0QnVuZGxlSWRcIjpcIlwifSxcImV4cHRrblwiOjAsXCJ1cHJcIjpbXCJSRUFERVJcIl19LFwiRU5fTUFQX0lPU19FTkxPR0dFUlwiOntcInByc1wiOlwiTE9HR0VSXCIsXCJwcmNcIjpcIk1BUElPU1wiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTI0MTQ3OTAsXCJjc3RcIjpudWxsLFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfSU9TX1BERk1ETFdSXCI6e1wicHJzXCI6XCJQREZNRExcIixcInByY1wiOlwiTUFQSU9TXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9JT1NfVklFV0VSXCI6e1wicHJzXCI6XCJWSUVXRVJcIixcInByY1wiOlwiTUFQSU9TXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxNTQ5ODU0MSxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9BTkRfUFVCU1VCXCI6e1wicHJzXCI6XCJQVUJTVUJcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9BTkRfRU5MT0dHRVJcIjp7XCJwcnNcIjpcIkxPR0dFUlwiLFwicHJjXCI6XCJNQVBBTkRcIixcImV4cHByZFwiOjI1MjY4NDk5MDAwMDAsXCJpc3NcIjoxNjMxNzEyNDE0NzkwLFwiY3N0XCI6bnVsbCxcImV4cHRrblwiOjAsXCJ1cHJcIjpbXCJSRUFERVJcIl19LFwiRU5fTUFQX0FORF9TR05CT1hcIjp7XCJwcnNcIjpcIlNHTkJPWFwiLFwicHJjXCI6XCJNQVBBTkRcIixcImV4cHByZFwiOjI1MjY4NDk5MDAwMDAsXCJpc3NcIjoxNjMxNzE2MDc3NjE3LFwiY3N0XCI6bnVsbCxcImV4cHRrblwiOjAsXCJ1cHJcIjpbXCJSRUFERVJcIl19LFwiRU5fTUFQX0lPU19TT0ZUX1NFUlZFUlwiOntcInByc1wiOlwiRU5TRVJWXCIsXCJwcmNcIjpcIk1BUElPU1wiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTYwNzc2MTcsXCJjc3RcIjpudWxsLFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfSU9TX1BVQlNVQlwiOntcInByc1wiOlwiUFVCU1VCXCIsXCJwcmNcIjpcIk1BUElPU1wiLFwiZXhwcHJkXCI6MjUyNjg0OTkwMDAwMCxcImlzc1wiOjE2MzE3MTI0MTQ3OTAsXCJjc3RcIjpudWxsLFwiZXhwdGtuXCI6MCxcInVwclwiOltcIlJFQURFUlwiXX0sXCJFTl9NQVBfQU5EX1BERk1ETFdSXCI6e1wicHJzXCI6XCJQREZNRExcIixcInByY1wiOlwiTUFQQU5EXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9JT1NfRElHSVRTSUdOXCI6e1wicHJzXCI6XCJFTkRJR0lcIixcInByY1wiOlwiTUFQSU9TXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxMjQxNDc5MCxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfSxcIkVOX01BUF9JT1NfQklPTElCXCI6e1wicHJzXCI6XCJCSU9MSUJcIixcInByY1wiOlwiTUFQSU9TXCIsXCJleHBwcmRcIjoyNTI2ODQ5OTAwMDAwLFwiaXNzXCI6MTYzMTcxNjA3NzYxNyxcImNzdFwiOm51bGwsXCJleHB0a25cIjowLFwidXByXCI6W1wiUkVBREVSXCJdfX0iLCJkaWQiOiJwb3N0bWFuVVVJRCIsImxjZCI6IntcInV1aWRcIjpcImFiYjNkNmNiLTZjN2EtNDVkZC1hOTAzLTgwOTliZGU0M2VjYVwiLFwidXNyXCI6bnVsbCxcIm93blwiOm51bGwsXCJjc3NcIjpcIkdSRUNJQVwiLFwiY3NjXCI6XCJOQkdYWFhcIn0iLCJ1c3IiOiJlbm1vYmFwcCJ9LCJqdGkiOiI2YzRiNDRlMS00YmIwLTRmMGQtOGVmYS02YjQ4M2JlZTAzYjkiLCJpYXQiOjE2MzE3MTkwNzUsImlzcyI6ImVuYXV0aCIsInN1YiI6ImFiYjNkNmNiLTZjN2EtNDVkZC1hOTAzLTgwOTliZGU0M2VjYSJ9.Ts4pFJKYu59dggc02sIIMJVZJ5zMa8PRC55RaXVLQbsO6X-2FLVsHHAckJX9Qs5nFWZQMUybBA9lO6v5e4mtdz2ZXo7PHn03WIJyiIRWPeCQQgFtt7Chah-y9iMRpn0MTkNrIgXCSpQ9qZpss-Lre3RTgr2F_SoAm0CRt8F52s9FrD6ENvSAXtRXC_mWwTRSkzubwHRC5w-bzKrS73YmIIO5Z3ynEDgtWz6A2VRfOOuxezroMbtK81i1e2cKDMUGwY4-ri8Q_UCfZ73RICvFdNlMZNhRTtDhSJ21c7cT9z9XE4SUeGILEUH-Rxx6ZuGfHYTccU8YgQ2o6PbK9mSS9g"))
            .with(ENMobileSdkConfig(skipSSL = false,certificateOwnerInfo = ENCertificateOwnerInfo()))
            .with(ENViewer.Builder()
                .with(applicationContext = applicationContext)
                .build())
            .with(ENPdfMiddleware.Builder()
                .with(applicationContext)
                .build())
            .with(ENSignatureBox.Builder()
                .with(applicationContext = applicationContext)
                .with(signatureBoxConfig = ENSignatureBoxConfig(useAlpha = true,
                    signatureSourceType = ENSignatureSourceType.Any,
                    signatureImageConfig = ENSignatureImageConfig.signatureSignerNameAndTimestamp))
                .build())
            .with(ENBio.Builder()
                .with(applicationContext = applicationContext)
                .build())
            .build()
    }

    private fun initUI(){
        btnStart = findViewById(R.id.btnStart)
        btnStart.setOnClickListener(this)
        btnStart.isEnabled = initialized

        btnShareLastPdf = findViewById(R.id.btnShareLastPdf)
        btnShareLastPdf.setOnClickListener(this)
        btnShareLastPdf.isEnabled = true
    }
    private fun showProgressDialog(title: String? = getString(R.string.info), content: String? = getString(R.string.please_wait)): Dialog {
        val dialogProgress = ENDialog.getInstance().dialog(activity = this, dialogType = ENDialogType.progress,
            dialogTextConfig = ENDialogTextConfig(title = title, content = content))
        dialogProgress.show()
        return dialogProgress
    }
    var progressDialog: Dialog?=null
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnStart ->{
                if(initialized){
                    progressDialog = showProgressDialog(title = getString(R.string.starting_signature),content = getString(R.string.please_wait))
                    val pdfName = "Demo12_Verdi_PDFA1b.pdf"
                    val pdfFile = File(getExternalFilesDir(null)!!,pdfName)
                    ENFileUtils.copyAssetFileToInternalStorage(applicationContext.assets,getExternalFilesDir(null)!!,pdfName)
                    ENMobileSDK.getInstance().openDocument(ENBase64Utils.toBase64(ENFileUtils.getBytes(pdfFile.inputStream())!!),
                        ENBase64Utils.toBase64(application.assets.open("encert.pem").readBytes()))
                    Thread(Runnable {
                        runOnUiThread {
                            progressDialog!!.dismiss()
                        }
                    }).start()
                }
            }
            R.id.btnShareLastPdf->{
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
            }
        }
    }
}