package com.tp1.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public record ProdutoDto(Long id, String nome, BigDecimal preco, int quantidadeEmEstoque) {
	@Override
	public String toString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		String precoFormatado = preco == null ? "-" : nf.format(preco);
		return String.format("Produto[id=%s, nome=%s, preco=%s, estoque=%d]",
				id == null ? "-" : id.toString(), nome, precoFormatado, quantidadeEmEstoque);
	}
}
