package sample.controller;

import com.google.gson.Gson;
import sample.model.User;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseController {

    public static void updateUser(User user) {
        String username = user.getUsername();
        try {
            FileWriter fileWriter = new FileWriter(getUserDirectory(username));
            Gson gson = new Gson();
            gson.toJson(user, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User getUserByName(String username) {
        try {
            FileReader fileReader = new FileReader(getUserDirectory(username));
            Gson gson = new Gson();
            User user = gson.fromJson(fileReader, User.class);
            fileReader.close();
            return user;
        } catch (FileNotFoundException fileNotFoundException) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getUserDirectory(String username) {
        return "src" + File.separator + "main" + File.separator + "resources" + File.separator +
                "sample" + File.separator + "users" + File.separator + username + ".json";
    }

    public static HashMap<String, String> getMaps() {
        HashMap<String, String> result = new HashMap<>();
        File path = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator +
                "sample" + File.separator + "maps");
        File[] files = path.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                try {
                    String map = new String(Files.readAllBytes(file.toPath()));
                    result.put(file.getName(), map);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return result;

    }

    public static ArrayList<User> getAllUsers() {
        ArrayList<User> players = new ArrayList<>();
        File folder = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator +
                "sample" + File.separator + "users");
        File[] files = folder.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isFile() && file.getAbsolutePath().endsWith(".json")) {
                players.add(getUserByName(file.getName().substring(0, file.getName().length() - 5)));
            }
        }
        return players;
    }

    public static void removeUser(User user) throws Exception {
        File file = new File(getUserDirectory(user.getUsername()));
        if (!file.delete()) {
            throw new Exception("UNKNOWN ERROR IN DELETION");
        }
    }

    public static String getMap(String name){
        File path = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator +
                "sample" + File.separator + "maps" + File.separator + name);
        String map = null;
        try {
            map = new String(Files.readAllBytes(path.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static HashMap<String, String> getMapsOfUser(String username) {
        HashMap<String, String> maps = new HashMap<>();
        User user = getUserByName(username);
        for (String map : user.getMaps()) {
            maps.put(map,getMap(map) );
        }
        return maps;
    }

    public static void addMap(String name, String mapString) {
        File path = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator +
                "sample" + File.separator + "maps" + File.separator + name);
        try {
            FileWriter fileWriter = new FileWriter(path.toString());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(mapString);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
