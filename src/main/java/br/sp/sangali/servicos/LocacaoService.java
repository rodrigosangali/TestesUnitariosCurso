package br.sp.sangali.servicos;

import static br.sp.sangali.utils.DataUtils.adicionarDias;

import java.util.Date;
import java.util.List;

import br.sp.sangali.daos.LocacaoDAO;
import br.sp.sangali.entidades.Filme;
import br.sp.sangali.entidades.Locacao;
import br.sp.sangali.entidades.Usuario;
import br.sp.sangali.exceptions.FilmeSemEstoqueException;
import br.sp.sangali.exceptions.LocadoraException;
import br.sp.sangali.utils.DataUtils;

public class LocacaoService {
	
	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes, Date dataLocacao) throws Exception{
		
		if (usuario == null) {
			throw new LocadoraException("Usuário não informado");
		}
		
		if (filmes == null) {
			throw new FilmeSemEstoqueException("Filme não informado");
		}
		
		
		Boolean negativado = null; 
		try {
			negativado = spcService.possuiNegativado(usuario);
			
		}catch(Exception e) {
			throw new LocadoraException("Problemas com SPC, tente novamente");
		}
		
		if (negativado) {
			throw new LocadoraException("Usuario Negativado"); 
			
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(dataLocacao);
		
		double valorTotalLocacao = 0d;
		double desconto= 0;
		for (int i=0; i<filmes.size(); i++){
			
			if (filmes.get(i).getEstoque() == 0) {
				throw new FilmeSemEstoqueException("Filme não informado");
			}
			
			switch(i){
				case 2:
					desconto = filmes.get(i).getPrecoLocacao() * 0.25;
					break;
				case 3:
					desconto = filmes.get(i).getPrecoLocacao() * 0.50;
					break;
				case 4:
					desconto = filmes.get(i).getPrecoLocacao() * 0.75;
					break;
				case 5:
					desconto = filmes.get(i).getPrecoLocacao() * 1;
					break;
			}
			valorTotalLocacao += filmes.get(i).getPrecoLocacao() - desconto;
		}
		locacao.setValor(valorTotalLocacao);
		
		//Entrega no dia seguinte
		Date dataEntrega = adicionarDias(dataLocacao, 1);
		// Verificar se é sabado, se for, adicionar mais um dia para entregar na segunda
		if (DataUtils.verificarDiaSemana(dataLocacao, 7)) dataEntrega = adicionarDias(dataEntrega, 1);	

		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		dao.salvar(locacao);
		
		return locacao;
	}

	
	public void notificarAtrasados() {
		List<Locacao> locacoes = dao.obterLocacoesPendetes();
		for(Locacao locacao: locacoes) {
			if(locacao.getDataRetorno().before(new Date())) {
				emailService.notificarAtraso(locacao.getUsuario());
			}
		}
	}
	
	public void prorrogarLocacao(Locacao locacao, int dias) {
		Locacao novaLocacao = new Locacao();
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setFilmes(locacao.getFilme());
		novaLocacao.setDataLocacao(new Date());
		novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
		novaLocacao.setValor(locacao.getValor() * dias);
		dao.salvar(novaLocacao);
	}
	
}