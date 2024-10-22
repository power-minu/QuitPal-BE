package minu.quitPal.entity;

import jakarta.persistence.*;
import lombok.*;
import minu.quitPal.entity.user.User;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String place;
    private int amount;
    private LocalDate purchaseDate;
    @Setter
    private boolean checked = false;
    @Setter
    private boolean expired = false;

    @Builder
    public Transaction(String place, int amount, LocalDate purchaseDate) {
        this.place = place;
        this.amount = amount;
        this.purchaseDate = purchaseDate;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void assignUser(User user) {
        this.user = user;
        user.addTransaction(this);
    }

}
