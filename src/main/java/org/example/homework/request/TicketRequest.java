package org.example.homework.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.homework.entity.TicketStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TicketRequest {
    private String passengerName;
    private String travelDate;
    private String sourceStation;
    private String destinationStation;
    private Double price;
    private boolean paymentStatus;
    private TicketStatus ticketStatus;
    private String seatNumber;
}


