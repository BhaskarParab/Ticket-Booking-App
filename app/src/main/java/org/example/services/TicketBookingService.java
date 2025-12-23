package org.example.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.entities.Ticket;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.util.UserServiceUtil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

public class TicketBookingService {

  private User user;

  private List<User> userList;

  private static final ObjectMapper objectMapper = new ObjectMapper()
      .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

  private static final String USERS_PATH = "../localDb/users.json";

  public TicketBookingService(User user1) throws IOException {
    this.user = user1;

    loadUsers();
  }

  public TicketBookingService() throws IOException {
    loadUsers();
  }

  public void loadUsers() throws IOException {
    userList = objectMapper.readValue(new File(USERS_PATH), new TypeReference<List<User>>() {
    });
  }

  public Boolean loginUser() {
    Optional<User> foundUser = userList.stream().filter(user1 -> {
      return user1.getName().equals(user.getName())
          && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
    }).findFirst();
    return foundUser.isPresent();
  }

  public Boolean signUp(User user1) {
    try {
      userList.add(user1);
      saveUserListToFile();
      return Boolean.TRUE;
    } catch (IOException ex) {
      return Boolean.FALSE;
    }
  }

  private void saveUserListToFile() throws IOException {
    File usersFile = new File(USERS_PATH);
    objectMapper.writeValue(usersFile, userList);
  }

  // json --> Object (User) --> Deserialise
  // Object (User) --> json --> Serialize

  public void fetchBooking() {
    Optional<User> userFetched = userList.stream()
        .filter(user1 -> user1.getName().equals(user.getName())
                && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword()))
        .findFirst();

    if (userFetched.isEmpty()) {
        System.out.println("No user found or not logged in.");
        return;
    }

    User loggedInUser = userFetched.get();
    List<Ticket> tickets = loggedInUser.getTicketsBooked();

    if (tickets.isEmpty()) {
        System.out.println("You have no booked tickets.");
        return;
    }

    System.out.println("Your Booked Tickets:");
    for (Ticket ticket : tickets) {
        Train train = ticket.getTrain();
        String departureTime = train.getStationTimes().get(ticket.getSource());
        String arrivalTime = train.getStationTimes().get(ticket.getDestination());

        System.out.println("--------------------------------------------------");
        System.out.println("Ticket ID: " + ticket.getTicketId());
        System.out.println("User: " + loggedInUser.getName());
        System.out.println("Train ID: " + train.getTrainId());
        System.out.println("From: " + ticket.getSource() + " | Departure: " + departureTime);
        System.out.println("To: " + ticket.getDestination() + " | Arrival: " + arrivalTime);
        System.out.println("Date of Travel: " + ticket.getDateOfTravel());
        System.out.println("Seat: Row " + ticket.getRow() + " | Column " + ticket.getSeat());
    }
    System.out.println("--------------------------------------------------");
}


  public Boolean cancelBooking(String ticketId) {
  try {
    Optional<User> foundUser = userList.stream()
        .filter(u -> u.getName().equals(user.getName())
            && UserServiceUtil.checkPassword(
                user.getPassword(),
                u.getHashedPassword()))
        .findFirst();

    if (foundUser.isEmpty()) {
      return Boolean.FALSE;
    }

    User loggedInUser = foundUser.get();

    Optional<Ticket> ticketOpt = loggedInUser.getTicketsBooked()
        .stream()
        .filter(t -> t.getTicketId().equals(ticketId))
        .findFirst();

    if (ticketOpt.isEmpty()) {
      return Boolean.FALSE;
    }

    Ticket ticket = ticketOpt.get();

    Train train = ticket.getTrain();

    int row = ticket.getRow();
    int col = ticket.getSeat();

    List<List<Integer>> seats = train.getSeats();
    seats.get(row).set(col, 0);
    train.setSeats(seats);

    TrainService trainService = new TrainService();
    trainService.addTrain(train);

    loggedInUser.getTicketsBooked().remove(ticket);

    saveUserListToFile();

    System.out.println("Ticket cancelled successfully!");
    return Boolean.TRUE;

  } catch (IOException ex) {
    ex.printStackTrace();
    return Boolean.FALSE;
  }
}


  public List<Train> getTrains(String source, String destination) {
    try {
      TrainService trainService = new TrainService();
      return trainService.searchTrains(source, destination);
    } catch (IOException ex) {
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

  public List<List<Integer>> fetchSeats(Train train) {
    return train.getSeats();
  }

  public Boolean bookTrainSeat(
      Train train,
      int row,
      int seat,
      String source,
      String destination,
      String dateOfTravel) {
    try {
      Optional<User> foundUser = userList.stream()
          .filter(u -> u.getName().equals(user.getName())
              && UserServiceUtil.checkPassword(
                  user.getPassword(),
                  u.getHashedPassword()))
          .findFirst();

      if (foundUser.isEmpty()) {
        return Boolean.FALSE;
      }

      User loggedInUser = foundUser.get();

      List<List<Integer>> seats = train.getSeats();

      if (row < 0 || row >= seats.size()
          || seat < 0 || seat >= seats.get(row).size()) {
        return Boolean.FALSE;
      }

      if (seats.get(row).get(seat) == 1) {
        return Boolean.FALSE;
      }

      seats.get(row).set(seat, 1);
      train.setSeats(seats);

      TrainService trainService = new TrainService();
      trainService.addTrain(train);

      String ticketId = java.util.UUID.randomUUID().toString();

      Ticket ticket = new Ticket(
          ticketId,
          loggedInUser.getUserId(),
          loggedInUser.getName(),
          source,
          destination,
          dateOfTravel,
          train,
          row,
          seat
        );

      loggedInUser.getTicketsBooked().add(ticket);

      saveUserListToFile();

      System.out.println("Ticket booked successfully!");
      System.out.println("Ticket ID: " + ticketId);

      return Boolean.TRUE;

    } catch (IOException ex) {
      ex.printStackTrace();
      return Boolean.FALSE;
    }
  }

}
