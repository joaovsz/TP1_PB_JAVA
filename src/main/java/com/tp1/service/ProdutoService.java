package com.tp1.service;

import com.tp1.dto.CreateProdutoCommand;
import com.tp1.dto.ProdutoDto;
import com.tp1.dto.UpdateProdutoCommand;
import com.tp1.exception.RecursoNaoEncontradoException;
import com.tp1.exception.ValidacaoException;
import com.tp1.model.CategoriaProduto;
import com.tp1.model.Produto;
import com.tp1.repository.ProdutoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProdutoService {

    private static final int MIN_NOME_LENGTH = 3;
    private static final int MAX_NOME_LENGTH = 100;

    private final ProdutoRepository repository;
    private long proximoId = 1L;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public ProdutoDto criarProduto(CreateProdutoCommand cmd) {
        validar(cmd.nome(), cmd.preco(), cmd.quantidade());

        if (repository.buscarPorNome(cmd.nome()).isPresent()) {
            throw new ValidacaoException("Produto com este nome já existe.");
        }

        Produto novo = new Produto(proximoId++, cmd.nome().trim(), cmd.preco(), cmd.quantidade());
        Produto salvo = repository.salvar(novo);
        return mapToDto(salvo);
    }

    public ProdutoDto atualizarProduto(UpdateProdutoCommand cmd) {
        buscarProdutoPorId(cmd.id());

        validar(cmd.nome(), cmd.preco(), cmd.quantidade());

        repository.buscarPorNome(cmd.nome()).ifPresent(p -> {
            if (!p.id().equals(cmd.id())) {
                throw new ValidacaoException("Outro produto já utiliza este nome.");
            }
        });

        Produto atualizado = new Produto(cmd.id(), cmd.nome().trim(), cmd.preco(), cmd.quantidade());
        Produto salvo = repository.salvar(atualizado);
        return mapToDto(salvo);
    }

    public void deletarProduto(Long id) {
        if (repository.buscarPorId(id).isEmpty()) {
            throw new RecursoNaoEncontradoException("Produto com id " + id + " não encontrado para deleção.");
        }
        repository.deletarPorId(id);
    }

    public ProdutoDto buscarProdutoPorId(Long id) {
        Produto p = repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto com id " + id + " não encontrado."));
        return mapToDto(p);
    }

    public List<ProdutoDto> listarTodosProdutos() {
        return repository.buscarTodos().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private void validar(String nome, BigDecimal preco, int quantidade) {
        if (nome == null || nome.trim().length() < MIN_NOME_LENGTH || nome.trim().length() > MAX_NOME_LENGTH) {
            throw new ValidacaoException("Nome deve ter entre " + MIN_NOME_LENGTH + " e " + MAX_NOME_LENGTH + " caracteres.");
        }
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacaoException("Preço deve ser positivo.");
        }
        if (quantidade < 0) {
            throw new ValidacaoException("Quantidade em estoque não pode ser negativa.");
        }
    }

    public double calcularImpostoPorCategoria(CategoriaProduto categoria) {
        return switch (categoria) {
            case ELETRONICO -> 0.18;
            case LIVRO -> 0.05;
            case ROUPA -> 0.12;
        };
    }

    private ProdutoDto mapToDto(Produto p) {
        return new ProdutoDto(p.id(), p.nome(), p.preco(), p.quantidadeEmEstoque());
    }
}
