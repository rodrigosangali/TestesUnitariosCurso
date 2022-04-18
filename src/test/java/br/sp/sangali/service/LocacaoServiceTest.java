package br.sp.sangali.service;

import static org.hamcrest.CoreMatchers.is;

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
		filme.add(new Filme("Capitão Philips", 1, 10.0));

		
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
		filme.add(new Filme("Capitão Philips", 1, 10.0));

		Date dataLocacao = DataUtils.obterData(02, 04, 2022);
		
		locacao = locacaoService.alugarFilme(usuario1, filme, dataLocacao);
		
		Assert.assertTrue(DataUtils.verificarDiaSemana(locacao.getDataRetorno(), 2));
	}

	@Test
	public void notShouldUserNegatived() throws FilmeSemEstoqueException {
		
		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		
		Mockito.when(spcService.possuiNegativado(Mockito.any(Usuario.class))).thenReturn(true);

		filme.add(new Filme("Top Gun", 5, 15.0));
		
		try {
			locacaoService.alugarFilme(usuario1, filme, new Date());
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario Negativado"));
		}
		
		Mockito.verify(spcService).possuiNegativado(usuario1);
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Outro atrasado").agora();
		
		
		List<Locacao> locacoes = Arrays.asList(
				LocacaoBuilder.umaLocacao().atrasado().comUsuario(usuario).agora(),
				LocacaoBuilder.umaLocacao().comUsuario(usuario2).agora(),
				LocacaoBuilder.umaLocacao().atrasado().comUsuario(usuario3).agora(),
				LocacaoBuilder.umaLocacao().atrasado().comUsuario(usuario3).agora());
		
		Mockito.when(dao.obterLocacoesPendetes()).thenReturn(locacoes);
		
		locacaoService.notificarAtrasados();
		
		// Verifica se o usuario recebeu e-mail
		Mockito.verify(emailService).notificarAtraso(usuario);
		// Verifica se o usuario 3 recebeu e-mail
		Mockito.verify(emailService, Mockito.times(2)).notificarAtraso(usuario3);
		// Verifica se o usuario 2 não recebeu o e-mail
		Mockito.verify(emailService, Mockito.never()).notificarAtraso(usuario2);
		// Verifica se mais nenhum email foi enviado de modo mais incisivo
		Mockito.verifyNoMoreInteractions(emailService);
		// Outra forma de verificar se nunca foi acionado
		Mockito.verifyZeroInteractions(spcService);
		// Verifica para qualquer usuario
		Mockito.verify(emailService, Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));
		
	}
}



