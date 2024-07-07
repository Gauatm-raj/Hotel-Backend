package com.hotelservice.Book.Hotel.Service;

import com.hotelservice.Book.Hotel.Exception.InvalidBookingRequestException;
import com.hotelservice.Book.Hotel.Model.BookedRoom;
import com.hotelservice.Book.Hotel.Model.Room;
import com.hotelservice.Book.Hotel.Repository.BookedRoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookedRoomService {

    @Autowired
    BookedRoomRepo bookedRoomRepo;

    @Autowired
    RoomService roomService;
    public  List<BookedRoom> getAllBookingsByRoomId(Long id) {

        return bookedRoomRepo.findByRoomId(id);
    }

    public void cancelBooking(Long bookingId) {

        bookedRoomRepo.deleteById(bookingId);
    }

    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-In Date Must come before Check-Out Date");
        }
        Room room= roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings= room.getBookings();
        boolean roomIsAvailable= roomIsAvailable(bookingRequest,existingBookings);
         if(roomIsAvailable){
             room.addBooking(bookingRequest);
             bookedRoomRepo.save(bookingRequest);
         }else{
             throw new InvalidBookingRequestException("Sorry This room is not available for the selected date");
         }
         return bookingRequest.getBookingConfirmationCode();
    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream().noneMatch( existingBooking->
                bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                ||(bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())

                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                ||(bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }

    public List<BookedRoom> getAllBookings() {
        return bookedRoomRepo.findAll();
    }

    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookedRoomRepo.findByBookingConfirmationCode(confirmationCode);
    }
}
