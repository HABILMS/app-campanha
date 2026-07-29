package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Leadership
import com.example.ui.theme.CampaignBluePrimary
import com.example.ui.theme.CampaignEmerald
import com.example.ui.theme.CampaignGoldAccent
import com.example.ui.viewmodel.CampaignViewModel

@Composable
fun LiderancasScreen(
    viewModel: CampaignViewModel,
    leaderships: List<Leadership>
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("liderancas_screen")
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
                            text = "Módulo 9: Lideranças Comunitárias",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Mapeamento de Articuladores Regionais e Famílias",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CampaignBluePrimary),
                        modifier = Modifier.testTag("add_leadership_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cadastrar Liderança", color = Color.White, fontSize = 11.sp)
                    }
                }
            }

            items(leaderships) { lead ->
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
                                Icon(Icons.Default.Groups, contentDescription = null, tint = CampaignBluePrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = lead.nome, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }

                            Surface(
                                color = CampaignGoldAccent.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Influência ${lead.nivelInfluencia}",
                                    color = CampaignGoldAccent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "📍 Bairro: ${lead.bairro} (${lead.comunidade})", fontSize = 12.sp)
                        Text(text = "Segmento: ${lead.segmento}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "Contato: ${lead.contato}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(color = CampaignEmerald.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp)) {
                                Text(
                                    text = "👨‍👩‍👧‍👦 Famílias: ${lead.familiasImpactadas}",
                                    color = CampaignEmerald,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Surface(color = CampaignBluePrimary.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp)) {
                                Text(
                                    text = "🗳️ Apoiadores: ${lead.apoiadoresGarantidos}",
                                    color = CampaignBluePrimary,
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

        if (showDialog) {
            AddLeadershipModal(
                onDismiss = { showDialog = false },
                onConfirm = { l ->
                    viewModel.addLeadership(l) {
                        showDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun AddLeadershipModal(
    onDismiss: () -> Unit,
    onConfirm: (Leadership) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var comunidade by remember { mutableStateOf("") }
    var contato by remember { mutableStateOf("") }
    var familias by remember { mutableStateOf("100") }
    var apoiadores by remember { mutableStateOf("200") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cadastrar Liderança", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome da Liderança") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = bairro, onValueChange = { bairro = it }, label = { Text("Bairro") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = comunidade, onValueChange = { comunidade = it }, label = { Text("Comunidade / Entidade") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = contato, onValueChange = { contato = it }, label = { Text("Contato / Tel") }, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = familias, onValueChange = { familias = it }, label = { Text("Famílias") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = apoiadores, onValueChange = { apoiadores = it }, label = { Text("Apoiadores") }, modifier = Modifier.weight(1f))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nome.isNotBlank()) {
                        onConfirm(
                            Leadership(
                                nome = nome,
                                bairro = bairro,
                                comunidade = comunidade,
                                contato = contato,
                                familiasImpactadas = familias.toIntOrNull() ?: 50,
                                apoiadoresGarantidos = apoiadores.toIntOrNull() ?: 100
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CampaignBluePrimary)
            ) {
                Text("Salvar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
