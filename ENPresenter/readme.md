# ENPresenter

## Table of Contents

1. [Gradle Dependency](#gradle-dependency)
2. [Basics](#basics)
3. [ENPresenter Actions](#presenterActions)

## Gradle Dependency

![badge_version](badge_version.svg)

The `presenter` module contains only a template used by `viewer` to render document. It is customized based on client.

```gradle
dependencies {
    implementation "com.euronovate.presenter:presenter:1.0.0"
}
```
## Basics

You shouldn't do anything because presenter is automatically built on `ENViewer` builder.

Layout color,theme are automatically set based on clientName returned from `ENAuth`after **licenseKey** or **Jwt** check

### ENPresenter Actions

```kotlin
ENPresenter.getInstance().pathPdfJs : String
```
By this method you can obtain a path of layout/theme of viewer