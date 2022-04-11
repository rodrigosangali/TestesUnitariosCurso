package br.sp.sangali.servicos;

import br.sp.sangali.entidades.Usuario;

public interface EmailService {
	
	public void notificarAtraso(Usuario usuario);

}
