package br.sp.sangali.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import br.sp.sangali.builders.FilmeBuilder;
import br.sp.sangali.builders.LocacaoBuilder;
import br.sp.sangali.builders.UsuarioBuilder;
import br.sp.sangali.daos.LocacaoDAO;
import br.sp.sangali.entidades.Filme;
import br.sp.sangali.entidades.Locacao;
import br.sp.sangali.entidades.Usuario;
import br.sp.sangali.exceptions.FilmeSemEstoqueException;
import br.sp.sangali.exceptions.LocadoraException;
import br.sp.sangali.servicos.EmailService;
import br.sp.sangali.servicos.LocacaoService;
import br.sp.sangali.servicos.SPCService;
import br.sp.sangali.utils.DataUtils;


public class LocacaoServiceTest {

	private LocacaoService locacaoService;
	private SPCService spcService;
	private EmailService emailService;
	private LocacaoDAO dao;
	
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		locacaoService = new LocacaoService();
		dao = Mockito.mock(LocacaoDAO.class);
		spcService = Mockito.mock(SPCService.class);
		emailService = Mockito.mock(EmailService.class);

		locacaoService.setLocacaoDAO(dao);
		locacaoService.setSPCService(spcService);
		locacaoService.setEmailService(emailService);
	}

	@Test
	public void alugarFilmeTest() throws Exception {
		
		List<Filme> filme = Arrays.asList(new Filme("Top Gun", 5, 10.1));
		Usuario usuario1 = new Usuario("Rodrigo");
		Locacao locacao = locacaoService.alugarFilme(usuario1, filme, new Date());
		
		// Verificar o valor da loca��o
		Assert.assertEquals(locacao.getValor(), 10.1, 0.01);
		// Verificar se a data de loca��o a data atual
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		// Verificar se a data de retorno � data atual + 1
		Assert.assertTrue(DataUtils.isMesmaData(
				locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}

	// Esperando uma excess�o, se nao ocorrer o teste falha, n�o consegue verificar qual excess�o foi lan�ada 
	@Test(expected=FilmeSemEstoqueException.class)
	public void locacaoSemEstoqueTest() throws Exception {
		
		List<Filme> filme = Arrays.asList(new Filme("Top Gun", 0, 10.1));
		Usuario usuario1 = new Usuario("Rodrigo");
		locacaoService.alugarFilme(usuario1, filme, new Date());
	}
	
	@Test
	public void itShouldDescontThirdRentMovie() throws LocadoraException, FilmeSemEstoqueException {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
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
	public void shouldDescontSixthRentMovie() throws LocadoraException, FilmeSemEstoqueException {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		Locacao locacao = new Locacao();
		
		filme.add(new Filme("Top Gun", 5, 15.0));
		filme.add(new Filme("Matrix", 1, 15.0));
		filme.add(new Filme("Tropa de Elite", 1, 20.0));
		filme.add(new Filme("O Vento levou", 1, 10.0));
		filme.add(new Filme("Homem Aranha", 1, 20.0));
		filme.add(new Filme("Capit�o Philips", 1, 10.0));

		
		locacao = locacaoService.alugarFilme(usuario1, filme, new Date());
		
		Assert.assertEquals(locacao.getValor().doubleValue(), 55.0, 0.01);
	}

	@Test
	public void shouldDeliveryMonday() throws LocadoraException, FilmeSemEstoqueException, ParseException {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		Locacao locacao = new Locacao();
		
		filme.add(new Filme("Top Gun", 5, 15.0));
		filme.add(new Filme("Matrix", 1, 15.0));
		filme.add(new Filme("Tropa de Elite", 1, 20.0));
		filme.add(new Filme("O Vento levou", 1, 10.0));
		filme.add(new Filme("Homem Aranha", 1, 20.0));
		filme.add(new Filme("Capit�o Philips", 1, 10.0));

		Date dataLocacao = DataUtils.obterData(02, 04, 2022);
		
		locacao = locacaoService.alugarFilme(usuario1, filme, dataLocacao);
		
		Assert.assertTrue(DataUtils.verificarDiaSemana(locacao.getDataRetorno(), 2));
	}

	@Test
	public void notShouldUserNegatived() throws LocadoraException, FilmeSemEstoqueException {
		
		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		
		Mockito.when(spcService.possuiNegativado(usuario1)).thenReturn(true);

		exception.expect(LocadoraException.class);
		exception.expectMessage("Usuario Negativado");
		
		filme.add(new Filme("Top Gun", 5, 15.0));
		locacaoService.alugarFilme(usuario1, filme, new Date());
		
		Mockito.verify(spcService).possuiNegativado(usuario1);
		

	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		List<Locacao> locacoes = Arrays.asList(
				LocacaoBuilder.umaLocacao()
					.comUsuario(usuario)
					.comDataRetorno(DataUtils.obterDataComDiferencaDias(-2))
					.agora());
		
		Mockito.when(dao.obterLocacoesPendetes()).thenReturn(locacoes);
		
		locacaoService.notificarAtrasasos();
		
		Mockito.verify(emailService).notificarAtraso(usuario);
		
		
	}
}



