package com.example.getsms

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.getsms.ui.theme.GetsmsTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    private val SMS_PERMISSION_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        // Check and request SMS permissions
        if (hasSmsPermission() && hasSmsReceivePermission()) {
            initializeApp()
        } else {
            requestSmsPermissions()
        }
    }

    private fun initializeApp() {
        setContent {
            GetsmsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("User")
                    logMessage("MainActivity created")
                }
            }
        }
    }

    private fun logMessage(message: String) {
        Log.d("MainActivityLog", message)
    }

    private fun hasSmsPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // On versions below Marshmallow, permissions are granted at installation time
            true
        }
    }

    private fun hasSmsReceivePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // On versions below Marshmallow, permissions are granted at installation time
            true
        }
    }

    private fun requestSmsPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (!hasSmsPermission()) {
            permissionsToRequest.add(Manifest.permission.READ_SMS)
        }

        if (!hasSmsReceivePermission()) {
            permissionsToRequest.add(Manifest.permission.RECEIVE_SMS)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                SMS_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            // Check if the permissions are granted
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permissions granted, proceed with your app logic
                initializeApp()
            } else {
                // Permissions denied, handle accordingly (e.g., show a message or close the app)
                // You may want to inform the user about the importance of the permissions
                // and guide them to grant them in the device settings.
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }
}

