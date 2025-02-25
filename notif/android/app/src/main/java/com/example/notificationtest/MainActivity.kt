package com.example.notificationtest

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationtest.ui.theme.NotificationTestTheme

class MainActivity : ComponentActivity() {
    private val channelId = "test_notifications"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hasPermissions = checkNotificationPermissions()
        if (!hasPermissions) requestNotificationPermissions()

        createNotificationChannel()

        enableEdgeToEdge()
        setContent {
            NotificationTestApp()
        }
    }

    @Composable
    fun NotificationTestApp() {

        NotificationTestTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center
                ) {
                    Greeting(name = "Android", onSendNotification = { sendNotification() })
                }
            }
        }
    }

    private fun checkNotificationPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        Log.v("NotificationTest", "Granted")
                    } else {
                        Log.v("NotificationTest", "Refused")
                    }
                }

            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
                channelId,
                "test channel",
                NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "Channel for test notifications"
        }
        val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(channel)
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Test notification")
            .setContentText("This is a test")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.v("NotificationTest", "No permissions")



                return
            }
            Log.v("NotificationTest", "We have permissions")
            notify(1, builder.build())
            notify(3, builder.build())
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, onSendNotification: () -> Unit) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Hello $name!", modifier = Modifier.padding(bottom = 16.dp))

        Button(onClick = { onSendNotification() }) {
            Text("Send Notification")
        }
    }
}