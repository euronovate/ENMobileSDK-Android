# ENViewer

## Table of Contents

1. [Gradle Dependency](#gradle-dependency)
2. [Basics](#basics)
3. [Exception](#ensettings)
4. [ENMobileSdk Extension](#enlogger)


## Gradle Dependency

![badge_version](badge_version.svg)

The `viewer` module contains extensions to the core module, such as a document (pdf) rendering and all actions handling with acrofields for example:

* textfield
* checkbox
* radiobutton
* signature

```gradle
dependencies {
 	implementation "com.euronovate.viewer:viewer:1.0.0"
}
```

## Basics

Here's a very basic example of inizialization of ENViewer in ENMobileSdk builder. You have to do this operation once time because we keep istance of each modules.

```kotlin
.with(ENViewer.Builder()
     .with(applicationContext = applicationContext)
     .build())
```
You have to **respect** *.with* order like in above example.

![Viewer image](imgViewer.png)


### ENViewer Actions

```kotlin
ENViewer.getInstance().start(pdfContainer: PdfContainer)

```
to open viewer with `pdfContainer`. 


```kotlin
ENViewer.getInstance().isAlreadyVisible() 

```
If you want to check if viewer is alreadyVisible. (it also consider signaturebox over viewer). `return Bool`

### ENMobileSDK Extensions

When user adds in your app this module, automatically he will find himself in `ENMobileSdk.getInstance()` this methods:

**OPEN DOCUMENT LOCAL**

```kotlin
ENMobileSDK.openDocument(documentBase64: String, certPemBase64: String?=null): Boolean
```
You can use this method to open a document local in `base64`, and you can also pass `pem` used to crypt ***biometricData***. It is optional because we support too only ***graphometric signature***

Exist another similar method to open document with byte array.

```kotlin
ENMobileSDK.openDocument(data : ByteArray,certPemBase64: String?=null): Boolean
```

**OPEN DOCUMENT REMOTE**

```kotlin
suspend fun ENMobileSDK.openRemoteDocument(documentGuid: String): Boolean
```
Use this method to open document with *`guid`*. 

As you can see from code this request is suspend, so you need to call in couroutine, because we use **IO** interaction over **Network**

If you want handle `ENSoftServerException` or other type you have to use `try / catch `