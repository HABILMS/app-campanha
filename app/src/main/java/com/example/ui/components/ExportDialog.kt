package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CampaignBluePrimary
import com.example.ui.theme.CampaignEmerald

@Composable
fun ExportReportDialog(
    sectionName: String,
    onDismiss: () -> Unit,
    onExportConfirmed: (String) -> Unit
) {
    var selectedFormat by remember { mutableStateOf("PDF") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    tint = CampaignBluePrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Exportar Relatório - $sectionName",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Selecione o formato desejado para exportação e sincronização no Google Drive / Sheets:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedFormat == "PDF",
                        onClick = { selectedFormat = "PDF" },
                        label = { Text("📄 PDF Oficial") },
                        leadingIcon = { Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )

                    FilterChip(
                        selected = selectedFormat == "Excel",
                        onClick = { selectedFormat = "Excel" },
                        label = { Text("📊 Excel (.xlsx)") },
                        leadingIcon = { Icon(Icons.Default.TableChart, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedFormat == "CSV",
                        onClick = { selectedFormat = "CSV" },
                        label = { Text("📝 CSV Texto") }
                    )

                    FilterChip(
                        selected = selectedFormat == "Sheets",
                        onClick = { selectedFormat = "Sheets" },
                        label = { Text("🟢 Google Sheets Sync") },
                        leadingIcon = { Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onExportConfirmed(selectedFormat)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = CampaignBluePrimary),
                modifier = Modifier.testTag("confirm_export_button")
            ) {
                Text("Gerar Arquivo / Sincronizar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
