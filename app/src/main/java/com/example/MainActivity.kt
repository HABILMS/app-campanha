package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ExportReportDialog
import com.example.ui.components.GeminiAnalysisBottomSheet
import com.example.ui.screens.*
import com.example.ui.theme.CampaignBluePrimary
import com.example.ui.theme.CampaignEmerald
import com.example.ui.theme.CampaignGoldAccent
import com.example.ui.theme.CampaignTheme
import com.example.ui.viewmodel.CampaignViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CampaignViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

            CampaignTheme(darkTheme = isDarkMode) {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: CampaignViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val aiState by viewModel.aiState.collectAsStateWithLifecycle()

    val collaborators by viewModel.collaborators.collectAsStateWithLifecycle()
    val materials by viewModel.materials.collectAsStateWithLifecycle()
    val payments by viewModel.payments.collectAsStateWithLifecycle()
    val attendance by viewModel.attendance.collectAsStateWithLifecycle()
    val goals by viewModel.goals.collectAsStateWithLifecycle()
    val leaderships by viewModel.leaderships.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle()
    val auditLogs by viewModel.auditLogs.collectAsStateWithLifecycle()
    val totalSpent by viewModel.totalSpent.collectAsStateWithLifecycle()

    var showAiBottomSheet by remember { mutableStateOf(false) }
    var exportSectionName by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Display user messages via Snackbar
    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Gestão de Campanha",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = CampaignGoldAccent
                        )
                        Text(
                            text = "Perfil: ${currentRole.label}",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    // AI Quick Button
                    IconButton(
                        onClick = { showAiBottomSheet = true },
                        modifier = Modifier.testTag("top_bar_ai_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "IA Gemini",
                            tint = CampaignGoldAccent
                        )
                    }

                    // Dark Mode Toggle Button
                    IconButton(onClick = { viewModel.toggleDarkMode() }) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Tema",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CampaignBluePrimary)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.testTag("bottom_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = currentScreen == "dashboard",
                    onClick = { viewModel.navigateTo("dashboard") },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                    label = { Text("Dashboard", fontSize = 10.sp) }
                )

                NavigationBarItem(
                    selected = currentScreen == "equipe",
                    onClick = { viewModel.navigateTo("equipe") },
                    icon = { Icon(Icons.Default.Group, contentDescription = null) },
                    label = { Text("Equipe", fontSize = 10.sp) }
                )

                NavigationBarItem(
                    selected = currentScreen == "perfil",
                    onClick = { viewModel.navigateTo("perfil") },
                    icon = { Icon(Icons.Default.Psychology, contentDescription = null) },
                    label = { Text("Habilidades", fontSize = 10.sp) }
                )

                NavigationBarItem(
                    selected = currentScreen == "materiais",
                    onClick = { viewModel.navigateTo("materiais") },
                    icon = { Icon(Icons.Default.Inventory, contentDescription = null) },
                    label = { Text("Materiais", fontSize = 10.sp) }
                )

                NavigationBarItem(
                    selected = currentScreen == "pagamentos",
                    onClick = { viewModel.navigateTo("pagamentos") },
                    icon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                    label = { Text("Financeiro", fontSize = 10.sp) }
                )

                NavigationBarItem(
                    selected = currentScreen == "frequencia",
                    onClick = { viewModel.navigateTo("frequencia") },
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    label = { Text("Ponto GPS", fontSize = 10.sp) }
                )

                NavigationBarItem(
                    selected = currentScreen == "metas",
                    onClick = { viewModel.navigateTo("metas") },
                    icon = { Icon(Icons.Default.EmojiEvents, contentDescription = null) },
                    label = { Text("Metas", fontSize = 10.sp) }
                )

                NavigationBarItem(
                    selected = currentScreen == "admin",
                    onClick = { viewModel.navigateTo("admin") },
                    icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null) },
                    label = { Text("Admin", fontSize = 10.sp) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                "dashboard" -> DashboardScreen(
                    viewModel = viewModel,
                    collaborators = collaborators,
                    totalSpent = totalSpent ?: 0.0,
                    onNavigateTo = { viewModel.navigateTo(it) },
                    onOpenAiDrawer = { showAiBottomSheet = true }
                )

                "equipe" -> EquipeScreen(
                    viewModel = viewModel,
                    collaborators = collaborators,
                    onOpenExportDialog = { exportSectionName = "Equipe de Colaboradores" }
                )

                "perfil" -> PerfilEPerfilPoliticoScreen(
                    viewModel = viewModel,
                    collaborators = collaborators
                )

                "materiais" -> MateriaisScreen(
                    viewModel = viewModel,
                    materials = materials,
                    collaborators = collaborators
                )

                "pagamentos" -> PagamentosScreen(
                    viewModel = viewModel,
                    payments = payments,
                    collaborators = collaborators,
                    totalSpent = totalSpent ?: 0.0
                )

                "frequencia" -> FrequenciaScreen(
                    viewModel = viewModel,
                    attendance = attendance,
                    collaborators = collaborators
                )

                "metas" -> MetasScreen(
                    viewModel = viewModel,
                    goals = goals
                )

                "liderancas" -> LiderancasScreen(
                    viewModel = viewModel,
                    leaderships = leaderships
                )

                "eventos" -> EventosScreen(
                    viewModel = viewModel,
                    events = events
                )

                "admin" -> PainelAdminScreen(
                    viewModel = viewModel,
                    auditLogs = auditLogs,
                    currentRole = currentRole,
                    onRoleChanged = { viewModel.setRole(it) }
                )
            }

            // Gemini AI Bottom Sheet
            if (showAiBottomSheet) {
                GeminiAnalysisBottomSheet(
                    aiState = aiState,
                    onDismiss = { showAiBottomSheet = false },
                    onRunAnalysis = { type ->
                        viewModel.runGeminiAnalysis(type)
                    }
                )
            }

            // Export Modal Dialog
            exportSectionName?.let { section ->
                ExportReportDialog(
                    sectionName = section,
                    onDismiss = { exportSectionName = null },
                    onExportConfirmed = { format ->
                        viewModel.showMessage("Relatório de '$section' gerado no formato $format e salvo no Google Drive!")
                    }
                )
            }
        }
    }
}
