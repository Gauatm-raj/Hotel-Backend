package com.hotelservice.Book.Hotel.Repository;

import com.hotelservice.Book.Hotel.Model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookedRoomRepo extends JpaRepository<BookedRoom,Long> {
}
