# ENPubSub

## Table of Contents

1. [Gradle Dependency](#gradle-dependency)
2. [Basics](#basics)
3. [ENPubSub Actions](#pubsubActions)

## Gradle Dependency

![badge_version](badge_version.svg)

The `PubSub ` module allow to estabilish a connection with websocket in particual with these types of ws:

* signalR
* WebSocket


```gradle
dependencies {
    implementation "com.euronovate.pubsub:pubsub:1.0.0"
}
```

## Basics

Here's a very basic example of inizialization of ENPubSub in ENMobileSdk builder. You have to do this operation once time because we keep istance of each modules.

```kotlin
 .with(pubSub = ENPubSub.Builder()
      .with(ENPubSubConfig(type = ENPubSubType.signalR,
  	   		connectionParams = { return@ENPubSubConfig getSignalRToken() }))
                .with(applicationContext)
                .build())
```
You have to **respect** *.with* order like in above example.



## ENPubSubConfig

This is a class that allow to config this module.

```kotlin
 ENPubSubConfig(var type: ENPubSubType?= null,
                var connectionParams: (suspend () -> Pair<String, String>?)? = null)
```

`type` is an enum of `ENPubSubType` it contains a list of connectionType available at this moment we support:

* websocket
* signalR

`type` is a function that user can implement that must be return a Pair with `URL` and `accessToken` if available. They are mandatory to estabilish connection.

### ENPubSub Actions

**Init connection**

```kotlin
ENPubSub.getInstance().init()
```
By this method you can init connection and start. If you set connectionParam will be called with this method.

**Connection Params**

As we said before is necessary provide at module two value accessToken and Url, in certain case user must request these to another api, so we have made it available to you a function suspend where you can pass this class:

```kotlin
class ENPubSubConnectionParamConfig(
    var url: String,
    var bodyParameters: FormBody? = null,
    var bodyJson: JSONObject? = null,
    var headerParameters: HashMap<String, String>? = null
)
```
After initialization of this class you can call this actions:

```kotlin
ENPubSub.getInstance().requestConnectionParam<ResponseObject>(ENPubSubConnectionParamConfig(...))
```
This maybe return you url and access token or other that you can pass in `configurationParams` with a `Pair<String,String>`

**Comunication between client/server**

Comunication between client and server must be with a message with format JSONObject.
**Subscribe to Event**

We have a set of specific event that you can subcribe to them. They is included in `ENPubSubChannel` at this moment we have:
* startSign
* all

This is an example with ***startSign***
 
```kotlin
ENPubSub.getInstance().subscribe(ENPubSubChannel.startSign){
		//it is jsonObject
       val startSignDTO = Gson().fromJson((it as JSONObject).toString(), StartSignDTO::class.java)
       ... open document ... send event to mobilesdk
       ENMobileSDK.emitEvent(ENEventType.signDocument, startSignDTO) 
}```

**Background/Foreground**

With `ENApplication` we handle automatically connection / disconnection when user put in ***background*** or ***foreground*** application