package carsharing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Customer {
    private int id;
    private String name;
    private int rentedCarId;
}
