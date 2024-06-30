package com.hotelservice.Book.Hotel.Controller;

import com.hotelservice.Book.Hotel.Model.Room;
import com.hotelservice.Book.Hotel.Response.RoomResponse;
import com.hotelservice.Book.Hotel.Service.RoomService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

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
            return ResponseEntity.ok(roomResponses);
        }
    }

    private RoomResponse getRoomResponse(Room room) {
    }

}
