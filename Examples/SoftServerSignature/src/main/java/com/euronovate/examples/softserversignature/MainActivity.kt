package com.euronovate.examples.softserversignature

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
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
import com.euronovate.mobilesdk.callback.ENMobileInitializationCallback
import com.euronovate.mobilesdk.model.ENCertificateOwnerInfo
import com.euronovate.mobilesdk.model.ENMobileSDKResponse
import com.euronovate.mobilesdk.model.ENMobileSdkConfig
import com.euronovate.mobilesdk.model.ENNetworkConfig
import com.euronovate.mobilesdk.ui.dialog.ENDialog
import com.euronovate.mobilesdk.ui.dialog.ENDialogTextConfig
import com.euronovate.mobilesdk.ui.dialog.ENDialogType
import com.euronovate.pdfmiddleware.ENPdfMiddleware
import com.euronovate.pdfmiddleware.extension.with
import com.euronovate.pdfmiddleware.model.ENPdfMiddlewareConfig
import com.euronovate.signaturebox.ENSignatureBox
import com.euronovate.signaturebox.extension.with
import com.euronovate.signaturebox.model.ENSignatureBoxConfig
import com.euronovate.signaturebox.model.ENSignatureImageConfig
import com.euronovate.signaturebox.model.ENSignatureImageModeConfig
import com.euronovate.signaturebox.model.enums.ENSignatureContentMode
import com.euronovate.softserver.ENSoftServer
import com.euronovate.softserver.extension.with
import com.euronovate.softserver.domain.model.ENSoftServerConfig
import com.euronovate.utils.preferences.ENSettings
import com.euronovate.utils.preferences.with
import com.euronovate.utils.util.ENFileUtils
import com.euronovate.viewer.ENViewer
import com.euronovate.viewer.extension.openRemoteDocument
import com.euronovate.viewer.extension.with
import com.euronovate.viewer.model.ENViewerConfig
import com.euronovate.viewer.model.enums.ENSignFieldPlaceholder
import kotlinx.coroutines.*

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
            .with(context = applicationContext)
            .with(settings = ENSettings.Builder().build())
            .with(logger = ENLogger.Builder()
                .with(ENLoggerConfig(true,ENLogger.VERBOSE))
                .build())
            .with(initializationCallback = this@MainActivity)
            .with(authConfig = ENAuthConfig("your licenseKey", "your server Url", jwt = "your license jwt"
            ))
            .with(ENMobileSdkConfig(
                networkConfig = ENNetworkConfig(skipSSL = false),
                languageConfig = ENLanguageConfig(languageEnabled = arrayListOf(ENLanguageType.en)),
                certificateOwnerInfo = ENCertificateOwnerInfo()))
            .with(ENViewer.Builder()
                .with(ENViewerConfig(signFieldPlaceholder = ENSignFieldPlaceholder.defaultPlaceholder()))
                .build())
            .with(ENPdfMiddleware.Builder().with(
                ENPdfMiddlewareConfig(
                closeDocumentStatusOnConfirm = true,
                abortDocumentStatusOnCancel = true
            )
            ).build())
            .with(ENSignatureBox.Builder()
                .with(signatureBoxConfig = ENSignatureBoxConfig(
                    signatureSourceType = ENSignatureSourceType.Any,
                    signatureImageConfig = ENSignatureImageConfig(useAlpha = true,
                        signatureContentMode = ENSignatureContentMode.keepFieldRatio,
                        signatureImageModeConfig = ENSignatureImageModeConfig.signatureSignerNameAndTimestamp(watermarkReservedHeight = 0.3f))))
                .build())
            .with(ENBio.Builder().build())
            .with(ENSoftServer.Builder()
                .with(ENSoftServerConfig(baseUrl = "baseUrl", "licenseCode"))
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
        val dialogProgress = ENDialog.getInstance().dialog(activity = this, dialogType = ENDialogType.horizontalProgress,
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
                    GlobalScope.launch {
                        try{
                            ENMobileSDK.getInstance().openRemoteDocument("your guid")
                        }catch(softServerEx: Exception){
                            softServerEx.printStackTrace()
                            runOnUiThread { Toast.makeText(applicationContext,softServerEx.message, Toast.LENGTH_LONG).show() }
                        }
                        Thread(Runnable {
                            runOnUiThread {
                                progressDialog!!.dismiss()
                            }
                        }).start()
                    }

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
            else -> {}
        }
    }
}