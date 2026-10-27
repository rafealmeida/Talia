package br.com.projeto.view;

import br.com.projeto.controller.RegisterController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RegisterView {

    private final RegisterController controller = new RegisterController();

    public void start(Stage stage) {
        Label titulo = new Label("Criar Conta");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome completo");
        campoNome.setMaxWidth(280);

        TextField campoEmail = new TextField();
        campoEmail.setPromptText("E-mail");
        campoEmail.setMaxWidth(280);

        PasswordField campoSenha = new PasswordField();
        campoSenha.setPromptText("Senha (mínimo 6 caracteres)");
        campoSenha.setMaxWidth(280);

        PasswordField campoConfirmaSenha = new PasswordField();
        campoConfirmaSenha.setPromptText("Confirme a senha");
        campoConfirmaSenha.setMaxWidth(280);

        // Label para mostrar mensagens de erro ou sucesso
        Label lblMensagem = new Label("");
        lblMensagem.setTextFill(Color.RED);

        Button btnCadastrar = new Button("Cadastrar");
        btnCadastrar.setPrefWidth(280);
        btnCadastrar.setStyle("-fx-background-color: #FFB6C1; -fx-text-fill: #4A4A4A; -fx-font-weight: bold; -fx-cursor: hand;");

        Button btnVoltar = new Button("Voltar para Login");
        btnVoltar.setStyle("-fx-background-color: transparent; -fx-text-fill: #666666; -fx-underline: true; -fx-cursor: hand;");
        btnVoltar.setOnAction(e -> {
            stage.close();
            new LoginView().start(new Stage());
        });

        // Evento de clique do botão
        btnCadastrar.setOnAction(e -> {

            String resultado = controller.processarCadastro(
                    campoNome.getText(),
                    campoEmail.getText(),
                    campoSenha.getText(),
                    campoConfirmaSenha.getText()
            );

            if (resultado.equals("SUCESSO")) {
                lblMensagem.setTextFill(Color.GREEN);
                lblMensagem.setText("Cadastro realizado com sucesso!");
                // Opcional: Aqui poderíamos já redirecionar para a tela de Login
            } else {
                lblMensagem.setTextFill(Color.RED);
                lblMensagem.setText(resultado);
            }
        });

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.getChildren().addAll(titulo, campoNome, campoEmail, campoSenha, campoConfirmaSenha, btnCadastrar, btnVoltar, lblMensagem);
        layout.setStyle("-fx-background-color: #FFF0F5;");

        Scene scene = new Scene(layout, 400, 550);
        stage.setTitle("Cadastro - Talia");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}