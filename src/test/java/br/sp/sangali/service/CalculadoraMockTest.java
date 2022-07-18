package br.sp.sangali.service;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import br.sp.sangali.servicos.Calculadora;

public class CalculadoraMockTest {
	
	
	@Test
	public void test() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
		Mockito.when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);

			
		Assert.assertEquals(5, calc.somar(1, 100000));
		System.out.println(argCapt.getAllValues());
	}

}
