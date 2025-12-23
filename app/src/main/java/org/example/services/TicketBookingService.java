package org.example.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.entities.Train;
import org.example.entities.User;
import org.example.util.UserServiceUtil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

public class TicketBookingService {

  private User user;

  private List<User> userList;

  private static final ObjectMapper objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

  private static final String USERS_PATH = "../localDb/users.json";

  public TicketBookingService(User user1) throws IOException{
    this.user = user1;
 
    loadUsers();
  }

  public TicketBookingService() throws IOException{ 
    loadUsers();
  }

  public void loadUsers() throws IOException{
    userList = objectMapper.readValue(new File(USERS_PATH), new TypeReference<List<User>>() {});
  }

  public Boolean loginUser(){
    Optional<User> foundUser = userList.stream().filter(user1 -> {
      return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
    }).findFirst();
    return foundUser.isPresent();
  }

  public Boolean signUp(User user1){
  try{
      userList.add(user1);
      saveUserListToFile();
      return Boolean.TRUE;
    }catch (IOException ex){
      return Boolean.FALSE;
    }
  }

  private void saveUserListToFile() throws IOException{
    File usersFile = new File(USERS_PATH);
    objectMapper.writeValue(usersFile, userList);
  }

  //json --> Object (User) --> Deserialise
  //Object (User) --> json --> Serialize

  public void fetchBooking(){
    Optional<User> userFetched = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        if(userFetched.isPresent()){
            userFetched.get().printTickets();
        }
  }

  public Boolean cancelBooking(String ticketId){
    try{
      Optional<User> foundUser = userList.stream().filter(user1 -> {
      return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
    }).findFirst();

      if(foundUser.isEmpty()){
        return Boolean.FALSE;
      }

      User loggedInUser = foundUser.get();

      boolean checkTicketRemoved = loggedInUser.getTicketsBooked().remove(ticketId);

      if(!checkTicketRemoved){
        return Boolean.FALSE;
      }

      saveUserListToFile();
      return Boolean.TRUE;
    } catch (IOException ex){
    return Boolean.FALSE;
    }
  }

  public List<Train> getTrains(String source, String destination){
    try{
      TrainService trainService = new TrainService();
      return trainService.searchTrains(source, destination);
    }catch(IOException ex){
      return new ArrayList<>();
    }
  }

  public List<Train> getAllTrains() {
    try {
        TrainService trainService = new TrainService();
        return trainService.getAllTrains();
    } catch (IOException ex) {
        return new ArrayList<>();
    }
}

  public List<List<Integer>> fetchSeats(Train train){
            return train.getSeats();
    }

     public Boolean bookTrainSeat(Train train, int row, int seat) {
        try{
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                    return true; // Booking successful
                } else {
                    return false; // Seat is already booked
                }
            } else {
                return false; // Invalid row or seat index
            }
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }
}
