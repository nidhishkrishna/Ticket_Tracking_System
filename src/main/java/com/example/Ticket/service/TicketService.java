package com.example.Ticket.service;
import com.example.Ticket.model.Status;
import com.example.Ticket.model.Ticket;
import com.example.Ticket.model.User;
import com.example.Ticket.repository.TicketRepository;
import com.example.Ticket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {


    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    UserRepository userRepository;


    public ResponseEntity<String> createTicket(Ticket ticket) {


            if (ticket.getCreatedBy() == null || ticket.getCreatedBy().getId() == null) {
                return new ResponseEntity<>("Admin Should not be null!!", HttpStatus.BAD_REQUEST);
            }
            if (ticket.getDescription().trim() == null || ticket.getDescription().trim().isEmpty()) {
                return new ResponseEntity<>("Description Should not be null", HttpStatus.BAD_REQUEST);
            }
            if (ticket.getAssignedUser() == null || ticket.getAssignedUser().getId() == null) {
                return new ResponseEntity<>("Assignee Should not be null!", HttpStatus.BAD_REQUEST);
            }

            User admin = userRepository.findById(ticket.getCreatedBy().getId()).orElse(null);
            if (admin == null) {
                return new ResponseEntity<>("Admin Not Found", HttpStatus.BAD_REQUEST);
            }
            if (admin.getIs_admin().equals("no")) {
                return new ResponseEntity<>("User is not a Admin!!", HttpStatus.BAD_REQUEST);
            }

            User assignee = userRepository.findById(ticket.getAssignedUser().getId()).orElse(null);

            if (assignee == null) {
                return new ResponseEntity<>("Assignee Not Found", HttpStatus.BAD_REQUEST);

            }
            if (admin.getId().equals(assignee.getId())) {
                return new ResponseEntity<>("Admin and Assignee should not be same!", HttpStatus.BAD_REQUEST);
            }
            if (assignee.getIs_admin().equals(admin.getIs_admin())) {
                return new ResponseEntity<>("Assignee Should not be admin", HttpStatus.BAD_REQUEST);
            }
            ticket.setStatus(Status.OPEN);
            ticket.setCreatedBy(admin);
            ticket.setAssignedUser(assignee);
            ticketRepository.save(ticket);
            return new ResponseEntity<>("Ticket Created Successfully!!", HttpStatus.OK);


    }


    public String createTicket(Long adminId, String description)
    {
        return "Assignee ID should not be null! ";

    }



    public List<User> getAllUsers() {

        return  userRepository.findAll();
    }


    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public User getUserByID(long userid) {


       return  userRepository.findById(userid).orElse(null);

    }

    public Ticket getTicketByID(Long ticketid) {
        return ticketRepository.findById(ticketid).orElse(null);
    }

    public ResponseEntity<String> createUser(User user) {

        try {
            if (user.getIs_admin().trim().isEmpty() || user.getIs_admin().trim() == null) {
                return new ResponseEntity<>("isAdmin should not be null!!", HttpStatus.BAD_REQUEST);
            }

            if (user.getName().trim().isEmpty() || user.getIs_admin().trim() == null) {
                return new ResponseEntity<>("Name should not e null!!", HttpStatus.BAD_REQUEST);
            }

            userRepository.save(user);
            return new ResponseEntity<>("User Created Successfully", HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    public ResponseEntity<?> editStatus(Long ticketId,Ticket ticket) {
        try {

            Ticket ticket1 = ticketRepository.findById(ticketId).orElse(null);

            if (ticket1 == null) {
                return new ResponseEntity<>("Ticket ID is not found :" + ticketId, HttpStatus.BAD_REQUEST);
            }
            System.out.println("New Status is : " + ticket.getStatus());

            ticket1.setStatus(ticket.getStatus());
            ticketRepository.save(ticket1);
            return ResponseEntity.status(HttpStatus.OK).body("Ticket Status Updated!!");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }




    }
}
