package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class ServiceEntity(
  @PrimaryKey val id: String,
  val name: String,
  val category: String,
  val price: String,
  val duration: String,
  val description: String
)

@Entity(tableName = "professionals")
data class ProfessionalEntity(
  @PrimaryKey val id: String,
  val name: String,
  val specialty: String,
  val availability: String,
  val rating: String
)

@Entity(tableName = "appointments")
data class AppointmentEntity(
  @PrimaryKey val id: String,
  val clientName: String,
  val serviceName: String,
  val time: String,
  val date: String,
  val professionalName: String,
  val status: String
)

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey val email: String,
  val name: String,
  val passwordHash: String,
  val role: String // "ADMIN", "PROFESSIONAL", "CLIENT"
)
