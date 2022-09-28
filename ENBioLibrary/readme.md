# ENBioLibrary

## Table of Contents

1. [Gradle Dependency](#gradle-dependency)
2. [Basics](#basics)
3. [ENBioLibraryActions](#ENBioLibraryActions)

## Gradle Dependency
![](https://badgen.net/badge/stable/1.1.0/blue)

#### [BioLibrary Tutorial and Samples](biolibrary/readme.md)

The `BioLibrary` module allow to collect all biometric data while you are signing document. We collect `x` `y` and `pressure` , `timestamp` etc. All data are crypted and in base64.

```gradle
dependencies {
  implementation "com.euronovate.bio:bio:1.1.0"
}
```

## Basics

Here's a very basic example of inizialization of ENBioLibrary in ENMobileSdk builder. You have to do this operation once time because we keep istance of each modules.

```gradle
.with(ENBio.Builder().build())
```

You have to **respect** *.with* order like in above example.

## ENBioLibraryActions

### addSignatureTouchedPoint

this method allow to collect all info about one point (x,y,pressure,timestamp ecc...)  

```kotlin
ENBio.getInstance().addSignatureTouchedPoint(SignatureTouchedPoint(eventX = eventX, eventY = eventY, eventPressure = event.pressure, isUpEvent = false, eventTime = event.eventTime,signatureSourceType = touchEventInputMethod))
```
you need to pass `SignatureTouchedPoint`


### isSourceTypeConsistent

if you want to check your current sourceMethod if it is consistent with other package, you will call this method. You have to pass `signatureSourceType`

```kotlin
ENBio.getInstance().isSourceTypeConsistent(touchEventInputMethod)
```
`return Bool`


### clearBioPackets

It allow to clear all biometricPacket in memory

```kotlin
ENBio.getInstance().clearBioPackets()
```

### getBioContent

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
    isFea = true,
    debugMode: Boolean = false
)
```

This is method is used to obtain a base64 of biometric data collected during the signature process or in `debugMode` can return an arraylist of `ENSignaturePoint`


So this function return a sealed class called: `ENBioResponse`

```kotlin
sealed class ENBioResponse {  
    data class encodedXml(val value: String?) : ENBioResponse() // base64  
    data class rawData(val points: ArrayList<ENSignaturePoint>?) : ENBioResponse()  
}
```

**encodedXml** is returned a base64 like a string 

**rawData** returned an arraylist of `ENSignaturePoint` is only used in debugMode


### getFirstSignaturePointEventTime

This function return a `Long` timeInMillisecond for the first point touched on drawer or collected by `ENBio`

```kotlin
ENBio.getInstance().getFirstSignaturePointEventTime()
```