package com.euronovate.examples.ds

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.euronovate.examples.ds.theme.ENMobileSdkTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            ENMobileSdkTheme {
                MainScreen()
            }
        }
    }
}