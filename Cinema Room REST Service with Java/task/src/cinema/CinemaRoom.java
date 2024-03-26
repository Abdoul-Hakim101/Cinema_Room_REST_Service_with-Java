package cinema;

import java.util.List;

public record CinemaRoom(int rows, int columns, List<Seat> seats) {
}