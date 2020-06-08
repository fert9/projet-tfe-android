package com.example.sannerqr;

class User {
    private String username;
     private  int id;

    public User(String username ,int id) {
        this.id=id;
        this.username = username;

    }
    public User(  ) {

    }
    public String getUsername() {
        return username;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
