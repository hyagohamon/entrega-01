import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class RunnerAcme {
    public static void main(String[] args) {
        Produto produto1 = new Produto("Música", Paths.get("caminho/para/musica-01.mp3"), BigDecimal.valueOf(5.99));
        Produto produto2 = new Produto("Vídeo", Paths.get("caminho/para/video-01.mp4"), BigDecimal.valueOf(8.99));
        Produto produto3 = new Produto("Imagem", Paths.get("caminho/para/imagem-01.jpg"), BigDecimal.valueOf(1.99));

        Cliente cliente1 = new Cliente("Airton Maciel Costa");
        Cliente cliente2 = new Cliente("Maria de Jesus Pereira");

        Pagamento hoje = new Pagamento(Arrays.asList(produto1, produto2), LocalDate.now(), cliente1);
        Pagamento ontem = new Pagamento(Arrays.asList(produto2, produto3), LocalDate.now().minusDays(1), cliente2);
        Pagamento mesPassado = new Pagamento(Arrays.asList(produto3), LocalDate.now().minusMonths(1), cliente2);

        List<Pagamento> pagamentos = new ArrayList<>();
        pagamentos.add(hoje);
        pagamentos.add(ontem);
        pagamentos.add(mesPassado);

        System.out.println("Ordenando pela data de compra");
        System.out.println("--------------------------");

        pagamentos.stream().sorted(Comparator.comparing(Pagamento::getDataCompra)).forEach(pagamento -> {
            System.out.println("Data de Compra: " + pagamento.getDataCompra());
            System.out.println("Cliente: " + pagamento.getCliente().getNome());
            System.out.println("Produtos: " + pagamento.getProdutos());
        });

        System.out.println("--------------------------");

        BigDecimal somaPagamentoHoje = calcularSomaPagamento(hoje);
        System.out.println("Soma do Pagamento Hoje (Optional): " + somaPagamentoHoje);

        BigDecimal somaPagamentoHojeDouble = calcularSomaPagamento(hoje);
        double somaDouble = somaPagamentoHojeDouble.doubleValue();
        System.out.println("Soma do Pagamento Hoje (Double): " + somaDouble);

        System.out.println("--------------------------");

        System.out.println("Total todos os pagamentos = " + calcularValorTotal(pagamentos));

        System.out.println("quantidade de produtos vendidos = " + quantidadePorProduto(pagamentos));
        System.out.println("--------------------------");


        Map<Cliente, List<Produto>> mapaClienteProdutos = new HashMap<>();
        adicionarProdutos(mapaClienteProdutos, cliente1, produto1, produto2);
        adicionarProdutos(mapaClienteProdutos, cliente2, produto3, produto1);
        adicionarProdutos(mapaClienteProdutos, cliente1, produto2, produto3);

        Cliente clienteQueGastouMais = null;
        BigDecimal maiorValorGasto = BigDecimal.ZERO;

        for (Map.Entry<Cliente, List<Produto>> entry : mapaClienteProdutos.entrySet()) {
            Cliente cliente = entry.getKey();
            BigDecimal valorGasto = calcularValorGasto(entry.getValue());

            if (valorGasto.compareTo(maiorValorGasto) > 0) {
                clienteQueGastouMais = cliente;
                maiorValorGasto = valorGasto;
            }
        }

        System.out.println("Cliente que gastou mais: " + clienteQueGastouMais.getNome());
        System.out.println("Valor gasto: " + maiorValorGasto);
        System.out.println("--------------------------");


        int mesDesejado = 6;
        int anoDesejado = 2023;
        BigDecimal faturamentoTotal = BigDecimal.ZERO;
        for (Pagamento pagamento : pagamentos) {
            LocalDate dataCompra = pagamento.getDataCompra();
            int mesCompra = dataCompra.getMonthValue();
            int anoCompra = dataCompra.getYear();

            if (mesCompra == mesDesejado && anoCompra == anoDesejado) {
                List<Produto> produtos = pagamento.getProdutos();
                for (Produto produto : produtos) {
                    faturamentoTotal = faturamentoTotal.add(produto.getPreco());
                }
            }
        }
        System.out.println("Faturamento total em " + mesDesejado + "/" + anoDesejado + ": " + faturamentoTotal);


        Assinatura assinatura1 = new Assinatura(new BigDecimal("99.98"), LocalDateTime.of(2022, 6, 1,10,0), cliente1);
        Assinatura assinatura2 = new Assinatura(new BigDecimal("99.98"), LocalDateTime.of(2023, 2, 1,15,0), LocalDateTime.of(2023, 3, 30,23,0), cliente2);
        Assinatura assinatura3 = new Assinatura(new BigDecimal("99.98"), LocalDateTime.of(2023, 3, 10,14,0), LocalDateTime.of(2023, 5, 31,23,0), cliente1);

        LocalDateTime dataAtual = LocalDateTime.now();
        long mesesAtivosAssinatura1 = ChronoUnit.MONTHS.between(assinatura1.getBegin(), assinatura1.getEnd().orElse(dataAtual));
        long mesesAtivosAssinatura2 = ChronoUnit.MONTHS.between(assinatura2.getBegin(), assinatura2.getEnd().orElse(dataAtual));
        long mesesAtivosAssinatura3 = ChronoUnit.MONTHS.between(assinatura3.getBegin(), assinatura3.getEnd().orElse(dataAtual));

        System.out.println("Tempo em meses da assinatura ativa: " + mesesAtivosAssinatura1 + " meses");
        System.out.println("Tempo em meses da assinatura2 finalizada: " + mesesAtivosAssinatura2 + " meses");
        System.out.println("Tempo em meses da assinatura3 finalizada: " + mesesAtivosAssinatura3 + " meses");

        BigDecimal valorPagoAssinatura1 = assinatura1.getMensalidade().multiply(new BigDecimal(mesesAtivosAssinatura1));
        System.out.println("valor pago da assinatura1 até o momento: " + valorPagoAssinatura1);

        BigDecimal valorPagoAssinatura2 = assinatura2.getMensalidade().multiply(new BigDecimal(mesesAtivosAssinatura2));
        System.out.println("valor pago da assinatura2 até o momento: " + valorPagoAssinatura2);

        BigDecimal valorPagoAssinatura3 = assinatura3.getMensalidade().multiply(new BigDecimal(mesesAtivosAssinatura3));
        System.out.println("valor pago da assinatura3 até o momento: " + valorPagoAssinatura3);
        System.out.println("--------------------------");

    }


    private static BigDecimal calcularValorGasto(List<Produto> produtos) {
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (Produto produto : produtos) {
            valorTotal = valorTotal.add(produto.getPreco());
        }
        return valorTotal;
    }

    private static void adicionarProdutos(Map<Cliente, List<Produto>> mapaClienteProdutos, Cliente cliente, Produto... produtos) {
        List<Produto> listaProdutos = mapaClienteProdutos.getOrDefault(cliente, new ArrayList<>());
        listaProdutos.addAll(Arrays.asList(produtos));
        mapaClienteProdutos.put(cliente, listaProdutos);
    }

    private static Map<Produto, Integer> quantidadePorProduto(List<Pagamento> pagamentos) {
        Map<Produto, Integer> quantidadePorProduto = new HashMap<>();
        for (Pagamento pagamento : pagamentos) {
            List<Produto> produtos = pagamento.getProdutos();
            for (Produto produto : produtos) {
                quantidadePorProduto.put(produto, quantidadePorProduto.getOrDefault(produto, 0) + 1);
            }
        }
        return quantidadePorProduto;
    }

    private static BigDecimal calcularSomaPagamento(Pagamento pagamento) {
        List<Produto> produtos = pagamento.getProdutos();
        BigDecimal soma = BigDecimal.ZERO;
        for (Produto produto : produtos) {
            soma = soma.add(produto.getPreco());
        }
        return soma;
    }

    private static BigDecimal calcularValorTotal(List<Pagamento> pagamentos) {
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (Pagamento pagamento : pagamentos) {
            List<Produto> produtos = pagamento.getProdutos();
            for (Produto produto : produtos) {
                valorTotal = valorTotal.add(produto.getPreco());
            }
        }
        return valorTotal;
    }


}
