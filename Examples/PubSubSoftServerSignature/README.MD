# PubSubSoftServerSignature

#### [View Releases and Changelogs](https://github.com/euronovate/ENMobileSDK-Android)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

---
![Viewer image](resReadme/imgViewer.png)

# Setup

Pull all project in your Android studio workspace, after open folder with android studio directly.

You must have an internet connection valid.

After you have to replace:

*licenseKey & baseUrl ENAuth in MainActivity
*licenseKey & baseUrl ENSoftServer in MainActivity
*access token,url, type ENPubSub in MainActivity

# Features

In this demo project we use these modules:

-Viewer
-SignatureBox
-BioLibrary
-SoftServer

To start signature, you have to tap on button "open", after confirm or abort pdf you can share pdf with specific button

## Sign Apk

We have created a certificate base to sign apk but you can remove it or replace with your.

Default credentials is:

`alias: euronovate`

`psw1: euronovate`

`ps2: euronovate`

You can find last apk in projectFolder/release (if you manually clear project, you will not find it and you have to rebuild)