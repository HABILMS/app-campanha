package com.example.data.repository

import com.example.data.db.CampaignDatabase
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CampaignRepository(private val db: CampaignDatabase) {

    val collaborators: Flow<List<Collaborator>> = db.collaboratorDao().getAll()
    val materials: Flow<List<MaterialDelivery>> = db.materialDao().getAll()
    val payments: Flow<List<PaymentRecord>> = db.paymentDao().getAll()
    val attendance: Flow<List<AttendanceRecord>> = db.attendanceDao().getAll()
    val goals: Flow<List<GoalRecord>> = db.goalDao().getAll()
    val leaderships: Flow<List<Leadership>> = db.leadershipDao().getAll()
    val events: Flow<List<CampaignEvent>> = db.eventDao().getAll()
    val auditLogs: Flow<List<AuditLog>> = db.auditLogDao().getAll()
    val totalSpent: Flow<Double?> = db.paymentDao().getTotalSpent()

    suspend fun insertCollaborator(collaborator: Collaborator): Long = withContext(Dispatchers.IO) {
        // Validation check for duplicates
        val existing = db.collaboratorDao().getByCpf(collaborator.cpf)
        if (existing != null && existing.id != collaborator.id) {
            throw IllegalArgumentException("CPF já cadastrado na campanha (${collaborator.cpf})")
        }
        val count = db.collaboratorDao().insert(collaborator)
        // Auto audit log
        db.auditLogDao().insert(
            AuditLog(
                usuario = "Admin Geral",
                perfil = "Administrador",
                acao = "CADASTRO_COLABORADOR",
                detalhe = "Cadastrado colaborador ${collaborator.nome} (Matrícula: ${collaborator.matricula})"
            )
        )
        count
    }

    suspend fun updateCollaborator(collaborator: Collaborator) = withContext(Dispatchers.IO) {
        db.collaboratorDao().update(collaborator)
    }

    suspend fun deleteCollaborator(id: Long) = withContext(Dispatchers.IO) {
        db.collaboratorDao().deleteById(id)
    }

    suspend fun insertMaterialDelivery(delivery: MaterialDelivery): Long = withContext(Dispatchers.IO) {
        db.materialDao().insert(delivery)
    }

    suspend fun insertPayment(payment: PaymentRecord): Long = withContext(Dispatchers.IO) {
        db.paymentDao().insert(payment)
    }

    suspend fun insertAttendance(record: AttendanceRecord): Long = withContext(Dispatchers.IO) {
        db.attendanceDao().insert(record)
    }

    suspend fun insertGoal(goal: GoalRecord): Long = withContext(Dispatchers.IO) {
        db.goalDao().insert(goal)
    }

    suspend fun updateGoal(goal: GoalRecord) = withContext(Dispatchers.IO) {
        db.goalDao().update(goal)
    }

    suspend fun insertLeadership(leadership: Leadership): Long = withContext(Dispatchers.IO) {
        db.leadershipDao().insert(leadership)
    }

    suspend fun insertEvent(event: CampaignEvent): Long = withContext(Dispatchers.IO) {
        db.eventDao().insert(event)
    }

    suspend fun logAudit(usuario: String, perfil: String, acao: String, detalhe: String) = withContext(Dispatchers.IO) {
        db.auditLogDao().insert(
            AuditLog(
                usuario = usuario,
                perfil = perfil,
                acao = acao,
                detalhe = detalhe
            )
        )
    }

    suspend fun seedSampleDataIfEmpty() = withContext(Dispatchers.IO) {
        // Pre-populate realistic campaign data if database is empty
        val existingCollabs = db.collaboratorDao().getAll()
        // We do a one-shot query check
        val isCollabEmpty = db.collaboratorDao().getById(1) == null
        if (isCollabEmpty) {
            val sampleCollabs = listOf(
                Collaborator(
                    id = 1,
                    matricula = "CAMP-2026-0001",
                    nome = "Carlos Eduardo Silva",
                    cpf = "123.456.789-01",
                    rg = "12.345.678-9",
                    nascimento = "15/05/1988",
                    sexo = "Masculino",
                    estadoCivil = "Casado",
                    telefone = "(11) 98765-4321",
                    whatsapp = "(11) 98765-4321",
                    email = "carlos.silva@campanha.com.br",
                    endereco = "Av. Paulista, 1500 - Ap 42",
                    cep = "01310-200",
                    cidade = "São Paulo",
                    bairro = "Bela Vista",
                    regiaoUrbana = "Centro",
                    zona = "1ª Zona",
                    secaoEleitoral = "0245",
                    tituloEleitor = "1234.5678.9012",
                    pix = "123.456.789-01",
                    banco = "Banco do Brasil (001)",
                    profissao = "Coordenador Regional",
                    escolaridade = "Superior Completo",
                    instagram = "@carlossilva_pol",
                    facebook = "/carlossilvapol",
                    tiktok = "@carlossilvamob",
                    photoUri = "",
                    status = "Ativo",
                    candidatosAnteriores = "Campanha Prefeito 2024, Deputado 2022",
                    liderancaComunitaria = true,
                    influenciaPolitica = "Alta",
                    pessoasMobilizaveis = 250,
                    entidades = "Associação de Bairro, Igreja, Comércio Local",
                    habilidades = "Panfletagem, Porta a Porta, Coordenação, Mobilização, Eventos",
                    temCarro = true,
                    temMoto = false,
                    temBicicleta = false,
                    cnhCategoria = "B",
                    temNotebook = true,
                    temAndroid = true,
                    temIPhone = false,
                    temInternet = true,
                    diasDisponiveis = "Segunda a Sábado",
                    horariosDisponiveis = "Integral (08h às 18h)",
                    coordenadorResponsavel = "Coordenador Geral"
                ),
                Collaborator(
                    id = 2,
                    matricula = "CAMP-2026-0002",
                    nome = "Mariana Alcantara Santos",
                    cpf = "234.567.890-12",
                    rg = "23.456.789-0",
                    nascimento = "22/11/1994",
                    sexo = "Feminino",
                    estadoCivil = "Solteira",
                    telefone = "(11) 97654-3210",
                    whatsapp = "(11) 97654-3210",
                    email = "mariana.santos@campanha.com.br",
                    endereco = "Rua Clélia, 450",
                    cep = "05042-000",
                    cidade = "São Paulo",
                    bairro = "Lapa",
                    regiaoUrbana = "Zona Oeste",
                    zona = "250ª Zona",
                    secaoEleitoral = "0112",
                    tituloEleitor = "2345.6789.0123",
                    pix = "mariana.santos@email.com",
                    banco = "Itaú (341)",
                    profissao = "Designer & Social Media",
                    escolaridade = "Pós-Graduação",
                    instagram = "@marianamkt_pol",
                    facebook = "/marianasantos",
                    tiktok = "@marianasp",
                    photoUri = "",
                    status = "Ativo",
                    candidatosAnteriores = "Vereador 2024",
                    liderancaComunitaria = false,
                    influenciaPolitica = "Média",
                    pessoasMobilizaveis = 80,
                    entidades = "Universidade, Grêmio Estudantil",
                    habilidades = "Design, Social Media, WhatsApp, Filmagem, Fotografia",
                    temCarro = false,
                    temMoto = true,
                    temBicicleta = true,
                    cnhCategoria = "A",
                    temNotebook = true,
                    temAndroid = false,
                    temIPhone = true,
                    temInternet = true,
                    diasDisponiveis = "Segunda a Sexta",
                    horariosDisponiveis = "Flexível",
                    coordenadorResponsavel = "Mariana Alcantara Santos"
                ),
                Collaborator(
                    id = 3,
                    matricula = "CAMP-2026-0003",
                    nome = "Roberto Gomes de Oliveira",
                    cpf = "345.678.901-23",
                    rg = "34.567.890-1",
                    nascimento = "03/03/1975",
                    sexo = "Masculino",
                    estadoCivil = "Divorciado",
                    telefone = "(11) 91234-5678",
                    whatsapp = "(11) 91234-5678",
                    email = "roberto.gomes@campanha.com.br",
                    endereco = "Rua Amador Bueno, 120",
                    cep = "04752-000",
                    cidade = "São Paulo",
                    bairro = "Santo Amaro",
                    regiaoUrbana = "Zona Sul",
                    zona = "328ª Zona",
                    secaoEleitoral = "0089",
                    tituloEleitor = "3456.7890.1234",
                    pix = "(11) 91234-5678",
                    banco = "Bradesco (237)",
                    profissao = "Motorista de Som",
                    escolaridade = "Ensino Médio",
                    instagram = "@roberto_som",
                    facebook = "/robertogomes",
                    tiktok = "",
                    photoUri = "",
                    status = "Ativo",
                    candidatosAnteriores = "Deputado Estadual 2022, Senador 2018",
                    liderancaComunitaria = true,
                    influenciaPolitica = "Alta",
                    pessoasMobilizaveis = 500,
                    entidades = "Sindicato dos Transportadores, Clube de Futebol",
                    habilidades = "Motorista, Carro de Som, Bandeiraço, Mobilização, Fiscal",
                    temCarro = true,
                    temMoto = false,
                    temBicicleta = false,
                    cnhCategoria = "D",
                    temNotebook = false,
                    temAndroid = true,
                    temIPhone = false,
                    temInternet = true,
                    diasDisponiveis = "Todos os dias",
                    horariosDisponiveis = "Manhã e Tarde",
                    coordenadorResponsavel = "Carlos Eduardo Silva"
                ),
                Collaborator(
                    id = 4,
                    matricula = "CAMP-2026-0004",
                    nome = "Fernanda Lima de Souza",
                    cpf = "456.789.012-34",
                    rg = "45.678.901-2",
                    nascimento = "10/08/2000",
                    sexo = "Feminino",
                    estadoCivil = "Solteira",
                    telefone = "(11) 98888-7777",
                    whatsapp = "(11) 98888-7777",
                    email = "fernanda.lima@campanha.com.br",
                    endereco = "Av. Radial Leste, 2300",
                    cep = "03310-000",
                    cidade = "São Paulo",
                    bairro = "Tatuapé",
                    regiaoUrbana = "Zona Leste",
                    zona = "252ª Zona",
                    secaoEleitoral = "0155",
                    tituloEleitor = "4567.8901.2345",
                    pix = "456.789.012-34",
                    banco = "Caixa Econômica (104)",
                    profissao = "Promotora de Eventos",
                    escolaridade = "Superior Incompleto",
                    instagram = "@fer_limasp",
                    facebook = "/ferlimacampanha",
                    tiktok = "@ferlimatiktok",
                    photoUri = "",
                    status = "Ativo",
                    candidatosAnteriores = "Primeira Campanha",
                    liderancaComunitaria = false,
                    influenciaPolitica = "Média",
                    pessoasMobilizaveis = 120,
                    entidades = "Universidade, Grupo de Jovens",
                    habilidades = "Panfletagem, Porta a Porta, Bandeiraço, Eventos, Atendimento",
                    temCarro = false,
                    temMoto = false,
                    temBicicleta = true,
                    cnhCategoria = "B",
                    temNotebook = true,
                    temAndroid = true,
                    temIPhone = false,
                    temInternet = true,
                    diasDisponiveis = "Quarta a Domingo",
                    horariosDisponiveis = "Integral",
                    coordenadorResponsavel = "Carlos Eduardo Silva"
                )
            )

            sampleCollabs.forEach { db.collaboratorDao().insert(it) }

            // Materials
            val materialsList = listOf(
                MaterialDelivery(collaboratorId = 1, collaboratorNome = "Carlos Eduardo Silva", camiseta = 2, bone = 2, bandeira = 5, adesivo = 50, praguinha = 200, santinhos = 1000, perfuraco = 2, manual = 1),
                MaterialDelivery(collaboratorId = 3, collaboratorNome = "Roberto Gomes de Oliveira", camiseta = 3, bone = 3, bandeira = 10, adesivo = 100, praguinha = 500, santinhos = 3000, perfuraco = 5, manual = 2),
                MaterialDelivery(collaboratorId = 4, collaboratorNome = "Fernanda Lima de Souza", camiseta = 1, bone = 1, bandeira = 2, adesivo = 30, praguinha = 100, santinhos = 500, perfuraco = 0, manual = 1)
            )
            materialsList.forEach { db.materialDao().insert(it) }

            // Payments
            val paymentsList = listOf(
                PaymentRecord(collaboratorId = 1, collaboratorNome = "Carlos Eduardo Silva", pix = "123.456.789-01", banco = "Banco do Brasil", valor = 3500.00, ajudaDeCusto = 500.00, tipo = "Mensal", situacao = "Pago"),
                PaymentRecord(collaboratorId = 2, collaboratorNome = "Mariana Alcantara Santos", pix = "mariana.santos@email.com", banco = "Itaú", valor = 2800.00, ajudaDeCusto = 300.00, tipo = "Mensal", situacao = "Pago"),
                PaymentRecord(collaboratorId = 3, collaboratorNome = "Roberto Gomes de Oliveira", pix = "(11) 91234-5678", banco = "Bradesco", valor = 120.00, ajudaDeCusto = 80.00, tipo = "Diária + Combustível", situacao = "Pago"),
                PaymentRecord(collaboratorId = 4, collaboratorNome = "Fernanda Lima de Souza", pix = "456.789.012-34", banco = "Caixa Econômica", valor = 100.00, ajudaDeCusto = 30.00, tipo = "Diária", situacao = "Pago")
            )
            paymentsList.forEach { db.paymentDao().insert(it) }

            // Attendance
            val attendanceList = listOf(
                AttendanceRecord(collaboratorId = 1, collaboratorNome = "Carlos Eduardo Silva", tipo = "Entrada", evento = "Bandeiraço Centro", bairro = "Bela Vista", atividade = "Coordenação Geral"),
                AttendanceRecord(collaboratorId = 3, collaboratorNome = "Roberto Gomes de Oliveira", tipo = "Entrada", evento = "Carreata Zona Sul", bairro = "Santo Amaro", atividade = "Condução Carro de Som"),
                AttendanceRecord(collaboratorId = 4, collaboratorNome = "Fernanda Lima de Souza", tipo = "Entrada", evento = "Porta a Porta Tatuapé", bairro = "Tatuapé", atividade = "Panfletagem de Rua")
            )
            attendanceList.forEach { db.attendanceDao().insert(it) }

            // Goals
            val goalsList = listOf(
                GoalRecord(collaboratorId = 1, collaboratorNome = "Carlos Eduardo Silva", metaVisitas = 100, realizadasVisitas = 95, metaCadastros = 40, realizadasCadastros = 38, metaReunioes = 10, realizadasReunioes = 9),
                GoalRecord(collaboratorId = 3, collaboratorNome = "Roberto Gomes de Oliveira", metaVisitas = 80, realizadasVisitas = 78, metaCadastros = 25, realizadasCadastros = 22, metaReunioes = 5, realizadasReunioes = 5),
                GoalRecord(collaboratorId = 4, collaboratorNome = "Fernanda Lima de Souza", metaVisitas = 60, realizadasVisitas = 58, metaCadastros = 30, realizadasCadastros = 29, metaReunioes = 4, realizadasReunioes = 4)
            )
            goalsList.forEach { db.goalDao().insert(it) }

            // Leaderships
            val leadershipsList = listOf(
                Leadership(nome = "Dona Maria da Silva", bairro = "Bela Vista", comunidade = "Comunidade da Paz", nivelInfluencia = "Alta", contato = "(11) 99988-1122", familiasImpactadas = 180, apoiadoresGarantidos = 350, segmento = "Igreja & Comunitário"),
                Leadership(nome = "Pastor João Mendes", bairro = "Tatuapé", comunidade = "Jardim Anália", nivelInfluencia = "Estratégica", contato = "(11) 98877-3344", familiasImpactadas = 300, apoiadoresGarantidos = 620, segmento = "Igreja Evangélica"),
                Leadership(nome = "Mestre Benedito (Esporte)", bairro = "Santo Amaro", comunidade = "Vila Andrade", nivelInfluencia = "Alta", contato = "(11) 97766-5544", familiasImpactadas = 120, apoiadoresGarantidos = 280, segmento = "Projetos Sociais & Esporte")
            )
            leadershipsList.forEach { db.leadershipDao().insert(it) }

            // Events
            val eventsList = listOf(
                CampaignEvent(titulo = "Grande Carreata da Vitória - Zona Sul", tipo = "Carreata", local = "Praça Floriano Peixoto", bairro = "Santo Amaro", responsavel = "Carlos Eduardo Silva", descricao = "Concentração de 50 carros e trios elétricos com distribuição de bandeiras e praguinhas.", listaPresencaCount = 85, status = "Em Andamento"),
                CampaignEvent(titulo = "Reunião de Alinhamento com Lideranças", tipo = "Reunião", local = "Comitê Central - Av. Paulista 1500", bairro = "Bela Vista", responsavel = "Coordenador Geral", descricao = "Encontro de alinhamento estratégico com 25 lideranças comunitárias.", listaPresencaCount = 32, status = "Agendado"),
                CampaignEvent(titulo = "Bandeiraço e Panfletagem Tatuapé", tipo = "Bandeiraço", local = "Estação Metrô Tatuapé", bairro = "Tatuapé", responsavel = "Fernanda Lima de Souza", descricao = "Ação de impacto no horário de pico com entrega de 5.000 santinhos.", listaPresencaCount = 20, status = "Agendado")
            )
            eventsList.forEach { db.eventDao().insert(it) }

            // Audit Log
            db.auditLogDao().insert(
                AuditLog(
                    usuario = "Sistema",
                    perfil = "Administrador",
                    acao = "INICIALIZACAO_SISTEMA",
                    detalhe = "Banco de dados inicializado com dados demonstrativos de campanha política."
                )
            )
        }
    }
}
