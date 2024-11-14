# MultiLayoutViewerSignatureBox

#### [View Releases and Changelogs](https://github.com/euronovate/ENMobileSDK-Android)

# Setup

Clone the repository and open the Example folder with Android Studio.

Sync and rebuild the project. The project is then set up with one application module for the SDK feature.

Open MultiLayoutViewerSignatureBox module, and in MainScreenViewModel replace:

```kotlin
private val SDK_ONLINE_LICENSE_KEY = "your-license-key"
private val SDK_ONLINE_LICENSE_URL = "your-license-server-url"
private val SDK_OFFLINE_LICENSE_JWT = "your-offline-license-jwt"
```

with the values provided by Euronovate.

To try the SDK a license is mandatory:
- online license: set the LICENSE_KEY / LICENSE_URL
- offline license: set the LICENSE_JWT

# Features

This demo uses:

- ENCore
- ENViewer
- ENSignatureBox
- ENBio

a user can choose between multiple types of viewer and signature box, and then open a document to take a look at the layout selected