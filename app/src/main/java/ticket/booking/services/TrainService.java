package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;
import ticket.booking.entities.TrainList;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
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
        File trainFile = new File(TRAIN_PATH);
        TrainList trainListWrapper = objectMapper.readValue(trainFile, TrainList.class);
        if (trainListWrapper != null && trainListWrapper.getTrains() != null) {
            trainList = trainListWrapper.getTrains();
        } else {
            trainList = new ArrayList<>();
        }
    }

    private Boolean validTrain(Train train, String source, String dest) {
        List<String> stationOrder = train.getStations();
        
        // Case-insensitive search for stations
        int sourceIndex = -1;
        int destIndex = -1;
        
        for (int i = 0; i < stationOrder.size(); i++) {
            if (stationOrder.get(i).equalsIgnoreCase(source)) {
                sourceIndex = i;
            }
            if (stationOrder.get(i).equalsIgnoreCase(dest)) {
                destIndex = i;
            }
        }

        return sourceIndex != -1 && destIndex != -1 && sourceIndex < destIndex;
    }

    public void saveTrainChanges(Train updatedTrain) throws IOException {
        // Update the train in our list
        boolean found = false;
        for (int i = 0; i < trainList.size(); i++) {
            if (trainList.get(i).getTrainID().equals(updatedTrain.getTrainID())) {
                trainList.set(i, new Train(updatedTrain));  // Use copy constructor
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.out.println("Warning: Train not found in the list");
            return;
        }
        
        // Save the updated list back to file
        TrainList wrapper = new TrainList();
        wrapper.setTrains(trainList);
        objectMapper.writeValue(new File(TRAIN_PATH), wrapper);
    }
}
