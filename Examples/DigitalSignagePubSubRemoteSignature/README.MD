# DigitalSignagePubSubRemoteSignature

#### [View Releases and Changelogs](https://github.com/euronovate/ENMobileSDK-Android)

# Setup

Clone the repository and open the Example folder with Android Studio.

Sync and rebuild the project. The project is then set up with one application module for the SDK feature.

Open DigitalSignagePubSubRemoteSignature module, and in MainScreenViewModel replace:

```kotlin
private val SDK_ONLINE_LICENSE_KEY = "your-license-key"
private val SDK_ONLINE_LICENSE_URL = "your-license-server-url"
private val SDK_OFFLINE_LICENSE_JWT = "your-offline-license-jwt"

private val SOFTSERVER_URL = "your-softserver-url"
private val SOFTSERVER_LICENSE = "your-softserver-license"

private val DIGITAL_SIGNAGE_SERVER_URL = "your-digital-signage-server-url"
private val DIGITAL_SIGNAGE_SERVER_LICENSE_KEY = "your-digital-signage-license-key"

private val PUBSUB_URL = "your-pubsub-url"
private val PUBSUB_ACCESS_TOKEN = "your-pubsub-access-token"
```

with the values provided by Euronovate.

To try the SDK a license is mandatory:
- online license: set the LICENSE_KEY / LICENSE_URL
- offline license: set the LICENSE_JWT

To try the features of this example also a SoftServer, Digital Signage and PubSub Server are required

# Features

This demo uses:

- ENCore
- ENViewer
- ENSignatureBox
- ENBio
- ENSoftServer
- ENPubSub
- ENDigitalSignage

a user can start the digital signage and pub sub and then try a remote signature sending commands messages via pub sub.