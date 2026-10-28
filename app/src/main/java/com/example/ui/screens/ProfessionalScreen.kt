package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppointmentItem

@Composable
fun ProfessionalScreen() {
  var isAvailable by remember { mutableStateOf(true) }

  val todaysAgenda = listOf(
    AppointmentItem("101", "Lucas Mendes", "Corte & Styling Premium", "09:00", "Hoje", "Carlos Silva", "Confirmado"),
    AppointmentItem("102", "Juliana Costa", "Coloração & Reflexos", "10:30", "Hoje", "Carlos Silva", "Confirmado"),
    AppointmentItem("103", "Roberto Dias", "Barba Terapia", "14:00", "Hoje", "Carlos Silva", "Pendente"),
    AppointmentItem("104", "Fernanda Lima", "Spa Capilar", "16:00", "Hoje", "Carlos Silva", "Confirmado")
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Área do Profissional",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Text(
          text = "Carlos Silva • Estilista Master",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      Switch(
        checked = isAvailable,
        onCheckedChange = { isAvailable = it },
        colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.testTag("availability_switch")
      )
    }

    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = if (isAvailable) "Status: Disponível para Atendimento" else "Status: Em Pausa / Indisponível",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "4 atendimentos agendados para hoje",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        Icon(
          imageVector = if (isAvailable) Icons.Default.CheckCircle else Icons.Default.Schedule,
          contentDescription = "Status",
          tint = MaterialTheme.colorScheme.primary
        )
      }
    }

    Text(
      text = "Agenda de Hoje",
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onBackground
    )

    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      items(todaysAgenda) { apt ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "${apt.time} - ${apt.clientName}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = apt.serviceName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
              )
            }
            Button(
              onClick = { /* Complete action */ },
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Text("Detalhes", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary)
            }
          }
        }
      }
    }
  }
}
