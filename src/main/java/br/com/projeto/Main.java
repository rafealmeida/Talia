package br.com.projeto;

import br.com.projeto.config.DatabaseConfig;
import br.com.projeto.view.SplashView;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        // Conecta ao banco de dados em segundo plano
        DatabaseConfig.getDatabase();

        // Inicia a interface gráfica exibindo a animação GSAP
        Application.launch(SplashView.class, args);
    }
}