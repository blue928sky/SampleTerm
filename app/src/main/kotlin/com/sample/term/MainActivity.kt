package com.sample.term

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sample.term.ui.TermScreen
import com.sample.term.ui.theme.TermTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TermTheme {
                TermScreen(
                    navigateToNextScreen = {
                        Toast.makeText(
                            this,
                            "Clicked",
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                )
            }
        }
    }
}
