package ticket.booking.services;
import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;
import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBookingService {
    private User user;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String USER_PATH = "app/src/main/java/ticket/booking/localDb/users.json";
    private List<User> userList;
    private Boolean loginStatus = Boolean.FALSE;
    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUser();
        if(!loginUser()) {
            throw new IOException("Login failed");
        }
    }
    public UserBookingService() throws IOException{
        loadUser();
    }

    private List<User> loadUser() throws IOException {
        File users = new File(USER_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
        return userList;
    }
    public boolean loginUser() {
        Optional<User> foundUser = userList.stream()
                .filter((user) -> user.getName().equalsIgnoreCase(this.user.getName()) &&
                        UserServiceUtil.checkPassword(this.user.getPassword(), user.getHashedPassword()))
                .findFirst();
        if(foundUser.isPresent()){
            loginStatus = Boolean.TRUE;
            this.user = foundUser.get();
        }
        return foundUser.isPresent();
    }
    public Boolean getLoginStatus(){
        return loginStatus;
    }
    private void saveUserListToFile() throws IOException{
        File usersfile = new File(USER_PATH);
        objectMapper.writeValue(usersfile, userList);
    }

    public boolean signUPUser(User user){
        try {
            userList.add(user);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void fetchBooking(){
        user.printTicketBooked();
    }

    public Boolean cancelBooking(String ticketId) throws IOException {
        List<Ticket> tickets = this.user.getTicketBooked();
        Ticket ticketToCancel = null;
        
        // Find the ticket to cancel
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equals(ticketId)) {
                ticketToCancel = ticket;
                break;
            }
        }
        
        if (ticketToCancel == null) {
            return false;
        }
        
        // Remove the ticket from user's bookings
        tickets.remove(ticketToCancel);
        
        // Update the user data in the file
        saveUserListToFile();
        
        return true;
    }

    public User getUser() {
        return user;
    }

    public List<User> getUserList() {
        return userList;
    }

    public List<Train> getTrains(String source, String dest) {
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source,dest);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public List<List<Integer>> fetchSeats(Train trainSelectedForBooking) {
        return trainSelectedForBooking.getSeats();
    }

    public Boolean bookTrainSeat(Train trainSelectedForBooking, int row, int col) {
        if(trainSelectedForBooking.getSeats().get(row).get(col) == 0){
            trainSelectedForBooking.getSeats().get(row).set(col, 1);
            try {
                // Save the updated train data
                TrainService trainService = new TrainService();
                trainService.saveTrainChanges(trainSelectedForBooking);
                return Boolean.TRUE;
            } catch (IOException e) {
                System.out.println("Failed to save seat booking: " + e.getMessage());
                return Boolean.FALSE;
            }
        } else {
            return Boolean.FALSE;
        }
    }

    public void createTicket(Train train, int row, int col) throws IOException {
        Ticket ticket = new Ticket(
            UUID.randomUUID().toString(),
            user.getUserid(),
            user.getName(),
            train.getStations().get(0),  // source station
            train.getStations().get(train.getStations().size() - 1),  // destination station
            100,  // default price
            train,
            new Date()
        );
        
        user.getTicketBooked().add(ticket);
        saveUserListToFile();  // Save the updated user data
    }
}
