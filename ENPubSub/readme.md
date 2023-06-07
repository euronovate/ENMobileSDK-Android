# ENPubSub

## Table of Contents
1. [Gradle Dependency](#gradle-dependency)
2. [Basics](#basics)
3. [ENPubSubConfig](#ENPubSubConfig)
4. [ENPubSubActions](#ENPubSubActions)

## Gradle Dependency

![](https://badgen.net/badge/stable/1.3.1/blue)

The `PubSub `module help you to estabilish a connection with websocket. At this moment we support these types:

* signalR
* WebSocket


```gradle
dependencies {
    implementation "com.euronovate.pubsub:pubsub:1.3.1"
}
```

## Basics

Here's a very basic example of inizialization of ENPubSub in ENMobileSdk builder. You have to do this operation once time because we keep istance of each modules.

```kotlin
 .with(pubSub = ENPubSub.Builder()
      .with(ENPubSubConfig(type = ENPubSubType.signalR,
  	   		connectionParams = { return@ENPubSubConfig getSignalRToken() }))
                .build())
```
You have to **respect** *.with* order like in above example.


## ENPubSubConfig

This is a class that allow to config this module.

```kotlin
class ENPubSubConfig(var type: ENPubSubType?= null,  
					 var pubSubReconnectionType: ENPubSubReconnectionType = ENPubSubReconnectionType.disabled(),
                     var connectionParams: (suspend () -> Pair<String, String>?)? = null)
```

`type` is an enum of `ENPubSubType` it contains a list of connectionType

`pubSubReconnectionType` is an sealed class used like an advanced enum:

```kotlin
sealed class ENPubSubReconnectionType {  
    data class disabled(val status: String = "disabled"): ENPubSubReconnectionType()  
    data class enabled(var connectionTimerInterval: Int = 60, var reconnectionTimerInterval: Int = 15): ENPubSubReconnectionType()  
}
```
You have a two possibility:
1) disable timer for reconnection
2) or enable and you can specify how often in seconds `connectionTimerInterval` & `reconnectionTimerInterval`

`connectionParams` is a function that user can implement that must be return a Pair with `URL` and `accessToken` if available. They are mandatory to estabilish connection.

## ENPubSubActions

**Init connection**

```kotlin
ENPubSub.getInstance().init()
```
By this method you can init connection and start. If you set connectionParam will be called by this method.

**Connection Params**

As we said before is necessary provide at module two value **accessToken** and **Url**, in certain case user must request these to another api, so we have made it available to you a function suspended where you can pass this class:

```kotlin
class ENPubSubConnectionParamConfig(
    var url: String,
    var bodyParameters: FormBody? = null,
    var bodyJson: JSONObject? = null,
    var headerParameters: HashMap<String, String>? = null,
    var oAuth2: Boolean=false
)
```

-**url** of request
-**oAuth2**: you can specificy if he will be able to use oauth2 properly configured in core
-**header body** etc... like http client request (okhttp)

After initialization of this class you can call this actions:

```kotlin
ENPubSub.getInstance().requestConnectionParam<ResponseObject>(ENPubSubConnectionParamConfig(...))
```
This maybe return you url and access token or other that you can pass in `configurationParams` with a `Pair<String,String>`

**Comunication between client/server**

Comunication between client and server must be with a message with format JSONObject.

**Subscribe to Event**

We have a set of specific event that you can subcribe to them. They are included in `ENPubSubChannel` , at this moment we have:
* startSign
* all

This is an example with ***startSign***
 
```kotlin
ENPubSub.getInstance().subscribe(ENPubSubChannel.startSign){
		//it is jsonObject
       val signDocumentGuidDTO = Gson().fromJson((it as JSONObject).toString(), ENSignDocumentGuidDTO::class.java)  

		ENMobileSDK.emitEvent(ENEventType.signDocument, signDocumentGuidDTO.convertToEvent())
}
```

**Background/Foreground**

With `ENApplication` we handle automatically connection / disconnection when user put in ***background*** or ***foreground*** application