package sample.model;

import java.util.ArrayList;

public class User implements Comparable<User> {
    private String username;
    private String password;
    private int score;
    private final ArrayList<String> maps = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        maps.add("beginning");
        maps.add("pacmac");

    }

    public ArrayList<String> getMaps() {
        return maps;
    }

    public void addMap(String map){
        maps.add(map);
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int amount) {
        score += amount;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public int compareTo(User user) {
        return user.score - this.score;
    }
}
