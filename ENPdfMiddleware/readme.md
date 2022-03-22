# ENPdfMiddleware

## Table of Contents

1. [Gradle Dependency](#gradle-dependency)
2. [Basics](#basics)
3. [ENPdfMiddlewareException](#ENPdfMiddlewareException)


## Gradle Dependency
![](https://badgen.net/badge/stable/1.0.0/blue)

The `pdfMiddleware` module is the brain of the processing about pdf Document.

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

**extractPdfStrctureModel**: by this method you can obtain pdfContainer. You have to pass document in byteArray or if document is remote (guid) you have to pass too `Document` 
```kotlin
ENPdfMiddleware.getInstance().extractPdfStructureModel(document: ByteArray): PdfContainer
```

**extractPdfStructureModel remote**: 

`Document` is a class obtain from softserver `document/find` or `document/download`

```kotlin
ENPdfMiddleware.getInstance().extractPdfStructureModel(document: ByteArray,remoteDocument: Document): PdfContainer
```
return `PdfContainer` is an aggregator of all document information. You can pass it directly to viewer


### ENPdfMiddlewareException

ENPdfMiddlewareException is a class in module

All the basic exceptions are also considered for this module, but we find one more which is this

`invalidPdf` is return during elaboration the document and it cause exception.

