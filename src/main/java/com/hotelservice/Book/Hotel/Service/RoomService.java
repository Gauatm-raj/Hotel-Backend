package com.hotelservice.Book.Hotel.Service;

import com.hotelservice.Book.Hotel.Exception.IntervalServerException;
import com.hotelservice.Book.Hotel.Exception.ResorceNotFoundException;
import com.hotelservice.Book.Hotel.Model.Room;
import com.hotelservice.Book.Hotel.Repository.Roomrepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private Roomrepo roomrepo;
    public Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomprice) throws IOException, SQLException {
        Room room= new Room();
        room.setRoomType(roomType);
        System.out.println(room.getRoomType());
        room.setRoomPrice(roomprice);
        if(!photo.isEmpty()){
            byte [] photoByte=photo.getBytes();
            Blob photoBlob= new SerialBlob(photoByte);
            room.setPhoto(photoBlob);
        }

        return roomrepo.save(room);
    }

    public List<String> getAllRoomTypes() {
        return roomrepo.findDistinctRoomType();
    }

    public List<Room> getAllRooms() {
        return roomrepo.findAll();
    }

    public byte[] getRoomPhotoByRoomId(Long id) throws SQLException {
        Optional<Room> room= roomrepo.findById(id);

        if(room.isEmpty()){
            throw new ResorceNotFoundException("Sorry ");
        }
        Blob photoBlob= room.get().getPhoto();
        if(photoBlob!=null){
            return photoBlob.getBytes(1,(int)photoBlob.length());
        }
        return null;
    }

    public void deleteRoom(Long roomId) {
        Optional<Room> room= roomrepo.findById(roomId);
        if(room.isPresent()){
            roomrepo.deleteById(roomId);
        }
    }

    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoByte) {
        Room room= roomrepo.findById(roomId).orElseThrow(()-> new ResorceNotFoundException("Room not found"));
        if(roomType!=null){
          room.setRoomType(roomType);
        }
        if(roomPrice!=null) room.setRoomPrice(roomPrice);
        if(photoByte!=null && photoByte.length>0){
            try{
               room.setPhoto(new SerialBlob(photoByte));
            }catch(SQLException e){
                throw new IntervalServerException("Error updating photo room ");
            }
        }
        return roomrepo.save(room);
    }

    public Optional<Room> getRoomById(Long roomId) {
        Optional<Room> room= roomrepo.findById(roomId);
        return room;
    }
}
