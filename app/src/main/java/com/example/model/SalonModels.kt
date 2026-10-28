package com.example.model

sealed class SalonScreen(val route: String, val title: String, val iconName: String) {
  object Home : SalonScreen("home", "Início", "home")
  object Client : SalonScreen("client", "Área Cliente", "person")
  object Professional : SalonScreen("professional", "Área Profissional", "badge")
  object Admin : SalonScreen("admin", "Área Admin", "admin_panel_settings")
}

data class ServiceItem(
  val id: String,
  val name: String,
  val category: String,
  val price: String,
  val duration: String,
  val description: String
)

data class AppointmentItem(
  val id: String,
  val clientName: String,
  val serviceName: String,
  val time: String,
  val date: String,
  val professionalName: String,
  val status: String // "Confirmado", "Pendente", "Concluído"
)
