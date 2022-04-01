package br.sp.sangali.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import br.sp.sangali.entidades.Filme;
import br.sp.sangali.entidades.Locacao;
import br.sp.sangali.entidades.Usuario;
import br.sp.sangali.exceptions.FilmeSemEstoqueException;
import br.sp.sangali.exceptions.LocadoraException;
import br.sp.sangali.servicos.LocacaoService;
import br.sp.sangali.utils.DataUtils;


public class LocacaoServiceTest {

	@Test
	public void alugarFilmeTest() throws Exception {
		
		List<Filme> filme = Arrays.asList(new Filme("Top Gun", 5, 10.1));
		Usuario usuario1 = new Usuario("Rodrigo");
		LocacaoService locacaoService = new LocacaoService();
		Locacao locacao = locacaoService.alugarFilme(usuario1, filme, new Date());
		
		// Verificar o valor da locação
		Assert.assertEquals(locacao.getValor(), 10.1, 0.01);
		// Verificar se a data de locação a data atual
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		// Verificar se a data de retorno é data atual + 1
		Assert.assertTrue(DataUtils.isMesmaData(
				locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}

	// Esperando uma excessão, se nao ocorrer o teste falha, não consegue verificar qual excessão foi lançada 
	@Test(expected=FilmeSemEstoqueException.class)
	public void locacaoSemEstoqueTest() throws Exception {
		
		List<Filme> filme = Arrays.asList(new Filme("Top Gun", 0, 10.1));
		Usuario usuario1 = new Usuario("Rodrigo");
		LocacaoService locacaoService = new LocacaoService();
		
		locacaoService.alugarFilme(usuario1, filme, new Date());
	}
	
	@Test
	public void itShouldDescontThirdRentMovie() throws LocadoraException, FilmeSemEstoqueException {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		LocacaoService locacaoService = new LocacaoService();
		Locacao locacao = new Locacao();
		
		filme.add(new Filme("Top Gun", 5, 15.0));
		filme.add(new Filme("Matrix", 1, 15.0));
		filme.add(new Filme("Tropa de Elite", 1, 20.0));
		
		locacao = locacaoService.alugarFilme(usuario1, filme, new Date());
		
		Assert.assertEquals(locacao.getValor().doubleValue(), 45.0, 0.01);
	}

	@Test
	public void itShouldDescontFourthRentMovie() throws LocadoraException, FilmeSemEstoqueException {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		LocacaoService locacaoService = new LocacaoService();
		Locacao locacao = new Locacao();
		
		filme.add(new Filme("Top Gun", 5, 15.0));
		filme.add(new Filme("Matrix", 1, 15.0));
		filme.add(new Filme("Tropa de Elite", 1, 20.0));
		filme.add(new Filme("O Vento levou", 1, 10.0));
		
		locacao = locacaoService.alugarFilme(usuario1, filme, new Date());
		
		Assert.assertEquals(locacao.getValor().doubleValue(), 50.0, 0.01);
	}
	
	@Test
	public void itShouldDescontFifthRentMovie() throws LocadoraException, FilmeSemEstoqueException {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		LocacaoService locacaoService = new LocacaoService();
		Locacao locacao = new Locacao();
		
		filme.add(new Filme("Top Gun", 5, 15.0));
		filme.add(new Filme("Matrix", 1, 15.0));
		filme.add(new Filme("Tropa de Elite", 1, 20.0));
		filme.add(new Filme("O Vento levou", 1, 10.0));
		filme.add(new Filme("Homem Aranha", 1, 20.0));
		
		locacao = locacaoService.alugarFilme(usuario1, filme, new Date());
		
		Assert.assertEquals(locacao.getValor().doubleValue(), 55.0, 0.01);
	}

	@Test
	public void ShouldDescontSixthRentMovie() throws LocadoraException, FilmeSemEstoqueException {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		LocacaoService locacaoService = new LocacaoService();
		Locacao locacao = new Locacao();
		
		filme.add(new Filme("Top Gun", 5, 15.0));
		filme.add(new Filme("Matrix", 1, 15.0));
		filme.add(new Filme("Tropa de Elite", 1, 20.0));
		filme.add(new Filme("O Vento levou", 1, 10.0));
		filme.add(new Filme("Homem Aranha", 1, 20.0));
		filme.add(new Filme("Capitão Philips", 1, 10.0));

		
		locacao = locacaoService.alugarFilme(usuario1, filme, new Date());
		
		Assert.assertEquals(locacao.getValor().doubleValue(), 55.0, 0.01);
	}

	@Test
	public void ShouldDeliveryMonday() throws LocadoraException, FilmeSemEstoqueException, ParseException {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		LocacaoService locacaoService = new LocacaoService();
		Locacao locacao = new Locacao();
		
		filme.add(new Filme("Top Gun", 5, 15.0));
		filme.add(new Filme("Matrix", 1, 15.0));
		filme.add(new Filme("Tropa de Elite", 1, 20.0));
		filme.add(new Filme("O Vento levou", 1, 10.0));
		filme.add(new Filme("Homem Aranha", 1, 20.0));
		filme.add(new Filme("Capitão Philips", 1, 10.0));

		Date dataLocacao = DataUtils.obterData(02, 04, 2022);
		
		locacao = locacaoService.alugarFilme(usuario1, filme, dataLocacao);
		
		Assert.assertTrue(DataUtils.verificarDiaSemana(locacao.getDataRetorno(), 2));
	}



}



