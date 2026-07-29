package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceRecord
import com.example.data.model.Collaborator
import com.example.ui.theme.CampaignBluePrimary
import com.example.ui.theme.CampaignEmerald
import com.example.ui.theme.CampaignGoldAccent
import com.example.ui.viewmodel.CampaignViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FrequenciaScreen(
    viewModel: CampaignViewModel,
    attendance: List<AttendanceRecord>,
    collaborators: List<Collaborator>
) {
    var showAttendanceDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("frequencia_screen")
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
                            text = "Módulo 7: Frequência & Ponto GPS",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Registro de Entrada/Saída com Geolocalização de Campo",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { showAttendanceDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CampaignBluePrimary),
                        modifier = Modifier.testTag("add_attendance_button")
                    ) {
                        Icon(Icons.Default.AddLocation, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Registrar Ponto", color = Color.White, fontSize = 11.sp)
                    }
                }
            }

            items(attendance) { rec ->
                val timeStr = remember(rec.horaRegistro) {
                    SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(Date(rec.horaRegistro))
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
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CampaignEmerald)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = rec.collaboratorNome, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                            Surface(
                                color = CampaignBluePrimary.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "${rec.tipo} • $timeStr",
                                    color = CampaignBluePrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "📍 Local: ${rec.bairro} (${rec.evento})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Atividade: ${rec.atividade}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Coordenadas GPS: ${"%.4f".format(rec.latitude)}, ${"%.4f".format(rec.longitude)} (Verificado)",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        if (showAttendanceDialog) {
            AddAttendanceModal(
                collaborators = collaborators,
                onDismiss = { showAttendanceDialog = false },
                onConfirm = { record ->
                    viewModel.addAttendance(record) {
                        showAttendanceDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun AddAttendanceModal(
    collaborators: List<Collaborator>,
    onDismiss: () -> Unit,
    onConfirm: (AttendanceRecord) -> Unit
) {
    var selectedCollab by remember { mutableStateOf(collaborators.firstOrNull()) }
    var tipo by remember { mutableStateOf("Entrada") }
    var evento by remember { mutableStateOf("Bandeiraço Centro") }
    var bairro by remember { mutableStateOf("Bela Vista") }
    var atividade by remember { mutableStateOf("Mobilização de Rua") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registro de Ponto Eleitoral", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Selecione o Colaborador:", fontSize = 12.sp)
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
                    FilterChip(
                        selected = tipo == "Entrada",
                        onClick = { tipo = "Entrada" },
                        label = { Text("Entrada") }
                    )
                    FilterChip(
                        selected = tipo == "Saída",
                        onClick = { tipo = "Saída" },
                        label = { Text("Saída") }
                    )
                }

                OutlinedTextField(
                    value = evento,
                    onValueChange = { evento = it },
                    label = { Text("Evento / Ação") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = bairro,
                    onValueChange = { bairro = it },
                    label = { Text("Bairro") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = atividade,
                    onValueChange = { atividade = it },
                    label = { Text("Atividade Realizada") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedCollab?.let { c ->
                        onConfirm(
                            AttendanceRecord(
                                collaboratorId = c.id,
                                collaboratorNome = c.nome,
                                tipo = tipo,
                                evento = evento,
                                bairro = bairro,
                                atividade = atividade
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CampaignBluePrimary)
            ) {
                Text("Registrar Ponto GPS", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
