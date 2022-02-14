# Core

## Table of Contents

1. [Gradle Dependency](#gradle-dependency)
2. [Basics](#basics)
3. [ENSettings](#ensettings)
4. [ENLogger](#enlogger)
5. [Initialization Callback] (#initialization-callback)
6. [ENAuth](#enauth)
7. [ENDialog](#endialog)
8. [Utilities](#utilities)
9. [Config](#config)
10. [Theming](#theming)
11. [ENEventInternal](#eventInternal)
12. [Actions] (#actions)
13. [Exceptions] (#exceptions)

## Gradle Dependency

![badge_version](badge_version.svg)

The `core` module contains everything you need to get started with the library. It contains all
core and:

* utilities
* logger
* enauth
* endialog
* ensettings

```gradle
dependencies {
  implementation 'com.euronovate.mobilesdk:core:1.0.0'
  
  //OTHER NECESSARY
  implementation 'com.google.code.gson:gson:2.8.7'
  implementation 'androidx.preference:preference-ktx:1.1.1'
  implementation 'com.vmadalin:easypermissions-ktx:1.0.0'
  
  //lifecycle
  implementation "android.arch.lifecycle:extensions:1.1.1"
  implementation "androidx.lifecycle:lifecycle-runtime:2.0.0"
  implementation "androidx.lifecycle:lifecycle-extensions:2.0.0"
  annotationProcessor "androidx.lifecycle:lifecycle-compiler:2.0.0"
  
   // crypto
  implementation "org.bouncycastle:bcprov-jdk15on:1.61"
  implementation "org.bouncycastle:bcpkix-jdk15on:1.61"
  
  //API okhttp
  implementation "com.squareup.okhttp3:okhttp:4.7.2"
  implementation "com.squareup.okhttp3:logging-interceptor:4.7.2"
  implementation 'org.conscrypt:conscrypt-android:2.2.1'
  
  //LOGGING
  implementation "org.slf4j:slf4j-simple:1.6.1"
    //IO
  implementation 'org.simpleframework:simple-xml:2.7.1'
  api 'io.jsonwebtoken:jjwt-api:0.10.7'
  implementation 'androidx.exifinterface:exifinterface:1.3.2'
  runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.10.7'
  runtimeOnly('io.jsonwebtoken:jjwt-orgjson:0.10.7') {
     exclude group: 'org.json', module: 'json' //provided by Android natively
  }
}
```

## Basics

Here's a very basic example of inizialization of ENMobileSDK. You have to do this operation once time because we keep istance of each modules.

```kotlin
ENMobileSDK.Builder()
     .with(settings = ENSettings.Builder(applicationContext).build())
     .with(logger = ENLogger.Builder()
            .with(applicationContext)
            .with(ENLoggerConfig(true,ENLogger.VERBOSE)).build())
     .with(applicationContext)
     .with(initializationCallback = this@MainActivity)
     .with(authConfig = ENAuthConfig(licenseKey = "your license key of enauth", serverUrl= "enauth server url"))
     .with(ENMobileSdkConfig(skipSSL = false,certificateOwnerInfo = ENCertificateOwnerInfo()))
     .with(theme = ENDefaultTheme())
     .build()
```
You have to **respect** *.with* order like in above example.

## ENSettings

The `settings` class is used to change runtime and save in persistance area all parameters in enmobilesdk and each submodules config.

This is a simple snippet

```kotlin
.with(settings = ENSettings.Builder()
	 .with(context = applicationContext)
	 .build())

```
Only `context` is required. After inizialization you can use

### ENSettings Actions

```kotlin
ENSettings.getInstance().start()

```
to open settings activity like this:

![settings](settings.png)

You can also getValue saved in settings (String, Boolean at this moment):

```kotlin
ENSettings.getInstance().getValue("key", defaultValue)

```
Or you can add custom settings editable in activity after:

```kotlin
ENSettings.getInstance().addSettings("section name", ENSettingsConf("key","label name", ENSettingsConfType.input, valueFinal))
```
**ENSettingsConfType** is an enum with these options:

* *input*
* *switch*
* *label*

## ENLogger

The `logger`class is used to trace exception,debug info, api or anything. All logs are written in different file *.txt* with name based on current day with this format: *dd/MM/yyyy*

This is a simple snippet

```kotlin
.with(logger = ENLogger.Builder()
	.with(applicationContext)
	.with(ENLoggerConfig(debuggable = true, logLevel = ENLogger.VERBOSE, logServerConfig = ENLogServerConfig(baseUrl = null, null)))
	.build()
)
```
You have to **respect** *.with* order like in above example.

*applicationContext* is required

*loggerConfig* is required and with ENLoggerConfig class you can enable,disable logging, configurate logserver or level of logging.

```kotlin
ENLoggerConfig(var debuggable: Boolean, @LogLevel var logLevel: Int,
               var logServerConfig: ENLogServerConfig?=null))
```
* **debuggable**: you can enable / disable logging
* **loglevel**: is an enum like android native (VERBOSE,ERROR,DEBUG) with this you can choice level of logging
* **logServerConfig**: is optional and is used to comunicate with LOGSERVER in Backend. 
This is the constructor:

```kotlin
ENLogServerConfig(baseUrl: String? = null, licenseCode: String? = null)
```
Exist a class to handle ENAuth Exception. It is called `ENLoggerException`

### ENLogger Actions

Log an error

```kotlin
ENLogger.getInstance().e(TAG, "[${javaClass.simpleName}]: ${Exception().stackTrace[0].lineNumber} $message",exception)
```

Log an info message

```kotlin
ENLogger.getInstance().i(TAG, "[${javaClass.simpleName}]: " + "${Exception().stackTrace[0].lineNumber} ${message}")
```

Send log files after compression in .zip


```kotlin
ENLogger.getInstance().sendLogEmailIntent("app name",context)
```
Return only zip path file with logs inside:

```kotlin
ENLogger.getInstance().zipLogFile()
```

## Initialization Callback

This callback is required and it allow to notify user when all'sdks are initialized and ENAuth activated all product or not.

```kotlin
.with(initializationCallback: ENMobileInitializationCallback<String>)
```

ENMobileInitializationCallback is a class that contains:

```kotlin
fun didGetResponse(response: ENMobileSDKResponse<String>?) {
        when (response) {
            is ENMobileSDKResponse.error -> {
                //handle error with response.error
            }
            is ENMobileSDKResponse.success -> {
                //your code after initialization
            }
        }
    }
```

## ENAuth

ENAuth is a fundamental class used to activate and checking license of each submodules.
You can configure ENAuth in two ways:
***with jwt*** for example to use in offline mode

```kotlin
.with(authConfig = ENAuthConfig(licenseKey = "yourLicenseKey",
                serverUrl = "yourUrl",
                jwt = "yourJwt"))
```

or online mode with licenseKey and serverUrl:

```kotlin
.with(authConfig = ENAuthConfig(licenseKey = "yourLicenseKey", serverUrl = "yourUrl"))
```

If you haven't serverUrl and licenseKey or Jwt you can contact [info@euronovate.com ]() to request a trial. **Without their sdk won't initialized** so if you try to call an instance of a modules you will get a **crash**

There is other parameter that you can pass via constructor of ENAuthConfig:

```kotlin
ENAuthConfig(
    var licenseKey: String,
    var serverUrl: String,
    var username: String? = null,
    var password: String? = null,
    var productId: String? = "MAPAND:*",
    var jwt: String? = null,
)
```

for example you can request an activation for onyl a specific product, or you can pass username e password.

Exist a class to handle ENAuth Exception. It is called `ENAuthException`

###ENAuthException

We have default exception core, but there are other type specific to ENAuthException:

* **licenseVerificationWrongDeviceId** is returned when your jwt contains a wrong device uuid different of your current device.
* **someProductsHaveProblems**  your jwt contains a different list of product(module) that you are trying to activate.
* **noProductToActivate** is returned when there isn't product(module) available to be activate.
* **licenseJwtEmpty** your jwt is empty or not valid.

## ENDialog

ENDialog is a class that allows to prompt a new dialog. There is a differente type of dialog. All types they are included in enum: `ENDialogType`

```kotlin
enum class ENDialogType {
    positive,
    negative,
    warning,
    progress,
    question
}
```
You can prompt a dialog with:

```kotlin
ENDialog.getInstance().dialog(activity: Activity, 
	dialogType: ENDialogType, 
	listener: ENDialogInterface?=null, 
	dialogTextConfig: ENDialogTextConfig?=null
)
```

listener is a class like a callback that handle onClick or/and onLongClick of allUI:

```kotlin
override fun onClick(view: View, dialogItem: ENDialogUItem, dialog: Dialog) {
       super.onClick(view, dialogItem, dialog)
       when(dialogItem){
       	ENDialogUItem.buttonRight ->{
                   
       	}
       	else->{} 
       }
}
```

ENDialogUItem is an enum that contains all UI Item in dialog:

```kotlin
enum class ENDialogUItem {
    buttonLeft,
    buttonRight,
    title,
    subTitle,
    iconDx
}
```

To Customize text inside a dialog.

```kotlin
ENDialogTextConfig( var title: String?= null, var content: String?=null,
var rightButton: String?=null, var leftButton: String?=null)
```
![settings](positivedialog.png)


**ProgressDialog (Indeterminate)**

```kotlin
ENDialog.getInstance().dialog(ENApplication.currentActivity()!!,dialogType = ENDialogType.progress,
	dialogTextConfig = ENDialogTextConfig(title= "title", content= "content"))
```

![settings](progressdialog.png)

###ENDialog Actions

After initialized dialog you can use all method like Dialog Android

[https://developer.android.com/reference/android/app/AlertDialog](https://developer.android.com/reference/android/app/AlertDialog)

for example `.show()` `.dismiss()`


## Utilities

Is a package that contains a set of class Utils. It is initialized automatically with core build.
You can call all static method from your, we use their inside the core. 
> This is the list of Files utils:

* `ENAnimationUtils`
* `ENBase64Utils`
* `ENBitmapUtils`
* `ENBlurUtils`
* `ENCanvasUtils`
* `ENColorUtils`
* `ENCryptoUtils`
* `ENDateUtils`
* `ENDeviceUtils`
* `ENFileUtils`
* `ENGenericUtils`
* `ENHomonymsUtils`
* `ENImageUtils`
* `ENJSONUtils`
* `ENLanguageUtils`
* `ENNetworkUtils`
* `ENResourcesUtils`
* `ENStringUtils`
* `ENTypefaceUtils`
* `ENViewUtils`
* `ENWebViewUtils`

It also contain an `ENApplication` and `ENActivitityLifecycleCallback` they are necessary with **ENPubSub** modules

You have to set in manifest ENApplication like this:

```kotlin
<application
...
android:name=".ENApplication"
```

or you can extend your application .kt like this:

```kotlin
class MainApplication : ENApplication(){
    ...
}
```

##ENMobileSDKConfig

As you can guess from the builder source code, you have the possibility to configure some parameters of the "core"

This is an example

```kotlin
.with(mobileSdkConfig = ENMobileSdkConfig(skipSSL = true,
                OAuth2Config = null,
                certificateOwnerInfo = ENCertificateOwnerInfo(),
                certificateIntegrity = "base64ofpemwithprivatekey",
                languageConfig = ENLanguageConfig(selectorVisible = true,languageEnabled = arrayListOf(ENLanguageType.en, ENLanguageType.el))
))
```

* `skipSSL` allow to ignore https not valid or expired if a true
* `certificateOwner`allow user to customize entity of certificate autogenerated during applying signature in field. If `certificateIntegrity` was set this parameter won't used.
The constructor is:

```kotlin
ENCertificateOwnerInfo (
    var organization: String? = "Euronovate SA",
    var countryCode: String? = "Switzerland",
    var localityName: String? = "Lugano",
    var commonName: String? = "Android SDK",
)
```

* `certificateOwner` must contains base64 of pem with privateKey and certificate. If it is set, the certificate generation operation will be skipped during the application of the first signature in the document
* `languageConfig` allow user to set a list of avaiable language in app based on our enum `ENLanguageType` in the specific config you can also decided if enable or not languagePicker `ENViewer` and/or `ENDigitalSignage`
* `enableSignatureOverwrite` is a flag that allow to overwrite a signature in a document if set a true, but if you set a false you won't able to overwrite a signature after the first
* `OAuth2Config` if you need to enable OAuth2 for each api request you can configure it with this class.

```kotlin
 ENOAuth2Config(
    var url: String,
    var bodyParameters: FormBody? = null,
    var bodyJson: JSONObject? = null,
    var headerParameters: HashMap<String, String>? = null,
)
```
You have to pass `url` compulsory. `bodyParameters` or `bodyJson` or `headerParameters` is optionally and you can configure if you need.

Response must be like this:

```kotlin
class ENOAuth2Response(
    @SerializedName("access_token")
    var accessToken: String?,
    @SerializedName("expires_in")
    var expiresIn: Long?
)
```

If OAuth2 is configured in each request will be an `Authorization Bearer` with `accessToken` that you have provided to us

## Theming (ENTheme)

Each client can customize some ui parts of sdk at this moment:


```kotlin
abstract class ENTheme {
    abstract fun signatureBoxTheme(): ENSignatureBoxTheme
    abstract fun dialogsTheme(): ENDialogsTheme
    abstract fun digitalSignageTheme(): ENDigitalSignageTheme
}
```

In `ENSignatureBox` we have explained how to customized it theme.

Idem with `ENDigitalSignageTheme`.

Instead `ENDialogsTheme` if you want customize follow these examples:

```kotlin
class ENDefaultDialogsTheme: ENDialogsTheme(){
    override fun positiveDialog(): ENDialogConfig {
        val context = ENMobileSDK.getInstance().applicationContext
        return ENDialogConfig(title = ENUIViewStyle(textSize = 33f,textColor = context.getColor(R.color.lightblue),textTypeface = font().regular()),
            subTitle = ENUIViewStyle(textSize = 25f,textColor = context.getColor(R.color.whitelightsubtitle),textTypeface = font().light()),
            bg= ENUIViewStyle(bgColor = context.getColor(R.color.bgcolordialog)),
            rightButton = ENUIViewStyle(textSize = 25f,textColor = context.getColor(R.color.yellow),textTypeface = font().regular(),borderColor = context.getColor(
                R.color.lightblue),borderWidth = 1),
            rightBand = ENUIViewStyle(bgColor = context.getColor(R.color.lightblue)),
            rightIcon = ENUIViewStyle(srcImage = R.drawable.ic_done,tintColor = context.getColor(R.color.white))
        )
    }

    override fun negativeDialog(): ENDialogConfig {
        val context = ENMobileSDK.getInstance().applicationContext
        return ENDialogConfig(title = ENUIViewStyle(textSize = 33f,textColor = context.getColor(R.color.lightblue),textTypeface =  font().regular()),
            subTitle = ENUIViewStyle(textSize = 25f,textColor = context.getColor(R.color.whitelightsubtitle),textTypeface =  font().light()),
            bg= ENUIViewStyle(bgColor = context.getColor(R.color.bgcolordialog)),
            rightButton = ENUIViewStyle(textSize = 25f,textColor = context.getColor(R.color.yellow),textTypeface = font().regular(),borderColor = context.getColor(
                R.color.lightblue),borderWidth = 1),
            rightBand = ENUIViewStyle(bgColor = context.getColor(R.color.redsemidark)),
            rightIcon = ENUIViewStyle(srcImage = R.drawable.ic_error,tintColor = context.getColor(R.color.white))
        )
    }
    override fun warningDialog(): ENDialogConfig {
        val context = ENMobileSDK.getInstance().applicationContext
        return ENDialogConfig(title = ENUIViewStyle(textSize = 33f,textColor = context.getColor(R.color.lightblue),textTypeface = font().regular()),
            subTitle = ENUIViewStyle(textSize = 25f,textColor = context.getColor(R.color.whitelightsubtitle),textTypeface = font().light()),
            bg= ENUIViewStyle(bgColor = context.getColor(R.color.bgcolordialog)),
            rightButton = ENUIViewStyle(textSize = 25f,textColor = context.getColor(R.color.yellow),textTypeface = font().regular(),
                borderColor = context.getColor(R.color.lightblue),borderWidth = 1), rightBand = ENUIViewStyle(bgColor = context.getColor(R.color.yellow)),
            rightIcon = ENUIViewStyle(srcImage = R.drawable.ic_warning,tintColor = context.getColor(R.color.titletextprogressdialog))
        )
    }

    override fun progressDialog(): ENDialogConfig {
        val context = ENMobileSDK.getInstance().applicationContext
        return ENDialogConfig(title = ENUIViewStyle(textSize = 35f,textColor = context.getColor(R.color.titletextprogressdialog),textTypeface = font().regular()),
            subTitle = ENUIViewStyle(textSize = 17f,textColor = context.getColor(R.color.gray),textTypeface = font().light()),
            bg= ENUIViewStyle(bgColor = context.getColor(R.color.bgcolordialogprogress),tintColor = context.getColor(R.color.white))
        )
    }

    override fun questionDialog(): ENDialogConfig {
        val context = ENMobileSDK.getInstance().applicationContext
        return ENDialogConfig(title = ENUIViewStyle(textSize = 33f,textColor = context.getColor(R.color.lightblue),textTypeface =  font().regular()),
            subTitle = ENUIViewStyle(textSize = 25f,textColor = context.getColor(R.color.whitelightsubtitle),textTypeface = font().light()),
            bg= ENUIViewStyle(bgColor = context.getColor(R.color.bgcolordialog)),
            rightButton = ENUIViewStyle(textSize = 25f,textColor = context.getColor(R.color.yellow),textTypeface = font().regular(),borderColor = context.getColor(R.color.lightblue),borderWidth = 1),
            rightBand = ENUIViewStyle(bgColor = context.getColor(R.color.lightblue)),
            rightIcon = ENUIViewStyle(srcImage = R.drawable.ic_question,tintColor = context.getColor(R.color.white)),
            leftButton =  ENUIViewStyle(textSize = 25f,textColor = context.getColor(R.color.whitelightsubtitle), textTypeface = font().regular(),borderColor = context.getColor(R.color.lightblue),borderWidth = 1)
        )
    }

    override fun font(): ENFont {
        return ENDefaultFont()
    }
}

```

You can also customize font with `font` function, you have to override it like this:

```kotlin
class ENDefaultFont: ENFont(){
    override fun light(): Typeface {
        return ResourcesCompat.getFont(ENMobileSDK.getInstance().applicationContext, R.font.font_ttf)!!
    }
    override fun medium(): Typeface {
        return ResourcesCompat.getFont(ENMobileSDK.getInstance().applicationContext, R.font.font_ttf)!!
    }
    override fun regular(): Typeface {
        return ResourcesCompat.getFont(ENMobileSDK.getInstance().applicationContext, R.font.font_ttf)!!
    }
    override fun bold(): Typeface {
        return ResourcesCompat.getFont(ENMobileSDK.getInstance().applicationContext, R.font.font_ttf)!!
    }
}
```

## Event Driven Internal

There is an EventDriven inside a mobileSdk, at this moment we have these events:

enum class ENEventType {
    appForegrounded, -> when app is put in foreground (is vibile)
    appBackgrounded, -> when app is put in bg (not visible)
    coreInitialized, -> when ENMobileSdk finished initialization
    signDocument -> when receive an event to sign a document
}

You can `subscribe / unsubscribe` or `emit` to their.

**Subscription**

```kotlin
ENMobileSDK.subscribeEvent(ENEventCallback(event ...) { it (is Any)
	...your code when you receive message
})
```
**Unsubscription**

```kotlin
ENMobileSDK.unSubscribeEvent(ENEventCallback..)
```
**Emit Event**

You can emite event to a specific event and pass your custom data, for example:


```kotlin
ENMobileSDK.emitEvent(ENEventType.signDocument,StartSignDTO("guid"))
```

##ENMobileSdk Actions

There is a list of public method that aren't included in (settings,event driven ecc..)

**getLanguage**

```kotlin
ENMobileSDK.getInstance().getLanguage()
```
This method return current `Locale` selected in application (not based on system language). 


**setLanguage**

```kotlin
ENMobileSDK.getInstance().setLanguage(resources!!.configuration!!.locales.get(0))
```
With this method you can overwrite current language in app. After this you have to restart current activity at this moment we don't use automatic reaction of changing programmatically.

**getResourcesLocalized**

If you want to get context already localized you will cal this method and your work will simple.

```kotlin
ENMobileSDK.getInstance().getResourcesLocalized()
```

For example: if you need to update label with correct language you can use this.


##ENMobileSdkException

There is a set of exception specific used in core and submodules:

* **sdkNotInitialized** this is error is returned when there is an error with initialization.
* **missingParameterOfInitMobileSdk** is returned when you didn't follow .with order or you didn't pass parameter. Together with the error we will return the missing field type.
* **genericError** is returned when we didn't recognize error, in certain case we use it but we specify reason.
* **serverException** is returned when server of your baseurl isn't unreachable
* **noInternetConnection** is returned when you have disabled wifi or mobile. You will receive this error after api request.
* **errorObtainOAuth2Token** if you activated OAuth2, it is return when we receive an error from OAuth2 provider.