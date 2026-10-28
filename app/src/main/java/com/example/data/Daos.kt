package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
  @Query("SELECT * FROM services")
  fun getAllServices(): Flow<List<ServiceEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertService(service: ServiceEntity)

  @Query("DELETE FROM services WHERE id = :id")
  suspend fun deleteService(id: String)

  @Query("SELECT COUNT(*) FROM services")
  suspend fun getCount(): Int
}

@Dao
interface ProfessionalDao {
  @Query("SELECT * FROM professionals")
  fun getAllProfessionals(): Flow<List<ProfessionalEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProfessional(prof: ProfessionalEntity)

  @Query("DELETE FROM professionals WHERE id = :id")
  suspend fun deleteProfessional(id: String)

  @Query("SELECT COUNT(*) FROM professionals")
  suspend fun getCount(): Int
}

@Dao
interface AppointmentDao {
  @Query("SELECT * FROM appointments")
  fun getAllAppointments(): Flow<List<AppointmentEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAppointment(apt: AppointmentEntity)

  @Query("UPDATE appointments SET status = :status WHERE id = :id")
  suspend fun updateStatus(id: String, status: String)

  @Query("SELECT COUNT(*) FROM appointments")
  suspend fun getCount(): Int
}

@Dao
interface UserDao {
  @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
  suspend fun getUserByEmail(email: String): UserEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(user: UserEntity)

  @Query("SELECT COUNT(*) FROM users")
  suspend fun getCount(): Int
}
