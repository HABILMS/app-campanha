package com.example.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Collaborator
import com.example.ui.theme.CampaignBluePrimary
import com.example.ui.theme.CampaignEmerald
import com.example.ui.theme.CampaignGoldAccent
import com.example.ui.viewmodel.CampaignViewModel

@Composable
fun PerfilEPerfilPoliticoScreen(
    viewModel: CampaignViewModel,
    collaborators: List<Collaborator>
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Perfil Político, 1: Matriz Habilidades, 2: Logística

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("perfil_politico_screen")
    ) {
        Text(
            text = "Módulos 2, 3 & 4: Perfil, Habilidades & Logística",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Mapeamento Estratégico da Força de Trabalho Eleitoral",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Módulo 2: Político") },
                icon = { Icon(Icons.Default.HowToVote, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Módulo 3: Habilidades") },
                icon = { Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Módulo 4: Logística") },
                icon = { Icon(Icons.Default.DirectionsCar, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            0 -> PoliticalProfileTabContent(collaborators)
            1 -> SkillsMatrixTabContent(collaborators)
            2 -> LogisticsTabContent(collaborators)
        }
    }
}

@Composable
fun PoliticalProfileTabContent(collaborators: List<Collaborator>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(collaborators) { collab ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = collab.nome, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Surface(
                            color = CampaignGoldAccent.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Mobiliza: ${collab.pessoasMobilizaveis} pessoas",
                                color = CampaignGoldAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "🏛️ Campanhas Anteriores: ${collab.candidatosAnteriores.ifBlank { "Primeira participação" }}",
                        fontSize = 12.sp
                    )

                    Text(
                        text = "🤝 Entidades: ${collab.entidades.ifBlank { "Associação de Bairro" }}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(
                            onClick = {},
                            label = { Text("Influência: ${collab.influenciaPolitica}", fontSize = 10.sp) }
                        )
                        if (collab.liderancaComunitaria) {
                            AssistChip(
                                onClick = {},
                                label = { Text("Liderança Comunitária ✓", fontSize = 10.sp) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SkillsMatrixTabContent(collaborators: List<Collaborator>) {
    val skillsList = listOf(
        "Panfletagem", "Porta a Porta", "Bandeiraço", "Motorista",
        "Carro de Som", "Fotografia", "Filmagem", "Design",
        "Social Media", "WhatsApp", "Coordenação", "Eventos",
        "Mobilização", "Fiscal", "Digitador", "Atendimento"
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CampaignBluePrimary.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Matriz Multi-Seleção de Habilidades", fontWeight = FontWeight.Bold, color = CampaignBluePrimary)
                    Text(text = "Mapeamento automático de aptidões para alocação rápida em eventos", fontSize = 11.sp)
                }
            }
        }

        items(collaborators) { collab ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = collab.nome, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Simulated Tag Cloud of Skills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val activeSkills = collab.habilidades.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        val skillsToShow = if (activeSkills.isNotEmpty()) activeSkills else listOf("Panfletagem", "Mobilização", "Eventos")

                        skillsToShow.forEach { skill ->
                            Surface(
                                color = CampaignBluePrimary.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = skill,
                                    fontSize = 10.sp,
                                    color = CampaignBluePrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LogisticsTabContent(collaborators: List<Collaborator>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(collaborators) { collab ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = collab.nome, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(text = "Disponibilidade: ${collab.diasDisponiveis} (${collab.horariosDisponiveis})", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = if (collab.temCarro) CampaignEmerald.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (collab.temCarro) "🚗 Carro OK" else "🚗 Sem Carro",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (collab.temCarro) CampaignEmerald else Color.Gray,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            color = if (collab.temMoto) CampaignEmerald.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (collab.temMoto) "🏍️ Moto OK" else "🏍️ Sem Moto",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (collab.temMoto) CampaignEmerald else Color.Gray,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            color = if (collab.temNotebook) CampaignEmerald.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (collab.temNotebook) "💻 Notebook OK" else "💻 Sem Notebook",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (collab.temNotebook) CampaignEmerald else Color.Gray,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
