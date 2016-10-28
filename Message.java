package com.codeshastra.coderr.provideameal;

public class Message {
    private long id;
    private String name;
    private String address;
    private String number,meals;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String message) {
        this.name = message;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String message) {
        this.address = message;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMeals() {
        return meals;
    }

    public void setMeals(String meals) {
        this.meals = meals;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return name;
    }
}
