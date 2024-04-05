package com.needs.api.needsapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.needs.api.needsapi.model.Helper;
import org.springframework.stereotype.Component;

@Component
public class UserFileDAO implements UserDAO{
    private static final Logger LOG = Logger.getLogger(UserFileDAO.class.getName());
    Map<String,Helper> users;   // Provides a local cache of the user objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between User
                                        // objects and JSON text format written
                                        // to the file
    private String filename;    // Filename to read from and write to

    /**
     * Creates a User File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public UserFileDAO(@Value("${users.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the users from the file
    }

    /**
    * Generates an array of {@linkplain Helper users} from the tree map for any
    *
    * @return  The array of {@link Helper users}, may be empty
    */
    public Helper[] getUsersArray() throws IOException { 
        ArrayList<Helper> userArrayList = new ArrayList<>();
        for (Helper user : users.values()) {
            userArrayList.add(user);
        }

        Helper[] userArray = new Helper[userArrayList.size()];
        userArrayList.toArray(userArray);
        return userArray;
    }

    /**
     * Saves the {@linkplain Helper users} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Helper users} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Helper[] userArray = getUsersArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),userArray);
        return true;
    }

    /**
    ** Checks the {@linkplain Helper users} from the map to see if one has the same username
    *
    *  @return true if the {@link Helper users} contains the username
    *
    *  @return false if the {@link Helper users} does not contain the username
    */
    public Boolean hasUser(String username) {
        synchronized(users) {
            if (users.containsKey(username))
                return true;
            else
                return false;
        }
    }
    
     /* Loads {@linkplain Helper users} from the JSON file into the map
     * 
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        users = new TreeMap<>();
        // Deserializes the JSON objects from the file into an array of users
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Helper[] userArray = objectMapper.readValue(new File(filename),Helper[].class);

        // Add each user to the tree map and keep track of the greatest id
        for (Helper user : userArray) {
            users.put(user.getUsername(),user);
        }
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Helper createUser(Helper user) throws IOException {
        synchronized(users) {
            String username = user.getUsername();
            String password = user.getPassword();
            username= username.toLowerCase();
            // Checks for username in the map of users. 
            if( hasUser(username)!=true){
                Helper newUser = new Helper(username,password,null);
                users.put(username, newUser);
                save();
                return newUser;
            }
            else
                return null;
        }
    }

    
    /**
    ** {@inheritDoc}
    */
    @Override
    public Helper getUser(String username,String password) throws IOException {
        synchronized(users) {
            username= username.toLowerCase();
            if(users.containsKey(username)){
                Helper user = users.get(username);
                if (user.getPassword().equals(password)){
                    return user;
                }
                return null;
            }
            else 
                return null;
        }
    }
    
    /**
    ** {@ingeritDoc}
    */
    @Override
    public Helper updateHelper(Helper helper) throws IOException {
        synchronized(users) {
            if (users.containsKey(helper.getUsername()) == false) {
                return null;
            }
            System.out.println("\nold fundbasket passed in"+helper.getfundBasket()+"\n");
            System.out.println("old fundbasket in the map"+users.get(helper.getUsername()).getfundBasket()+"\n");
            users.put(helper.getUsername(),helper);
            System.out.println("new fundbasket"+users.get(helper.getUsername()).getfundBasket()+"\n");
            save();
            return helper;
        }
    }
   
}