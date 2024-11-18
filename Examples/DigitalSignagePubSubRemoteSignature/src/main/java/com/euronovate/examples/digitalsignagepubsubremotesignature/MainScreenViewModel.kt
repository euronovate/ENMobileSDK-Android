package com.euronovate.examples.digitalsignagepubsubremotesignature

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.euronovate.auth.model.ENAuthConfig
import com.euronovate.bio.ENBio
import com.euronovate.bio.extension.with
import com.euronovate.bio.model.enums.ENSignatureSourceType
import com.euronovate.digitalsignage.ENDigitalSignage
import com.euronovate.digitalsignage.extension.with
import com.euronovate.digitalsignage.model.ENCTAConfig
import com.euronovate.digitalsignage.model.ENDigitalSignageConfig
import com.euronovate.digitalsignage.model.ENDigitalSignageMediaConfig
import com.euronovate.digitalsignage.model.ENDigitalSignageUIConfig
import com.euronovate.digitalsignage.model.enums.ENCTAPresentationType
import com.euronovate.digitalsignage.model.enums.ENCTAType
import com.euronovate.logger.ENLogger
import com.euronovate.logger.extension.with
import com.euronovate.logger.model.ENLanguageConfig
import com.euronovate.logger.model.ENLanguageType
import com.euronovate.logger.model.ENLoggerConfig
import com.euronovate.mobilesdk.ENMobileSDK
import com.euronovate.mobilesdk.extensions.emitEvent
import com.euronovate.mobilesdk.model.ENCertificateOwnerInfo
import com.euronovate.mobilesdk.model.ENDialogDocumentListModel
import com.euronovate.mobilesdk.model.ENDialogListModel
import com.euronovate.mobilesdk.model.ENMobileSDKResponse
import com.euronovate.mobilesdk.model.ENMobileSdkConfig
import com.euronovate.mobilesdk.model.ENNetworkConfig
import com.euronovate.mobilesdk.model.enums.ENEventType
import com.euronovate.mobilesdk.model.responses.business.document.ENSignDocumentGuidDTO
import com.euronovate.mobilesdk.theme.ENDefaultTheme
import com.euronovate.mobilesdk.ui.dialog.ENDialog
import com.euronovate.pdfmiddleware.ENPdfMiddleware
import com.euronovate.pdfmiddleware.extension.with
import com.euronovate.pdfmiddleware.model.ENPdfMiddlewareConfig
import com.euronovate.pubsub.ENPubSub
import com.euronovate.pubsub.extension.subscribe
import com.euronovate.pubsub.extension.with
import com.euronovate.pubsub.model.ENPubSubConfig
import com.euronovate.pubsub.model.enums.ENPubSubChannel
import com.euronovate.pubsub.model.enums.ENPubSubReconnectionType
import com.euronovate.pubsub.model.enums.ENPubSubType
import com.euronovate.signaturebox.ENSignatureBox
import com.euronovate.signaturebox.extension.with
import com.euronovate.signaturebox.model.ENSignatureBoxConfig
import com.euronovate.signaturebox.model.ENSignatureImageConfig
import com.euronovate.signaturebox.model.ENSignatureImageModeConfig
import com.euronovate.signaturebox.model.enums.ENSignatureBoxType
import com.euronovate.signaturebox.model.enums.ENSignatureContentMode
import com.euronovate.softserver.ENSoftServer
import com.euronovate.softserver.domain.model.ENSoftServerConfig
import com.euronovate.softserver.extension.with
import com.euronovate.utils.preferences.ENSettings
import com.euronovate.utils.preferences.with
import com.euronovate.viewer.ENViewer
import com.euronovate.viewer.extension.with
import com.euronovate.viewer.model.ENViewerConfig
import com.euronovate.viewer.model.enums.ENSignFieldPlaceholder
import com.euronovate.viewer.model.enums.ENViewerBarType
import com.euronovate.viewer.model.enums.ENViewerType
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject

enum class InitializationState {
    NotInitialized,
    Initialized,
}

class MainScreenViewModel : ViewModel() {

    private val SDK_ONLINE_LICENSE_KEY = "your-license-key"
    private val SDK_ONLINE_LICENSE_URL = "your-license-server-url"
    private val SDK_OFFLINE_LICENSE_JWT = "your-offline-license-jwt"

    private val SOFTSERVER_URL = "your-softserver-url"
    private val SOFTSERVER_LICENSE = "your-softserver-license"

    private val DIGITAL_SIGNAGE_SERVER_URL = "your-digital-signage-server-url"
    private val DIGITAL_SIGNAGE_SERVER_LICENSE_KEY = "your-digital-signage-license-key"

    private val PUBSUB_URL = "your-pubsub-url"
    private val PUBSUB_ACCESS_TOKEN = "your-pubsub-access-token"

    private val _initializationState = MutableStateFlow(InitializationState.NotInitialized)
    val initializationState: StateFlow<InitializationState>
        get() = _initializationState

    fun initLibrary(activity: Activity) {
        ENMobileSDK.Builder()
            .with(context = activity)
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
            .with(
                pubSub = ENPubSub.Builder()
                    .with(
                        ENPubSubConfig(
                            type = ENPubSubType.webSocket,
                            connectionParams = { return@ENPubSubConfig Pair(PUBSUB_URL, PUBSUB_ACCESS_TOKEN) },
                            pubSubReconnectionType = ENPubSubReconnectionType.enabled(connectionTimerInterval = 60, reconnectionTimerInterval = 15)
                        )
                    )
                    .build()
            )
            .with(ENBio.Builder().build())
            .with(
                // NOTE: the same initialization can be used both for local and remote signature
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
    }

    fun startDigitalSignageAndPubSub(activity: Activity) {
        ENDigitalSignage.getInstance().start()
        ENPubSub.getInstance().init()
        ENPubSub.getInstance().subscribe(ENPubSubChannel.startSign) {
            try {
                val signDocumentGuidDTO = Gson().fromJson((it as JSONObject).toString(), ENSignDocumentGuidDTO::class.java)
                ENMobileSDK.emitEvent(ENEventType.signDocument, signDocumentGuidDTO.convertToEvent())
            } catch (ex: Exception) {
                ENDialog.showGenericErrorDialog(
                    activity,
                    messageContent = ex.localizedMessage
                )
            }
        }
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