# Changelogs
1. [v1.0.1](#v1.0.1)

## v1.0.1
-Core: new dialog type: list , input
-Logger: now you can log startup of application before init, and you can trace your env.
-Core: OAuth2 we are using **expiresin** to cache / remove token returned by the response first time.
-Core: keepScreenAlwaysOn
-DigitalSignage: offline mode, you can use your local media (Image,Video).
-PdfMiddleware: updated pdflib to 2.0.1
-Presenter: updated presentation layer to v1
-ENPubSub: we have introduced timer for reconnetion. See ENPubSubConfig.
-ENViewer: added idleForTimeout , must be configured. SignFieldPlaceholder to customize text.