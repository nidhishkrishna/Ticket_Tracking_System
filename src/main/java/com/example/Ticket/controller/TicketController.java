package com.example.Ticket.controller;


import com.example.Ticket.model.Ticket;
import com.example.Ticket.model.User;
import com.example.Ticket.service.TicketService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    @Autowired
    TicketService ticketService;


    @GetMapping("/getusers")
    public ResponseEntity<List<User>> getUserData()
    {
        List<User> users=ticketService.getAllUsers();

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(users);


    }

    @GetMapping("/getusers/{userid}")
    public ResponseEntity<?> getUserById(@PathVariable("userid") Long userid) {
        User users = ticketService.getUserByID(userid);
        if(users==null)
        {
            String errorMessage = "User Not found for the id :" +userid;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }


    @GetMapping("/gettickets/{ticketid}")
    public ResponseEntity<?> getTicketById(@PathVariable("ticketid") Long ticketid) {
        Ticket ticket = ticketService.getTicketByID(ticketid);
        if(ticket==null)
        {
            String errorMessage = "ticket Not found for the id :" +ticketid;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ticket);
    }

    @GetMapping("/gettickets")
    public ResponseEntity<List<Ticket>> getTicketData()
    {
        List<Ticket> tickets = ticketService.getAllTickets();
        if(tickets==null)
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(tickets);
    }


    @PostMapping("/createticket")
    public ResponseEntity<String> createTicket(@RequestBody Ticket ticket) {
        return ticketService.createTicket(ticket);
    }

    @PostMapping("/createuser")
    public ResponseEntity<String> createUser(@RequestBody User user)
    {
        return ticketService.createUser(user);
    }

    @PutMapping("/edit-status/{ticketid}")
    public ResponseEntity<?> editStatus(@PathVariable("ticketid") Long ticketid, @RequestBody Ticket ticket)
    {
        return ticketService.editStatus(ticketid,ticket);
    }


}
