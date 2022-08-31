package br.sp.sangali.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.module.ModuleDescriptor.Builder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

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

import br.sp.sangali.matchers.MatchersProprios;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LocacaoService.class })
public class LocacaoServiceTest_PowerMock {

	@InjectMocks
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
		locacaoService = PowerMockito.spy(locacaoService);
		
	}



	@Test
	public void shouldDeliveryMonday() throws Exception {

		List<Filme> filme = new ArrayList<Filme>();
		Usuario usuario1 = new Usuario("Rodrigo");
		Locacao locacao = new Locacao();

		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 4, 2017));

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
	public void deveAlugarFilme() throws Exception {
		// Cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().comValor(5.0).agora());

		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 4, 2017));

		// action
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

		// verify
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));

	}
	
	@Test
	public void deveAlugarFilmeSemCalcularValor() throws Exception {
		
		//cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//Mockar um metodo privado (serviço, metodo e entrada do metodo) 
		PowerMockito.doReturn(1.0).when(locacaoService, "calcularValorLocacao", filmes);
		
		//ação
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
			
		//verificação
		Assert.assertThat(locacao.getValor(), is(1.0));
		
	}
	
}
