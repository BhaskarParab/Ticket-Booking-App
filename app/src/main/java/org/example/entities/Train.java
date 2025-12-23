package org.example.entities;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Train {

  @JsonProperty("train_id")
  private String trainId;

  @JsonProperty("train_no")
  private String trainNo;

  private List<List<Integer>> seats;

  @JsonProperty("station_times")
  private Map<String, String> stationTimes;

  private List<String> stations;

  public Train() {}

  public String getTrainId() {
    return trainId;
  }

  public String getTrainNo() {
    return trainNo;
  }

  public List<List<Integer>> getSeats() {
    return seats;
  }

  public Map<String, String> getStationTimes() {
    return stationTimes;
  }

  public List<String> getStations() {
    return stations;
  }

  public void setTrainId(String trainId) {
    this.trainId = trainId;
  }

  public void setTrainNo(String trainNo) {
    this.trainNo = trainNo;
  }

  public void setSeats(List<List<Integer>> seats) {
    this.seats = seats;
  }

  public void setStationTimes(Map<String, String> stationTimes) {
    this.stationTimes = stationTimes;
  }

  public void setStations(List<String> stations) {
    this.stations = stations;
  }
}
