package com.hotelservice.Book.Hotel.Repository;

import com.hotelservice.Book.Hotel.Model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookedRoomRepo extends JpaRepository<BookedRoom,Long> {
    List<BookedRoom> findByRoomId(Long id);

   Optional<BookedRoom>  findByBookingConfirmationCode(String confirmationCode);
}
