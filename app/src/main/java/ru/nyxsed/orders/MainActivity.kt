package ru.nyxsed.orders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.nyxsed.orders.ui.MainScreen
import ru.nyxsed.orders.ui.theme.OrdersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OrdersTheme {
                MainScreen()
            }
        }
    }
}
