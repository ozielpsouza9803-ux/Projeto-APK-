package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest

fun hashPassword(password: String): String {
  val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
  return bytes.joinToString("") { "%02x".format(it) }
}

@Database(entities = [ServiceEntity::class, ProfessionalEntity::class, AppointmentEntity::class, UserEntity::class], version = 2, exportSchema = false)
abstract class SalonDatabase : RoomDatabase() {
  abstract fun serviceDao(): ServiceDao
  abstract fun professionalDao(): ProfessionalDao
  abstract fun appointmentDao(): AppointmentDao
  abstract fun userDao(): UserDao

  companion object {
    @Volatile
    private var INSTANCE: SalonDatabase? = null

    fun getDatabase(context: Context): SalonDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          SalonDatabase::class.java,
          "latelier_salon_database"
        )
          .fallbackToDestructiveMigration() // safely migrate demo versioning
          .addCallback(SalonDatabaseCallback())
          .build()
        INSTANCE = instance
        instance
      }
    }

    private class SalonDatabaseCallback : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          CoroutineScope(Dispatchers.IO).launch {
            populateInitialData(database)
          }
        }
      }
    }

    suspend fun populateInitialData(database: SalonDatabase) {
      val serviceDao = database.serviceDao()
      val professionalDao = database.professionalDao()
      val appointmentDao = database.appointmentDao()
      val userDao = database.userDao()

      if (serviceDao.getCount() == 0) {
        val defaultServices = listOf(
          ServiceEntity("1", "Corte & Styling Premium", "Cabelo", "R$ 120", "45 min", "Corte personalizado com lavagem e finalização exclusiva."),
          ServiceEntity("2", "Barba Terapia & Alinhamento", "Barba", "R$ 80", "30 min", "Tratamento completo para barba com toalha quente e óleos nobres."),
          ServiceEntity("3", "Coloração & Reflexos", "Química", "R$ 250", "120 min", "Técnicas modernas de coloração, mechas e tonalização."),
          ServiceEntity("4", "Manicure & Pedicure Spa", "Unhas", "R$ 90", "60 min", "Cuidados completos para mãos e pés com esmaltação premium."),
          ServiceEntity("5", "Spa Capilar & Hidratação", "Tratamento", "R$ 180", "60 min", "Recuperação profunda dos fios com produtos importados.")
        )
        defaultServices.forEach { serviceDao.insertService(it) }
      }

      if (professionalDao.getCount() == 0) {
        val defaultProfessionals = listOf(
          ProfessionalEntity("1", "Carlos Silva", "Cabelo & Barba", "Seg a Sex (09h - 18h)", "4.9 ⭐"),
          ProfessionalEntity("2", "Mariana Lima", "Coloração & Química", "Ter a Sáb (10h - 19h)", "4.8 ⭐"),
          ProfessionalEntity("3", "Beatriz Souza", "Manicure & Estética", "Seg a Sex (08h - 17h)", "5.0 ⭐"),
          ProfessionalEntity("4", "Rafael Santos", "Cortes Masculinos", "Qua a Dom (12h - 21h)", "4.7 ⭐")
        )
        defaultProfessionals.forEach { professionalDao.insertProfessional(it) }
      }

      if (appointmentDao.getCount() == 0) {
        val defaultAppointments = listOf(
          AppointmentEntity("1", "Ana Souza", "Corte & Styling Premium", "14:00", "Hoje", "Carlos Silva", "Confirmado"),
          AppointmentEntity("2", "Ana Souza", "Spa Capilar & Hidratação", "10:30", "Amanhã", "Mariana Lima", "Pendente")
        )
        defaultAppointments.forEach { appointmentDao.insertAppointment(it) }
      }

      if (userDao.getCount() == 0) {
        val defaultUsers = listOf(
          UserEntity("admin@latelier.com", "Administrador Geral", hashPassword("admin123"), "ADMIN"),
          UserEntity("pro@latelier.com", "Carlos Silva (Profissional)", hashPassword("pro123"), "PROFESSIONAL"),
          UserEntity("cliente@latelier.com", "Ana Souza (Cliente)", hashPassword("cliente123"), "CLIENT")
        )
        defaultUsers.forEach { userDao.insertUser(it) }
      }
    }
  }
}
