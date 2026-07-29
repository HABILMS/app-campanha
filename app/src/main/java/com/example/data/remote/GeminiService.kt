package com.example.data.remote

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun analyzeCampaignData(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "🤖 **Análise Inteligente IA Gemini (Modo Demonstração)**:\n\n" +
                    "• **Redistribuição de Equipe**: Recomenda-se deslocar 12 colaboradores do Centro Urbano para o Bairro Jardim América (cobertura atual em apenas 28%).\n" +
                    "• **Previsão de Material**: Previsão de esgotamento de santinhos e praguinhas no Bairro Vila Nova em 48h. Necessário emissão de lote de 10.000 unidades.\n" +
                    "• **Alerta de Produtividade**: A equipe do setor Norte alcançou 96% das metas de visitas, enquanto o setor Leste requer acompanhamento logístico suplementar.\n" +
                    "• **Recomendação Estratégica**: Intensificar bandeiraços nas avenidas principais nos horários de pico (17h-19h)."
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val jsonPayload = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
            }

            val body = jsonPayload.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext "Erro na API Gemini (${response.code}): $responseBody"
            }

            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val candidate = candidates.getJSONObject(0)
                val content = candidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    return@withContext parts.getJSONObject(0).optString("text", "Sem texto gerado.")
                }
            }
            "Sem resposta válida retornada pelo modelo Gemini."
        } catch (e: Exception) {
            "Falha na análise da IA Gemini: ${e.localizedMessage}"
        }
    }
}
