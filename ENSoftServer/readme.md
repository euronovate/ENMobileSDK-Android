# ENSoftServer

## Table of Contents
1. [Gradle Dependency](#gradle-dependency)
2. [Basics](#basics)
3. [ENSoftServerConfig](#ENSoftServerConfig)
4. [ENSoftServerRepositories](#ENSoftServerRepositories)

## Gradle Dependency

![badge_version](badge_version.svg)

#### [SoftServer Tutorial and Samples](softserver/readme.md)

The `SoftServer` contains all api request used to interact with document, for example:

* find document
* download
* update status document
* signPdf
* appose Checkbox, TextField, RadioButton

```gradle
dependencies {
  implementation "com.euronovate.softserver:softserver:1.0.0"
}
```

## Basics

Here's a very basic example of inizialization of ENSoftServer in ENMobileSdk builder. You have to do this operation once time because we keep istance of each modules.

```kotlin
.with(ENSoftServer.Builder()
      .with(applicationContext)
      .with(ENSoftServerConfig(baseUrl = BuildConfig.ENSOFTSERVER_SERVER_URL, BuildConfig.ENSOFTSERVER_LICENSE_KEY))
      .build())
```

You have to **respect** *.with* order like in above example.

## ENSoftServerConfig

The constructor is: 

```kotlin
class ENSoftServerConfig(
    var baseUrl: String,
    var licenseCode: String
)
```

`baseUrl` and `licenseCode` are mandatory

## ENSoftServerRepositories

All method are `suspend` so you need to call this in coroutine or async context.

**Custom Repository**

This class contains all api with baseEndpoint `business/custom/`

> *At this moment we have:*

`execMap` -> used to call a custom action available in softserver

**Document Repository**

This class contains all api with baseEndpoint `business/document/`

You can each method from this:

```kotlin
ENSoftServer.getInstance().getDocumentRepository()
``` 

> *At this moment we have:*

`acquireOneShotFromStream` -> allow to upload programmatically a document to softserver.

`download` -> return a detail of document, you need to pass guid.

`find` -> return a detail of a document, you need to pass guid.

`acquireSign` -> apply signature in document in specific field.

`acquireAcrofieldValue` -> with this method you can apply value of radiobutton,checkbox,textbox to document.

`updateStatusDocument` --> this method update document status (ABORT,CLOSE,UPDATE)

`documentTypeFind` --> allow to get all category type find of documents


**Dossier Repository**

This class contains all api with baseEndpoint `business/dossier/`

At this moment we have:

...tbd

##ENSoftServerException

**ENSoftServerException** is based in ***ENMobileSDKException*** but there is other exception.

You can handle softserver exception with `try / catch`

**notAuthorized** -> is returned when an api response with 401 http code