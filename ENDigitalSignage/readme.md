# ENDigitalSignage

## Table of Contents

1. [Gradle Dependency](#Gradle-Dependency)
2. [Basics](#basics)
3. [ENDigitalSignageConfig](#ENDigitalSignageConfig)
4. [ENDigitalSignageActions](#ENDigitalSignageActions)
5. [ENDigitalSignageTheme](#ENDigitalSignageTheme)

## Gradle Dependency
![](https://badgen.net/badge/stable/1.1.0/blue)

![badge_version](slideshow.png)

The `digitalsignage` module contains a main activity `ENDigitalSignageActivity` that allow to have a slideshow with video/image in loop in waiting for a new document to sign/edit

```gradle
dependencies {
  implementation "com.euronovate.digitalsignage:digitalsignage:1.1.0"
}
```
## Basics

Here's a very basic example of inizialization of ENDigitalSignage.

```kotlin
ENDigitalSignage.Builder()
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
  var digitalSignageMediaConfig: ENDigitalSignageMediaConfig?=null,  
  var digitalSignageUIConfig: ENDigitalSignageUIConfig?=null)
```
`baseUrl` and `licenseCode` are mandatory, instead `digitalSignageMediaConfig` and `digitalSignageUIConfig` are optional.

**ENDigitalSignageMediaConfig**

This is the constructor:
```kotlin
ENDigitalSignageMediaConfig(
 var localMediaContents: ArrayList<ENLocalMedia>?=null,  
 var landPlaceholderAssetName: String? = null,  
 var portPlaceholderAssetName: String? = null)
 ```
- `localMediaContents` is an array of objects ENLocalMedia used to play slideshow only **OFFLINE** mode with your assets. 
   An example of localMedia initiliazation:
```kotlin
ENLocalMedia(assetName = "landscape_placeholder.png",duration = 5000, ENDigitalSignageContentType.Image)
 ```
**duration** -> is in millisecond. It is a coutdown onFinish ds will go to next media.
**assetName** -> name and extension of file in assets folder of android.
**type** -> you can use `ENDigitalSignageContentType` enum with these possibilities: 1) `Image` 2) `Video`
- `landPlaceholderAssetName` this is a string parameter, must contain assetName of placeholder image in landscape mode.
-  `portPlaceholderAssetName` this is a string parameter, must contain assetName of placeholder image in portrait mode.

**ENDigitalSignageUIConfig**

Constructor is:
```kotlin
class ENDigitalSignageUIConfig(var buttonLeftBottom: ENCTAConfig?=null,  
 var directlyDs: ENCTAConfig?=null)
 ```
With this class you can customize action inside digitalSignage component available:
- `buttonLeftBottom` exist a button in bottom of ds, you can choiche how to show and it action with `ENCTAConfig`
- `directlyDs` you have available another action "double tap" to ds, you can choiche it action with `ENCTAConfig`

**ENCTAConfig**

Class used to customize cta, how to be available , visible

```kotlin
class ENCTAConfig (var actionType: ENCTAType?=null,  
    var presentationType: ENCTAPresentationType?=null) 
```

- `actionType` is a sealed class used like an enum, at this moment we have this operation allowed:

```kotlin
sealed class ENCTAType {  
    data class guid(val guid: String?=null): ENCTAType()  
    data class documentList(val documents: ArrayList<ENDialogListModel>): ENCTAType()  
    data class putBackgroundApp(val msg: String?=null): ENCTAType()  
}
```
	1) guid : show dialog with field used to insert a guid of document to open
	2) documentlist: show dialog with a document list passed via parameter.
	3) putBackgroundApp: force background mode of your app

- `presentationType` simple enum used to specify how to be visibile current CTA:
```kotlin
enum class ENCTAPresentationType {  
    always,  
    fade,  
    easterEgg  
}
```
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
You can see screenshoot:(PAD01)
![tag](tag.png)