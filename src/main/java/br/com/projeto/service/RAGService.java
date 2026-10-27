package br.com.projeto.service;

import java.util.Arrays;
import java.util.List;

public class RAGService {

    // Lista de palavras que a IA deve ignorar na busca
    private static final List<String> STOP_WORDS = Arrays.asList("o", "a", "os", "as", "um", "uma", "de", "do", "da", "em", "no", "na", "para", "com", "que", "e", "é", "qual", "como");

    // Simulação do seu arquivo de conhecimento local (Base de Dados do RAG)
    private static final List<String> BASE_CONHECIMENTO = Arrays.asList(
            "Meu nome é Talia, sou uma assistente virtual focada em produtividade e atendimento.",
            "Fui desenvolvida pela aluna Raquel Fernandes, como projeto final do curso técnico.",
            "Minha interface foi construída utilizando Java 21 e JavaFX, com identidade visual na cor rosa champanhe.",
            "Meus dados de autenticação de usuários são armazenados de forma super segura no MongoDB Atlas, na nuvem.",
            "Eu possuo uma arquitetura assíncrona não-bloqueante, o que significa que minha tela não trava enquanto eu penso.",
            "Eu utilizo um motor de RAG nativo criado do zero, sem depender de bibliotecas externas pesadas."
    );

    public String buscarContexto(String pergunta) {
        // Converte a pergunta para minúsculo e separa por espaços
        String[] palavras = pergunta.toLowerCase().split(" ");

        // Remove as stop words para achar a essência da pergunta
        for (String base : BASE_CONHECIMENTO) {
            for (String palavra : palavras) {
                if (!STOP_WORDS.contains(palavra) && palavra.length() > 2) {
                    if (base.toLowerCase().contains(palavra)) {
                        return "Use esta informação como base para responder: " + base + "\n\nPergunta do usuário: " + pergunta;
                    }
                }
            }
        }
        return pergunta; // Se não achar nada no RAG, envia a pergunta normal para a Groq
    }
}