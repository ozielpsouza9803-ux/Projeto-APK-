package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ServiceItem

@Composable
fun HomeScreen(
  onNavigateToClient: () -> Unit,
  onNavigateToProfessional: () -> Unit,
  onNavigateToAdmin: () -> Unit
) {
  val featuredServices = listOf(
    ServiceItem("1", "Corte & Styling Premium", "Cabelo", "R$ 120", "45 min", "Corte personalizado com lavagem e finalização exclusiva."),
    ServiceItem("2", "Barba Terapia & Alinhamento", "Barba", "R$ 80", "30 min", "Tratamento completo para barba com toalha quente e óleos nobres."),
    ServiceItem("3", "Coloração & Reflexos", "Química", "R$ 250", "120 min", "Técnicas modernas de coloração, mechas e tonalização."),
    ServiceItem("4", "Spa Capilar & Hidratação", "Tratamento", "R$ 180", "60 min", "Recuperação profunda dos fios com produtos importados.")
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Hero Banner
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("hero_banner_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(
                colors = listOf(
                  MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                  MaterialTheme.colorScheme.surface
                )
              )
            )
            .padding(24.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(
              shape = RoundedCornerShape(50),
              color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
              contentColor = MaterialTheme.colorScheme.primary
            ) {
              Text(
                text = "✨ SALÃO UNISSEX DE ALTO PADRÃO",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = "L'Atelier Salon",
              style = MaterialTheme.typography.headlineLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = "Elegância, sofisticação e cuidado personalizado para você. Descubra uma nova experiência em beleza.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    // Role Navigation Cards Section
    item {
      Text(
        text = "Acesso Rápido por Perfil",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        RoleCard(
          title = "Cliente",
          subtitle = "Agendar & Favoritos",
          icon = Icons.Default.Person,
          modifier = Modifier.weight(1f).testTag("role_client_card"),
          onClick = onNavigateToClient
        )
        RoleCard(
          title = "Profissional",
          subtitle = "Minha Agenda",
          icon = Icons.Default.Badge,
          modifier = Modifier.weight(1f).testTag("role_professional_card"),
          onClick = onNavigateToProfessional
        )
        RoleCard(
          title = "Admin",
          subtitle = "Gestão & Relatórios",
          icon = Icons.Default.AdminPanelSettings,
          modifier = Modifier.weight(1f).testTag("role_admin_card"),
          onClick = onNavigateToAdmin
        )
      }
    }

    // Featured Services Section Header
    item {
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Serviços em Destaque",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        TextButton(onClick = onNavigateToClient) {
          Text("Ver todos", color = MaterialTheme.colorScheme.primary)
        }
      }
    }

    // Featured Services List
    items(featuredServices) { service ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("service_item_${service.id}"),
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
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Text(
                text = service.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
              ) {
                Text(
                  text = service.category,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  fontSize = 10.sp,
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = service.description,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
              Text(
                text = "⏱ ${service.duration}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = service.price,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
              onClick = onNavigateToClient,
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
              modifier = Modifier.height(32.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
              Text("Reservar", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimary)
            }
          }
        }
      }
    }
  }
}

@Composable
fun RoleCard(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Card(
    modifier = modifier.clickable(onClick = onClick),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(22.dp)
        )
      }
      Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1
      )
    }
  }
}
