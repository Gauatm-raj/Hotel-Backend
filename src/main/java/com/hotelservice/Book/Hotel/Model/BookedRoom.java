package com.hotelservice.Book.Hotel.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class BookedRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private LocalDate checkInDate;


    private LocalDate checkOutDate;

    private String guestFullName;

    private String guestEmail;

    @Column(name = "adults")
    private int noOfAdults;

    @Column(name = "childrens")
    private int noOfChildrens;

    @Column(name = "totalguest")
    private int totalGuests;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "roomid")
    private Room room;

    @Column(name = "confirmationcode")
    private String bookingConfirmationCode;

    public void calculateTotalGuests(){
        this.totalGuests= this.noOfAdults + this.noOfChildrens;
    }

    public void setNoOfAdults(int noOfAdults) {
        this.noOfAdults = noOfAdults;
        calculateTotalGuests();
    }

    public void setNoOfChildrens(int noOfChildrens) {
        this.noOfChildrens = noOfChildrens;
        calculateTotalGuests();
    }

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }

}
