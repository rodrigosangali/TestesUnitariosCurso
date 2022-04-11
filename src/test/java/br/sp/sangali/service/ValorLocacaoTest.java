package br.sp.sangali.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import br.sp.sangali.daos.LocacaoDAO;
import br.sp.sangali.entidades.Filme;
import br.sp.sangali.entidades.Locacao;
import br.sp.sangali.entidades.Usuario;
import br.sp.sangali.exceptions.FilmeSemEstoqueException;
import br.sp.sangali.exceptions.LocadoraException;
import br.sp.sangali.servicos.LocacaoService;
import br.sp.sangali.servicos.SPCService;

@RunWith(Parameterized.class)
public class ValorLocacaoTest {
	
	public LocacaoService locacaoService;
	private SPCService spcService;
	
	@Parameter
	public List<Filme> filmes;

	@Parameter(value=1)
	public Double valorDaLocacao; 

	@Parameter(value=2)
	public String cenario;
	
	@Before
	public void setup() {
		locacaoService = new LocacaoService();
		LocacaoDAO dao = Mockito.mock(LocacaoDAO.class);
		spcService = Mockito.mock(SPCService.class);
		
		locacaoService.setLocacaoDAO(dao);
		locacaoService.setSPCService(spcService);
		
	}
	
	private static Filme filme1 = new Filme("Top Gun", 5, 15.0);
	private static Filme filme2 = new Filme("Matrix", 1, 15.0);
	private static Filme filme3 = new Filme("Tropa de Elite", 1, 20.0);
	private static Filme filme4 = new Filme("O Vento levou", 1, 10.0);
	private static Filme filme5 = new Filme("Homem Aranha", 1, 20.0);
	private static Filme filme6 = new Filme("Capitão Philips", 1, 10.0);
	private static Filme filme7 = new Filme("Como se fosse a primeira vez", 1, 10.0);
	
	@Parameters(name= "{index} = {1} - {2}")
	public static Collection<Object[]> getParametros(){
		return Arrays.asList(new Object[][] {
			{Arrays.asList(filme1, filme2), 30.0, "Sem desconto no 2º filme"},
			{Arrays.asList(filme1, filme2, filme3), 45.0, "Desconto 25% no 3º filme"},
			{Arrays.asList(filme1, filme2, filme3, filme4), 50.0, "Desconto 50% no 4º filme"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5), 55.0, "Desconto 75% no 5º filme"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 55.0, "Desconto 100% no 6º filme" },
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 55.0, "Sem desconto no 7º filme"}

		});
	}
	
	@Test
	public void ShouldDescontRentMovie() throws LocadoraException, FilmeSemEstoqueException {

		Usuario usuario1 = new Usuario("Rodrigo");
		
		Locacao locacao = locacaoService.alugarFilme(usuario1, filmes, new Date());
		
		Assert.assertEquals(locacao.getValor().doubleValue(), valorDaLocacao, 0.01);
	}


}
