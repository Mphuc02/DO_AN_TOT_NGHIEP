package dev.payment.model;

import lombok.*;

@AllArgsConstructor
@Getter
public enum InvoiceStatus {
    WAITING_PAY_ONLINE("WAITING_PAY_ONLINE"),
    PAY_ONLINE_FAIL("PAY_ONLINE_FAIL"),
    PAY_ONLINE_SUCCESS("PAY_ONLINE_SUCCESS");

    private final String value;
}