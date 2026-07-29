package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
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
import com.example.data.model.MaterialDelivery
import com.example.ui.theme.CampaignBluePrimary
import com.example.ui.theme.CampaignEmerald
import com.example.ui.theme.CampaignGoldAccent
import com.example.ui.viewmodel.CampaignViewModel

@Composable
fun MateriaisScreen(
    viewModel: CampaignViewModel,
    materials: List<MaterialDelivery>,
    collaborators: List<Collaborator>
) {
    var showDeliveryDialog by remember { mutableStateOf(false) }

    val totalSantinhos = materials.sumOf { it.santinhos }
    val totalBandeiras = materials.sumOf { it.bandeira }
    val totalAdesivos = materials.sumOf { it.adesivo }
    val totalCamisetas = materials.sumOf { it.camiseta }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("materiais_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Módulo 5: Entrega de Material",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Histórico de Distribuição & Controle de Estoque",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { showDeliveryDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CampaignBluePrimary),
                        modifier = Modifier.testTag("add_material_delivery_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Registrar Entrega", color = Color.White, fontSize = 11.sp)
                    }
                }
            }

            // Inventory Summary Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MaterialInventoryCard("Santinhos", "$totalSantinhos", "Unidades", CampaignBluePrimary, Modifier.weight(1f))
                    MaterialInventoryCard("Bandeiras", "$totalBandeiras", "Unidades", CampaignGoldAccent, Modifier.weight(1f))
                    MaterialInventoryCard("Adesivos", "$totalAdesivos", "Unidades", CampaignEmerald, Modifier.weight(1f))
                    MaterialInventoryCard("Camisetas", "$totalCamisetas", "Unidades", Color(0xFF8B5CF6), Modifier.weight(1f))
                }
            }

            item {
                Text(
                    text = "Histórico de Entregas Realizadas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(materials) { item ->
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
                                Icon(Icons.Default.LocalShipping, contentDescription = null, tint = CampaignBluePrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = item.collaboratorNome, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Text(
                                text = "Entregue por: ${item.entreguePor}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (item.santinhos > 0) AssistChip(onClick = {}, label = { Text("📄 Santinhos: ${item.santinhos}", fontSize = 10.sp) })
                            if (item.bandeira > 0) AssistChip(onClick = {}, label = { Text("🚩 Bandeiras: ${item.bandeira}", fontSize = 10.sp) })
                            if (item.adesivo > 0) AssistChip(onClick = {}, label = { Text("🏷️ Adesivos: ${item.adesivo}", fontSize = 10.sp) })
                            if (item.praguinha > 0) AssistChip(onClick = {}, label = { Text("⭐ Praguinhas: ${item.praguinha}", fontSize = 10.sp) })
                        }
                    }
                }
            }
        }

        if (showDeliveryDialog) {
            AddMaterialDeliveryModal(
                collaborators = collaborators,
                onDismiss = { showDeliveryDialog = false },
                onConfirm = { delivery ->
                    viewModel.addMaterialDelivery(delivery) {
                        showDeliveryDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun MaterialInventoryCard(
    name: String,
    count: String,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
            Text(text = count, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = unit, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun AddMaterialDeliveryModal(
    collaborators: List<Collaborator>,
    onDismiss: () -> Unit,
    onConfirm: (MaterialDelivery) -> Unit
) {
    var selectedCollab by remember { mutableStateOf(collaborators.firstOrNull()) }
    var santinhos by remember { mutableStateOf("500") }
    var bandeira by remember { mutableStateOf("2") }
    var adesivo by remember { mutableStateOf("50") }
    var camiseta by remember { mutableStateOf("1") }
    var bone by remember { mutableStateOf("1") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Entrega de Material", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Selecione o Colaborador:", fontSize = 12.sp)
                // Collab Selection
                Column {
                    collaborators.take(4).forEach { c ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedCollab?.id == c.id,
                                onClick = { selectedCollab = c }
                            )
                            Text(text = "${c.nome} (${c.bairro})", fontSize = 12.sp)
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = santinhos,
                        onValueChange = { santinhos = it },
                        label = { Text("Santinhos") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = bandeira,
                        onValueChange = { bandeira = it },
                        label = { Text("Bandeiras") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = adesivo,
                        onValueChange = { adesivo = it },
                        label = { Text("Adesivos") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = camiseta,
                        onValueChange = { camiseta = it },
                        label = { Text("Camisetas") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedCollab?.let { c ->
                        onConfirm(
                            MaterialDelivery(
                                collaboratorId = c.id,
                                collaboratorNome = c.nome,
                                santinhos = santinhos.toIntOrNull() ?: 0,
                                bandeira = bandeira.toIntOrNull() ?: 0,
                                adesivo = adesivo.toIntOrNull() ?: 0,
                                camiseta = camiseta.toIntOrNull() ?: 0,
                                bone = bone.toIntOrNull() ?: 0
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CampaignBluePrimary)
            ) {
                Text("Confirmar Entrega", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
