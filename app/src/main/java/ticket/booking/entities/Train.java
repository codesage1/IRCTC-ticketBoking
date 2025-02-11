package ticket.booking.entities;

import java.sql.Time;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Train {
    private String trainID;
    private String trainNo;
    private List<List<Integer>> seats;
    private Map<String, String> stationTimes;
    private List<String> stations;

    public Train(String trainID, String trainNo, List<List<Integer>> seats, Map<String, String> stationTimes, List<String> stations) {
        this.trainID = trainID;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stationTimes = stationTimes;
        this.stations = stations;
    }

    public Train() {
    }

    // Copy constructor
    public Train(Train other) {
        this.trainID = other.trainID;
        this.trainNo = other.trainNo;
        // Deep copy of seats
        if (other.seats != null) {
            this.seats = new ArrayList<>();
            for (List<Integer> row : other.seats) {
                this.seats.add(new ArrayList<>(row));
            }
        }
        // Deep copy of station times
        if (other.stationTimes != null) {
            this.stationTimes = new HashMap<>(other.stationTimes);
        }
        // Deep copy of stations
        if (other.stations != null) {
            this.stations = new ArrayList<>(other.stations);
        }
    }

    // Getters and Setters
    public String getTrainID() {
        return trainID;
    }

    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public Map<String, String> getStationTimes() {
        return stationTimes;
    }

    public void setStationTimes(Map<String, String> stationTimes) {
        this.stationTimes = stationTimes;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }

    public String getTrainInfo(){
        return String.format("Train ID: %s Train no: %s Seats: %s", trainID, trainNo, seats.size());
    }
}
