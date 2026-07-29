package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Receipt
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
import com.example.data.model.PaymentRecord
import com.example.ui.theme.CampaignBluePrimary
import com.example.ui.theme.CampaignEmerald
import com.example.ui.theme.CampaignGoldAccent
import com.example.ui.viewmodel.CampaignViewModel

@Composable
fun PagamentosScreen(
    viewModel: CampaignViewModel,
    payments: List<PaymentRecord>,
    collaborators: List<Collaborator>,
    totalSpent: Double
) {
    var showPaymentDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("pagamentos_screen")
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
                            text = "Módulo 6: Controle Financeiro & Pagamentos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Ajuda de Custo, Diárias e Emissão de Recibos",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { showPaymentDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CampaignBluePrimary),
                        modifier = Modifier.testTag("add_payment_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Novo Pagamento", color = Color.White, fontSize = 11.sp)
                    }
                }
            }

            // Total Investment Banner Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CampaignBluePrimary)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "TOTAL EXECUTADO NA CAMPANHA", color = CampaignGoldAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "R$ ${"%.2f".format(totalSpent)}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Inclui diárias, ajudas de custo de transporte e reembolsos",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Histórico Financeiro & Comprovantes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(payments) { pay ->
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
                            Text(text = pay.collaboratorNome, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(
                                text = "R$ ${"%.2f".format(pay.valor + pay.ajudaDeCusto)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CampaignEmerald
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Tipo: ${pay.tipo} • Ajuda Custo: R$ ${"%.2f".format(pay.ajudaDeCusto)}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "Chave PIX: ${pay.pix} (${pay.banco})",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = if (pay.situacao == "Pago") CampaignEmerald.copy(alpha = 0.2f) else CampaignGoldAccent.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = pay.situacao,
                                    color = if (pay.situacao == "Pago") CampaignEmerald else CampaignGoldAccent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }

                            TextButton(onClick = { viewModel.showMessage("Recibo Digital ${pay.id} exportado com sucesso!") }) {
                                Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Baixar Recibo PDF", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        if (showPaymentDialog) {
            AddPaymentModal(
                collaborators = collaborators,
                onDismiss = { showPaymentDialog = false },
                onConfirm = { payment ->
                    viewModel.addPayment(payment) {
                        showPaymentDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun AddPaymentModal(
    collaborators: List<Collaborator>,
    onDismiss: () -> Unit,
    onConfirm: (PaymentRecord) -> Unit
) {
    var selectedCollab by remember { mutableStateOf(collaborators.firstOrNull()) }
    var valor by remember { mutableStateOf("150.00") }
    var ajudaDeCusto by remember { mutableStateOf("50.00") }
    var tipo by remember { mutableStateOf("Diária + Transporte") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Lançar Pagamento / Ajuda de Custo", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Selecione o Favorecido:", fontSize = 12.sp)
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
                            Text(text = "${c.nome} (PIX: ${c.pix})", fontSize = 12.sp)
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = valor,
                        onValueChange = { valor = it },
                        label = { Text("Valor Principal (R$)") },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = ajudaDeCusto,
                        onValueChange = { ajudaDeCusto = it },
                        label = { Text("Ajuda Custo (R$)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo de Pagamento") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedCollab?.let { c ->
                        onConfirm(
                            PaymentRecord(
                                collaboratorId = c.id,
                                collaboratorNome = c.nome,
                                pix = c.pix.ifBlank { "Chave CPF" },
                                banco = c.banco.ifBlank { "Banco do Brasil" },
                                valor = valor.toDoubleOrNull() ?: 0.0,
                                ajudaDeCusto = ajudaDeCusto.toDoubleOrNull() ?: 0.0,
                                tipo = tipo,
                                situacao = "Pago"
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CampaignBluePrimary)
            ) {
                Text("Efetuar Pagamento", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
