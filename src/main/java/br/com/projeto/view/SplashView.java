package br.com.projeto.view;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.net.URL;

public class SplashView extends Application {

    @Override
    public void start(Stage splashStage) {
        // Inicializa o WebView do JavaFX
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Carrega o arquivo HTML que criamos na pasta resources/web
        URL url = getClass().getResource("/web/splash.html");
        if (url != null) {
            webEngine.load(url.toExternalForm());
        } else {
            System.err.println("Arquivo splash.html não encontrado!");
        }

        StackPane root = new StackPane(webView);
        Scene scene = new Scene(root, 600, 400);

        // Remove as bordas do Windows para ficar parecido com uma splash screen real
        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.setScene(scene);
        splashStage.centerOnScreen();
        splashStage.show();

        // Define um temporizador (PauseTransition) equivalente ao tempo da sua animação GSAP
        // Após 4 segundos, fecha a Splash Screen e tenta abrir o Login
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(event -> {
            splashStage.close();
            abrirTelaDeLogin();
        });
        delay.play();
    }

    private void abrirTelaDeLogin() {
        System.out.println("Animação finalizada. Chamando a tela de Login...");

        // Cria uma nova janela (Stage) e inicia a LoginView nela
        Stage loginStage = new Stage();
        LoginView loginView = new LoginView();
        loginView.start(loginStage);
    }
}