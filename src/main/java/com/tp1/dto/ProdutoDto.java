package com.tp1.dto;

import java.math.BigDecimal;

public record ProdutoDto(Long id, String nome, BigDecimal preco, int quantidadeEmEstoque) {
}
