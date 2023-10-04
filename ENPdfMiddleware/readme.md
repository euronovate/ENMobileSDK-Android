# ENPdfMiddleware

## Table of Contents

- [ENPdfMiddleware](#enpdfmiddleware)
  - [Table of Contents](#table-of-contents)
  - [Gradle Dependency](#gradle-dependency)
  - [Basics](#basics)
  - [ENPDFMiddlewareConfig](#enpdfmiddlewareconfig)
    - [ENPdfMiddleware Actions](#enpdfmiddleware-actions)
    - [ENPdfMiddlewareException](#enpdfmiddlewareexception)


## Gradle Dependency
![](https://badgen.net/badge/stable/1.3.7/blue)

The `pdfMiddleware` module is the brain of the processing about pdf Document.

```gradle
dependencies {
    implementation "com.euronovate.pdfmiddleware:pdfMiddleware:1.3.7"
}
```
## Basics

Here's a very basic example of inizialization of ENPdfMiddleware.

```kotlin
.with(ENPdfMiddleware.Builder().with(ENPdfMiddlewareConfig(closeDocumentStatusOnConfirm = true, abortDocumentStatusOnCancel = true)).build())
```
                
You have to **respect** *.with* order like in above example.


## ENPDFMiddlewareConfig

As you can guess from the builder source code, you have the possibility to configure some parameters (some are optional other not) of the "core"

The Constructor is:
```kotlin
class ENPdfMiddlewareConfig (  
       val closeDocumentStatusOnConfirm: Boolean,   
       val abortDocumentStatusOnCancel: Boolean)
)
```
and this is an example:

```kotlin
with(ENPdfMiddlewareConfig(closeDocumentStatusOnConfirm = true, abortDocumentStatusOnCancel = true)
```
* `closeDocumentStatusOnConfirm` is a flag used in viewer, if it's true after you will tap on close document viewer will call pdfMiddleware(softserver) to close document ---> `updateDocumentStatus` to **CLOSE**

* `closeDocumentStatusOnConfirm` is a flag used in viewer, if it's true after you will tap on abort document viewer will call pdfMiddleware(softserver) to  `updateDocumentStatus` with status **ABORTED**

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

