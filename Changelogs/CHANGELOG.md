# Changelog

## v 1.3.16

- support for Android 14
- fix a crash during the offline license verification
- implementation of FES with OTP signature
- fix the management of default textfields and checkboxes
- better management of ABORT/CLOSE documents
- better management of textfields and checkboxes for already signed documents

## v 1.3.15

- fix for android 14 crash
- improved biodata generation speed

## v 1.3.14

- refactor didSignDocument event: guid parameter name changed to documentGuid, add signatureName to identify the signatureImage

## v 1.3.13

- change custom watermark behavior introduced with v 1.3.10. Now is an additional information below the potential signer name or timestamp

## v 1.3.12

- add didSignDocument SDK event that carries the GUID of the document and the image (Bitmap) of the newly applied signature

## v 1.3.11

- add custom FES and FEA type into ENSignDocument

## v 1.3.10

- add custom watermark height and list of strings into ENSignDocument

## v 1.3.9

- fix default values for ENTXT and ENCHK

## v 1.3.8

- fix crash on EncryptedPreferences

## v 1.3.7

- update ENLibPdf to 2.3.2
- reduce minSdk from 23 to 21 (only to compile, no Android 5 support)
- fix version's label translation on viewer

## v 1.3.6

- fix signature priority in online flow
- improved bookmark parsing

## v 1.3.5

- update pdf viewer to 1.5.22 to fix checkbox rendering for 1x1 and 2x2 checkboxes
- update ENLibPdf to 2.3.1
- add signatures priority check
- add acrofields mandatory check

## v 1.3.4

- Implement Digital Dignage filters by ext01...ext10

## v 1.3.3

- Remove sensitive information from settings page

## v 1.3.2

- Fix on Digital Dignage filter by device name/tag/serial

## v 1.3.1

- Digital Dignage filter by device name/tag/serial

## v 1.3.0

- add Use Cases
- add new configuration object for PDFMiddleware

## v 1.2.3

- Improved UI during signature

## v 1.2.2

- Hover pen management
- Minor fix

## v 1.2.1

- Integrated ENLibPdf 2.1.6
- Improved online acrofield management
- PadES B-B signature

## v 1.2.0

- Optimized textbox, checkbox and signatures elaboration.
- Bug fixes

## v 1.1.1

- Resolved a bug where hash and documentSize values weren't updated after acquire sign

## v 1.1.0

- ENSignatureBox new parameter optional `showFullScreen` 
- ENSignatureBox new type `graphologist`
- ENSignatureBox now you can show this with debugMode
- ENSignatureBox return an enum in callback with: `success`,  `rawdata`,  `error`
- ENBio return an enum in function `getEnBioContent` and this function has new parameter `debugMode` 
- ENSignatureBox: renamed SignatureTouchedPoint -> ENSignaturePoint
- ENGenericUtils: new function to convert `MotionEventType` to a string

## v 1.0.3

- ENMobileSdkConfig new parameter optional `appVersion` used in ENDigitalSignage and `ENViewer` with `ENViewerBarView`
- Fixed TimeConsumingUtil in partial method
- Minor Fixes

## v 1.0.2
- ENSignatureBox: new themes in config by enum `ENSignatureBoxType`
- ENViewer: refactor + renamed themes , added new themes `simple`  (default and old) and `theme1`  you can choiche them in `ENViewerConfig` by `ENViewerType`
- ENViewer: created a new component called `ENViewerBar` you can customize by theme and choiche base layout in `ENViewerConfig` with parameter `ENViewerBarType`
- ENViewer new `dimens` value overridable by user for icon in `ENViewerBar` and layout 
- ENCore new circular progress dialog by `ENDialogType`
- ENMobileSdk: added `considerAllSignatureFieldCharacters` bool value to consider all characters and not consider group special characters in document.
- ENPdfMiddleware:  Improved signPdf via softserver

## v 1.0.1
- ENDialog: new types: list and input.
- ENLogger: now you can log startup of application before init, and you can trace your env.
- ENCore: OAuth2 we are using **expiresin** to cache / remove token returned by the response first time.
- ENCore: keepScreenAlwaysOn new flag in mobilesdkconfig.
- ENDigitalSignage: offline mode, you can use your local media (Image,Video).
- ENPdfMiddleware: updated pdflib to 2.0.1
- ENPresenter: updated presentation layer to 1.5.11
- ENPubSub: we have introduced timer for reconnetion. See ENPubSubConfig.
- ENViewer: added idleForTimeout , must be configured. SignFieldPlaceholder to customize text.