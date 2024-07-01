package com.hotelservice.Book.Hotel.Exception;

public class IntervalServerException extends RuntimeException {
    public IntervalServerException(String errorUpdatingPhotoRoom) {
        super(errorUpdatingPhotoRoom);
    }
}
