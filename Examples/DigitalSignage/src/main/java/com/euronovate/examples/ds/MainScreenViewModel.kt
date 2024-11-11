package com.euronovate.examples.ds

import android.content.Context
import androidx.lifecycle.ViewModel
import com.euronovate.auth.model.ENAuthConfig
import com.euronovate.digitalsignage.ENDigitalSignage
import com.euronovate.digitalsignage.extension.with
import com.euronovate.digitalsignage.model.ENCTAConfig
import com.euronovate.digitalsignage.model.ENDigitalSignageConfig
import com.euronovate.digitalsignage.model.ENDigitalSignageMediaConfig
import com.euronovate.digitalsignage.model.ENDigitalSignageUIConfig
import com.euronovate.digitalsignage.model.ENLocalMedia
import com.euronovate.digitalsignage.model.enums.ENCTAPresentationType
import com.euronovate.digitalsignage.model.enums.ENCTAType
import com.euronovate.digitalsignage.model.enums.ENDigitalSignageContentType
import com.euronovate.logger.ENLogger
import com.euronovate.logger.extension.with
import com.euronovate.logger.model.ENLanguageConfig
import com.euronovate.logger.model.ENLanguageType
import com.euronovate.logger.model.ENLoggerConfig
import com.euronovate.mobilesdk.ENMobileSDK
import com.euronovate.mobilesdk.model.ENCertificateOwnerInfo
import com.euronovate.mobilesdk.model.ENDialogDocumentListModel
import com.euronovate.mobilesdk.model.ENDialogListModel
import com.euronovate.mobilesdk.model.ENMobileSDKResponse
import com.euronovate.mobilesdk.model.ENMobileSdkConfig
import com.euronovate.mobilesdk.model.ENNetworkConfig
import com.euronovate.mobilesdk.theme.ENDefaultTheme
import com.euronovate.pdfmiddleware.ENPdfMiddleware
import com.euronovate.pdfmiddleware.extension.with
import com.euronovate.pdfmiddleware.model.ENPdfMiddlewareConfig
import com.euronovate.utils.preferences.ENSettings
import com.euronovate.utils.preferences.with
import com.euronovate.viewer.ENViewer
import com.euronovate.viewer.extension.with
import com.euronovate.viewer.model.ENViewerConfig
import com.euronovate.viewer.model.enums.ENSignFieldPlaceholder
import com.euronovate.viewer.model.enums.ENViewerBarType
import com.euronovate.viewer.model.enums.ENViewerType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

enum class InitializationState {
    NotInitialized,
    LocalDS,
    RemoteDS,
}

class MainScreenViewModel : ViewModel() {

    private val SDK_ONLINE_LICENSE_KEY = "your-license-key"
    private val SDK_ONLINE_LICENSE_URL = "your-license-server-url"
    private val SDK_OFFLINE_LICENSE_JWT = "your-offline-license-jwt"

    private val DIGITAL_SIGNAGE_SERVER_URL = "your-digital-signage-server-url"
    private val DIGITAL_SIGNAGE_SERVER_LICENSE_KEY = "your-digital-signage-license-key"

    private val _initializationState = MutableStateFlow(InitializationState.NotInitialized)
    val initializationState: StateFlow<InitializationState>
        get() = _initializationState

    fun initLibraryForLocalDigitalSignage(context: Context) {

        ENMobileSDK.free()

        ENMobileSDK.Builder()
            .with(context = context)
            .with(settings = ENSettings.Builder().build())
            .with(
                logger = ENLogger.Builder()
                    .with(ENLoggerConfig(true, ENLogger.VERBOSE))
                    .build()
            )
            .with(
                mobileSdkConfig = ENMobileSdkConfig(
                    networkConfig = ENNetworkConfig(skipSSL = true),
                    certificateOwnerInfo = ENCertificateOwnerInfo(),
                    languageConfig = ENLanguageConfig(selectorVisible = true, languageEnabled = arrayListOf(ENLanguageType.en))
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
                ENDigitalSignage.Builder()
                    .with(
                        digitalSignageConfig = ENDigitalSignageConfig(
                            baseUrl = null, licenseCode = null,
                            digitalSignageMediaConfig = ENDigitalSignageMediaConfig(
                                landPlaceholderAssetName = "local_ds_landscape.jpeg", portPlaceholderAssetName = "local_ds_portrait.jpeg",
                                localMediaContents = arrayListOf(
                                    ENLocalMedia(assetName = "local_ds_landscape.jpeg", duration = 5000, ENDigitalSignageContentType.Image),
                                    ENLocalMedia(assetName = "local_ds_landscape.jpeg", duration = 4000, ENDigitalSignageContentType.Image),
                                    ENLocalMedia(assetName = "local_ds_landscape.jpeg", duration = 3000, ENDigitalSignageContentType.Image),
                                    ENLocalMedia(assetName = "local_ds_portrait.jpeg", duration = 3000, ENDigitalSignageContentType.Image)
                                )
                            ),
                            digitalSignageUIConfig = ENDigitalSignageUIConfig(
                                buttonLeftBottom = ENCTAConfig(
                                    actionType = ENCTAType.guid(),
                                    presentationType = ENCTAPresentationType.easterEgg
                                ), directlyDs = ENCTAConfig(
                                    actionType = ENCTAType.documentList(
                                        getDigitalSignageDocumentList()
                                    )
                                )
                            ),
                        )
                    )
                    .build()
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
                        closeDocumentStatusOnConfirm = false,
                        abortDocumentStatusOnCancel = false
                    )
                ).build()
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
                            InitializationState.LocalDS
                        }
                    }

                    else -> {}
                }
            }
            .build()
    }

    fun initLibraryForRemoteDigitalSignage(context: Context) {

        ENMobileSDK.free()

        ENMobileSDK.Builder()
            .with(context = context)
            .with(settings = ENSettings.Builder().build())
            .with(
                logger = ENLogger.Builder()
                    .with(ENLoggerConfig(true, ENLogger.VERBOSE))
                    .build()
            )
            .with(
                mobileSdkConfig = ENMobileSdkConfig(
                    networkConfig = ENNetworkConfig(skipSSL = true),
                    certificateOwnerInfo = ENCertificateOwnerInfo(),
                    languageConfig = ENLanguageConfig(selectorVisible = true, languageEnabled = arrayListOf(ENLanguageType.en))
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
                ENDigitalSignage.Builder()
                    .with(
                        digitalSignageConfig = ENDigitalSignageConfig(
                            baseUrl = DIGITAL_SIGNAGE_SERVER_URL, licenseCode = DIGITAL_SIGNAGE_SERVER_LICENSE_KEY,
                            digitalSignageMediaConfig = ENDigitalSignageMediaConfig(landPlaceholderAssetName = "remote_ds_default_landscape.jpg", portPlaceholderAssetName = "remote_ds_default_portrait.jpg"),
                            digitalSignageUIConfig = ENDigitalSignageUIConfig(
                                buttonLeftBottom = ENCTAConfig(
                                    actionType = ENCTAType.guid(),
                                    presentationType = ENCTAPresentationType.easterEgg
                                ), directlyDs = ENCTAConfig(
                                    actionType = ENCTAType.documentList(
                                        getDigitalSignageDocumentList()
                                    )
                                )
                            ),
                        )
                    )
                    .build()
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
                        closeDocumentStatusOnConfirm = false,
                        abortDocumentStatusOnCancel = false
                    )
                ).build()
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
                            InitializationState.RemoteDS
                        }
                    }

                    else -> {}
                }
            }
            .build()
    }

    fun startDigitalSignage() {
        ENDigitalSignage.getInstance().start()
    }

    private fun getDigitalSignageDocumentList(): ArrayList<ENDialogListModel> {
        return arrayListOf(
            ENDialogDocumentListModel(
                title = "1 mandatory sign 1 optional (1-mandatory-1-optional-adobe.pdf)",
                subTitle = "Single page document with one mandatory signature and one optional signature",
                assetName = "1-mandatory-1-optional-adobe.pdf"
            ),
        )
    }
}