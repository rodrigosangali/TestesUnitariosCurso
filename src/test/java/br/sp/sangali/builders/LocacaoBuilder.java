package br.sp.sangali.builders;

import java.util.Arrays;
import java.util.Date;

import br.sp.sangali.entidades.Locacao;
import br.sp.sangali.entidades.Usuario;

public class LocacaoBuilder {
	
	private Locacao locacao;
	
	private LocacaoBuilder() {}
	
	public static LocacaoBuilder umaLocacao() {
		LocacaoBuilder builder = new LocacaoBuilder();
		builder.locacao = new Locacao();
		builder.locacao.setFilmes(Arrays.asList(FilmeBuilder.umFilme().agora()));
		
		return builder;
		
	}
	
	public LocacaoBuilder comDataRetorno(Date dataRetorno) {
		locacao.setDataRetorno(dataRetorno);
		return this;
	}
	
	public LocacaoBuilder comUsuario(Usuario usuario) {
		locacao.setUsuario(usuario);
		return this;
	}
	
	public Locacao agora() {
		return locacao;
	}
	

}
