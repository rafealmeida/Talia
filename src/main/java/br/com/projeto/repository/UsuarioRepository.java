package br.com.projeto.repository;

import br.com.projeto.config.DatabaseConfig;
import br.com.projeto.model.Usuario;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class UsuarioRepository {
    private final MongoCollection<Document> collection;

    public UsuarioRepository() {
        MongoDatabase database = DatabaseConfig.getDatabase();
        // Cria ou acessa a tabela/coleção chamada "users"
        this.collection = database.getCollection("users");
    }
    public void salvar(Usuario usuario) {
        Document doc = new Document("nome", usuario.getNome())
                .append("email", usuario.getEmail())
                .append("senha", usuario.getSenha()); // Na próxima etapa vamos criptografar isso!

        collection.insertOne(doc);
        System.out.printf("Usuário salvo com sucesso no MongoDB");
    }
    public Usuario buscarPorEmail(String email) {
        Document doc = collection.find(new Document("email", email)).first();

        if (doc != null) {
            Usuario usuario = new Usuario();
            usuario.setId(doc.getObjectId("_id"));
            usuario.setNome(doc.getString("nome"));
            usuario.setEmail(doc.getString("email"));
            usuario.setSenha(doc.getString("senha"));
            return usuario;


        }
        return null; // Retorna nulo de não encontrar o usuário
    }
}