package africa.semicolon.regcrow.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class TransactionResponse {
    private Long Id;
    private Long sellerId;
    private Long buyerId;
    private String description;
    private BigDecimal amount;
    private String paymentMethod;
}
