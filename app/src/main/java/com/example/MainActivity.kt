package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.UserEntity
import com.example.model.SalonScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ClientScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfessionalScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.SalonViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val viewModel: SalonViewModel = viewModel()
        val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

        if (currentUser == null) {
          AuthScreen(viewModel)
        } else {
          SalonAppRoot(viewModel, currentUser!!)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalonAppRoot(viewModel: SalonViewModel, currentUser: UserEntity) {
  var currentScreen by remember { mutableStateOf<SalonScreen>(SalonScreen.Home) }
  var showRestrictedDialog by remember { mutableStateOf(false) }

  val items = listOf(
    SalonScreen.Home,
    SalonScreen.Client,
    SalonScreen.Professional,
    SalonScreen.Admin
  )

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(text = "L'Atelier Salon", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "Perfil: ${currentUser.name} (${currentUser.role})", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
          }
        },
        actions = {
          IconButton(
            onClick = { viewModel.logout() },
            modifier = Modifier.testTag("logout_button")
          ) {
            Icon(Icons.Default.ExitToApp, contentDescription = "Sair", tint = MaterialTheme.colorScheme.primary)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
      ) {
        items.forEach { screen ->
          val selected = currentScreen.route == screen.route
          NavigationBarItem(
            selected = selected,
            onClick = {
              if (screen is SalonScreen.Admin && currentUser.role != "ADMIN") {
                showRestrictedDialog = true
              } else {
                currentScreen = screen
              }
            },
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
          onNavigateToAdmin = {
            if (currentUser.role == "ADMIN") {
              currentScreen = SalonScreen.Admin
            } else {
              showRestrictedDialog = true
            }
          }
        )
        is SalonScreen.Client -> ClientScreen(viewModel)
        is SalonScreen.Professional -> ProfessionalScreen()
        is SalonScreen.Admin -> {
          if (currentUser.role == "ADMIN") {
            AdminScreen(viewModel)
          } else {
            RestrictedAccessScreen()
          }
        }
      }
    }
  }

  if (showRestrictedDialog) {
    AlertDialog(
      onDismissRequest = { showRestrictedDialog = false },
      title = { Text(text = "Acesso Restrito", fontWeight = FontWeight.Bold) },
      text = { Text(text = "O Painel Administrativo é restrito a usuários com perfil ADMINISTRADOR. Seu perfil atual é '${currentUser.role}'.") },
      confirmButton = {
        Button(onClick = { showRestrictedDialog = false }) {
          Text("Entendido")
        }
      },
      containerColor = MaterialTheme.colorScheme.surface
    )
  }
}

@Composable
fun RestrictedAccessScreen() {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(24.dp)
    ) {
      Icon(
        imageVector = Icons.Default.AdminPanelSettings,
        contentDescription = "Restrito",
        tint = MaterialTheme.colorScheme.error,
        modifier = Modifier.size(64.dp)
      )
      Text(
        text = "Acesso Negado",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.error
      )
      Text(
        text = "Esta área é exclusiva para administradores do salão.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
