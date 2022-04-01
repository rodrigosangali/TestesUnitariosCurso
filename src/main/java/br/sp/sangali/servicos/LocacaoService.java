package br.sp.sangali.servicos;

import static br.sp.sangali.utils.DataUtils.adicionarDias;

import java.util.Date;
import java.util.List;

import br.sp.sangali.entidades.Filme;
import br.sp.sangali.entidades.Locacao;
import br.sp.sangali.entidades.Usuario;
import br.sp.sangali.exceptions.FilmeSemEstoqueException;
import br.sp.sangali.exceptions.LocadoraException;
import br.sp.sangali.utils.DataUtils;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes, Date dataLocacao) throws LocadoraException, FilmeSemEstoqueException{
		
		if (usuario == null) {
			throw new LocadoraException("Usuário não informado");
		}
		
		if (filmes == null) {
			throw new LocadoraException("Filme não informado");
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(dataLocacao);
		
		double valorTotalLocacao = 0d;
		double desconto= 0;
		for (int i=0; i<filmes.size(); i++){
			
			if (filmes.get(i).getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
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
		//TODO adicionar mÃ©todo para salvar
		
		return locacao;
	}

	public static void main(String[] args) {
		
	}
}