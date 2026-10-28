package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppointmentItem
import com.example.model.ServiceItem

@Composable
fun ClientScreen() {
  var selectedTab by remember { mutableStateOf(0) }
  val tabs = listOf("Serviços & Agendamento", "Meus Agendamentos", "Favoritos")

  val myAppointments = listOf(
    AppointmentItem("1", "Ana Souza", "Corte & Styling Premium", "14:00", "Hoje", "Carlos Silva", "Confirmado"),
    AppointmentItem("2", "Ana Souza", "Spa Capilar & Hidratação", "10:30", "Amanhã", "Mariana Lima", "Pendente")
  )

  val allServices = listOf(
    ServiceItem("1", "Corte & Styling Premium", "Cabelo", "R$ 120", "45 min", "Corte personalizado com lavagem e finalização exclusiva."),
    ServiceItem("2", "Barba Terapia & Alinhamento", "Barba", "R$ 80", "30 min", "Tratamento completo para barba com toalha quente e óleos nobres."),
    ServiceItem("3", "Coloração & Reflexos", "Química", "R$ 250", "120 min", "Técnicas modernas de coloração, mechas e tonalização."),
    ServiceItem("4", "Manicure & Pedicure Spa", "Unhas", "R$ 90", "60 min", "Cuidados completos para mãos e pés com esmaltação premium."),
    ServiceItem("5", "Spa Capilar & Hidratação", "Tratamento", "R$ 180", "60 min", "Recuperação profunda dos fios com produtos importados.")
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Text(
      text = "Área do Cliente",
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.primary
    )
    Text(
      text = "Agende horários, acompanhe seus serviços e favoritos.",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(16.dp))

    ScrollableTabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = MaterialTheme.colorScheme.primary,
      edgePadding = 0.dp
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = { Text(title, fontWeight = FontWeight.SemiBold) },
          modifier = Modifier.testTag("client_tab_$index")
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    when (selectedTab) {
      0 -> {
        // Services catalog & booking start
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(12.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          items(allServices) { service ->
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
                    text = service.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = "${service.category} • ${service.duration} • ${service.price}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = service.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
                Button(
                  onClick = { /* Open booking flow */ },
                  colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                  Text("Agendar", color = MaterialTheme.colorScheme.onPrimary)
                }
              }
            }
          }
        }
      }
      1 -> {
        // My Appointments
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(12.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          items(myAppointments) { apt ->
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = apt.serviceName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (apt.status == "Confirmado") MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                  ) {
                    Text(
                      text = apt.status,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                      fontSize = 11.sp,
                      color = MaterialTheme.colorScheme.primary
                    )
                  }
                }
                Text(
                  text = "Profissional: ${apt.professionalName}",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = "Data: ${apt.date} às ${apt.time}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
      2 -> {
        // Favorites
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.Favorite,
              contentDescription = "Favoritos",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Nenhum serviço favorito ainda",
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}
