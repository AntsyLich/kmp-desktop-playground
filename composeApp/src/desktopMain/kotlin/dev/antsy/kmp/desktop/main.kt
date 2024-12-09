package dev.antsy.kmp.desktop

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.configureSwingGlobalsForCompose
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.awaitApplication
import com.formdev.flatlaf.IntelliJTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.Window
import java.io.ByteArrayInputStream
import javax.swing.LookAndFeel
import javax.swing.SwingUtilities
import javax.swing.UIManager
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.seconds

val lightColor = """
{
    "name": "Test",
    "dark": false,
    "author": "Antsy",
    "ui": {
        "*": {
            "foreground": "#000000",
            "background": "#ffffff"
        }
    }
}
""".trimIndent()

val darkColor = """
{
    "name": "Test",
    "dark": true,
    "author": "Antsy",
    "ui": {
        "*": {
            "foreground": "#ffffff",
            "background": "#000000"
        }
    }
}
""".trimIndent()

fun String.createLaf(): LookAndFeel {
    val inputStream = ByteArrayInputStream(toByteArray())
    return IntelliJTheme.createLaf(inputStream)
}

@OptIn(
    DelicateCoroutinesApi::class,
    ExperimentalComposeUiApi::class,
)
suspend fun main()  {
    configureSwingGlobalsForCompose()
    GlobalScope.launch {
        withUIContext {
            UIManager.setLookAndFeel(darkColor.createLaf())
            for (index in 5 downTo 1) {
                println("Changing to light theme in $index seconds")
                delay(1.seconds)
            }
            println("Changing to light theme")
            UIManager.setLookAndFeel(lightColor.createLaf())
            Window.getWindows().forEach(SwingUtilities::updateComponentTreeUI)
        }
    }
    awaitApplication {
        DisposableEffect(Unit) {
            onDispose {
                exitProcess(0)
            }
        }
        Window(
            onCloseRequest = ::exitApplication,
            title = "KotlinProject",
        ) {
            App()
        }
    }
}

suspend fun <T> withUIContext(block: suspend CoroutineScope.() -> T): T = withContext(Dispatchers.Main, block)
