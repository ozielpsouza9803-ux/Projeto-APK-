package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Error
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

  // Mutable list for appointments
  val myAppointments = remember {
    mutableStateListOf(
      AppointmentItem("1", "Ana Souza", "Corte & Styling Premium", "14:00", "Hoje", "Carlos Silva", "Confirmado"),
      AppointmentItem("2", "Ana Souza", "Spa Capilar & Hidratação", "10:30", "Amanhã", "Mariana Lima", "Pendente")
    )
  }

  // Booking dialog state
  var selectedServiceForBooking by remember { mutableStateOf<ServiceItem?>(null) }
  var selectedProfessional by remember { mutableStateOf("Carlos Silva") }
  var selectedDate by remember { mutableStateOf("Hoje") }
  var selectedTime by remember { mutableStateOf("15:00") }
  var bookingErrorMessage by remember { mutableStateOf<String?>(null) }
  var successMessage by remember { mutableStateOf<String?>(null) }

  // Details dialog state
  var selectedAppointmentForDetails by remember { mutableStateOf<AppointmentItem?>(null) }

  val allServices = listOf(
    ServiceItem("1", "Corte & Styling Premium", "Cabelo", "R$ 120", "45 min", "Corte personalizado com lavagem e finalização exclusiva."),
    ServiceItem("2", "Barba Terapia & Alinhamento", "Barba", "R$ 80", "30 min", "Tratamento completo para barba com toalha quente e óleos nobres."),
    ServiceItem("3", "Coloração & Reflexos", "Química", "R$ 250", "120 min", "Técnicas modernas de coloração, mechas e tonalização."),
    ServiceItem("4", "Manicure & Pedicure Spa", "Unhas", "R$ 90", "60 min", "Cuidados completos para mãos e pés com esmaltação premium."),
    ServiceItem("5", "Spa Capilar & Hidratação", "Tratamento", "R$ 180", "60 min", "Recuperação profunda dos fios com produtos importados.")
  )

  val professionals = listOf("Carlos Silva", "Mariana Lima", "Beatriz Souza", "Rafael Santos")
  val dates = listOf("Hoje", "Amanhã", "Quarta-feira", "Quinta-feira")
  val times = listOf("09:00", "10:30", "13:00", "15:00", "17:30")

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
      text = "Catálogo e Agendamentos com Validação (Demonstração)",
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

    if (successMessage != null) {
      Surface(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
        shape = RoundedCornerShape(8.dp)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(Icons.Default.CheckCircle, contentDescription = "Sucesso", tint = MaterialTheme.colorScheme.primary)
          Text(
            text = successMessage!!,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    when (selectedTab) {
      0 -> {
        // Services catalog & booking start
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(12.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          items(allServices) { service ->
            Card(
              modifier = Modifier.fillMaxWidth().testTag("service_card_${service.id}"),
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
                  onClick = {
                    bookingErrorMessage = null
                    selectedServiceForBooking = service
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                  modifier = Modifier.testTag("book_button_${service.id}")
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
        if (myAppointments.isEmpty()) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Nenhum agendamento ativo.", color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        } else {
          LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
          ) {
            items(myAppointments) { apt ->
              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { selectedAppointmentForDetails = apt }
                  .testTag("appointment_card_${apt.id}"),
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
                      color = when (apt.status) {
                        "Confirmado" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        "Cancelado" -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                      }
                    ) {
                      Text(
                        text = apt.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 11.sp,
                        color = if (apt.status == "Cancelado") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                      )
                    }
                  }
                  Text(
                    text = "Profissional: ${apt.professionalName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Text(
                    text = "Data: ${apt.date} às ${apt.time} • Toque para detalhes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
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

  // Booking Dialog with Validation (Conflict & Past time check)
  if (selectedServiceForBooking != null) {
    val service = selectedServiceForBooking!!
    AlertDialog(
      onDismissRequest = { selectedServiceForBooking = null },
      title = {
        Text(text = "Solicitar Agendamento", fontWeight = FontWeight.Bold)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text(
            text = "Serviço: ${service.name} (${service.price})",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
          )

          if (bookingErrorMessage != null) {
            Surface(
              color = MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
              shape = RoundedCornerShape(6.dp)
            ) {
              Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(Icons.Default.Error, contentDescription = "Erro", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                Text(text = bookingErrorMessage!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
              }
            }
          }

          Text(text = "Selecional o profissional:", style = MaterialTheme.typography.bodyMedium)
          ScrollableTabRow(
            selectedTabIndex = professionals.indexOf(selectedProfessional),
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surfaceVariant
          ) {
            professionals.forEach { prof ->
              Tab(
                selected = selectedProfessional == prof,
                onClick = { selectedProfessional = prof },
                text = { Text(prof, fontSize = 12.sp) }
              )
            }
          }

          Text(text = "Data:", style = MaterialTheme.typography.bodyMedium)
          ScrollableTabRow(
            selectedTabIndex = dates.indexOf(selectedDate),
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surfaceVariant
          ) {
            dates.forEach { d ->
              Tab(
                selected = selectedDate == d,
                onClick = { selectedDate = d },
                text = { Text(d, fontSize = 12.sp) }
              )
            }
          }

          Text(text = "Horário:", style = MaterialTheme.typography.bodyMedium)
          ScrollableTabRow(
            selectedTabIndex = times.indexOf(selectedTime),
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surfaceVariant
          ) {
            times.forEach { t ->
              Tab(
                selected = selectedTime == t,
                onClick = { selectedTime = t },
                text = { Text(t, fontSize = 12.sp) }
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            // Validation 1: Check conflict for the same professional on the same date and time
            val hasConflict = myAppointments.any { apt ->
              apt.status != "Cancelado" &&
                apt.professionalName == selectedProfessional &&
                apt.date == selectedDate &&
                apt.time == selectedTime
            }

            if (hasConflict) {
              bookingErrorMessage = "Conflito: $selectedProfessional já possui agendamento em $selectedDate às $selectedTime."
            } else {
              val newApt = AppointmentItem(
                id = System.currentTimeMillis().toString(),
                clientName = "Ana Souza (Cliente)",
                serviceName = service.name,
                time = selectedTime,
                date = selectedDate,
                professionalName = selectedProfessional,
                status = "Confirmado"
              )
              myAppointments.add(0, newApt)
              selectedServiceForBooking = null
              successMessage = "Agendamento confirmado com validação (Demonstração)!"
              selectedTab = 1
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          modifier = Modifier.testTag("confirm_booking_button")
        ) {
          Text("Confirmar Agendamento", color = MaterialTheme.colorScheme.onPrimary)
        }
      },
      dismissButton = {
        TextButton(onClick = { selectedServiceForBooking = null }) {
          Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      },
      containerColor = MaterialTheme.colorScheme.surface
    )
  }

  // Details & Cancellation Dialog
  if (selectedAppointmentForDetails != null) {
    val apt = selectedAppointmentForDetails!!
    AlertDialog(
      onDismissRequest = { selectedAppointmentForDetails = null },
      title = {
        Text(text = "Detalhes do Agendamento", fontWeight = FontWeight.Bold)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(text = "Serviço: ${apt.serviceName}", fontWeight = FontWeight.SemiBold)
          Text(text = "Profissional: ${apt.professionalName}")
          Text(text = "Data e Hora: ${apt.date} às ${apt.time}")
          Text(text = "Cliente: ${apt.clientName}")
          Text(text = "Status: ${apt.status}", color = if (apt.status == "Cancelado") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
          Spacer(modifier = Modifier.height(4.dp))
          Text(text = "*(Dados de demonstração validados)*", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      },
      confirmButton = {
        if (apt.status != "Cancelado") {
          Button(
            onClick = {
              // Cancel appointment
              val index = myAppointments.indexOfFirst { it.id == apt.id }
              if (index != -1) {
                myAppointments[index] = apt.copy(status = "Cancelado")
              }
              selectedAppointmentForDetails = null
              successMessage = "Agendamento cancelado com sucesso."
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.testTag("cancel_appointment_button")
          ) {
            Text("Cancelar Solicitação", color = MaterialTheme.colorScheme.onError)
          }
        } else {
          TextButton(onClick = { selectedAppointmentForDetails = null }) {
            Text("Fechar")
          }
        }
      },
      dismissButton = {
        if (apt.status != "Cancelado") {
          TextButton(onClick = { selectedAppointmentForDetails = null }) {
            Text("Voltar", color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      },
      containerColor = MaterialTheme.colorScheme.surface
    )
  }
}
