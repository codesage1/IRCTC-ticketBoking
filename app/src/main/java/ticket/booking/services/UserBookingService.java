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


public class UserBookingService {
    private User user;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String USER_PATH = "app/src/main/java/ticket/booking/localDb/users.json";
    private List<User> userList;

    public UserBookingService(User user) throws IOException {
        this.user = user;
        if(loginUser())
            loadUser();
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
                        UserServiceUtil.checkPassword(user.getHashedPassword(), this.user.getHashedPassword()))
                .findFirst();
        return foundUser.isPresent();
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

    public Boolean cancelBooking(String ticketID) throws IOException {
        List<Ticket> tickets= this.user.getTicketBooked();
        tickets.removeIf(ticket -> ticket.getTicketId().equalsIgnoreCase(ticketID));
        saveUserListToFile();
        return Boolean.FALSE;
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
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
}
