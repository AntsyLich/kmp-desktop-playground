package dev.antsy.kmp.desktop

import ProjectTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.configureSwingGlobalsForCompose
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.awaitApplication
import com.formdev.flatlaf.IntelliJTheme
import java.awt.Dimension
import java.awt.Window
import java.io.ByteArrayInputStream
import javax.swing.SwingUtilities
import javax.swing.UIManager
import kotlin.system.exitProcess

val themeTemplate = """
{
    "name": "SwingUI",
    "dark": %b,
    "author": "Mihon Open Source Project",
    "ui": { "*": { "background": "%s" } }
}
""".trimIndent()

inline val Color.rgbHex get() = String.format("#%06X", 0xFFFFFF and toArgb())

fun updateDecorationTheme(darkMode: Boolean, background: Color) {
    val theme = themeTemplate.format(darkMode, background.rgbHex)
    val laf = ByteArrayInputStream(theme.toByteArray()).use(IntelliJTheme::createLaf)
    UIManager.setLookAndFeel(laf)
    Window.getWindows().forEach(SwingUtilities::updateComponentTreeUI)
}

const val darkTheme = false
val backgroundColor = if (darkTheme) Color.Black else Color.White

@OptIn(ExperimentalComposeUiApi::class)
suspend fun main()  {
    configureSwingGlobalsForCompose()
    updateDecorationTheme(darkTheme, backgroundColor)
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
            LaunchedEffect(window) {
                window.minimumSize = Dimension(800, 600)
            }
            ProjectTheme(darkTheme) {
                App()
            }
        }
    }
}
