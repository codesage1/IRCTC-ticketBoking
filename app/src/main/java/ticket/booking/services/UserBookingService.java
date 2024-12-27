package ticket.booking.services;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.User;

import java.io.File;

import static java.util.spi.ToolProvider.findFirst;

public class UserBookingService {
    private User user;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String USER_PATH = "app/src/main/java/ticket/booking/localDb/users.json";
    private List<User> userList;

    public UserBookingService(User user) throws IOException {
        this.user = user;
        File users = new File(USER_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public boolean loginUser() {
        Optional<User> foundUser = userList.stream()
                .filter(user -> user.getName().equals(this.user.getName()) &&
                        UserServiceUtil.checkPassword(user.getHashedPassword(), this.user.getHashedPassword()))
                .findFirst();

        return foundUser.isPresent();
    }
    public boolean signUPUser(){
        try {
            userList.add(this.user);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            throw new RuntimeException(e);
            return Boolean.FALSE;
        }
    }
}
