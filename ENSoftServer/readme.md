# ENSoftServer

## Table of Contents
- [ENSoftServer](#ensoftserver)
  - [Table of Contents](#table-of-contents)
  - [Gradle Dependency](#gradle-dependency)
  - [Basics](#basics)
  - [ENSoftServerConfig](#ensoftserverconfig)
  - [ENSoftServerRepositories (Document/Custom API)](#ensoftserverrepositories-documentcustom-api)
  - [Dossier API](#dossier-api)
  - [UseCase](#usecase)
    - [Document](#document)
    - [Dossier](#dossier)
    - [Type](#type)
    - [User](#user)
  - [ENSoftServerException](#ensoftserverexception)


## Gradle Dependency
![](https://badgen.net/badge/stable/1.3.20/blue)

The `SoftServer` module contains the APIs used to interact with documents:

* find document
* download document
* update document status
* acquire signature
* acquire Checkbox, TextField

It also includes an API to retrieve information from dossiers:

* find dossier
* find documents in dossier

```gradle
dependencies {
  implementation "com.euronovate.softserver:softserver:1.3.20"
}
```

## Basics

Here's a very basic example of inizialization of ENSoftServer in ENMobileSdk builder. You have to do this operation once time because we keep istance of each modules.

```kotlin
.with(ENSoftServer.Builder()
      .with(ENSoftServerConfig(baseUrl = "your server url", "your license key")).build())
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

## ENSoftServerRepositories (Document/Custom API)

All method are `suspend` so you need to call this in a coroutine or async context.

**Custom Repository**

This class contains all api with baseEndpoint `business/custom/`

You can call each method like this:

```kotlin
ENSoftServer.getInstance().getCustomRepository()
``` 

As of now there is:

`execMap` -> used to call a custom action available in softserver

**Document Repository**

This class contains all api with baseEndpoint `business/document/`

You can call each method like this:

```kotlin
ENSoftServer.getInstance().getDocumentRepository()
``` 

As of now there are:

`acquireOneShotFromStream` -> allow to upload programmatically a document to softserver.

`download` -> return a detail of document, you need to pass guid.

`find` -> return a detail of a document, you need to pass guid.

`acquireSign` -> apply signature in document in specific field.

`acquireAcrofieldValue` -> with this method you can apply value of radiobutton,checkbox,textbox to document.

`updateStatusDocument` --> this method update document status (ABORT,CLOSE,UPDATE)

`documentTypeFind` --> allow to get all category type find of documents

## Dossier API

API with base endpoint `business/dossier/`

All method are suspend so you need to call this in a coroutine or async context.

As of now there are:

`findDossier` -> Retrieve dossier metadata given its GUID.

```kotlin
ENMobileSDK.getInstance().findDossier(dossierGuid = "GUID")
``` 

`findDocumentsInDossier` -> Retrieve all the documents in a dossier given the dossier's GUID.

```kotlin
ENMobileSDK.getInstance().findDocumentsInDossier(dossierGuid = "GUID")
``` 

> Please note that all the functions for the dossier's API could throw an exception that has to be managed.

## UseCase

With 1.3.0 we introduced compatibility with `hilt/dagger` and we started using of usecase to obtain info, like softserver. This API is an alternative to the old API.

As of now we released:

### Document

-`DocumentFindUseCase` : used to retrieve a specific document.
-`DocumentUpdateStatusUseCase` : used to updated document status.

### Dossier

-`DossierFindDocumentsUseCase` : obtain a set list of all documents assigned to a dossier.
-`DossierFindSingleUseCase` : retrieve single dossier
-`DossierFindUseCase` : retrieve a list of dossier by userId
-`DossierStatusUpdateUseCase` : used to updated dossier status.

### Type

-`TypeFindUseCase` : obtain a list of all type in database

### User

-`FindUserUseCase` : authenticate inside softserver

**EXAMPLE** 

you have to initialize useCase via Hilt for each useCase in your activity, after that you can call by istance and method `.invoke`

```
documentFindUseCase.invoke(  
    Gson().fromJson(  
        payload.toString(),  
        DocumentFindPayload::class.java  
    )  
)
```

## ENSoftServerException

**ENSoftServerException** is based in ***ENMobileSDKException*** but there is other exception.

You can handle softserver exception with `try / catch`

**notAuthorized** -> is returned when an api response with 401 http code