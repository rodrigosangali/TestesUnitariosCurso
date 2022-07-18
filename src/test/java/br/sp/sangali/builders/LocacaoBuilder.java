package br.sp.sangali.builders;

import java.util.Arrays;
import java.util.Date;

import br.sp.sangali.entidades.Locacao;
import br.sp.sangali.entidades.Usuario;
import br.sp.sangali.utils.DataUtils;

public class LocacaoBuilder {
	
	private Locacao locacao;
	
	private LocacaoBuilder() {}
	
	public static LocacaoBuilder umaLocacao() {
		LocacaoBuilder builder = new LocacaoBuilder();
		builder.locacao = new Locacao();
		builder.locacao.setFilmes(Arrays.asList(FilmeBuilder.umFilme().agora()));
		builder.locacao.setDataLocacao(new Date());
		builder.locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(+1));
		builder.locacao.setValor(4.0);
		return builder;
		
	}
	
	public LocacaoBuilder comDataRetorno(Date dataRetorno) {
		locacao.setDataRetorno(dataRetorno);
		return this;
	}
	
	
	public LocacaoBuilder atrasado() {
		locacao.setDataLocacao(DataUtils.obterDataComDiferencaDias(-4));
		locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(-2));
		
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
