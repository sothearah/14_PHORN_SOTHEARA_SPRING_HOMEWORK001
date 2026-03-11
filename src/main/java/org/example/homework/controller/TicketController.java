package org.example.homework.controller;

import org.example.homework.entity.NewTicket;
import org.example.homework.entity.Ticket;
import org.example.homework.entity.TicketStatus;
import org.example.homework.entity.UpdatatePaymentStatus;
import org.example.homework.request.ApiResponseTicket;
import org.example.homework.request.TicketRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    private final ArrayList<Ticket> TICKETS = new ArrayList<>();
    private final AtomicLong ATOMIC_LONG = new AtomicLong(1);

    public TicketController() {
        TICKETS.add(new Ticket(ATOMIC_LONG.getAndIncrement(), "Sotheara Phorn", LocalDate.now().format(formatter), "Siem Reap", "Phnom Penh", 18.75, true, TicketStatus.BOOKED, "A11"));
        TICKETS.add(new Ticket(ATOMIC_LONG.getAndIncrement(), "Kanika Chham", LocalDate.now().format(formatter), "Kandal", "Phnom Penh", 10.75, true, TicketStatus.CANCELLED, "B21"));
        TICKETS.add(new Ticket(ATOMIC_LONG.getAndIncrement(), "Sreynit Hiem", LocalDate.now().format(formatter), "Siem Reap", "Phnom Penh", 13.75, true, TicketStatus.BOOKED, "C31"));
        TICKETS.add(new Ticket(ATOMIC_LONG.getAndIncrement(), "Soknika Hort", LocalDate.now().format(formatter), "Siem Reap", "Phnom Penh", 12.75, true, TicketStatus.BOOKED, "D41"));
    }

//    ArrayList<ApiResponseTicket> apiResponseTickets = new ArrayList<>(
//            List.of(new ApiResponseTicket(true, "Tickets retrieved successfully", HttpStatus.OK, TICKETS, LocalDateTime.now())
//    );


    //    DONE
    @GetMapping
    public ResponseEntity<?> getAllTicket() {
        ApiResponseTicket<ArrayList<Ticket>> responseTicket = new ApiResponseTicket<>(true, "Ticket fetched successfully", HttpStatus.OK, TICKETS, LocalDateTime.now());
        return ResponseEntity.ok(responseTicket);
    }

    //    DONE
    @GetMapping("/{search}")
    public ResponseEntity<?> searchTicketByPassengerName(@RequestParam String passengerName) {
        for (Ticket ticket : TICKETS) {
            if (ticket.getPassengerName().equals(passengerName)) {
                ApiResponseTicket<Ticket> responseTicket = new ApiResponseTicket<>(true, "Ticket fetched successfully", HttpStatus.OK, ticket, LocalDateTime.now());
                return ResponseEntity.ok(responseTicket);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    //    DONE
    @GetMapping("/search/{ticket-id}")
    public ResponseEntity<?> searchTicketById(@PathVariable("ticket-id") Long ticketId) {
        for (Ticket ticket : TICKETS) {
            if (ticket.getTicketId().equals(ticketId)) {
                ApiResponseTicket<Ticket> responseTicket = new ApiResponseTicket<>(true, "Ticket fetched successfully", HttpStatus.OK, ticket, LocalDateTime.now());
                return ResponseEntity.ok(responseTicket);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    //DONE
    @GetMapping("/search/{filter}")
    public ResponseEntity<?> filterTicket(@RequestParam TicketStatus ticketStatus, @RequestParam String travelDate) {
        ArrayList<Ticket> foundTicket = new ArrayList<>();
        ApiResponseTicket<ArrayList<Ticket>> apiResponseTicket;
        for (Ticket ticket : TICKETS) {
            if (ticket.getTicketStatus().equals(ticketStatus) && ticket.getTravelDate().equals(travelDate)) {
                foundTicket.add(ticket);
            } else {
                apiResponseTicket = new ApiResponseTicket<>(false, "Ticket fetched false", HttpStatus.OK, foundTicket, LocalDateTime.now());
            }
        }
        apiResponseTicket = new ApiResponseTicket<>(true, "Ticket fetched successfully", HttpStatus.OK, foundTicket, LocalDateTime.now());
        return new ResponseEntity<>(apiResponseTicket, HttpStatus.OK);
    }

    // DONE
    @PutMapping("/{ticket-id}")
    public ResponseEntity<?> updateById(@PathVariable("ticket-id") Long ticketId, @RequestBody TicketRequest ticketRequest) {
        for (Ticket ticket : TICKETS) {
            if (ticket.getTicketId().equals(ticketId)) {
                ticket.setPassengerName(ticketRequest.getPassengerName());
                ticket.setSourceStation(ticketRequest.getSourceStation());
                ticket.setDestinationStation(ticketRequest.getDestinationStation());
                ticket.setPrice(ticketRequest.getPrice());
                ticket.setTicketStatus(ticketRequest.getTicketStatus());
                ticket.setSeatNumber(ticketRequest.getSeatNumber());
            }
            ApiResponseTicket<Ticket> responseTicket = new ApiResponseTicket<>(true, "Ticket updated successfully", HttpStatus.OK, ticket, LocalDateTime.now());
            return ResponseEntity.ok(responseTicket);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    //    DONE
    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody TicketRequest ticketRequest) {
        Ticket ticket = new Ticket(
                ATOMIC_LONG.getAndIncrement(),
                ticketRequest.getPassengerName(),
                ticketRequest.getTravelDate(),
                ticketRequest.getSourceStation(),
                ticketRequest.getDestinationStation(),
                ticketRequest.getPrice(),
                ticketRequest.isPaymentStatus(),
                ticketRequest.getTicketStatus(),
                ticketRequest.getSeatNumber()
        );
        TICKETS.add(ticket);
        return ResponseEntity.ok(TICKETS);
    }

    //    DONE
    @DeleteMapping("/{delete-ticket}")
    public void deleteTicketById(@RequestParam("/{delete-ticket}") Long deleteTicket) {
        TICKETS.removeIf(rm -> rm.getTicketId().equals(deleteTicket));
    }

    //DONE
    @PutMapping("/bulk")
    public ResponseEntity<?> updateMultipleId(@RequestBody UpdatatePaymentStatus status) {
        ArrayList<Ticket> update = new ArrayList<>();
        for (Long id : status.getTicketId()) {
            for (Ticket ticket : TICKETS) {
                if (ticket.getTicketId().equals(id)) {
                    ticket.setPaymentStatus(status.isPaymentStatus());
                    update.add(ticket);
                }
            }
        }
        ApiResponseTicket<ArrayList<Ticket>> responseTicket = new ApiResponseTicket<>(true, "success", HttpStatus.OK, update, LocalDateTime.now());
        return new ResponseEntity<>(responseTicket, HttpStatus.OK);
    }

    @PostMapping("/{bulk}")
    public ResponseEntity<?> createMultiTicket(@RequestBody ArrayList<NewTicket> createTicket) {
        ArrayList<Ticket> addTicket = new ArrayList<>();
//        ApiResponseTicket<ArrayList<Ticket>> responseTicket = new ApiResponseTicket<>();

        for (NewTicket ticketRequest : createTicket) {
            Ticket ticket = new Ticket(
                    ATOMIC_LONG.getAndIncrement(),
                    ticketRequest.getPassengerName(),
                    ticketRequest.getTravelDate(),
                    ticketRequest.getSourceStation(),
                    ticketRequest.getDestinationStation(),
                    ticketRequest.getPrice(),
                    ticketRequest.isPaymentStatus(),
                    ticketRequest.getTicketStatus(),
                    ticketRequest.getSeatNumber()
            );

            TICKETS.add(ticket);
            addTicket.add(ticket);
            ApiResponseTicket<ArrayList<Ticket>> responseTicket = new ApiResponseTicket<>(true, "success", HttpStatus.OK, addTicket, LocalDateTime.now());
            return new ResponseEntity<>(responseTicket, HttpStatus.OK);

        }
        return null;
    }
}
