# ENDigitalSignage

## Table of Contents

1. [Gradle Dependency](#Gradle-Dependency)
2. [Basics](#basics)
3. [ENDigitalSignageConfig](#ENDigitalSignageConfig)
4. [ENDigitalSignageActions](#ENDigitalSignageActions)
5. [ENDigitalSignageTheme](#ENDigitalSignageTheme)

## Gradle Dependency
![](https://badgen.net/badge/stable/1.0.0/blue)

![badge_version](slideshow.png)

The `digitalsignage` module contains a main activity `ENDigitalSignageActivity` that allow to have a slideshow with video/image in loop in waiting for a new document to sign/edit

```gradle
dependencies {
  implementation "com.euronovate.digitalsignage:digitalsignage:1.0.0"
}
```
## Basics

Here's a very basic example of inizialization of ENDigitalSignage.

```kotlin
ENDigitalSignage.Builder()
     .with(applicationContext = applicationContext)
     .with(digitalSignageConfig = ENDigitalSignageConfig(baseUrl = yourServerUrl, licenseCode = yourLicenseCode, landPlaceholderAssetName = "landscape_placeholder.png", portPlaceholderAssetName = "portrait_placeholder.png"))
.build()
```
You have to **respect** *.with* order like in above example.

## ENDigitalSignageConfig

There is a config in builder of digital signage module.
This is the constructor:

```kotlin
ENDigitalSignageConfig(var baseUrl: String,
  var licenseCode: String,
  var landPlaceholderAssetName: String? = null,
  var portPlaceholderAssetName: String? = null)
```
`baseUrl` and `licenseCode` are mandatory, instead `landPlaceholderAssetName` and `portPlaceholderAssetName` are optional and they allow to customize images default: portrait, landscape 

## ENDigitalSignageActions

To start digitalSignage. 

```kotlin
ENDigitalSignage.getInstance().start()
```


## ENDigitalSignageTheme

You can customize ENDigitalSignageTheme like this:

```kotlin
class ENDefaultDigitalSignageTheme: ENDigitalSignageTheme() {
    override fun deviceTagNameLabel(): ENUIViewStyle {
        val context = ENMobileSDK.getInstance().applicationContext
        return ENUIViewStyle(bgColor = context.getColor(R.color.lightblue),
            textColor = context.getColor(R.color.white),textTypeface = font().light(),
            textSize = 20f, borderWidth = 1)
    }

    override fun deviceTagNameLayout(): ENUIViewStyle {
        val context = ENMobileSDK.getInstance().applicationContext
        return ENUIViewStyle(bgColor = context.getColor(R.color.lightblue),
            borderColor = context.getColor(R.color.gray))
    }

    override fun font(): ENFont {
        return ENDefaultFont()
    }
}
```
With this code you can only edit layout and color of **DEVICE NAME**, on bottom right of digital signage. 
You can see screenshoot on top of the Readme. (PAD001)