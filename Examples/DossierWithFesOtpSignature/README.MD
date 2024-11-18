# DossierWithFesOtpSignature

#### [View Releases and Changelogs](https://github.com/euronovate/ENMobileSDK-Android)

# Setup

Clone the repository and open the Example folder with Android Studio.

Sync and rebuild the project. The project is then set up with one application module for the SDK feature.

Open DossierWithFesOtpSignature module, and in MainScreenViewModel replace:

```kotlin
private val SDK_ONLINE_LICENSE_KEY = "your-license-key"
private val SDK_ONLINE_LICENSE_URL = "your-license-server-url"
private val SDK_OFFLINE_LICENSE_JWT = "your-offline-license-jwt"


private val SOFTSERVER_URL = "your-softserver-url"
private val SOFTSERVER_LICENSE = "your-softserver-license"
```

with the values provided by Euronovate.

To try the SDK a license is mandatory:
- online license: set the LICENSE_KEY / LICENSE_URL
- offline license: set the LICENSE_JWT

To try the Dossier API and OTP signature a working SoftServer is required. SoftServer must implement the OTP logic.

# Features

This demo uses:

- ENCore
- ENViewer
- ENSignatureBox
- ENBio
- ENSoftServer

a user can try:
- the Dossier API given a working SoftServer with a valid dossier.
- the OTP FES Signature given a Dossier with at least one document.