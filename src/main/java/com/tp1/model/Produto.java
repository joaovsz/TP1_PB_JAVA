package com.tp1.model;

import java.math.BigDecimal;

public record Produto(Long id, String nome, BigDecimal preco, int quantidadeEmEstoque) {
}
