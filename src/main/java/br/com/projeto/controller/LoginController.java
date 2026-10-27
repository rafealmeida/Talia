package br.com.projeto.controller;

import br.com.projeto.model.Usuario;
import br.com.projeto.service.UserService;
import br.com.projeto.util.SessaoUsuario;

public class LoginController {

    private final UserService userService;

    public LoginController() {
        this.userService = new UserService();
    }

    public boolean processarLogin(String email, String senha) {
        Usuario usuario = userService.autenticarUsuario(email, senha);

        if (usuario != null) {
            SessaoUsuario.setUsuarioLogado(usuario); // Salva a sessão
            return true;
        }
        return false;
    }
}