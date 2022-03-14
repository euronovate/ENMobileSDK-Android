package com.euronovate.examples.localsignature

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
import com.euronovate.examples.localsignature.R
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
            .with(authConfig = ENAuthConfig("your licenseKey", "your server Url"))
            .with(ENMobileSdkConfig(certificateOwnerInfo = ENCertificateOwnerInfo(),
                languageConfig = ENLanguageConfig(selectorVisible = true,
                    languageEnabled = arrayListOf(ENLanguageType.en)),
                networkConfig = ENNetworkConfig()
            ))
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