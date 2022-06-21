package br.com.todo.aplicacao.dto;

import br.com.todo.dominio.modelos.Usuario;

public class UsuarioDto {

    private Long id;

    private String nome;

    private String apelido;

    private String email;

    private Integer pontosConclusaoMetas;

    public UsuarioDto(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.apelido = usuario.getApelido();
        this.email = usuario.getEmail();
        this.pontosConclusaoMetas = usuario.getPontosConclusaoMetas();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getApelido() {
        return apelido;
    }

    public String getEmail() {
        return email;
    }

    public Integer getPontosConclusaoMetas() {
        return pontosConclusaoMetas;
    }
}
