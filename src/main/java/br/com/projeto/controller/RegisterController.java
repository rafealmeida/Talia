package br.com.projeto.controller;

import br.com.projeto.service.UserService;

public class RegisterController {
    private final UserService userService;

    public RegisterController() {
        this.userService = new UserService();
    }
    public String processarCadastro(String nome, String email, String senha, String confirmaSenha) {
        return userService.cadastrarUsuario(nome, email, senha, confirmaSenha);
    }
}
