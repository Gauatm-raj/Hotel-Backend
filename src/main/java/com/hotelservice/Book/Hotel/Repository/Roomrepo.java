package com.hotelservice.Book.Hotel.Repository;

import com.hotelservice.Book.Hotel.Model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Roomrepo extends JpaRepository<Room,Long> {
    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomType();
}
