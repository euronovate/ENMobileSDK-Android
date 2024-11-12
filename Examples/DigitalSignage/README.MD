# DigitalSignage

#### [View Releases and Changelogs](https://github.com/euronovate/ENMobileSDK-Android)

# Setup

Clone the repository and open the Example folder with android studio.

Sync and rebuild the project. The project is then setup with one application module for SDK feature.

In DigitalSignage module, in MainScreenViewModel, replace:

```kotlin
private val SDK_ONLINE_LICENSE_KEY = "your-license-key"
private val SDK_ONLINE_LICENSE_URL = "your-license-server-url"
private val SDK_OFFLINE_LICENSE_JWT = "your-offline-license-jwt"

private val DIGITAL_SIGNAGE_SERVER_URL = "your-digital-signage-server-url"
private val DIGITAL_SIGNAGE_SERVER_LICENSE_KEY = "your-digital-signage-license-key"
```

with the values provided by Euronovate

# Features

This demo uses:

- ENCore
- ENDigitalSignage
- ENViewer

a user can start a local digital signage or a remote one (given a valid digital signage server).

With a double tap on the screen an example of document can be showed, in read only mode, with the viewer.