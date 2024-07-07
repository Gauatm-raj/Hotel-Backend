package com.hotelservice.Book.Hotel.Controller;

import com.hotelservice.Book.Hotel.Exception.InvalidBookingRequestException;
import com.hotelservice.Book.Hotel.Exception.ResorceNotFoundException;
import com.hotelservice.Book.Hotel.Model.BookedRoom;
import com.hotelservice.Book.Hotel.Model.Room;
import com.hotelservice.Book.Hotel.Response.BookingResponse;
import com.hotelservice.Book.Hotel.Response.RoomResponse;
import com.hotelservice.Book.Hotel.Service.BookedRoomService;
import com.hotelservice.Book.Hotel.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookedRoomController {

    @Autowired
    BookedRoomService bookedRoomService;

    @Autowired
    RoomService roomService;

    @GetMapping("all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        List<BookedRoom> bookedRooms= bookedRoomService.getAllBookings();
        List<BookingResponse> bookingResponses=new ArrayList<>();
        for(BookedRoom room: bookedRooms){
            BookingResponse bookingResponse= getBookingResponse(room);
            bookingResponses.add(bookingResponse);
        }
        return  ResponseEntity.ok(bookingResponses);
    }

    private BookingResponse getBookingResponse(BookedRoom room) {
        Room theRoom=roomService.getRoomById(room.getRoom().getId()).get();
        RoomResponse room1 = new RoomResponse(theRoom.getId(),theRoom.getRoomType(),theRoom.getRoomPrice());
        return new BookingResponse(room.getId(),room.getCheckInDate(),room.getCheckOutDate(),
                room.getGuestFullName(),room.getGuestEmail(),room.getNoOfAdults(),room.getNoOfChildrens(),
                room.getTotalGuests(),room.getRoom(),room.getBookingConfirmationCode());
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        try{
          BookedRoom booking=bookedRoomService.findByBookingConfirmationCode(confirmationCode);
          BookingResponse bookingResponse=getBookingResponse(booking);
          return ResponseEntity.ok(bookingResponse);
        }catch(ResorceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId, @RequestBody BookedRoom bookingRequest){
        try{
          String confirmationCode= bookedRoomService.saveBooking(roomId,bookingRequest);
          return ResponseEntity.ok("Room booked Successfully: Confirmation Code = " + confirmationCode);
        }catch(InvalidBookingRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId){
        bookedRoomService.cancelBooking(bookingId);
    }

}
