# EO RO Officer Preparation App (Android)

A premium, highly-optimized, offline-first Android application designed to help candidates prepare for the **Rajasthan Public Service Commission (RPSC) Executive Officer (EO) and Revenue Officer (RO)** exam. Built with Jetpack Compose, Material 3, and Kotlin Coroutines/Flow, this app follows Clean Architecture principles with Room DB local persistence and beautiful visual elements.

---

## 🚀 Key Architectural Features
- **Modern UI**: Full Material Design 3 implementation with support for edge-to-edge layouts, custom dark mode, fluid transitions, and a premium visual theme (Cosmic Obsidian & Premium Gold).
- **Offline Mode & Room Database**: Persistent storage for study chapters, bookmarks, mock scores, custom flashcards, and reading highlights.
- **Kindle-Style Reader**: Seamless reading mode with custom highlights, page selectors, and multi-language translations (English & Hindi).
- **Adaptive Flashcards**: Flashcards for crucial terminologies and definitions, complete with favorites filters.
- **Robust CI/CD**: A comprehensive Codemagic pipeline configuration (`codemagic.yaml`) that automatically runs unit tests, compiles the debug APK, and builds/signs the release Android App Bundle (AAB).

---

## 🛠️ Project Structure & Gradle Build System

This project contains a fully buildable, production-ready Gradle Kotlin DSL setup (`.gradle.kts`) which can be imported directly into **Android Studio** or built on the command line and automated via **Codemagic** without requiring pre-generation.

### Directory Structure & Required Files:
```text
├── codemagic.yaml             # Complete Codemagic CI/CD Pipeline
├── app/
│   ├── build.gradle.kts       # App module Gradle configuration
│   ├── proguard-rules.pro     # Configured ProGuard/R8 Rules for production
│   └── src/                   # Main Kotlin Source, XML Resources & Test suite
├── gradle/
│   ├── libs.versions.toml     # Centralized Version Catalog (Dependency Management)
│   └── wrapper/
│       ├── gradle-wrapper.jar # Gradle wrapper executable binary
│       └── gradle-wrapper.properties
├── build.gradle.kts           # Root Gradle build file
├── settings.gradle.kts         # Module configurations
├── gradle.properties          # Configured parallel execution & incremental builds
├── local.properties.example   # Example file for local SDK directory setup
├── .gitattributes             # Prevents binary file corruption in Git
└── .gitignore                 # Configured VCS ignores
```

---

## 💻 Local Development Setup

To compile and run this project locally, ensure you have the following requirements:
- **JDK 21** installed on your system.
- **Android Studio Koala (or newer)**.

### Step 1: Clone the Repository
```bash
git clone https://github.com/your-username/RPSC-EO-Prep.git
cd RPSC-EO-Prep
```

### Step 2: Configure Android SDK Path
Copy `local.properties.example` to `local.properties`:
```bash
cp local.properties.example local.properties
```
Open `local.properties` and define your local Android SDK directory path:
```properties
sdk.dir=/Users/yourusername/Library/Android/sdk
```

### Step 3: Run Unit Tests
To run local JVM unit tests (which include tests for Room database, Content Version Manager syncing, and Repository functionality using **Robolectric**):
```bash
./gradlew testDebugUnitTest
```

### Step 4: Build Debug APK
To assemble a local debug APK:
```bash
./gradlew :app:assembleDebug
```
The resulting APK will be saved at:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 📦 Generating Production Artifacts (R8 & Optimizations Enabled)

The production configuration has **R8 minification (ProGuard)** and **resource shrinking** fully enabled for optimized size and maximum performance.

### 1. Build Release AAB (Android App Bundle)
To compile the release bundle:
```bash
./gradlew :app:bundleRelease
```
The generated bundle will be stored at:
`app/build/outputs/bundle/release/app-release.aab`

---

## 🤖 Codemagic CI/CD Pipeline

The `codemagic.yaml` pipeline automatically builds and tests your application on every push or pull request to the `main` or `master` branches.

### Pipeline Features:
1. **Java 21 Setup**: Configures the build machine with JDK 21.
2. **Dependency Caching**: Codemagic caches Gradle files and build outputs automatically to ensure fast, incremental execution.
3. **Unit Testing**: Automatically executes the JVM Robolectric suite.
4. **Debug Compilation**: Generates the debug APK and registers it as a downloadable build artifact.
5. **Keystore Decoding**: Looks for the `RELEASE_KEYSTORE_BASE64` environment variable. If found, it decodes it and signs the AAB. If not found, it automatically compiles a fallback self-signed keystore to guarantee that the compilation never fails in your builds.
6. **Release Compilation**: Generates the signed release AAB and uploads it as a build artifact.

---

## 🔑 Environment Variables for Codemagic Release Signing

To sign your release artifacts with your production credentials automatically on Codemagic, add the following variables under **Environment variables** (within the `keystore_credentials` group or globally) in your Codemagic application settings:

| Variable Name | Description | Value Example |
|---------------|-------------|---------------|
| `RELEASE_KEYSTORE_BASE64` | Base64-encoded string of your `.keystore` / `.jks` file | `base64 -w 0 my-key.jks` |
| `STORE_PASSWORD` | Password used to access the Keystore container | `your-secure-keystore-pass` |
| `KEY_PASSWORD` | Password for the specific key alias inside the Keystore | `your-secure-key-pass` |

---

## 🌐 Deploying to Google Play Store

Once you have downloaded the optimized `app-release.aab` compiled by Codemagic, follow these steps to deploy to the Play Store:

1. **Google Play Console**: Log in to your [Google Play Console](https://play.google.com/console).
2. **Select Application**: Choose **EO RO Officer** or create a new application if this is the first deployment.
3. **Internal/Closed Testing**: Navigate to **Testing > Internal testing** (recommended first step) or **Production**.
4. **Create New Release**: Click **Create new release** at the top right.
5. **Upload AAB**: Drag and drop the `app-release.aab` file downloaded from your GitHub workflow run.
6. **Release Notes**: Provide bilingual release notes (English & Hindi) detailing the new premium preparation features.
7. **Review and Rollout**: Complete the setup questionnaires, verify the target SDK, and click **Save and publish**!
