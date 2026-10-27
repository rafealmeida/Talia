package br.com.projeto.view;

import br.com.projeto.controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginView {

    private final LoginController controller = new LoginController();

    public void start(Stage stage) {
        // Criação dos componentes visuais
        Label titulo = new Label("Talia");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        titulo.setStyle("-fx-text-fill: #333333;");

        TextField campoEmail = new TextField();
        campoEmail.setPromptText("E-mail ou Identificador");
        campoEmail.setMaxWidth(280);
        campoEmail.setStyle("-fx-padding: 10px; -fx-font-size: 14px;");

        PasswordField campoSenha = new PasswordField();
        campoSenha.setPromptText("Senha");
        campoSenha.setMaxWidth(280);
        campoSenha.setStyle("-fx-padding: 10px; -fx-font-size: 14px;");

        Button btnLogin = new Button("Entrar");
        btnLogin.setPrefWidth(280);
        btnLogin.setStyle("-fx-background-color: #FFB6C1; -fx-text-fill: #4A4A4A; -fx-font-weight: bold; -fx-padding: 10px; -fx-font-size: 14px; -fx-cursor: hand;");

        Label linkCadastro = new Label("Não possui conta? Cadastre-se aqui.");
        linkCadastro.setOnMouseClicked(e -> {
            stage.close(); // Fecha a tela de login atual
            new RegisterView().start(new Stage()); // Abre a tela de cadastro
        });
        linkCadastro.setStyle("-fx-text-fill: #0066cc; -fx-underline: true; -fx-cursor: hand;");

        Label linkRecuperarSenha = new Label("Esqueci minha senha");
        linkRecuperarSenha.setStyle("-fx-text-fill: #666666; -fx-underline: true; -fx-cursor: hand;");

        // Label para exibir erros (adicione antes do VBox layout)
        Label lblErro = new Label("");
        lblErro.setStyle("-fx-text-fill: red;");

        // Novo evento de botão
        btnLogin.setOnAction(e -> {
            String email = campoEmail.getText();
            String senha = campoSenha.getText();

            if (email.isEmpty() || senha.isEmpty()) {
                lblErro.setText("Preencha o e-mail e a senha.");
                lblErro.setStyle("-fx-text-fill: red;");
            } else {
                boolean sucesso = controller.processarLogin(email, senha);
                if (sucesso) {
                    System.out.println("Login bem-sucedido! Bem vindo(a).");
                    lblErro.setText("Sucesso! Carregando...");
                    lblErro.setStyle("-fx-text-fill: green;");

                    // Fechando a janela atual (Login)
                    Stage janelaAtual = (Stage) btnLogin.getScene().getWindow();
                    janelaAtual.close();

                    // Abrindo a janela do Chat
                    new ChatView().start(new Stage());
                } else {
                    lblErro.setText("E-mail ou senha incorretos!");
                    lblErro.setStyle("-fx-text-fill: red;");
                }
            }
        });

        // Organizando tudo em uma coluna vertical (VBox)
        VBox layout = new VBox(20); // 20 pixels de espaçamento entre os itens
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #FFF0F5;");

        layout.getChildren().addAll(titulo, campoEmail, campoSenha, btnLogin, linkRecuperarSenha, linkCadastro, lblErro);

        // Criando a cena e adicionando à janela (Stage)
        Scene scene = new Scene(layout, 400, 550);

        stage.setTitle("Login - Talia");
        stage.setScene(scene);
        stage.setResizable(false); // Impede de redimensionar a tela de login
        stage.show();
    }
}

