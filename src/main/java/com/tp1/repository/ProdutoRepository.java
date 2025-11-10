package com.tp1.repository;

import com.tp1.model.Produto;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository {
    Produto salvar(Produto produto);
    Optional<Produto> buscarPorId(Long id);
    Optional<Produto> buscarPorNome(String nome);
    List<Produto> buscarTodos();
    void deletarPorId(Long id);
}
