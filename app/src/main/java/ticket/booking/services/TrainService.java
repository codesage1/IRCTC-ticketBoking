package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class TrainService {
    List<Train> trainList;
    String TRAIN_PATH = "app/src/main/java/ticket/booking/localDb/trains.json";
    private ObjectMapper objectMapper = new ObjectMapper();
    public TrainService() throws IOException {
        loadTrains();
    }
    public List<Train> searchTrains(String source, String dest) {
        return trainList.stream().filter(train -> validTrain(train,source,dest)).collect(Collectors.toList());
    }

    private void loadTrains() throws IOException {
        File users = new File(TRAIN_PATH);
        trainList = objectMapper.readValue(users, new TypeReference<List<Train>>() {});
    }

    private Boolean validTrain(Train train, String source, String dest) {
        List<String> stationOrder = train.getStations();

        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destIndex = stationOrder.indexOf(dest.toLowerCase());

        return sourceIndex != -1 && destIndex != -1 && sourceIndex < destIndex;
    }
}
