import com.tp1.dto.CreateProdutoCommand;
import com.tp1.dto.ProdutoDto;
import com.tp1.dto.UpdateProdutoCommand;
import com.tp1.repository.InMemoryProdutoRepository;
import com.tp1.repository.ProdutoRepository;
import com.tp1.service.ProdutoService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.text.NumberFormat;
import java.util.Locale;



void main() {

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
                    printTable(java.util.List.of(p));
                }
                case "2" -> {
                    List<ProdutoDto> todos = service.listarTodosProdutos();
                    printTable(todos);
                }
                case "3" -> {
                    System.out.print("Id: ");
                    Long id = Long.valueOf(sc.nextLine());
                    ProdutoDto found = service.buscarProdutoPorId(id);
                    printTable(java.util.List.of(found));
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
                    ProdutoDto updated = service.atualizarProduto(ucmd);
                    printTable(java.util.List.of(updated));
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

private static void printTable(List<ProdutoDto> produtos) {
    if (produtos == null || produtos.isEmpty()) {
        System.out.println("(nenhum produto)");
        return;
    }

    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    int idWidth = "ID".length();
    int nomeWidth = "NOME".length();
    int precoWidth = "PREÇO".length();
    int estWidth = "ESTOQUE".length();

    String[][] rows = new String[produtos.size()][4];
    for (int i = 0; i < produtos.size(); i++) {
        ProdutoDto p = produtos.get(i);
        String id = p.id() == null ? "-" : p.id().toString();
        String nome = p.nome() == null ? "-" : p.nome();
        String preco = p.preco() == null ? "-" : nf.format(p.preco());
        String est = String.valueOf(p.quantidadeEmEstoque());

        rows[i][0] = id;
        rows[i][1] = nome;
        rows[i][2] = preco;
        rows[i][3] = est;

        idWidth = Math.max(idWidth, id.length());
        nomeWidth = Math.max(nomeWidth, nome.length());
        precoWidth = Math.max(precoWidth, preco.length());
        estWidth = Math.max(estWidth, est.length());
    }

    String format = "| %-%ds | %-%ds | %-%ds | %-%ds |%n".replace("%-%d", "%-");
    String fmt = String.format("| %%-%ds | %%-%ds | %%-%ds | %%-%ds |%n", idWidth, nomeWidth, precoWidth, estWidth);


    System.out.printf(fmt, "ID", "NOME", "PREÇO", "ESTOQUE");
    for (String[] r : rows) {
        System.out.printf(fmt, r[0], r[1], r[2], r[3]);
    }
}
