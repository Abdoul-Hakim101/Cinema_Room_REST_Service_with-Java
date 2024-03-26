package cinema;

import cinema.Exception.ApiRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class CinemaRoomController {
    public final static List<Seat> seats = new ArrayList<>();

    static {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Seat seat = new Seat(row + 1, col + 1);
                seat.setPurchased(false);
                seat.setPrice();
                seats.add(seat);
            }
        }
    }

    @GetMapping("/seats")
    public CinemaRoom getSeats() {
        List<Seat> availableSeats = seats.stream()
                .filter(seat -> !seat.Purchased())
                .collect(Collectors.toList());
        System.out.println(availableSeats.size());
        availableSeats.forEach(seat -> System.out.println(seat.UUID()));
        return new CinemaRoom(9, 9, availableSeats);
    }

    @PostMapping("/purchase")
    public ResponseEntity<Object> purchaseSeat(@RequestBody Seat seat) {

        if (seat.getRow() < 1 || seat.getRow() > 9 || seat.getColumn() < 1 || seat.getColumn() > 9) {
            throw new ApiRequestException("The number of a row or a column is out of bounds!");
        }

        for (Seat value : seats) {
            if (value.getRow() == seat.getRow() && value.getColumn() == seat.getColumn()) {
                if (value.Purchased()) {
                    throw new ApiRequestException("The ticket has been already purchased!");
                }

                value.setPurchased(true);

                Map<String, Object> responseBody = new LinkedHashMap<>();
                responseBody.put("token", value.UUID());
                responseBody.put("ticket", value);
                System.out.println(value.UUID());
                return ResponseEntity.ok().body(responseBody);
            }
        }

        throw new ApiRequestException("The requested seat is not available!");
    }

    @PostMapping("/return")
    public ResponseEntity<Object> refundSeat(@RequestBody TokenDTO tokenDTO) {
        UUID token = tokenDTO.token();
        for (Seat value : seats) {
            if (value.UUID().equals(token)) {
                if (!value.Purchased()) {
                    throw new ApiRequestException("Wrong token!");
                }
                value.setPurchased(false);
                Map<String, Object> responseBody = new LinkedHashMap<>();
                responseBody.put("ticket", value);
                return ResponseEntity.ok().body(responseBody);
            }
        }
        throw new ApiRequestException("Wrong token!");
    }

    @GetMapping("/stats")
    public Statistics stat(@RequestParam(required = false) String password) {

        String passw = "super_secret";

        if (!passw.equals(password)){
            throw new ApiRequestException("The password is wrong!");
        }
        int totalIncome = 0;
        int totalAvailable = 0;
        int totalPurchased = 0;

        for (Seat seat : seats) {
            if (seat.Purchased()) {
                totalIncome += seat.getPrice();
                totalPurchased += 1;
            } else {
                totalAvailable += 1;
            }
        }
        return new Statistics(totalIncome, totalAvailable, totalPurchased);
    }

}

record TokenDTO(UUID token) {
}

record Statistics(int income, int available, int purchased) {
}