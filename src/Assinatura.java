import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

public class Assinatura {
    private  BigDecimal mensalidade;
    private  LocalDateTime begin;
    private  Optional<LocalDateTime> end;
    private  Cliente cliente;



    public Assinatura(BigDecimal mensalidade, LocalDateTime begin, Cliente cliente) {
        this.mensalidade = mensalidade;
        this.begin = begin;
        this.cliente = cliente;
        this.end = Optional.empty();

    }

    public Assinatura(BigDecimal mensalidade, LocalDateTime begin, LocalDateTime end, Cliente cliente) {
        this.mensalidade = mensalidade;
        this.begin = begin;
        this.end = Optional.of(end);
        this.cliente = cliente;
    }

    public BigDecimal getMensalidade() {
        return mensalidade;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public Optional<LocalDateTime> getEnd() {
        return end;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public long getMonthsActive() {
        LocalDateTime endDate = end.orElse(LocalDateTime.now());
        return Period.between(begin.toLocalDate(), endDate.toLocalDate()).toTotalMonths();
    }

    public BigDecimal getTotalPaid() {
        long monthsActive = getMonthsActive();
        return mensalidade.multiply(BigDecimal.valueOf(monthsActive));
    }
}