package org.example.homework.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.homework.entity.Ticket;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseTicket<T>{
    private boolean isSuccess;
    private String message;
    private HttpStatus status;
    private T payload;
    LocalDateTime timestamp;
}
