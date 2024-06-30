package com.hotelservice.Book.Hotel.Service;

import com.hotelservice.Book.Hotel.Model.BookedRoom;
import com.hotelservice.Book.Hotel.Repository.BookedRoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookedRoomService {

    @Autowired
    BookedRoomRepo bookedRoomRepo;
    public  List<BookedRoom> getAllBookingsByRoomId(Long id) {

        return new ArrayList<>();
    }
}
