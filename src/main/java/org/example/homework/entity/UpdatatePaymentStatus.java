package org.example.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatatePaymentStatus {
    private ArrayList<Long> ticketId;
    private boolean paymentStatus;
}
