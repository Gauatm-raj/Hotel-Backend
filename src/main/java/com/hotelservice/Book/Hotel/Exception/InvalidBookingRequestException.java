package com.hotelservice.Book.Hotel.Exception;

public class InvalidBookingRequestException extends RuntimeException{
    public InvalidBookingRequestException(String e){
        super(e);

    }
}
