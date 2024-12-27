package ticket.booking.entities;

import java.util.Date;

public class Ticket {
    private String ticketId;
    private String customerId;
    private String customerName;
    private String source;
    private String destination;
    private int price;
    private Train train;
    private Date dateOfTravel;

    // Constructor
    public Ticket(String ticketId, String customerId, String customerName, String source, String destination, int price, Train train, Date dateOfTravel) {
        this.ticketId = ticketId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.source = source;
        this.destination = destination;
        this.price = price;
        this.train = train;
        this.dateOfTravel = dateOfTravel;
    }

    // Getters and Setters
    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Date getDateOfTravel() {
        return dateOfTravel;
    }

    public void setDateOfTravel(Date dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }
}