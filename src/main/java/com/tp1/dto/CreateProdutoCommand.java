package com.tp1.dto;

import java.math.BigDecimal;

public record CreateProdutoCommand(String nome, BigDecimal preco, int quantidade) {
}
