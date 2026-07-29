package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.CampaignDatabase
import com.example.data.model.*
import com.example.data.remote.GeminiClient
import com.example.data.repository.CampaignRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class UserRole(val label: String, val accessLevel: String) {
    ADMINISTRADOR("Administrador", "Acesso Total (Geral, Financeiro, Logística, Configurações)"),
    FINANCEIRO("Financeiro", "Controle de Pagamentos, Ajuda de Custo e Recibos"),
    COORDENADOR_GERAL("Coordenador Geral", "Gestão de Equipe, Metas, Eventos e Lideranças"),
    COORDENADOR_REGIONAL("Coordenador Regional", "Frequência, Equipe Regional e Metas"),
    LOGISTICA("Logística", "Entrega e Controle de Materiais e Equipamentos"),
    COMUNICACAO("Comunicação", "Eventos, Mídias Sociais e Materiais"),
    CONSULTA("Consulta", "Apenas Visualização de Dashboards e Relatórios")
}

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class CampaignViewModel(application: Application) : AndroidViewModel(application) {

    private val db = CampaignDatabase.getDatabase(application)
    val repository = CampaignRepository(db)

    // Current User Profile Role
    private val _currentRole = MutableStateFlow(UserRole.ADMINISTRADOR)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // Dark Mode Toggle
    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // Navigation Screen
    private val _currentScreen = MutableStateFlow("dashboard")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    // Data Flows from Repository
    val collaborators: StateFlow<List<Collaborator>> = repository.collaborators
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val materials: StateFlow<List<MaterialDelivery>> = repository.materials
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val payments: StateFlow<List<PaymentRecord>> = repository.payments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val attendance: StateFlow<List<AttendanceRecord>> = repository.attendance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val goals: StateFlow<List<GoalRecord>> = repository.goals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val leaderships: StateFlow<List<Leadership>> = repository.leaderships
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val events: StateFlow<List<CampaignEvent>> = repository.events
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditLogs: StateFlow<List<AuditLog>> = repository.auditLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalSpent: StateFlow<Double?> = repository.totalSpent
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Gemini AI Analysis State
    private val _aiState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val aiState: StateFlow<UiState<String>> = _aiState.asStateFlow()

    // Search and Filter State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedNeighborhoodFilter = MutableStateFlow("Todos")
    val selectedNeighborhoodFilter: StateFlow<String> = _selectedNeighborhoodFilter.asStateFlow()

    // Notification / Toast Message
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedSampleDataIfEmpty()
        }
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
        viewModelScope.launch {
            repository.logAudit("Usuário Atual", role.label, "MUDANCA_PERFIL", "Perfil alterado para ${role.label}")
        }
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setNeighborhoodFilter(bairro: String) {
        _selectedNeighborhoodFilter.value = bairro
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun showMessage(msg: String) {
        _userMessage.value = msg
    }

    // Collaborator operations
    fun addCollaborator(collab: Collaborator, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                // Generate automatic ID & matricula
                val nextMatricula = "CAMP-2026-${(1000..9999).random()}"
                val finalCollab = collab.copy(matricula = nextMatricula)
                repository.insertCollaborator(finalCollab)
                _userMessage.value = "Colaborador ${finalCollab.nome} cadastrado com sucesso! Matrícula: ${finalCollab.matricula}"
                onSuccess()
            } catch (e: Exception) {
                _userMessage.value = "Erro no cadastro: ${e.localizedMessage}"
            }
        }
    }

    fun updateCollaborator(collab: Collaborator) {
        viewModelScope.launch {
            repository.updateCollaborator(collab)
            _userMessage.value = "Cadastro atualizado com sucesso."
        }
    }

    fun deleteCollaborator(id: Long) {
        viewModelScope.launch {
            repository.deleteCollaborator(id)
            _userMessage.value = "Colaborador removido."
        }
    }

    // Material operations
    fun addMaterialDelivery(delivery: MaterialDelivery, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertMaterialDelivery(delivery)
            _userMessage.value = "Entrega de materiais para ${delivery.collaboratorNome} registrada."
            onSuccess()
        }
    }

    // Payment operations
    fun addPayment(payment: PaymentRecord, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertPayment(payment)
            _userMessage.value = "Pagamento de R$ ${"%.2f".format(payment.valor)} registrado para ${payment.collaboratorNome}."
            onSuccess()
        }
    }

    // Attendance operations
    fun addAttendance(record: AttendanceRecord, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertAttendance(record)
            _userMessage.value = "Check-in de ${record.collaboratorNome} registrado (${record.tipo})."
            onSuccess()
        }
    }

    // Leadership operations
    fun addLeadership(leadership: Leadership, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertLeadership(leadership)
            _userMessage.value = "Liderança ${leadership.nome} cadastrada."
            onSuccess()
        }
    }

    // Event operations
    fun addEvent(event: CampaignEvent, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertEvent(event)
            _userMessage.value = "Evento '${event.titulo}' agendado."
            onSuccess()
        }
    }

    // Gemini AI Analysis
    fun runGeminiAnalysis(type: String) {
        viewModelScope.launch {
            _aiState.value = UiState.Loading
            val prompt = when (type) {
                "COBERTURA" -> "Analise os bairros e regiões urbanas da campanha eleitoral e identifique bairros com pouca cobertura de colaboradores. Sugira remanejamento de equipes para otimizar votos."
                "MATERIAIS" -> "Analise o ritmo de distribuição de santinhos, bandeiras e praguinhas. Qual a previsão de escassez e como otimizar o estoque para os próximos comícios?"
                "PRODUTIVIDADE" -> "Analise a lista de coordenadores e colaboradores com metas atrasadas ou baixa produtividade. Forneça recomendações de engajamento e treinamento."
                "RESUMO_DIARIO" -> "Gere um resumo executivo diário completo da campanha eleitoral para o candidato e coordenação geral (total de cadastros, gastos, eventos do dia e alertas)."
                else -> "Faça uma análise estratégica geral dos dados da campanha política e sugira 3 ações imediatas de alta eficácia territorial."
            }

            val result = GeminiClient.analyzeCampaignData(prompt)
            _aiState.value = UiState.Success(result)
        }
    }
}
