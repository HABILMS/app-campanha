package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CampaignEvent
import com.example.ui.theme.CampaignBluePrimary
import com.example.ui.theme.CampaignEmerald
import com.example.ui.theme.CampaignGoldAccent
import com.example.ui.viewmodel.CampaignViewModel

@Composable
fun EventosScreen(
    viewModel: CampaignViewModel,
    events: List<CampaignEvent>
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("eventos_screen")
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
                            text = "Módulo 10: Agenda de Eventos & Comícios",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Carreatas, Bandeiraços, Caminhadas e Reuniões",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CampaignBluePrimary),
                        modifier = Modifier.testTag("add_event_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Novo Evento", color = Color.White, fontSize = 11.sp)
                    }
                }
            }

            items(events) { ev ->
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
                                Icon(Icons.Default.Event, contentDescription = null, tint = CampaignBluePrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = ev.titulo, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }

                            Surface(
                                color = CampaignGoldAccent.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = ev.tipo,
                                    color = CampaignGoldAccent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "📍 Local: ${ev.local} (${ev.bairro})", fontSize = 12.sp)
                        Text(text = "👤 Responsável: ${ev.responsavel}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "📝 ${ev.descricao}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(color = CampaignEmerald.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp)) {
                                Text(
                                    text = "👥 Presenças Confirmadas: ${ev.listaPresencaCount}",
                                    color = CampaignEmerald,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            TextButton(onClick = { viewModel.showMessage("Lista de presença enviada para sincronização!") }) {
                                Text("Check-in de Evento", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AddEventModal(
                onDismiss = { showDialog = false },
                onConfirm = { ev ->
                    viewModel.addEvent(ev) {
                        showDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun AddEventModal(
    onDismiss: () -> Unit,
    onConfirm: (CampaignEvent) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Carreata") }
    var local by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var responsavel by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agendar Novo Evento", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título do Evento") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = tipo, onValueChange = { tipo = it }, label = { Text("Tipo (Carreata, Comício, Reunião)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = local, onValueChange = { local = it }, label = { Text("Endereço / Local") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = bairro, onValueChange = { bairro = it }, label = { Text("Bairro") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = responsavel, onValueChange = { responsavel = it }, label = { Text("Coordenador Responsável") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição Breve") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (titulo.isNotBlank()) {
                        onConfirm(
                            CampaignEvent(
                                titulo = titulo,
                                tipo = tipo,
                                local = local,
                                bairro = bairro,
                                responsavel = responsavel,
                                descricao = descricao
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CampaignBluePrimary)
            ) {
                Text("Agendar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
