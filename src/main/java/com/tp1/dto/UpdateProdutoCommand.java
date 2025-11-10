package com.tp1.dto;

import java.math.BigDecimal;

public record UpdateProdutoCommand(Long id, String nome, BigDecimal preco, int quantidade) {
}
