# ENDigitalSignage

## Table of Contents

1. [Gradle Dependency](#gradle-dependency)
2. [Basics](#basics)
3. [ENDigitalSignageConfig](#digitalsignageConfig)
4. [Theme](#theme)

## Gradle Dependency

![badge_version](badge_version.svg)

![badge_version](slideshow.png)

The `digitalsignage` module contains a module with a main actity called `ENDigitalSignageActivity` that allow to have a slideshow with video/image in loop in waiting for a new document to sign/edit

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
     .with(digitalSignageConfig = ENDigitalSignageConfig(baseUrl = BuildConfig.ENDIGITALSIGNAGE_SERVER_URL, licenseCode = BuildConfig.ENDIGITALSIGNAGE_LICENSE_KEY, landPlaceholderAssetName = "landscape_placeholder.png", portPlaceholderAssetName = "portrait_placeholder.png"))
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

## ENDigitalSignage Actions

```kotlin
ENDigitalSignage.getInstance().start()
```
to start digitalSignage. 

## ENDigitalSignageTheme

You can customize ENDigitalSignageTheme with this code:

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
With this code you can only customize layout and color of **DEVICE NAME** on bottom right of digital signage. You can see screenshoot on top of Readme. (PAD001)