package database;

import ucoach.data.model.Coach;
import ucoach.data.model.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by nikolai on 5/8/17.
 */
public class DataBase {
    private Map<Integer, Coach> coaches = new HashMap<>();
    private Map<Integer, User> users = new HashMap<>();

    public Coach createCoach(Coach coach) {
        coaches.put(coach.getId(), coach);

        return coach;
    }

    public Coach getCoachById(int id) {
        return coaches.get(id);
    }

    public void reset() {
        coaches.clear();
        users.clear();
    }

    public int getCoachesNum() {
        return coaches.size();
    }

    public Coach updateCoach(Coach updatedCoach) {
        return updatedCoach;
    }

    public User updateUser(User updatedUser) {
        return updatedUser;
    }

    public void deleteCoach(int id) {
        coaches.remove(id);
    }

    public User createUser(User user) {
        users.put(user.getId(), user);

        return user;
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public int getUserNum() {
        return users.size();
    }


    public void deleteUser(int id) {
        users.remove(id);
    }
}
