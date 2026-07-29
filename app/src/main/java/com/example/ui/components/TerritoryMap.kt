package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.People
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
import com.example.ui.theme.CampaignBluePrimary
import com.example.ui.theme.CampaignEmerald
import com.example.ui.theme.CampaignGoldAccent

data class NeighborhoodDensity(
    val name: String,
    val region: String,
    val collaboratorsCount: Int,
    val supportersCount: Int,
    val coveragePercentage: Int,
    val lat: Double,
    val lng: Double
)

@Composable
fun TerritoryMapVisualizer(
    collaborators: List<Collaborator>,
    onSelectNeighborhood: (String) -> Unit = {}
) {
    val neighborhoods = remember(collaborators) {
        listOf(
            NeighborhoodDensity("Bela Vista", "Centro", 45, 680, 88, -23.5615, -46.6559),
            NeighborhoodDensity("Tatuapé", "Zona Leste", 38, 520, 75, -23.5404, -46.5764),
            NeighborhoodDensity("Santo Amaro", "Zona Sul", 52, 940, 92, -23.6534, -46.7089),
            NeighborhoodDensity("Lapa", "Zona Oeste", 28, 390, 64, -23.5222, -46.7031),
            NeighborhoodDensity("Santana", "Zona Norte", 19, 210, 42, -23.5042, -46.6264),
            NeighborhoodDensity("Jardim América", "Zona Sul", 12, 130, 28, -23.5689, -46.6698)
        )
    }

    var selectedZone by remember { mutableStateOf("Todas") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("territory_map_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = null,
                        tint = CampaignBluePrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Mapa Territorial da Campanha",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Geolocalização & Densidade de Apoiadores",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    color = CampaignEmerald.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(CampaignEmerald)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "GPS Ativo",
                            color = CampaignEmerald,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Region Filter Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val zones = listOf("Todas", "Centro", "Zona Leste", "Zona Sul", "Zona Oeste", "Zona Norte")
                items(zones) { zone ->
                    FilterChip(
                        selected = selectedZone == zone,
                        onClick = { selectedZone = zone },
                        label = { Text(zone, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Map Representation Canvas / Grid Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                // Background Grid Dots simulation
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(4) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            repeat(6) {
                                Box(
                                    modifier = Modifier
                                        .size(3.dp)
                                        .background(Color.White.copy(alpha = 0.15f), CircleShape)
                                )
                            }
                        }
                    }
                }

                // Map Neighborhood Pins
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    val filtered = if (selectedZone == "Todas") neighborhoods else neighborhoods.filter { it.region == selectedZone }
                    filtered.chunked(3).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            rowItems.forEach { n ->
                                val badgeColor = when {
                                    n.coveragePercentage >= 80 -> CampaignEmerald
                                    n.coveragePercentage >= 50 -> CampaignGoldAccent
                                    else -> Color(0xFFEF4444)
                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable { onSelectNeighborhood(n.name) }
                                ) {
                                    Surface(
                                        color = badgeColor,
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.padding(2.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = "${n.name} (${n.coveragePercentage}%)",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Density Stats Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(CampaignEmerald, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Alta Cobertura (>80%)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(CampaignGoldAccent, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Média (50-80%)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(Color(0xFFEF4444), CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Baixa (<50%)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
