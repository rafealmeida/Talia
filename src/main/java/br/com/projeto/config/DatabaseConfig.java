package br.com.projeto.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
public class DatabaseConfig {
    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    public static MongoDatabase getDatabase(){
        if (database == null) {
            try {
                // Carrega as variáveis do arquivo .env
                Dotenv dotenv = Dotenv.load();
                String uri = dotenv.get("MONGODB_URI");

                // Verifica de a URI foi encontrada
                if (uri == null || uri.isEmpty()) {
                    throw new RuntimeException(
                            "MONGODB_URI não encontrada no arquivo .env"
                    );
                }

                // Conecta ao MongoDB Atlas
                mongoClient = MongoClients.create(uri);

                //Cria ou acessa o banco de dados chamado "chat_db"
                database = mongoClient.getDatabase("chat_db");
                System.out.println("Conexão com o MongoDB estabelecida com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro ao conectar no banco de dados: " + e.getMessage());
            }
        }
        return database;
    }
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }
}
