package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GoalRecord
import com.example.ui.theme.CampaignBluePrimary
import com.example.ui.theme.CampaignEmerald
import com.example.ui.theme.CampaignGoldAccent
import com.example.ui.viewmodel.CampaignViewModel

@Composable
fun MetasScreen(
    viewModel: CampaignViewModel,
    goals: List<GoalRecord>
) {
    val sortedGoals = remember(goals) {
        goals.sortedByDescending { it.percentualConclusao }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("metas_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Módulo 8: Metas & Ranking de Produtividade",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Acompanhamento de Visitas, Cadastros e Desempenho",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CampaignBluePrimary)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = CampaignGoldAccent,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Ranking Geral da Campanha", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(text = "Cálculo automático de metas atingidas", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                    }
                }
            }
        }

        item {
            Text(text = "Classificação de Colaboradores", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        itemsIndexed(sortedGoals) { index, goal ->
            val rankBadgeColor = when (index) {
                0 -> CampaignGoldAccent
                1 -> Color(0xFFC0C0C0)
                2 -> Color(0xFFCD7F32)
                else -> CampaignBluePrimary
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(rankBadgeColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}º",
                                    color = Color.Black,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = goal.collaboratorNome, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }

                        Text(
                            text = "${goal.percentualConclusao}% da Meta",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = CampaignEmerald
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = goal.percentualConclusao / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = CampaignEmerald,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("🚪 Visitas: ${goal.realizadasVisitas}/${goal.metaVisitas}", fontSize = 11.sp)
                        Text("📝 Cadastros: ${goal.realizadasCadastros}/${goal.metaCadastros}", fontSize = 11.sp)
                        Text("🤝 Reuniões: ${goal.realizadasReunioes}/${goal.metaReunioes}", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
