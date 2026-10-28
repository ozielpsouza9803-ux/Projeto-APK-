package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SalonViewModel(application: Application) : AndroidViewModel(application) {
  private val database = SalonDatabase.getDatabase(application)
  private val serviceDao = database.serviceDao()
  private val professionalDao = database.professionalDao()
  private val appointmentDao = database.appointmentDao()
  private val userDao = database.userDao()

  val services: StateFlow<List<ServiceEntity>> = serviceDao.getAllServices()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val professionals: StateFlow<List<ProfessionalEntity>> = professionalDao.getAllProfessionals()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val appointments: StateFlow<List<AppointmentEntity>> = appointmentDao.getAllAppointments()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Authentication State
  private val _currentUser = MutableStateFlow<UserEntity?>(null)
  val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

  private val _authError = MutableStateFlow<String?>(null)
  val authError: StateFlow<String?> = _authError.asStateFlow()

  init {
    viewModelScope.launch {
      if (userDao.getCount() == 0) {
        SalonDatabase.populateInitialData(database)
      }
    }
  }

  fun login(emailInput: String, passInput: String, onComplete: (Boolean) -> Unit) {
    viewModelScope.launch {
      val email = emailInput.trim().lowercase()
      val user = userDao.getUserByEmail(email)
      if (user != null && user.passwordHash == hashPassword(passInput)) {
        _currentUser.value = user
        _authError.value = null
        onComplete(true)
      } else {
        _authError.value = "E-mail ou senha incorretos."
        onComplete(false)
      }
    }
  }

  fun register(nameInput: String, emailInput: String, passInput: String, roleInput: String, onComplete: (Boolean) -> Unit) {
    viewModelScope.launch {
      val email = emailInput.trim().lowercase()
      val existing = userDao.getUserByEmail(email)
      if (existing != null) {
        _authError.value = "Este e-mail já está cadastrado."
        onComplete(false)
      } else if (nameInput.isBlank() || email.isBlank() || passInput.isBlank()) {
        _authError.value = "Preencha todos os campos."
        onComplete(false)
      } else {
        val newUser = UserEntity(
          email = email,
          name = nameInput.trim(),
          passwordHash = hashPassword(passInput),
          role = roleInput
        )
        userDao.insertUser(newUser)
        _currentUser.value = newUser
        _authError.value = null
        onComplete(true)
      }
    }
  }

  fun logout() {
    _currentUser.value = null
  }

  fun insertService(service: ServiceEntity) {
    viewModelScope.launch { serviceDao.insertService(service) }
  }

  fun deleteService(id: String) {
    viewModelScope.launch { serviceDao.deleteService(id) }
  }

  fun insertProfessional(prof: ProfessionalEntity) {
    viewModelScope.launch { professionalDao.insertProfessional(prof) }
  }

  fun deleteProfessional(id: String) {
    viewModelScope.launch { professionalDao.deleteProfessional(id) }
  }

  fun insertAppointment(apt: AppointmentEntity) {
    viewModelScope.launch { appointmentDao.insertAppointment(apt) }
  }

  fun updateAppointmentStatus(id: String, status: String) {
    viewModelScope.launch { appointmentDao.updateStatus(id, status) }
  }
}
