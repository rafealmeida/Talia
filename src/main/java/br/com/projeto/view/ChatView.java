package br.com.projeto.view;

import br.com.projeto.util.SessaoUsuario;
import br.com.projeto.service.GroqService;
import br.com.projeto.service.RAGService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class ChatView {

    private final GroqService groqService = new GroqService();
    private final RAGService ragService = new RAGService();

    // O cérebro local que vai lembrar de todas as conversas sem precisar da nuvem
    private final Map<String, String> memoriaConversas = new HashMap<>();
    private String conversaAtual = "Conversa 1";

    public void start(Stage stage) {

        // Inicializa a primeira conversa
        memoriaConversas.put("Conversa 1", "Talia: Olá! Como posso ajudar você hoje?\n");

        // --- 1. ÁREA DE MENSAGENS ---
        TextArea areaMensagens = new TextArea(memoriaConversas.get("Conversa 1"));
        areaMensagens.setEditable(false);
        areaMensagens.setWrapText(true);
        areaMensagens.setFont(Font.font("Segoe UI", 14));
        areaMensagens.setStyle("-fx-control-inner-background: #ffffff; -fx-text-fill: #4A4A4A;");

        // --- 2. BARRA LATERAL (SIDEBAR) ---
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: #F7E7CE;"); // Tom Champanhe

        Label lblOla = new Label("Olá, " + SessaoUsuario.getUsuarioLogado().getNome());
        lblOla.setStyle("-fx-text-fill: #4A4A4A; -fx-font-weight: bold; -fx-font-size: 16px;");

        Button btnNovaConversa = new Button("+ Nova Conversa");
        btnNovaConversa.setMaxWidth(Double.MAX_VALUE);
        btnNovaConversa.setStyle("-fx-background-color: #FFB6C1; -fx-text-fill: #4A4A4A; -fx-font-weight: bold; -fx-cursor: hand;");

        ListView<String> listaConversas = new ListView<>();
        listaConversas.getItems().add("Conversa 1");
        listaConversas.setStyle("-fx-background-color: transparent; -fx-text-fill: #4A4A4A;");

        // Menu de clique direito (Renomear e Excluir)
        ContextMenu menuContexto = new ContextMenu();
        MenuItem itemRenomear = new MenuItem("✏️ Renomear");
        itemRenomear.setOnAction(e -> {
            int index = listaConversas.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                String nomeAntigo = listaConversas.getItems().get(index);
                String nomeNovo = nomeAntigo + " (Editada)";
                listaConversas.getItems().set(index, nomeNovo);

                // Transfere o histórico para o novo nome
                String historico = memoriaConversas.remove(nomeAntigo);
                memoriaConversas.put(nomeNovo, historico);
                if (conversaAtual.equals(nomeAntigo)) conversaAtual = nomeNovo;
            }
        });

        MenuItem itemExcluir = new MenuItem("🗑️ Excluir");
        itemExcluir.setOnAction(e -> {
            String selecionado = listaConversas.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                listaConversas.getItems().remove(selecionado);
                memoriaConversas.remove(selecionado);
                areaMensagens.setText("Talia: Conversa excluída.\n");
            }
        });
        menuContexto.getItems().addAll(itemRenomear, itemExcluir);
        listaConversas.setContextMenu(menuContexto);

        // Ação de nova conversa
        btnNovaConversa.setOnAction(e -> {
            int num = listaConversas.getItems().size() + 1;
            String nova = "Conversa " + num;
            listaConversas.getItems().add(nova);
            memoriaConversas.put(nova, "Talia: Olá! Nova conversa iniciada.\n");
            listaConversas.getSelectionModel().select(nova);
        });

        Button btnExportar = new Button("💾 Exportar para TXT");
        btnExportar.setMaxWidth(Double.MAX_VALUE);
        btnExportar.setStyle("-fx-background-color: #A8E6CF; -fx-text-fill: #4A4A4A; -fx-font-weight: bold; -fx-cursor: hand;");

        // Ação para abrir a janela do Windows e escolher onde guardar
        btnExportar.setOnAction(e -> {
            try {
                javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
                fileChooser.setTitle("Guardar Relatório da Talia");
                // Substitui espaços por underline para evitar erros no nome do ficheiro
                fileChooser.setInitialFileName("Relatorio_Talia_" + conversaAtual.replace(" ", "_") + ".txt");
                fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Ficheiro de Texto", "*.txt"));

                // Abre a janela para o utilizador escolher o local
                java.io.File ficheiro = fileChooser.showSaveDialog(stage);

                if (ficheiro != null) {
                    String conteudo = areaMensagens.getText();
                    java.nio.file.Files.writeString(ficheiro.toPath(), conteudo);
                    areaMensagens.appendText("\n[SISTEMA]: Ficheiro exportado com sucesso!\n");
                }
            } catch (Exception ex) {
                areaMensagens.appendText("\n[SISTEMA]: Erro ao tentar exportar: " + ex.getMessage() + "\n");
            }
        });

        // Ação ao clicar numa conversa antiga (Resgata a memória exata dela)
        listaConversas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(oldVal)) {
                conversaAtual = newVal;
                areaMensagens.setText(memoriaConversas.getOrDefault(newVal, ""));
            }
        });

        Button btnSair = new Button("Sair (Logout)");
        btnSair.setMaxWidth(Double.MAX_VALUE);
        btnSair.setStyle("-fx-background-color: #E5989B; -fx-text-fill: white; -fx-cursor: hand;");
        btnSair.setOnAction(e -> {
            SessaoUsuario.logout();
            stage.close();
            new LoginView().start(new Stage());
        });

        sidebar.getChildren().addAll(lblOla, btnNovaConversa, btnExportar, listaConversas, btnSair);

        // --- 3. ÁREA CENTRAL ---
        BorderPane areaCentral = new BorderPane();
        areaCentral.setPadding(new Insets(20));
        areaCentral.setStyle("-fx-background-color: #FFF0F5;");
        areaCentral.setCenter(areaMensagens);

        // --- 4. BARRA DE DIGITAÇÃO (RODAPÉ) ---
        HBox rodape = new HBox(10);
        rodape.setPadding(new Insets(10, 0, 0, 0));
        rodape.setAlignment(Pos.CENTER);

        Button btnTema = new Button("🌙 Escuro");
        btnTema.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-cursor: hand;");
        btnTema.setOnAction(e -> {
            if (btnTema.getText().equals("🌙 Escuro")) {
                btnTema.setText("☀️ Claro");
                areaCentral.setStyle("-fx-background-color: #2b2b2b;");
                areaMensagens.setStyle("-fx-control-inner-background: #333333; -fx-text-fill: white;");
            } else {
                btnTema.setText("🌙 Escuro");
                areaCentral.setStyle("-fx-background-color: #FFF0F5;");
                areaMensagens.setStyle("-fx-control-inner-background: #ffffff; -fx-text-fill: #4A4A4A;");
            }
        });

        TextArea campoDigitacao = new TextArea();
        campoDigitacao.setPromptText("Digite a sua pergunta aqui...");
        campoDigitacao.setPrefRowCount(2);
        campoDigitacao.setWrapText(true);
        campoDigitacao.setMaxWidth(500);

        Button btnEnviar = new Button("Enviar");
        btnEnviar.setPrefHeight(45);
        btnEnviar.setStyle("-fx-background-color: #FFB6C1; -fx-text-fill: #4A4A4A; -fx-font-weight: bold; -fx-cursor: hand;");

        // Enviar pergunta com RAG e MEMÓRIA DE CONTEXTO (Diferencial 2)
        btnEnviar.setOnAction(e -> {
            String texto = campoDigitacao.getText().trim();
            if (!texto.isEmpty()) {
                areaMensagens.appendText("\nVocê: " + texto + "\n");
                memoriaConversas.put(conversaAtual, areaMensagens.getText());

                campoDigitacao.clear();
                btnEnviar.setDisable(true);
                areaMensagens.appendText("Talia: Digitando...\n");

                new Thread(() -> {
                    // 1. Pega os últimos 800 caracteres do chat para a IA "lembrar" do que estão a falar
                    String historico = areaMensagens.getText();
                    if (historico.length() > 800) {
                        historico = historico.substring(historico.length() - 800);
                    }

                    // 2. Junta a pergunta, o RAG (base de conhecimento) e a memória!
                    String textoComRAG = ragService.buscarContexto(texto);
                    String promptComMemoria = textoComRAG + "\n\n[Histórico da nossa conversa até agora para você lembrar do contexto]:\n" + historico;

                    // 3. Envia o pacote completo e super inteligente para a Groq
                    String respostaIA = groqService.enviarPergunta(promptComMemoria);

                    Platform.runLater(() -> {
                        areaMensagens.deleteText(areaMensagens.getLength() - 20, areaMensagens.getLength());
                        areaMensagens.appendText("Talia: " + respostaIA + "\n");
                        memoriaConversas.put(conversaAtual, areaMensagens.getText());
                        btnEnviar.setDisable(false);
                    });
                }).start();
            }
        });


        rodape.getChildren().addAll(btnTema, campoDigitacao, btnEnviar);
        areaCentral.setBottom(rodape);

        // --- 5. MONTAR A TELA FINAL ---
        BorderPane layoutPrincipal = new BorderPane();
        layoutPrincipal.setLeft(sidebar);
        layoutPrincipal.setCenter(areaCentral);

        Scene scene = new Scene(layoutPrincipal, 900, 600);

        // Ícone do projeto
        try {
            stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/icon.png")));
        } catch (Exception ex) { }

        stage.setTitle("Talia - Conectada");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.show();
    }
}