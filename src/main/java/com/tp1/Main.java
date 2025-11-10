package com.tp1;

import com.tp1.dto.CreateProdutoCommand;
import com.tp1.dto.ProdutoDto;
import com.tp1.dto.UpdateProdutoCommand;
import com.tp1.repository.InMemoryProdutoRepository;
import com.tp1.repository.ProdutoRepository;
import com.tp1.service.ProdutoService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProdutoRepository repo = new InMemoryProdutoRepository();
        ProdutoService service = new ProdutoService(repo);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("--- Sistema de Produtos (CLI) ---");
            System.out.println("1) Criar produto");
            System.out.println("2) Listar produtos");
            System.out.println("3) Buscar por id");
            System.out.println("4) Atualizar produto");
            System.out.println("5) Deletar produto");
            System.out.println("0) Sair");
            System.out.print("Escolha: ");
            var opt = sc.nextLine().trim();
            try {
                switch (opt) {
                    case "1" -> {
                        System.out.print("Nome: ");
                        String nome = sc.nextLine();
                        System.out.print("Preco: ");
                        BigDecimal preco = new BigDecimal(sc.nextLine());
                        System.out.print("Quantidade: ");
                        int q = Integer.parseInt(sc.nextLine());
                        CreateProdutoCommand cmd = new CreateProdutoCommand(nome, preco, q);
                        ProdutoDto p = service.criarProduto(cmd);
                        System.out.println("Criado: " + p);
                    }
                    case "2" -> {
                        List<ProdutoDto> todos = service.listarTodosProdutos();
                        todos.forEach(System.out::println);
                    }
                    case "3" -> {
                        System.out.print("Id: ");
                        Long id = Long.valueOf(sc.nextLine());
                        System.out.println(service.buscarProdutoPorId(id));
                    }
                    case "4" -> {
                        System.out.print("Id: ");
                        Long idU = Long.valueOf(sc.nextLine());
                        System.out.print("Nome: ");
                        String nomeU = sc.nextLine();
                        System.out.print("Preco: ");
                        BigDecimal precoU = new BigDecimal(sc.nextLine());
                        System.out.print("Quantidade: ");
                        int qU = Integer.parseInt(sc.nextLine());
                        UpdateProdutoCommand ucmd = new UpdateProdutoCommand(idU, nomeU, precoU, qU);
                        System.out.println(service.atualizarProduto(ucmd));
                    }
                    case "5" -> {
                        System.out.print("Id: ");
                        Long id = Long.valueOf(sc.nextLine());
                        service.deletarProduto(id);
                        System.out.println("Removido id=" + id);
                    }
                    case "0" -> {
                        System.out.println("Saindo...");
                        sc.close();
                        return;
                    }
                    default -> System.out.println("Opcao invalida");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
            System.out.println();
        }
    }
}
