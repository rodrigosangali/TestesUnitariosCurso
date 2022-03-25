package br.sp.sangali.service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.sp.sangali.entidades.Filme;
import br.sp.sangali.entidades.Locacao;
import br.sp.sangali.entidades.Usuario;
import br.sp.sangali.servicos.LocacaoService;
import br.sp.sangali.utils.DataUtils;


public class LocacaoServiceTest {


	@Test
	public void alugarFilmeTest() throws Exception {
		
		Filme filme = new Filme("Top Gun", 5, 10.1);
		Usuario usuario1 = new Usuario("Rodrigo");
		LocacaoService locacaoService = new LocacaoService();
		Locacao locacao = locacaoService.alugarFilme(usuario1, filme);
		
		// Verificar o valor da locação
		Assert.assertEquals(locacao.getValor(), 10.1, 0.01);
		// Verificar se a data de locação a data atual
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		// Verificar se a data de retorno é data atual + 1
		Assert.assertTrue(DataUtils.isMesmaData(
				locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}

	// Esperando uma excessão, se nao ocorrer o teste falha, não consegue verificar qual excessão foi lançada 
	@Test(expected=Exception.class)
	public void locacaoSemEstoqueTest() throws Exception {
		
		Filme filme = new Filme("Top Gun", 0, 10.1);
		Usuario usuario1 = new Usuario("Rodrigo");
		LocacaoService locacaoService = new LocacaoService();
		
		locacaoService.alugarFilme(usuario1, filme);
	}
	
	
	// Aqui não deixo o Junit controlar a exception
	@Test
	public void locacaoSemEstoqueTest2() {
		
		Filme filme = new Filme("Top Gun", 0, 10.1);
		Usuario usuario1 = new Usuario("Rodrigo");
		LocacaoService locacaoService = new LocacaoService();
		
		try {
			locacaoService.alugarFilme(usuario1, filme);
			Assert.fail("deveria lançar uma excessão");
		
		} catch (Exception e) {
			Assert.assertEquals(e.getMessage(), "Filme sem estoque");
		}
		
	}		


	// Alternativa ruim apresentada no curso
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void locacaoSemEstoqueTest3() throws Exception {
		
		Filme filme = new Filme("Top Gun", 0, 10.1);
		Usuario usuario1 = new Usuario("Rodrigo");
		LocacaoService locacaoService = new LocacaoService();
		
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");
		
		locacaoService.alugarFilme(usuario1, filme);
		
	}	
	
}
