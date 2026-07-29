package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collaborators")
data class Collaborator(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val matricula: String = "",
    val nome: String,
    val cpf: String,
    val rg: String = "",
    val nascimento: String = "",
    val sexo: String = "",
    val estadoCivil: String = "",
    val telefone: String = "",
    val whatsapp: String = "",
    val email: String = "",
    val endereco: String = "",
    val cep: String = "",
    val cidade: String = "São Paulo",
    val bairro: String = "",
    val regiaoUrbana: String = "Centro",
    val zona: String = "",
    val secaoEleitoral: String = "",
    val tituloEleitor: String = "",
    val pix: String = "",
    val banco: String = "",
    val profissao: String = "",
    val escolaridade: String = "",
    val instagram: String = "",
    val facebook: String = "",
    val tiktok: String = "",
    val photoUri: String = "",
    val cnhDocUri: String = "",
    val compEnderecoDocUri: String = "",
    val compBancarioDocUri: String = "",
    val status: String = "Ativo", // Ativo, Inativo, Pendente

    // Módulo 2: Perfil Político
    val candidatosAnteriores: String = "",
    val liderancaComunitaria: Boolean = false,
    val influenciaPolitica: String = "Média", // Alta, Média, Baixa
    val pessoasMobilizaveis: Int = 10,
    val entidades: String = "", // Igreja, Associação, Esporte, Comércio, Condomínio, etc.

    // Módulo 3: Habilidades (comma separated or flags)
    val habilidades: String = "", // Panfletagem, Porta a Porta, Bandeiraço, Motorista, Carro de Som, etc.

    // Módulo 4: Logística
    val temCarro: Boolean = false,
    val temMoto: Boolean = false,
    val temBicicleta: Boolean = false,
    val cnhCategoria: String = "",
    val temNotebook: Boolean = false,
    val temAndroid: Boolean = true,
    val temIPhone: Boolean = false,
    val temInternet: Boolean = true,
    val diasDisponiveis: String = "Seg-Sexta",
    val horariosDisponiveis: String = "Integral",

    val coordenadorResponsavel: String = "Coordenador Geral",
    val dataCadastro: Long = System.currentTimeMillis()
)
