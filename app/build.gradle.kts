import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    //
}

android {
    namespace = "com.prototype.aishiteru"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.prototype.aishiteru"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Load API key from local.properties
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(FileInputStream(localPropertiesFile))
    }
    val kayraApiKey: String = localProperties.getProperty("KAYRA_API_KEY") ?: ""

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        // Add the API key as a BuildConfig field
        all {
            buildConfigField("String", "KAYRA_API_KEY", "\"$kayraApiKey\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation("com.google.android.gms:play-services-maps:19.0.0") // maps!
    implementation("com.google.android.gms:play-services-location:21.3.0") //location
    implementation("androidx.core:core-ktx:1.12.") // for ActivityCompat?
    implementation("com.google.android.material:material:1.1.0")
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")

    // GSON Dependency for JSON parsing
    implementation("com.google.code.gson:gson:2.8.8")

    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Glide for Image loading and caching
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // Jsoup for HTML parsing
    implementation("org.jsoup:jsoup:1.14.3")

    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    // this is for sign in with google
    implementation("com.google.android.gms:play-services-auth:21.1.1")
    implementation("com.google.firebase:firebase-auth-ktx:23.0.0")
    implementation ("androidx.credentials:credentials:1.2.2")
    implementation ("androidx.credentials:credentials-play-services-auth:1.2.2")
    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // stylable toasts!
    implementation("io.github.muddz:styleabletoast:2.4.0")

    //circle images
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // for HTTP Requests (NovelAI)
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
}