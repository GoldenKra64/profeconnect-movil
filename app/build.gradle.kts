import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}
val chatbotUrl: String = localProperties.getProperty("SOCKET_URL") ?: "http://localhost:3000/chatbot"
val apiUrl: String = localProperties.getProperty("API_URL") ?: "http://localhost:3000/api/V1"

// Para el resto del equipo, usen API_URL, ya esta configurado con todo y la version


android {
    namespace = "com.example.myapplication"
    // Bloqueamos a la API 36 definida para el curso
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myapplication"
        // MinSdk 26 asegura compatibilidad con el 90% del mercado actual
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        
        buildConfigField("String", "CHATBOT_URL", "\"$chatbotUrl\"")
        buildConfigField("String", "API_URL", "\"$apiUrl\"")
    }

    compileOptions {
        // Forzamos Java 21 para evitar errores de bytecode
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation("io.socket:socket.io-client:2.1.1")
    implementation(libs.androidx.core.ktx)
    implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation("com.github.jeziellago:compose-markdown:0.7.2")
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    // Retrofit + OkHttp
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)
    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}