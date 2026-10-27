package br.com.projeto.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GroqService {

    private final String apiKey;
    private final HttpClient httpClient;

    public GroqService() {
        // Puxa a chave do arquivo .env (Regra 19 da atividade!)
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("GROQ_API_KEY");
        this.httpClient = HttpClient.newHttpClient();
    }

    public String enviarPergunta(String perguntaUsuario) {
        try {
            JsonArray mensagens = new JsonArray();

            // Instrução de sistema para forçar o Português
            JsonObject sistema = new JsonObject();
            sistema.addProperty("role", "system");
            sistema.addProperty("content", "Você é a Talia, uma assistente virtual. Responda SEMPRE em Português do Brasil. Seja concisa.");
            mensagens.add(sistema);

            // Mensagem do usuário
            JsonObject mensagemUsuario = new JsonObject();
            mensagemUsuario.addProperty("role", "user");
            mensagemUsuario.addProperty("content", perguntaUsuario);
            mensagens.add(mensagemUsuario);

            JsonObject corpoRequisicao = new JsonObject();
            // Atualizado para o modelo mais recente e rápido da Groq
            corpoRequisicao.addProperty("model", "openai/gpt-oss-20b");
            corpoRequisicao.add("messages", mensagens);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(corpoRequisicao.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                return jsonResponse.getAsJsonArray("choices")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("content").getAsString();
            } else {
                // O "Fofoqueiro" da IA: vai imprimir o erro exato no console
                System.out.println("====== ERRO NA GROQ ======");
                System.out.println(response.body());
                System.out.println("==========================");
                return "Desculpe, ocorreu um erro interno (Erro " + response.statusCode() + "). Olhe o console!";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro técnico ao tentar pensar na resposta.";
        }
    }
}