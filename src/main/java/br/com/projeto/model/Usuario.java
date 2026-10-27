package br.com.projeto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Usuario {
    @BsonId
    private ObjectId id; // O identificador único gerado pelo MongoDB
    @NotBlank
    private String nome;
    @NotBlank
    @Size(min = 8)
    private String senha;
    @NotBlank
    @Size(min = 8)
    private String confirmacao;
    @NotBlank
    private String email;


    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}
