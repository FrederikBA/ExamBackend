package dtos.Car;

import entities.Car;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CarDTO {
    private Integer id;
    private String regNumber;
    private String model;
    private String make;
    private int year;

    public static List<CarDTO> getFromList(List<Car> cars) {
        return cars.stream()
                .map(car -> new CarDTO(car))
                .collect(Collectors.toList());
    }

    public CarDTO(Car car) {
        this.id = car.getId();
        this.regNumber = car.getRegNumber();
        this.model = car.getModel();
        this.make = car.getMake();
        this.year = car.getYear();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarDTO carDTO = (CarDTO) o;
        return year == carDTO.year && Objects.equals(id, carDTO.id) && Objects.equals(regNumber, carDTO.regNumber) && Objects.equals(model, carDTO.model) && Objects.equals(make, carDTO.make);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, regNumber, model, make, year);
    }

    @Override
    public String toString() {
        return "CarDTO{" +
                "id=" + id +
                ", regNumber='" + regNumber + '\'' +
                ", model='" + model + '\'' +
                ", make='" + make + '\'' +
                ", year=" + year +
                '}';
    }
}
