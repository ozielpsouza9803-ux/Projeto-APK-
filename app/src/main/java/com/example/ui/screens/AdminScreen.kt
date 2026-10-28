package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ProfessionalEntity
import com.example.data.ServiceEntity
import com.example.viewmodel.SalonViewModel

@Composable
fun AdminScreen(viewModel: SalonViewModel) {
  var selectedTab by remember { mutableStateOf(0) }
  val tabs = listOf("Visão Geral", "Serviços", "Profissionais")

  val servicesList by viewModel.services.collectAsStateWithLifecycle()
  val professionalsList by viewModel.professionals.collectAsStateWithLifecycle()
  val appointments by viewModel.appointments.collectAsStateWithLifecycle()

  // Service Dialog State
  var showServiceDialog by remember { mutableStateOf(false) }
  var editingService by remember { mutableStateOf<ServiceEntity?>(null) }
  var serviceNameInput by remember { mutableStateOf("") }
  var serviceCategoryInput by remember { mutableStateOf("") }
  var servicePriceInput by remember { mutableStateOf("") }
  var serviceDurationInput by remember { mutableStateOf("") }
  var serviceDescInput by remember { mutableStateOf("") }
  var serviceToDelete by remember { mutableStateOf<ServiceEntity?>(null) }

  // Professional Dialog State
  var showProfDialog by remember { mutableStateOf(false) }
  var editingProf by remember { mutableStateOf<ProfessionalEntity?>(null) }
  var profNameInput by remember { mutableStateOf("") }
  var profSpecialtyInput by remember { mutableStateOf("") }
  var profAvailabilityInput by remember { mutableStateOf("") }
  var profRatingInput by remember { mutableStateOf("") }
  var profToDelete by remember { mutableStateOf<ProfessionalEntity?>(null) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text(
      text = "Painel Administrativo",
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.primary
    )
    Text(
      text = "Gestão persistida via Room (SQLite) local",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

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
          modifier = Modifier.testTag("admin_tab_$index")
        )
      }
    }

    when (selectedTab) {
      0 -> {
        // Overview Tab
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(16.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              MetricCard(title = "Faturamento Hoje", value = "R$ 3.450", icon = Icons.Default.AttachMoney, modifier = Modifier.weight(1f))
              MetricCard(title = "Agendamentos", value = "${appointments.size}", icon = Icons.Default.EventNote, modifier = Modifier.weight(1f))
            }
          }
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              MetricCard(title = "Profissionais Ativos", value = "${professionalsList.size}", icon = Icons.Default.Group, modifier = Modifier.weight(1f))
              MetricCard(title = "Serviços Cadastrados", value = "${servicesList.size}", icon = Icons.Default.ContentCut, modifier = Modifier.weight(1f))
            }
          }
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
              Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                  text = "💾 Persistência Room Ativa",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
                Text(
                  text = "Todos os dados de serviços, profissionais e agendamentos são salvos localmente e persistem ao reiniciar o aplicativo.",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
      1 -> {
        // Services Management Tab
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Lista de Serviços (${servicesList.size})",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
            Button(
              onClick = {
                editingService = null
                serviceNameInput = ""
                serviceCategoryInput = ""
                servicePriceInput = ""
                serviceDurationInput = ""
                serviceDescInput = ""
                showServiceDialog = true
              },
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
              modifier = Modifier.testTag("add_service_button")
            ) {
              Icon(Icons.Default.Add, contentDescription = "Adicionar", modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Novo Serviço")
            }
          }

          LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
          ) {
            items(servicesList) { service ->
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
                      style = MaterialTheme.typography.titleSmall,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = "${service.category} • ${service.duration} • ${service.price}",
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                      text = service.description,
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                  Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = {
                      editingService = service
                      serviceNameInput = service.name
                      serviceCategoryInput = service.category
                      servicePriceInput = service.price
                      serviceDurationInput = service.duration
                      serviceDescInput = service.description
                      showServiceDialog = true
                    }) {
                      Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { serviceToDelete = service }) {
                      Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error)
                    }
                  }
                }
              }
            }
          }
        }
      }
      2 -> {
        // Professionals Management Tab
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Lista de Profissionais (${professionalsList.size})",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
            Button(
              onClick = {
                editingProf = null
                profNameInput = ""
                profSpecialtyInput = ""
                profAvailabilityInput = ""
                profRatingInput = "5.0 ⭐"
                showProfDialog = true
              },
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
              modifier = Modifier.testTag("add_prof_button")
            ) {
              Icon(Icons.Default.Add, contentDescription = "Adicionar", modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Novo Profissional")
            }
          }

          LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
          ) {
            items(professionalsList) { prof ->
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
                      text = prof.name,
                      style = MaterialTheme.typography.titleSmall,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = "Especialidade: ${prof.specialty}",
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                      text = "Disponibilidade: ${prof.availability} • ${prof.rating}",
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                  Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = {
                      editingProf = prof
                      profNameInput = prof.name
                      profSpecialtyInput = prof.specialty
                      profAvailabilityInput = prof.availability
                      profRatingInput = prof.rating
                      showProfDialog = true
                    }) {
                      Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { profToDelete = prof }) {
                      Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error)
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  // Add / Edit Service Dialog
  if (showServiceDialog) {
    AlertDialog(
      onDismissRequest = { showServiceDialog = false },
      title = { Text(text = if (editingService == null) "Cadastrar Serviço" else "Editar Serviço", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = serviceNameInput,
            onValueChange = { serviceNameInput = it },
            label = { Text("Nome do Serviço") },
            singleLine = true
          )
          OutlinedTextField(
            value = serviceCategoryInput,
            onValueChange = { serviceCategoryInput = it },
            label = { Text("Categoria (ex: Cabelo, Barba)") },
            singleLine = true
          )
          OutlinedTextField(
            value = servicePriceInput,
            onValueChange = { servicePriceInput = it },
            label = { Text("Preço (ex: R$ 120)") },
            singleLine = true
          )
          OutlinedTextField(
            value = serviceDurationInput,
            onValueChange = { serviceDurationInput = it },
            label = { Text("Duração (ex: 45 min)") },
            singleLine = true
          )
          OutlinedTextField(
            value = serviceDescInput,
            onValueChange = { serviceDescInput = it },
            label = { Text("Descrição") }
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (serviceNameInput.isNotBlank()) {
              val id = editingService?.id ?: System.currentTimeMillis().toString()
              val newService = ServiceEntity(
                id = id,
                name = serviceNameInput,
                category = serviceCategoryInput.ifBlank { "Geral" },
                price = servicePriceInput.ifBlank { "R$ 100" },
                duration = serviceDurationInput.ifBlank { "30 min" },
                description = serviceDescInput
              )
              viewModel.insertService(newService)
              showServiceDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text("Salvar", color = MaterialTheme.colorScheme.onPrimary)
        }
      },
      dismissButton = {
        TextButton(onClick = { showServiceDialog = false }) {
          Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      },
      containerColor = MaterialTheme.colorScheme.surface
    )
  }

  // Delete Service Confirmation Dialog
  if (serviceToDelete != null) {
    val service = serviceToDelete!!
    AlertDialog(
      onDismissRequest = { serviceToDelete = null },
      title = { Text(text = "Confirmar Exclusão", fontWeight = FontWeight.Bold) },
      text = { Text(text = "Deseja realmente excluir o serviço \"${service.name}\"? Esta ação não poderá ser desfeita.") },
      confirmButton = {
        Button(
          onClick = {
            viewModel.deleteService(service.id)
            serviceToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Excluir", color = MaterialTheme.colorScheme.onError)
        }
      },
      dismissButton = {
        TextButton(onClick = { serviceToDelete = null }) {
          Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      },
      containerColor = MaterialTheme.colorScheme.surface
    )
  }

  // Add / Edit Professional Dialog
  if (showProfDialog) {
    AlertDialog(
      onDismissRequest = { showProfDialog = false },
      title = { Text(text = if (editingProf == null) "Cadastrar Profissional" else "Editar Profissional", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = profNameInput,
            onValueChange = { profNameInput = it },
            label = { Text("Nome do Profissional") },
            singleLine = true
          )
          OutlinedTextField(
            value = profSpecialtyInput,
            onValueChange = { profSpecialtyInput = it },
            label = { Text("Especialidade (ex: Cabelo & Barba)") },
            singleLine = true
          )
          OutlinedTextField(
            value = profAvailabilityInput,
            onValueChange = { profAvailabilityInput = it },
            label = { Text("Disponibilidade (ex: Seg a Sex)") },
            singleLine = true
          )
          OutlinedTextField(
            value = profRatingInput,
            onValueChange = { profRatingInput = it },
            label = { Text("Avaliação (ex: 4.9 ⭐)") },
            singleLine = true
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (profNameInput.isNotBlank()) {
              val id = editingProf?.id ?: System.currentTimeMillis().toString()
              val newProf = ProfessionalEntity(
                id = id,
                name = profNameInput,
                specialty = profSpecialtyInput.ifBlank { "Estilista" },
                availability = profAvailabilityInput.ifBlank { "Seg a Sex" },
                rating = profRatingInput.ifBlank { "5.0 ⭐" }
              )
              viewModel.insertProfessional(newProf)
              showProfDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text("Salvar", color = MaterialTheme.colorScheme.onPrimary)
        }
      },
      dismissButton = {
        TextButton(onClick = { showProfDialog = false }) {
          Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      },
      containerColor = MaterialTheme.colorScheme.surface
    )
  }

  // Delete Professional Confirmation Dialog
  if (profToDelete != null) {
    val prof = profToDelete!!
    AlertDialog(
      onDismissRequest = { profToDelete = null },
      title = { Text(text = "Confirmar Exclusão", fontWeight = FontWeight.Bold) },
      text = { Text(text = "Deseja realmente excluir o profissional \"${prof.name}\"? Esta ação não poderá ser desfeita.") },
      confirmButton = {
        Button(
          onClick = {
            viewModel.deleteProfessional(prof.id)
            profToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Excluir", color = MaterialTheme.colorScheme.onError)
        }
      },
      dismissButton = {
        TextButton(onClick = { profToDelete = null }) {
          Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      },
      containerColor = MaterialTheme.colorScheme.surface
    )
  }
}

@Composable
fun MetricCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(20.dp)
        )
      }
      Text(
        text = value,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
  }
}
