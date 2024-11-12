# DossierWithFesOtpSignature

#### [View Releases and Changelogs](https://github.com/euronovate/ENMobileSDK-Android)

# Setup

Clone the repository and open the Example folder with android studio.

Sync and rebuild the project. The project is then setup with one application module for SDK feature.

In DossierWithFesOtpSignature module, in MainScreenViewModel, replace:

```kotlin
private val SDK_ONLINE_LICENSE_KEY = "your-license-key"
private val SDK_ONLINE_LICENSE_URL = "your-license-server-url"
private val SDK_OFFLINE_LICENSE_JWT = "your-offline-license-jwt"


private val SOFTSERVER_URL = "your-softserver-url"
private val SOFTSERVER_LICENSE = "your-softserver-license"
```

with the values provided by Euronovate

# Features

This demo uses:

- ENCore
- ENViewer
- ENSignatureBox
- ENBio
- ENSoftServer

Given a valid Dossier GUID the API of the Dossier can be tried.

Given a Dossier with at least one document also an OTP FES Signature flow on SoftServer can be tried.