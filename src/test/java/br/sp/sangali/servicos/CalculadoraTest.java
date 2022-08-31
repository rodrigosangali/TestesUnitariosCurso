package br.sp.sangali.servicos;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.sp.sangali.exceptions.NaoPodeDividirPorZeroException;
import br.sp.sangali.runners.ParallelRunner;

@RunWith(ParallelRunner.class)
public class CalculadoraTest {
	
	private Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
		System.out.println("iniciando...");
	}
	
	
	@After
	public void tearDown() {
		System.out.println("finalizando...");
	}
	
	@Test
	public void deveSomarDoisValores() {
		
		// cenario
		int a = 5;
		int b = 3;
		
		//ação
		int resultado = calc.somar(a, b);
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		
		// cenario
		int a = 5;
		int b = 3;
		
		//ação
		int resultado = calc.subtrair(a, b);
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		
		// cenario
		int a = 5;
		int b = 3;
		
		//ação
		int resultado = calc.divide(a, b);
	}
	
	@Test
	public void deveMultiplicarDoisValores() {
		
		// cenario
		int a = 5;
		int b = 3;
		
		//ação
		int resultado = calc.multiplica(a, b);
	}



}
