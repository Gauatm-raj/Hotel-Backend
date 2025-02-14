package com.hotelservice.Book.Hotel.Controller;

import com.hotelservice.Book.Hotel.Exception.PhotoException;
import com.hotelservice.Book.Hotel.Exception.ResorceNotFoundException;
import com.hotelservice.Book.Hotel.Model.BookedRoom;
import com.hotelservice.Book.Hotel.Model.Room;
import com.hotelservice.Book.Hotel.Response.BookingResponse;
import com.hotelservice.Book.Hotel.Response.RoomResponse;
import com.hotelservice.Book.Hotel.Service.BookedRoomService;
import com.hotelservice.Book.Hotel.Service.RoomService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookedRoomService bookedRoomService;

    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponse> addNewRoom(@RequestParam("photo") MultipartFile photo,
                                                   @RequestParam("roomType") String roomType,
                                                   @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {
        Room savedRoom=roomService.addNewRoom(photo,roomType,roomPrice);
        RoomResponse response=new RoomResponse(savedRoom.getId(),savedRoom.getRoomType(),savedRoom.getRoomPrice());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/room-types")
    public List<String> getRoomType(){
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRoom() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();

        for(Room room:rooms){
            byte[] photoBytes=roomService.getRoomPhotoByRoomId(room.getId());
            if(photoBytes!=null && photoBytes.length>0){
                String base64photo= Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(base64photo);
                roomResponses.add(roomResponse);
            }

        }
        return ResponseEntity.ok(roomResponses);
    }

    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId){
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,@RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false)MultipartFile photo) throws IOException, SQLException {

        byte[] photoByte=photo!=null && !photo.isEmpty()? photo.getBytes(): roomService.getRoomPhotoByRoomId(roomId);

        Blob photoBlob=photoByte !=null && photoByte.length>0? new SerialBlob(photoByte):null;
        Room room =roomService.updateRoom(roomId,roomType,roomPrice,photoByte);
        room.setPhoto(photoBlob);
        RoomResponse roomResponse = getRoomResponse(room);
        return  ResponseEntity.ok(roomResponse);

    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId){
           Optional<Room> room= roomService.getRoomById(roomId);
           return room.map(r->{
               RoomResponse roomResponse=getRoomResponse(r);
               return ResponseEntity.ok(Optional.of(roomResponse));
           }).orElseThrow(()-> new ResorceNotFoundException("Room not found"));
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate checkOutDate,
            @RequestParam String roomType) throws SQLException {
        List<Room> availableRooms=roomService.getAvailableRooms(checkInDate,checkOutDate,roomType);
        List<RoomResponse> roomResponses=new ArrayList<>();
        for(Room room:availableRooms){
            byte[] photoByte=roomService.getRoomPhotoByRoomId(room.getId());
            if((photoByte != null) && (photoByte.length > 0)){
                String photoBase64= Base64.encodeBase64String(photoByte);
                RoomResponse roomResponse =getRoomResponse(room);
                roomResponse.setPhoto(photoBase64);
                roomResponses.add(roomResponse);
            }
        }
        if(roomResponses.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(roomResponses);
        }

    }

    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings=getAllBookingByRoomId(room.getId());
        List<BookingResponse> bookingInfo=bookings.stream()
                .map(booking-> new BookingResponse(booking.getId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),booking.getBookingConfirmationCode())).toList();

        byte[] photoByte=null;
        Blob photoBlob=room.getPhoto();
        if(photoBlob!=null){
            try{
               photoByte=photoBlob.getBytes(1,(int)photoBlob.length());
            }catch(SQLException e){
                throw new PhotoException("Error in getting photo");
            }
        }
        return new RoomResponse(room.getId(),room.getRoomType(),room.getRoomPrice(),
                room.isBooked(),photoByte,bookingInfo
                );
    }

    private List<BookedRoom> getAllBookingByRoomId(Long id) {

        return bookedRoomService.getAllBookingsByRoomId(id);
    }

}
