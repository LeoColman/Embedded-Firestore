# Embedded-Firestore

[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/Kerooker/Embedded-Firestore/Check)](https://github.com/Kerooker/Embedded-Firestore/actions)
[![GitHub](https://img.shields.io/github/license/Kerooker/Embedded-Firestore)](LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/top.colman.embeddedfirestore/embedded-firestore)](https://search.maven.org/search?q=g:top.colman.embeddedfirestore)

Embedded [Cloud Firestore](https://firebase.google.com/docs/firestore) client, to be used in unit testing Firestore.

This library simulates the features from `Firestore`, allowing you to write Unit Tests without mocking every single function from `Firestore`. 

This might not be as precise as the real Firestore, but will come close enough for most usages.

## Setting up with Gradle

Add this line to your `build.gradle` dependencies, and you should be good to go!

```kotlin
testImplementation("top.colman.embeddedfirestore:embedded-firestore:${version}")
``` 

## Usage

**Embedded Firestore** usage is pretty simple.

```kotlin
val client: Firestore = EmbeddedFirestore().createClient {
    // Optional: Start a collection
    createCollection("MyCollectionId") {
    
        // Optional: Start some documents
        createDocument("MyDocumentId", myPojo)
        createDocument("MyOtherDocumentId", myDocumentMap)
    }
}

val myTarget = MyClassThatUsesFirestore(client)
```

## Project State
This library is in a very early state. I just hacked around until my use case was met, and tried to make it at least usable for simpler cases. 

## Contributing
If you believe that you can help me improve the features of this library, please feel free to make a pull request or open an issue!