package org.example.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  private String name;

  private String password;

  @JsonProperty("hashed_password")
  private String hashedPassword;

  @JsonProperty("user_id")
  private String userId;
  
  @JsonProperty("tickets_booked")
  private List<Ticket> ticketsBooked;

  public User(String name, String password, String hashedPassword, List<Ticket> ticketsBooked, String userId){
    this.name = name;
    this.password = password;
    this.hashedPassword = hashedPassword;
    this.ticketsBooked = ticketsBooked;
    this.userId = userId;
  }

  public User(){}

  public String getName(){
    return name;
  }

  public String getPassword(){
    return password;
  }

  public String getHashedPassword(){
    return hashedPassword;
  }

  public String getUserId(){
    return userId;
  }

  public List<Ticket> getTicketsBooked(){
    return ticketsBooked;
  }

  public void printTickets(){
    if(ticketsBooked.size() <= 0){
      System.out.println("You have not booked any tickets");
    }
    for(int i = 0; i < ticketsBooked.size(); i++){
      System.out.println(ticketsBooked.get(i).getTicketInfo());
    }
  }

  public void setName(String name){
    this.name = name;
  }

  public void setPassword(String password){
    this.password = password;
  }

  public void setHashedPassword(String hashedPassword){
    this.hashedPassword = hashedPassword;
  }

  public void setUserId(String userId){
    this.userId = userId;
  }

  public void setTicketsBooked(List<Ticket> ticketsBooked){
    this.ticketsBooked = ticketsBooked;
  }
}
