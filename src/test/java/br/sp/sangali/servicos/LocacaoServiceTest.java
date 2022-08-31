package br.sp.sangali.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import br.sp.sangali.builders.FilmeBuilder;
import br.sp.sangali.builders.LocacaoBuilder;
import br.sp.sangali.builders.UsuarioBuilder;
import br.sp.sangali.daos.LocacaoDAO;
import br.sp.sangali.entidades.Filme;
import br.sp.sangali.entidades.Locacao;
import br.sp.sangali.entidades.Usuario;
import br.sp.sangali.exceptions.FilmeSemEstoqueException;
import br.sp.sangali.exceptions.LocadoraException;
import br.sp.sangali.utils.DataUtils;

import br.sp.sangali.matchers.MatchersProprios;
import br.sp.sangali.runners.ParallelRunner;

@RunWith(ParallelRunner.class)
public class LocacaoServiceTest {

	@InjectMocks @Spy
	private LocacaoService locacaoService;

	@Mock
	private SPCService spcService;
	@Mock
	private EmailService emailService;
	@Mock
	private LocacaoDAO dao;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		System.out.println("iniciando 2...");
	}
	
	@After
	public void tearDown(){
		System.out.println("finaliando 2...");
	}

	@Test
	public void alugarFilmeTest() throws Exception {

		List<Filme> filme = Arrays.asList(new Filme("Top Gun", 5, 10.1));
		Usuario usuario1 = new Usuario("Rodrigo");
		Locacao locacao = locacaoService.alugarFilme(usuario1, filme);

		// Verificar o valor da locação
		Assert.assertEquals(locacao.getValor(), 10.1, 0.01);
		// Verificar se a data de locação a data atual
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		// Verificar se a data de retorno é data atual + 1
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}

	// Esperando uma excessão, se nao ocorrer o teste falha, não consegue verificar
	// qual excessão foi lançada
	@Test(expected = FilmeSemEstoqueException.class)
	public void locacaoSemEstoqueTest() throws Exception {

		List<Filme> filme = Arrays.asList(new Filme("Top Gun", 0, 10.1));
		Usuario usuario1 = new Usuario("Rodrigo");
		locacaoService.alugarFilme(usuario1, filme);
	}

	@Test
	public void itShouldDescontThirdRentMovie() throws Exception {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		Locacao locacao = new Locacao();

		filme.add(new Filme("Top Gun", 5, 15.0));
		filme.add(new Filme("Matrix", 1, 15.0));
		filme.add(new Filme("Tropa de Elite", 1, 20.0));

		locacao = locacaoService.alugarFilme(usuario1, filme);

		Assert.assertEquals(locacao.getValor().doubleValue(), 45.0, 0.01);
	}

	@Test
	public void itShouldDescontFourthRentMovie() throws Exception {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		Locacao locacao = new Locacao();

		filme.add(new Filme("Top Gun", 5, 15.0));
		filme.add(new Filme("Matrix", 1, 15.0));
		filme.add(new Filme("Tropa de Elite", 1, 20.0));
		filme.add(new Filme("O Vento levou", 1, 10.0));

		locacao = locacaoService.alugarFilme(usuario1, filme);

		Assert.assertEquals(locacao.getValor().doubleValue(), 50.0, 0.01);
	}

	@Test
	public void itShouldDescontFifthRentMovie() throws Exception {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		Locacao locacao = new Locacao();

		filme.add(new Filme("Top Gun", 5, 15.0));
		filme.add(new Filme("Matrix", 1, 15.0));
		filme.add(new Filme("Tropa de Elite", 1, 20.0));
		filme.add(new Filme("O Vento levou", 1, 10.0));
		filme.add(new Filme("Homem Aranha", 1, 20.0));

		locacao = locacaoService.alugarFilme(usuario1, filme);

		Assert.assertEquals(locacao.getValor().doubleValue(), 55.0, 0.01);
	}

	@Test
	public void shouldDescontSixthRentMovie() throws Exception {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		Locacao locacao = new Locacao();

		filme.add(new Filme("Top Gun", 5, 15.0));
		filme.add(new Filme("Matrix", 1, 15.0));
		filme.add(new Filme("Tropa de Elite", 1, 20.0));
		filme.add(new Filme("O Vento levou", 1, 10.0));
		filme.add(new Filme("Homem Aranha", 1, 20.0));
		filme.add(new Filme("Capitão Philips", 1, 10.0));

		locacao = locacaoService.alugarFilme(usuario1, filme);

		Assert.assertEquals(locacao.getValor().doubleValue(), 55.0, 0.01);
	}

	@Test
	public void shouldDeliveryMonday() throws Exception {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		Locacao locacao = new Locacao();
		
		Mockito.doReturn(DataUtils.obterData(29, 4, 2017)).when(locacaoService).obterData();

		filme.add(new Filme("Top Gun", 5, 15.0));
		filme.add(new Filme("Matrix", 1, 15.0));
		filme.add(new Filme("Tropa de Elite", 1, 20.0));
		filme.add(new Filme("O Vento levou", 1, 10.0));
		filme.add(new Filme("Homem Aranha", 1, 20.0));
		filme.add(new Filme("Capitão Philips", 1, 10.0));

		locacao = locacaoService.alugarFilme(usuario1, filme);

		// Verify is Monday
		assertThat(locacao.getDataRetorno(), MatchersProprios.caiNumaSegunda());
			
	}

	@Test
	public void notShouldUserNegatived() throws Exception {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");

		Mockito.when(spcService.possuiNegativado(Mockito.any(Usuario.class))).thenReturn(true);

		filme.add(new Filme("Top Gun", 5, 15.0));

		try {
			locacaoService.alugarFilme(usuario1, filme);
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario Negativado"));
		}

		Mockito.verify(spcService).possuiNegativado(usuario1);
	}

	@Test
	public void deveTratarErroNoSPC() throws Exception {

		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		// Mock do servico do SPC e lança erro
		Mockito.when(spcService.possuiNegativado(usuario)).thenThrow(new Exception("Falha castratrófica"));

		// Verificação das mensagens, sempre antes da execução do serviço
		exception.expect(Exception.class);
		exception.expectMessage("Problemas com SPC, tente novamente");

		// No aluguel de filmes é utilizado o serviço do SPC
		locacaoService.alugarFilme(usuario, filmes);

	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {

		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Outro atrasado").agora();

		List<Locacao> locacoes = Arrays.asList(LocacaoBuilder.umaLocacao().atrasado().comUsuario(usuario).agora(),
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

	@Test
	public void deveProrrogarUmaAlocacao() {

		// cenario
		Locacao locacao = LocacaoBuilder.umaLocacao().agora();

		// acao
		locacaoService.prorrogarLocacao(locacao, 3);

		// verificacao
		// Captura os dados passado dentro do salvar
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();

		error.checkThat(locacaoRetornada.getValor(), is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), MatchersProprios.ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(3));

	}

	@Test
	public void deveAlugarFilme() throws Exception {
		// Cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().comValor(5.0).agora());
		
		Mockito.doReturn(DataUtils.obterData(28, 4, 2017)).when(locacaoService).obterData();

		// action
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

		// verify
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));

	}
	

	@Test
	public void deveCalcularValorLocacao() throws Exception {
	
		//cenario
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//acao
		Class<LocacaoService> clazz = LocacaoService.class;
		Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
		metodo.setAccessible(true);
		Double valor = (Double) metodo.invoke(locacaoService, filmes);
		
		//verificação
		Assert.assertThat(valor, is(15.0));
	}

}
