# LocalAndRemoteSignature

#### [View Releases and Changelogs](https://github.com/euronovate/ENMobileSDK-Android)


# Setup

Clone the repository and open the Example folder with Android Studio.

Sync and rebuild the project. The project is then set up with one application module for the SDK feature.

Open LocalAndRemoteSignature module, and in MainScreenViewModel replace:

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

To try the local signature no further parameters are required, there is an example document and a development certificate for biometric data packed with the app.
To try the remote signature a working SoftServer is required

# Features

This demo uses:

- ENCore
- ENViewer
- ENSignatureBox
- ENBio
- ENSoftServer

a user can try:
- the local signature with two APIS: a direct `openDocument` function and the `event` API
- the remote signature with two APIS: a direct `openRemoteDocument` function and the `event` API
- the `DIALOGS` API that provides a dialog interface, styled as the SDK, to insert documents GUID or to choose from a list of documents