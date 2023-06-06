package africa.semicolon.regcrow.dtos.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class TransactionInitiationRequest {
    private Long sellerId;
    private Long buyerId;
    private String description;
    private BigDecimal amount;
    private String paymentMethod;
}
