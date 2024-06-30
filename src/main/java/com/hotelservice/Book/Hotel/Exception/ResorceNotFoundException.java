package com.hotelservice.Book.Hotel.Exception;

public class ResorceNotFoundException extends RuntimeException {
    public ResorceNotFoundException(String sorry) {
        super(sorry);
    }
}
