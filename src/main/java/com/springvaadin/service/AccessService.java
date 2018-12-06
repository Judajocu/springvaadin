package com.springvaadin.service;

import com.springvaadin.repository.UserRepository;
import com.springvaadin.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class AccessService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String email, String firstName, String lastName, String password) throws Exception {

        try {
            return userRepository.save(new User(email, firstName, lastName, password));
        } catch (PersistenceException exp) {
            throw new PersistenceException("Error de persistencia al ingresar usuario");
        } catch (NullPointerException exp) {
            throw new NullPointerException("Data nula al ingresar usuario");
        } catch (Exception exp) {
            throw new Exception("Error general al ingresar usuario");
        }
    }

    public void editUser(User user) throws Exception {

        try {
            userRepository.delete(fetchAllRegisteredUser().get(0));

            userRepository.save(user);
        } catch (PersistenceException exp) {
            throw new PersistenceException("Error de persistencia al editar usuario");
        } catch (NullPointerException exp) {
            throw new NullPointerException("Data nula al editar usuario");
        } catch (Exception exp) {
            throw new Exception("Error general al edotar usuario");
        }
    }

    public boolean validateUserCredentials(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);

        return (user != null);
    }

    public List<User> fetchAllRegisteredUser() {
        return userRepository.findAll();
    }

}
