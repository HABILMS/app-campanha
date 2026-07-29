package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Collaborator
import com.example.ui.components.DigitalBadgeCard
import com.example.ui.theme.CampaignBluePrimary
import com.example.ui.theme.CampaignEmerald
import com.example.ui.theme.CampaignGoldAccent
import com.example.ui.viewmodel.CampaignViewModel

@Composable
fun EquipeScreen(
    viewModel: CampaignViewModel,
    collaborators: List<Collaborator>,
    onOpenExportDialog: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedRegion by remember { mutableStateOf("Todas") }
    var selectedCollaboratorForBadge by remember { mutableStateOf<Collaborator?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredList = remember(collaborators, searchQuery, selectedRegion) {
        collaborators.filter { c ->
            val matchesSearch = c.nome.contains(searchQuery, ignoreCase = true) ||
                    c.cpf.contains(searchQuery) ||
                    c.bairro.contains(searchQuery, ignoreCase = true)
            val matchesRegion = selectedRegion == "Todas" || c.regiaoUrbana == selectedRegion
            matchesSearch && matchesRegion
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("equipe_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Módulo 1: Cadastro da Equipe",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Total de ${collaborators.size} Colaboradores Cadastrados",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row {
                    IconButton(onClick = onOpenExportDialog) {
                        Icon(Icons.Default.Download, contentDescription = "Exportar", tint = CampaignBluePrimary)
                    }

                    FloatingActionButton(
                        onClick = { showAddDialog = true },
                        containerColor = CampaignBluePrimary,
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("add_collaborator_fab")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Novo Colaborador")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_collaborator_input"),
                placeholder = { Text("Buscar por nome, CPF ou bairro...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpar")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Region Chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val regions = listOf("Todas", "Centro", "Zona Leste", "Zona Sul", "Zona Oeste", "Zona Norte")
                regions.forEach { r ->
                    FilterChip(
                        selected = selectedRegion == r,
                        onClick = { selectedRegion = r },
                        label = { Text(r, fontSize = 11.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Collaborators List
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.PersonSearch,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nenhum colaborador encontrado",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredList) { collab ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("collaborator_card_${collab.id}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(CampaignBluePrimary.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = collab.nome.take(1).uppercase(),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CampaignBluePrimary
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = collab.nome,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = CampaignEmerald.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = collab.matricula,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = CampaignEmerald,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        Text(
                                            text = "CPF: ${collab.cpf} • Tel: ${collab.telefone}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "Bairro: ${collab.bairro} (${collab.regiaoUrbana}) • Zona: ${collab.zona}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Divider(color = MaterialTheme.colorScheme.outlineVariant)

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        if (collab.temCarro) AssistChip(onClick = {}, label = { Text("🚗 Carro", fontSize = 10.sp) })
                                        if (collab.temMoto) AssistChip(onClick = {}, label = { Text("🏍️ Moto", fontSize = 10.sp) })
                                        if (collab.liderancaComunitaria) AssistChip(onClick = {}, label = { Text("⭐ Liderança", fontSize = 10.sp) })
                                    }

                                    Button(
                                        onClick = { selectedCollaboratorForBadge = collab },
                                        colors = ButtonDefaults.buttonColors(containerColor = CampaignGoldAccent),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.testTag("view_badge_button_${collab.id}")
                                    ) {
                                        Icon(Icons.Default.Badge, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Crachá PDF", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Crachá Modal Preview
        selectedCollaboratorForBadge?.let { collab ->
            AlertDialog(
                onDismissRequest = { selectedCollaboratorForBadge = null },
                title = { Text("Crachá Digital de Mobilizador", fontWeight = FontWeight.Bold) },
                text = {
                    DigitalBadgeCard(
                        collaborator = collab,
                        onExportPdf = {
                            viewModel.showMessage("Crachá PDF gerado e enviado para o Google Drive para download!")
                            selectedCollaboratorForBadge = null
                        }
                    )
                },
                confirmButton = {
                    TextButton(onClick = { selectedCollaboratorForBadge = null }) {
                        Text("Fechar")
                    }
                }
            )
        }

        // Add Collaborator Dialog
        if (showAddDialog) {
            AddCollaboratorModal(
                onDismiss = { showAddDialog = false },
                onConfirm = { newCollab ->
                    viewModel.addCollaborator(newCollab) {
                        showAddDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun AddCollaboratorModal(
    onDismiss: () -> Unit,
    onConfirm: (Collaborator) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var regiaoUrbana by remember { mutableStateOf("Centro") }
    var profissao by remember { mutableStateOf("Mobilizador") }
    var pix by remember { mutableStateOf("") }
    var banco by remember { mutableStateOf("Banco do Brasil") }

    var temCarro by remember { mutableStateOf(false) }
    var temMoto by remember { mutableStateOf(false) }
    var liderancaComunitaria by remember { mutableStateOf(false) }
    var documentAttached by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, tint = CampaignBluePrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cadastrar Novo Colaborador", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome Completo *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_collab_name")
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = cpf,
                        onValueChange = { cpf = it },
                        label = { Text("CPF *") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("add_collab_cpf")
                    )

                    OutlinedTextField(
                        value = telefone,
                        onValueChange = { telefone = it },
                        label = { Text("Telefone / WA") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = bairro,
                        onValueChange = { bairro = it },
                        label = { Text("Bairro *") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("add_collab_bairro")
                    )

                    OutlinedTextField(
                        value = regiaoUrbana,
                        onValueChange = { regiaoUrbana = it },
                        label = { Text("Região Urbana") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = pix,
                        onValueChange = { pix = it },
                        label = { Text("Chave PIX") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = banco,
                        onValueChange = { banco = it },
                        label = { Text("Banco") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Checkbox Options
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = temCarro, onCheckedChange = { temCarro = it })
                    Text("Possui Carro Próprio", fontSize = 12.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = temMoto, onCheckedChange = { temMoto = it })
                    Text("Possui Moto", fontSize = 12.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = liderancaComunitaria, onCheckedChange = { liderancaComunitaria = it })
                    Text("É Liderança Comunitária", fontSize = 12.sp)
                }

                // Document Attachment Simulator Button
                Button(
                    onClick = { documentAttached = true },
                    colors = ButtonDefaults.buttonColors(containerColor = if (documentAttached) CampaignEmerald else CampaignBluePrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(if (documentAttached) Icons.Default.CheckCircle else Icons.Default.CloudUpload, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (documentAttached) "Documentos CNH/Comprovante Anexados ✓" else "Anexar Documentos (CNH / Comprovante)", fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nome.isNotBlank() && cpf.isNotBlank() && bairro.isNotBlank()) {
                        onConfirm(
                            Collaborator(
                                nome = nome,
                                cpf = cpf,
                                telefone = telefone,
                                whatsapp = if (whatsapp.isBlank()) telefone else whatsapp,
                                email = email,
                                bairro = bairro,
                                regiaoUrbana = regiaoUrbana,
                                profissao = profissao,
                                pix = pix,
                                banco = banco,
                                temCarro = temCarro,
                                temMoto = temMoto,
                                liderancaComunitaria = liderancaComunitaria
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CampaignBluePrimary),
                modifier = Modifier.testTag("save_collab_confirm_button")
            ) {
                Text("Salvar Colaborador", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
