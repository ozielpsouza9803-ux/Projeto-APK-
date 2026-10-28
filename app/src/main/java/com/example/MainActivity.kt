package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.model.SalonScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.ClientScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfessionalScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        SalonAppRoot()
      }
    }
  }
}

@Composable
fun SalonAppRoot() {
  var currentScreen by remember { mutableStateOf<SalonScreen>(SalonScreen.Home) }

  val items = listOf(
    SalonScreen.Home,
    SalonScreen.Client,
    SalonScreen.Professional,
    SalonScreen.Admin
  )

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
      ) {
        items.forEach { screen ->
          val selected = currentScreen.route == screen.route
          NavigationBarItem(
            selected = selected,
            onClick = { currentScreen = screen },
            icon = {
              val iconVector = when (screen) {
                is SalonScreen.Home -> Icons.Default.Home
                is SalonScreen.Client -> Icons.Default.Person
                is SalonScreen.Professional -> Icons.Default.Badge
                is SalonScreen.Admin -> Icons.Default.AdminPanelSettings
              }
              Icon(imageVector = iconVector, contentDescription = screen.title)
            },
            label = { Text(screen.title) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = MaterialTheme.colorScheme.primary,
              selectedTextColor = MaterialTheme.colorScheme.primary,
              unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
              unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
              indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_${screen.route}")
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentScreen) {
        is SalonScreen.Home -> HomeScreen(
          onNavigateToClient = { currentScreen = SalonScreen.Client },
          onNavigateToProfessional = { currentScreen = SalonScreen.Professional },
          onNavigateToAdmin = { currentScreen = SalonScreen.Admin }
        )
        is SalonScreen.Client -> ClientScreen()
        is SalonScreen.Professional -> ProfessionalScreen()
        is SalonScreen.Admin -> AdminScreen()
      }
    }
  }
}
