package com.semanticsquare.thrillio.managers;

import com.semanticsquare.thrillio.dao.UserDao;
import com.semanticsquare.thrillio.entities.User;

public class UserManager {
    private static final UserManager instance = new UserManager();
    private static final UserDao dao = new UserDao();

    private UserManager() {
    }

    public static UserManager getInstance() {
        return instance;
    }

    public User[] getUsers() {
        return dao.getUsers();
    }

    public User createUser(long id, String email, String password
            , String firstName, String lastName, int gender, String userType) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setGender(gender);
        user.setUserType(userType);
        return user;
    }
}
