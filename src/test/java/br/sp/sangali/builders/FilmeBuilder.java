package br.sp.sangali.builders;

import br.sp.sangali.entidades.Filme;

public class FilmeBuilder {
	
	private Filme filme;
	
	private FilmeBuilder() {}
	
	public static FilmeBuilder umFilme() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme  = new Filme();
		builder.filme.setEstoque(2);
		builder.filme.setNome("Top Gun");
		builder.filme.setPrecoLocacao(15.0);
		
		return builder;
	}
	
	
	public FilmeBuilder semEstoque() {
		filme.setEstoque(0);
		
		return this;
	}
	
	public Filme agora() {
		return filme;
	}

}
