package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Collaborator
import com.example.ui.components.TerritoryMapVisualizer
import com.example.ui.theme.CampaignBluePrimary
import com.example.ui.theme.CampaignEmerald
import com.example.ui.theme.CampaignGoldAccent
import com.example.ui.viewmodel.CampaignViewModel

@Composable
fun DashboardScreen(
    viewModel: CampaignViewModel,
    collaborators: List<Collaborator>,
    totalSpent: Double,
    onNavigateTo: (String) -> Unit,
    onOpenAiDrawer: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Image Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_banner_1785366279823),
                        contentDescription = "Comitê de Campanha",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(16.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column {
                            Surface(
                                color = CampaignGoldAccent,
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.padding(bottom = 4.dp)
                            ) {
                                Text(
                                    text = " ELEIÇÕES 2026 ",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                            Text(
                                text = "Comando Central da Campanha",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Gestão de Equipes, Metas e Mobilização Territorial",
                                color = Color.LightGray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Quick AI Insight Button
        item {
            Card(
                onClick = onOpenAiDrawer,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("open_gemini_ai_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CampaignBluePrimary)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = CampaignGoldAccent,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Análises Preditivas com IA Gemini",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Clique para analisar cobertura territorial, redistribuir equipes e prever materiais",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }

        // KPI Metric Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KpiStatCard(
                        title = "Cadastrados",
                        value = "${collaborators.size}",
                        subtitle = "Equipe em Campo",
                        icon = Icons.Default.Group,
                        accentColor = CampaignBluePrimary,
                        modifier = Modifier.weight(1f)
                    )

                    KpiStatCard(
                        title = "Ativos",
                        value = "${collaborators.count { it.status == "Ativo" }}",
                        subtitle = "100% Verificados",
                        icon = Icons.Default.CheckCircle,
                        accentColor = CampaignEmerald,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KpiStatCard(
                        title = "Investimento Gasto",
                        value = "R$ ${"%.2f".format(totalSpent)}",
                        subtitle = "Ajuda Custo & Pagamentos",
                        icon = Icons.Default.AttachMoney,
                        accentColor = CampaignGoldAccent,
                        modifier = Modifier.weight(1f)
                    )

                    KpiStatCard(
                        title = "Lideranças",
                        value = "3",
                        subtitle = "Famílias Mobilizadas",
                        icon = Icons.Default.Star,
                        accentColor = Color(0xFF8B5CF6),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Territory Map Section
        item {
            TerritoryMapVisualizer(
                collaborators = collaborators,
                onSelectNeighborhood = { bairro ->
                    viewModel.setNeighborhoodFilter(bairro)
                    onNavigateTo("equipe")
                }
            )
        }

        // Recent Collaborators Preview
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Colaboradores Recentes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                TextButton(onClick = { onNavigateTo("equipe") }) {
                    Text("Ver Todos", color = CampaignBluePrimary, fontWeight = FontWeight.Bold)
                }
            }
        }

        items(collaborators.take(3)) { collab ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("recent_collab_${collab.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(CampaignBluePrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = collab.nome.take(1).uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = CampaignBluePrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = collab.nome, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(
                            text = "${collab.profissao} • ${collab.bairro} (${collab.regiaoUrbana})",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        color = CampaignEmerald.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = collab.status,
                            color = CampaignEmerald,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KpiStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
