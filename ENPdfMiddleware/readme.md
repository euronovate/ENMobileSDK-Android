# ENPdfMiddleware

## Table of Contents

1. [Gradle Dependency](#gradle-dependency)
2. [Basics](#basics)
3. [ENPdfMiddleware Config](#pdfMiddlewareConfig)
4. [ENPdfMiddlewareException] (#pdfMiddlewareException)

## Gradle Dependency

![badge_version](badge_version.svg)

The `pdfMiddleware` module contains an extension of core and it is a brain of the processing about pdf Document for example:

* elaborate bookmarks
* signPdf
* apposeCheckbox
* apposeRadioButton
* apposeTextField

```gradle
dependencies {
    implementation "com.euronovate.pdfmiddleware:pdfMiddleware:1.0.0"
}
```
## Basics

Here's a very basic example of inizialization of ENPdfMiddleware.

```kotlin
.with(ENPdfMiddleware.Builder()
     .with(applicationContext)
     .build())
```
                
You have to **respect** *.with* order like in above example.


### ENPdfMiddleware Actions

```kotlin
ENPdfMiddleware.getInstance().extractPdfStructureModel(document: ByteArray): PdfContainer
```
By this method you can obtain pdfContainer. You have to pass document in byteArray or if document is remote (guid) you have to pass too `Document` 

```kotlin
ENPdfMiddleware.getInstance().extractPdfStructureModel(document: ByteArray,remoteDocument: Document): PdfContainer
```

`Document` is a class obtain from softserver `document/find` or `document/download`

`PdfContainer` is an aggregator of all document information. You can pass it directly to viewer

### ENPdfMiddlewareException

ENPdfMiddlewareException is a class in module

All the basic exceptions are also considered for this module, but we find one more which is this

`invalidPdf` is return during elaboration the document and it cause exception.

