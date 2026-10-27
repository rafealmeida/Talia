package br.com.projeto.service;

import br.com.projeto.model.Usuario;
import br.com.projeto.repository.UsuarioRepository;
import br.com.projeto.util.PasswordUtil;

public class UserService {
    private final UsuarioRepository repository;

    public UserService() {
        this.repository = new UsuarioRepository();
    }

    public String cadastrarUsuario(String nome, String email, String senha, String confirmaSenha) {
        // Validação de campos obrigatórios
        if (nome.trim().isEmpty() || email.trim().isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()) {
            return "Todos os campos são obrigatórios.";
        }
        // Validação de e-mail (simples)
        if (!email.contains("@") || !email.contains(".")) {
            return "Formato de e-mail inválido.";
        }
        // Validação de tamanho da senha
        if (senha.length() < 6) {
            return "A senha deve ter no mínimo 6 caracteres.";
        }
        // Validação de confirmação de senha
        if (!senha.equals(confirmaSenha)) {
            return "As senhas não conferem.";
        }
        // Verifica se o usuário já existe no MongoDB
        if (repository.buscarPorEmail(email) != null) {
            return "Já existe um usuário cadastrado com este e-mail.";
        }

        // Criptografa a senha antes de salvar
        String senhaCriptografada = PasswordUtil.hashPassword(senha);
        Usuario novoUsuario = new Usuario(nome, email, senhaCriptografada);

        repository.salvar(novoUsuario);
        return "SUCESSO";
    }

    public Usuario autenticarUsuario(String email, String senha) {
        // Limpa espaços vazios acidentais no início ou no fim
        String emailLimpo = email.trim();

        System.out.println("---------------------------------------------------");
        System.out.println("Tentando fazer login com o email: [" + emailLimpo + "]");

        // Busca o usuário no banco de dados
        Usuario usuario = repository.buscarPorEmail(emailLimpo);

        if (usuario != null) {
            System.out.println("=> Usuário encontrado no MongoDB! Nome: " + usuario.getNome());

            // Criptografa a senha que você acabou de digitar
            String senhaCriptografada = PasswordUtil.hashPassword(senha);

            System.out.println("=> Senha digitada (Criptografada): " + senhaCriptografada);
            System.out.println("=> Senha que está salva no banco : " + usuario.getSenha());

            if (usuario.getSenha().equals(senhaCriptografada)) {
                System.out.println("=> SUCESSO: As senhas batem perfeitamente!");
                return usuario;
            } else {
                System.out.println("=> ERRO: As senhas não coincidem.");
            }
        } else {
            System.out.println("=> ERRO: Nenhum usuário foi encontrado com esse e-mail.");
        }
        System.out.println("---------------------------------------------------");
        return null;
    }
}