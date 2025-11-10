package com.tp1.service;

import com.tp1.exception.RecursoNaoEncontradoException;
import com.tp1.exception.ValidacaoException;
import com.tp1.dto.ProdutoDto;
import com.tp1.repository.InMemoryProdutoRepository;
import com.tp1.repository.ProdutoRepository;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoServiceTest {

    private ProdutoService service;
    private ProdutoRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryProdutoRepository();
        service = new ProdutoService(repository);
    }

    @Test
    void testeCriarProduto_Sucesso() {
        ProdutoDto p = service.criarProduto(new com.tp1.dto.CreateProdutoCommand("Caneta Azul", new BigDecimal("5.00"), 100));
        assertNotNull(p.id());
        assertEquals("Caneta Azul", p.nome());

        ProdutoDto pBuscado = service.buscarProdutoPorId(p.id());
        assertEquals(p, pBuscado);
    }

    @Test
    void testeCriarProduto_Falha_PrecoNegativo() {
        assertThrows(ValidacaoException.class, () -> service.criarProduto(new com.tp1.dto.CreateProdutoCommand("Lapis", BigDecimal.ZERO, 50)));
        assertThrows(ValidacaoException.class, () -> service.criarProduto(new com.tp1.dto.CreateProdutoCommand("Lapis", new BigDecimal("-10.00"), 50)));
    }

    @Test
    void testeCriarProduto_Falha_NomeDuplicado() {
        service.criarProduto(new com.tp1.dto.CreateProdutoCommand("Borracha", new BigDecimal("2.00"), 30));
        assertThrows(ValidacaoException.class, () -> service.criarProduto(new com.tp1.dto.CreateProdutoCommand("Borracha", new BigDecimal("3.00"), 10)));
    }

    @Test
    void testeBuscarProduto_Falha_NaoEncontrado() {
        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarProdutoPorId(999L));
    }

    @Test
    void testeAtualizarProduto_Sucesso() {
        ProdutoDto pOriginal = service.criarProduto(new com.tp1.dto.CreateProdutoCommand("Caderno", new BigDecimal("20.00"), 10));
        ProdutoDto pAtualizado = service.atualizarProduto(new com.tp1.dto.UpdateProdutoCommand(pOriginal.id(), "Caderno Capa Dura", new BigDecimal("25.00"), 5));
        assertEquals(pOriginal.id(), pAtualizado.id());
        assertEquals("Caderno Capa Dura", pAtualizado.nome());
        ProdutoDto pBuscado = service.buscarProdutoPorId(pOriginal.id());
        assertEquals("Caderno Capa Dura", pBuscado.nome());
        assertEquals(0, new BigDecimal("25.00").compareTo(pBuscado.preco()));
    }

    @Test
    void testeDeletarProduto_Sucesso() {
        ProdutoDto p = service.criarProduto(new com.tp1.dto.CreateProdutoCommand("Apontador", new BigDecimal("3.00"), 50));
        Long id = p.id();
        assertNotNull(service.buscarProdutoPorId(id));
        service.deletarProduto(id);
        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarProdutoPorId(id));
    }

    @Property
    void propriedade_CriarEBuscar(
        @ForAll @AlphaChars @StringLength(min = 3, max = 100) String nome,
        @ForAll @DoubleRange(min = 0.01, max = 10000.0) double precoDouble,
        @ForAll @IntRange(min = 0, max = 1000) int quantidade
    ) {
        BigDecimal preco = BigDecimal.valueOf(precoDouble).setScale(2, RoundingMode.HALF_UP);
        ProdutoRepository repoLocal = new InMemoryProdutoRepository();
        ProdutoService serviceLocal = new ProdutoService(repoLocal);

        Assume.that(repoLocal.buscarPorNome(nome).isEmpty());

        ProdutoDto criado = serviceLocal.criarProduto(new com.tp1.dto.CreateProdutoCommand(nome, preco, quantidade));
        ProdutoDto buscado = serviceLocal.buscarProdutoPorId(criado.id());

        assertNotNull(buscado);
        assertEquals(criado, buscado);
        assertEquals(nome, buscado.nome());
        assertEquals(0, preco.compareTo(buscado.preco()));
    }
}
