package com.tp1.repository;

import com.tp1.model.Produto;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryProdutoRepository implements ProdutoRepository {
    private final Map<Long, Produto> database = new ConcurrentHashMap<>();

    @Override
    public Produto salvar(Produto produto) {
        database.put(produto.id(), produto);
        return produto;
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Optional<Produto> buscarPorNome(String nome) {
        return database.values().stream()
            .filter(p -> p.nome().equals(nome))
            .findFirst();
    }

    @Override
    public List<Produto> buscarTodos() {
        return new ArrayList<>(database.values());
    }

    @Override
    public void deletarPorId(Long id) {
        database.remove(id);
    }
}
