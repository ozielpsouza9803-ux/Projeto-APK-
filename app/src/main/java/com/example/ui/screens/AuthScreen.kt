package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.viewmodel.SalonViewModel

@Composable
fun AuthScreen(viewModel: SalonViewModel) {
  var isRegistering by remember { mutableStateOf(false) }
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var name by remember { mutableStateOf("") }
  var selectedRole by remember { mutableStateOf("CLIENT") } // "CLIENT", "PROFESSIONAL", "ADMIN"

  val authError by viewModel.authError.collectAsStateWithLifecycle()

  val roles = listOf(
    Pair("CLIENT", "Cliente"),
    Pair("PROFESSIONAL", "Profissional"),
    Pair("ADMIN", "Administrador")
  )

  Surface(
    modifier = Modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "L'Atelier Salon",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = if (isRegistering) "Criar nova conta" else "Entre com sua conta",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(24.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          if (authError != null) {
            Surface(
              color = MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = authError ?: "",
                modifier = Modifier.padding(12.dp),
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
              )
            }
          }

          if (isRegistering) {
            OutlinedTextField(
              value = name,
              onValueChange = { name = it },
              label = { Text("Nome Completo") },
              singleLine = true,
              modifier = Modifier.fillMaxWidth().testTag("auth_name_input")
            )

            Text("Selecione seu Perfil:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              roles.forEach { (roleKey, roleLabel) ->
                FilterChip(
                  selected = selectedRole == roleKey,
                  onClick = { selectedRole = roleKey },
                  label = { Text(roleLabel, fontSize = 11.sp) },
                  modifier = Modifier.weight(1f)
                )
              }
            }
          }

          OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("auth_email_input")
          )

          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().testTag("auth_password_input")
          )

          Button(
            onClick = {
              if (isRegistering) {
                viewModel.register(name, email, password, selectedRole) { success -> }
              } else {
                viewModel.login(email, password) { success -> }
              }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("auth_submit_button"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
          ) {
            Text(if (isRegistering) "Cadastrar" else "Entrar", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
          }

          TextButton(
            onClick = { isRegistering = !isRegistering },
            modifier = Modifier.align(Alignment.CenterHorizontally).testTag("auth_toggle_mode")
          ) {
            Text(
              text = if (isRegistering) "Já tem uma conta? Entre aqui" else "Não tem conta? Cadastre-se",
              color = MaterialTheme.colorScheme.primary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Quick Demo Preset Login Helpers
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "⚡ Acesso Rápido para Demonstração:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedButton(
              onClick = { viewModel.login("admin@latelier.com", "admin123") {} },
              modifier = Modifier.weight(1f),
              contentPadding = PaddingValues(4.dp)
            ) {
              Text("Admin", fontSize = 11.sp)
            }
            OutlinedButton(
              onClick = { viewModel.login("pro@latelier.com", "pro123") {} },
              modifier = Modifier.weight(1f),
              contentPadding = PaddingValues(4.dp)
            ) {
              Text("Profissional", fontSize = 11.sp)
            }
            OutlinedButton(
              onClick = { viewModel.login("cliente@latelier.com", "cliente123") {} },
              modifier = Modifier.weight(1f),
              contentPadding = PaddingValues(4.dp)
            ) {
              Text("Cliente", fontSize = 11.sp)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Security Notice mandated by instructions
      Text(
        text = "⚠️ Nota de Segurança: Autenticação local para demonstração em dispositivo. Não substitui segurança de servidor.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 11.sp,
        modifier = Modifier.padding(horizontal = 8.dp)
      )
    }
  }
}
