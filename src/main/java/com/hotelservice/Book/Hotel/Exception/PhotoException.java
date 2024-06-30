package com.hotelservice.Book.Hotel.Exception;

public class PhotoException extends RuntimeException {
    public PhotoException(String errorInGettingPhoto) {
        super(errorInGettingPhoto);
    }
}
