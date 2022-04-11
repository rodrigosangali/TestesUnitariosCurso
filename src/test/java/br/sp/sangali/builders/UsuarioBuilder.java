package br.sp.sangali.builders;

import br.sp.sangali.entidades.Usuario;

public class UsuarioBuilder {
	
	private Usuario usuario;
	
	//Constructor
	private UsuarioBuilder() {}
	
	public static UsuarioBuilder umUsuario() {
		UsuarioBuilder builder = new UsuarioBuilder();
		builder.usuario = new Usuario();
		builder.usuario.setNome("Rodrigo");
		
		return builder;
	}
	
	public Usuario agora() {
		return usuario;
	}

}
