# DigitalSignage

#### [View Releases and Changelogs](https://github.com/euronovate/ENMobileSDK-Android)

# Setup

Clone the repository and open the Example folder with Android Studio.

Sync and rebuild the project. The project is then set up with one application module for the SDK feature.

Open DigitalSignage module, and in MainScreenViewModel replace:

```kotlin
private val SDK_ONLINE_LICENSE_KEY = "your-license-key"
private val SDK_ONLINE_LICENSE_URL = "your-license-server-url"
private val SDK_OFFLINE_LICENSE_JWT = "your-offline-license-jwt"

private val DIGITAL_SIGNAGE_SERVER_URL = "your-digital-signage-server-url"
private val DIGITAL_SIGNAGE_SERVER_LICENSE_KEY = "your-digital-signage-license-key"
```

with the values provided by Euronovate.

To try the SDK a license is mandatory:
- online license: set the LICENSE_KEY / LICENSE_URL
- offline license: set the LICENSE_JWT

# Features

This demo uses:

- ENCore
- ENDigitalSignage
- ENViewer

a user can start:
- local digital signage: with images taken from app assets
- remote digital signage: a working digital signage server is required, and its URL and LICENSE must be set up in the parameters above.

By double-tapping on the digital signage screen, an example document can be shown in read-only mode with the viewer.