package br.sp.sangali.daos;

import java.util.List;

import br.sp.sangali.entidades.Locacao;

public interface LocacaoDAO {

	public void salvar(Locacao locacao);

	public List<Locacao> obterLocacoesPendetes();
}
