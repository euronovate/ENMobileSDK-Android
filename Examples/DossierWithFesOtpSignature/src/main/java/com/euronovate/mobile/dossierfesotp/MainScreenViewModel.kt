package com.euronovate.mobile.dossierfesotp

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.euronovate.auth.model.ENAuthConfig
import com.euronovate.bio.ENBio
import com.euronovate.bio.extension.with
import com.euronovate.bio.model.enums.ENSignatureSourceType
import com.euronovate.logger.ENLogger
import com.euronovate.logger.extension.with
import com.euronovate.logger.model.ENLanguageConfig
import com.euronovate.logger.model.ENLanguageType
import com.euronovate.logger.model.ENLogServerConfig
import com.euronovate.logger.model.ENLogServerConfigType
import com.euronovate.logger.model.ENLoggerConfig
import com.euronovate.mobilesdk.ENMobileSDK
import com.euronovate.mobilesdk.extensions.subscribeEvent
import com.euronovate.mobilesdk.model.ENCertificateOwnerInfo
import com.euronovate.mobilesdk.model.ENEventCallback
import com.euronovate.mobilesdk.model.ENMobileSDKResponse
import com.euronovate.mobilesdk.model.ENMobileSdkConfig
import com.euronovate.mobilesdk.model.ENNetworkConfig
import com.euronovate.mobilesdk.model.SignatureMode
import com.euronovate.mobilesdk.model.enums.ENCertificateIntegrityType
import com.euronovate.mobilesdk.model.enums.ENEventType
import com.euronovate.mobilesdk.theme.ENDefaultTheme
import com.euronovate.mobilesdk.ui.dialog.ENDialog
import com.euronovate.pdfmiddleware.ENPdfMiddleware
import com.euronovate.pdfmiddleware.extension.with
import com.euronovate.pdfmiddleware.model.ENPdfMiddlewareConfig
import com.euronovate.pdfmiddleware.model.PdfContainer
import com.euronovate.signaturebox.ENSignatureBox
import com.euronovate.signaturebox.extension.with
import com.euronovate.signaturebox.model.ENSignatureBoxConfig
import com.euronovate.signaturebox.model.ENSignatureImageConfig
import com.euronovate.signaturebox.model.ENSignatureImageModeConfig
import com.euronovate.signaturebox.model.enums.ENSignatureBoxType
import com.euronovate.signaturebox.model.enums.ENSignatureContentMode
import com.euronovate.softserver.ENSoftServer
import com.euronovate.softserver.domain.model.ENSoftServerConfig
import com.euronovate.softserver.extension.findDocumentsInDossier
import com.euronovate.softserver.extension.findDossier
import com.euronovate.softserver.extension.with
import com.euronovate.utils.preferences.ENSettings
import com.euronovate.utils.preferences.with
import com.euronovate.viewer.ENViewer
import com.euronovate.viewer.extension.openRemoteDocument
import com.euronovate.viewer.extension.with
import com.euronovate.viewer.model.ENViewerConfig
import com.euronovate.viewer.model.enums.ENSignFieldPlaceholder
import com.euronovate.viewer.model.enums.ENViewerBarType
import com.euronovate.viewer.model.enums.ENViewerType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class InitializationState {
    NotInitialized,
    Initialized
}

class MainScreenViewModel : ViewModel() {

    private val SDK_ONLINE_LICENSE_KEY = "your-license-key"
    private val SDK_ONLINE_LICENSE_URL = "your-license-server-url"
    private val SDK_OFFLINE_LICENSE_JWT = "your-offline-license-jwt"


    private val SOFTSERVER_URL = "your-softserver-url"
    private val SOFTSERVER_LICENSE = "your-softserver-license"

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    private val _initializationState = MutableStateFlow(InitializationState.NotInitialized)
    val initializationState: StateFlow<InitializationState>
        get() = _initializationState
    private val _dossierName = MutableStateFlow<String?>(null)
    val dossierName: StateFlow<String?>
        get() = _dossierName
    private val _dossierDescription = MutableStateFlow<String?>(null)
    val dossierDescription: StateFlow<String?>
        get() = _dossierDescription
    private val _documentsGuidAndSignatureComplete = MutableStateFlow(mapOf<String, Boolean>())
    val documentsGuidAndSignatureComplete: StateFlow<Map<String, Boolean>>
        get() = _documentsGuidAndSignatureComplete

    fun initLibrary(context: Context) {

        ENMobileSDK.free()

        ENMobileSDK.Builder()
            .with(context = context)
            .with(settings = ENSettings.Builder().build())
            .with(
                logger = ENLogger.Builder()
                    .with(
                        ENLoggerConfig(
                            debuggable = true,
                            logLevel = ENLogger.VERBOSE,
                            logServerConfig = ENLogServerConfig(ENLogServerConfigType.None())
                        )
                    )
                    .build()
            )
            .with(
                mobileSdkConfig = ENMobileSdkConfig(
                    networkConfig = ENNetworkConfig(
                        skipSSL = true,
                        OAuth2Config = null
                    ),
                    considerAllSignatureFieldCharacters = false,
                    certificateOwnerInfo = ENCertificateOwnerInfo(),
                    certificateIntegrity = ENCertificateIntegrityType.P12(
                        certificateBase64 = "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",
                        password = "euronovate"
                    ),
                    languageConfig = ENLanguageConfig(
                        selectorVisible = true,
                        languageEnabled = arrayListOf(
                            ENLanguageType.en,
                            ENLanguageType.it,
                        )
                    ),
                    defaultSignatureMode = SignatureMode.OtpByDocumentGuid(),
                )
            )
            .with(theme = ENDefaultTheme())
            .with(
                authConfig = ENAuthConfig(
                    licenseKey = SDK_ONLINE_LICENSE_KEY,
                    serverUrl = SDK_ONLINE_LICENSE_URL,
                    jwt = SDK_OFFLINE_LICENSE_JWT,
                )
            )
            .with(
                ENViewer.Builder()
                    .with(
                        ENViewerConfig(
                            signFieldPlaceholder = ENSignFieldPlaceholder.defaultPlaceholder(),
                            viewerType = ENViewerType.simple, viewerBarType = ENViewerBarType.simple
                        )
                    )
                    .build()
            )
            .with(
                ENPdfMiddleware.Builder().with(
                    ENPdfMiddlewareConfig(
                        closeDocumentStatusOnConfirm = true,
                        abortDocumentStatusOnCancel = false
                    )
                ).build()
            )
            .with(
                ENSignatureBox.Builder()
                    .with(
                        signatureBoxConfig = ENSignatureBoxConfig(
                            signatureSourceType = ENSignatureSourceType.Any,
                            signatureImageConfig = ENSignatureImageConfig(
                                useAlpha = true,
                                signatureContentMode = ENSignatureContentMode.ignoreFieldRatio,
                                signatureImageModeConfig = ENSignatureImageModeConfig.signatureSignerNameAndTimestamp(
                                    watermarkReservedHeight = 0.3f
                                )
                            ),
                            signatureBoxType = ENSignatureBoxType.simple,
                            minBioPackagesToAllowConfirmationUsingStylus = 450,
                            minBioPackagesToAllowConfirmationUsingFinger = 100,
                        ),
                    )
                    .build()
            )
            .with(ENBio.Builder().build())
            .with(
                ENSoftServer.Builder()
                    .with(
                        ENSoftServerConfig(
                            baseUrl = SOFTSERVER_URL,
                            licenseCode = SOFTSERVER_LICENSE,
                        )
                    )
                    .build()
            )
            .with { response ->
                when (response) {
                    is ENMobileSDKResponse.error -> {
                        _initializationState.update {
                            InitializationState.NotInitialized
                        }
                    }

                    is ENMobileSDKResponse.success -> {
                        _initializationState.update {
                            InitializationState.Initialized
                        }
                    }

                    else -> {}
                }
            }
            .build()

        // subscribe to close event to check if the document has been closed because the document cannot be signed anymore
        ENMobileSDK.subscribeEvent(ENEventCallback(ENEventType.closedDocument) {
            val pdfContainer = (it as PdfContainer)
            val updatedMap = _documentsGuidAndSignatureComplete.value.toMutableMap()
            updatedMap[pdfContainer.guid ?: ""] = true
            _documentsGuidAndSignatureComplete.value = updatedMap
        })
    }

    fun dossierFind(activity: Activity, dossierGuid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val dossier = ENMobileSDK.getInstance().findDossier(dossierGuid = dossierGuid)
                dossier?.let {
                    _dossierName.value = it.name ?: "-"
                    _dossierDescription.value = it.description ?: "-"
                } ?: run {
                    ENDialog.showGenericErrorDialog(
                        activity,
                        messageContent = "Dossier is null"
                    )
                }
            } catch (exception: Exception) {
                ENDialog.showGenericErrorDialog(
                    activity,
                    messageContent = exception.localizedMessage
                )
            }
            _isLoading.value = false
        }
    }

    fun dossierFindDocuments(activity: Activity, dossierGuid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val documents = ENMobileSDK.getInstance().findDocumentsInDossier(dossierGuid = dossierGuid)
                documents?.let {
                    _documentsGuidAndSignatureComplete.value = documents.associate { (it.guid ?: "-") to false }
                } ?: run {
                    ENDialog.showGenericErrorDialog(
                        activity,
                        messageContent = "Documents list is null"
                    )
                }
            } catch (exception: Exception) {
                ENDialog.showGenericErrorDialog(
                    activity,
                    messageContent = exception.localizedMessage
                )
            }
            _isLoading.value = false
        }
    }

    fun openDocumentForSignature(activity: Activity, documentGuid: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                ENMobileSDK.getInstance().openRemoteDocument(documentGuid = documentGuid)
            } catch (exception: Exception) {
                ENDialog.showGenericErrorDialog(
                    activity,
                    messageContent = exception.localizedMessage
                )
            }
        }
        _isLoading.value = false
    }
}