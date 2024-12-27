package ticket.booking.entities;

import java.util.List;

public class User {
    private String name;
    private String hashedPassword;
    private List<Ticket> ticketBooked;
    private String userid;

    public User(String name, String hashedPassword, List<Ticket> ticketBooked, String userid) {
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.ticketBooked = ticketBooked;
        this.userid = userid;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public List<Ticket> getTicketBooked() {
        return ticketBooked;
    }

    public void setTicketBooked(List<Ticket> ticketBooked) {
        this.ticketBooked = ticketBooked;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
