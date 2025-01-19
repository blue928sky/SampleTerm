import com.android.build.gradle.tasks.MergeResources
import org.jdom2.Document
import org.jdom2.Element
import org.jdom2.output.Format
import org.jdom2.output.XMLOutputter

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.sample.term"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sample.term"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    // 生成した strings.xml を認識させる
    sourceSets {
        named("main") {
            val buildDir = layout.buildDirectory.get().asFile
            res.srcDirs(buildDir.resolve("generated/custom/res"))
        }
    }
}

// リソースが生成されてマージされる前に実行するように依存を持たせる
tasks.withType<MergeResources> {
    dependsOn.add("generateTermTask")
}

// term.html を strings.xml に変換するタスクを追加
tasks.register("generateTermTask") {
    val stringId = "term_content"   // R.string.term_content
    val termFile = File(project.projectDir, "term.html")
    val outputDir = File(layout.buildDirectory.get().asFile, "generated/custom/res/values") // build/generated/custom/res/values/ 配下に生成

    doFirst {
        outputDir.mkdirs()

        val termHtml = termFile.readText()
        val term = termHtml
            // 改行削除
            .replace("""[\n\r]""".toRegex(), "")
            // コメント削除
            .replace("""<!--.+?-->""".toRegex(), "")
            // <body> タグ内のみ抽出
            .replace(""".*<body>(.+?)</body>.*""".toRegex(), "$1")

        val resourcesElement = Element("resources")
        val termContentElement = Element("string").setAttribute("name", stringId).setText(term)
        resourcesElement.addContent(termContentElement)

        val document = Document(resourcesElement)
        val outputter = XMLOutputter(Format.getPrettyFormat())

        File(outputDir, "strings.xml").writeText(outputter.outputString(document))
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
