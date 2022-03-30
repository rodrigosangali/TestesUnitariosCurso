package br.sp.sangali.servicos;

import static br.sp.sangali.utils.DataUtils.adicionarDias;

import java.util.Date;
import java.util.List;

import br.sp.sangali.entidades.Filme;
import br.sp.sangali.entidades.Locacao;
import br.sp.sangali.entidades.Usuario;
import br.sp.sangali.exceptions.FilmeSemEstoqueException;
import br.sp.sangali.exceptions.LocadoraException;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws LocadoraException, FilmeSemEstoqueException{
		
		if (usuario == null) {
			throw new LocadoraException("Usu�rio n�o informado");
		}
		
		if (filmes == null) {
			throw new LocadoraException("Filme n�o informado");
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		
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
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	public static void main(String[] args) {
		
	}
}