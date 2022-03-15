# ENBioLibrary

## Table of Contents

1. [Gradle Dependency](#gradle-dependency)
2. [Basics](#basics)
3. [ENBioLibraryActions](#ENBioLibraryActions)

## Gradle Dependency

![badge_version](badge_version.svg)

#### [BioLibrary Tutorial and Samples](biolibrary/readme.md)

The `BioLibrary` module allow to collect all biometric data while you are signing document. We collect `x` `y` and `pressure` , `timestamp` etc. All data are crypted and in base64.

```gradle
dependencies {
  implementation "com.euronovate.bio:bio:1.0.0"
}
```

## Basics

Here's a very basic example of inizialization of ENBioLibrary in ENMobileSdk builder. You have to do this operation once time because we keep istance of each modules.

```gradle
.with(ENBio.Builder()
      .with(applicationContext = applicationContext)
      .build())
```

You have to **respect** *.with* order like in above example.

## ENBioLibraryActions

**addSignatureTouchedPoint**

this method allow to collect all info about one point (x,y,pressure,timestamp ecc...)  

```kotlin
ENBio.getInstance().addSignatureTouchedPoint(SignatureTouchedPoint(eventX = eventX,
                    eventY = eventY, eventPressure = event.pressure, isUpEvent = false, eventTime = event.eventTime,signatureSourceType = touchEventInputMethod))
```
you need to pass `SignatureTouchedPoint`


**isSourceTypeConsistent**

if you want to check your current sourceMethod if it is consistent with other package, you will call this method. You have to pass `signatureSourceType`

```kotlin
ENBio.getInstance().isSourceTypeConsistent(touchEventInputMethod)
```
`return Bool`


**clearBioPackets**

It allow to clear all biometricPacket in memory and restart with array empty

```kotlin
ENBio.getInstance().clearBioPackets()
```

**getBioContent**

With this method user can obtain base64 based on xml of biometricData array.

```kotlin
ENBio.getInstance().getBioContent(
    context = context,
    publicKey = ENBase64Utils.fromBase64ByteArray(publicKey),
    softwareVersion = BuildConfig.VERSION_NAME,
    deviceOsName = "Android",
    deviceModelName = "${Build.MANUFACTURER}-${Build.MODEL}",
    deviceFirmwareVersion = ENDeviceUtils.formatOperatingSystemInformation(),
    deviceMaxWidth = "WIDTH",
    deviceMaxHeight = "HEIGHT",
    windowManager = windowManager,
    documentHash = documentHash,
    documentSize = documentSize,
    isFea = true
)
```



