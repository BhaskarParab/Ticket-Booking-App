package org.example.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {

  private String ticketId;

  private String userId;

  private String source;

  private String destination;

  private String dateOfTravel;

  private Train train;

  private String userName;

  private int row;

  private int seat;

  public Ticket(){};

  public Ticket(String ticketId, String userId, String userName, String source, String destination, String dateOfTravel, Train train, int row, int seat){
    this.ticketId = ticketId;
    this.userId = userId;
    this.userName = userName;
    this.source = source;
    this.destination = destination;
    this.dateOfTravel = dateOfTravel;
    this.train = train;
    this.row = row;
    this.seat = seat;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
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

  public String getDateOfTravel() {
    return dateOfTravel;
  }

  public void setDateOfTravel(String dateOfTravel) {
    this.dateOfTravel = dateOfTravel;
  }

  public Train getTrain() {
    return train;
  }

  public void setTrain(Train train) {
    this.train = train;
  }

  // public String getTicketInfo(){
  //   return String.format("Ticket ID: %s belongs to User: %s from %s to %s on %s", ticketId, userName, source, destination, dateOfTravel);
  // }

  public String getTicketId(){
    return ticketId;
  }

  public void setTicketId(String ticketId){
    this.ticketId = ticketId;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getSeat() {
    return seat;
  }

  public void setSeat(int seat) {
    this.seat = seat;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
}
