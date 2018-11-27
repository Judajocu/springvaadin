package com.practica.springvaadin.Services;

import com.practica.springvaadin.Models.User;
import com.practica.springvaadin.Repositories.UserRpository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class AccessService {

    @Autowired
    UserRpository userRpository;

    public void registerUser(String email, String firstname, String lastname, String password) throws Exception
    {
        try {
            userRpository.save(new User(email,firstname,lastname,password));
        }
        catch (PersistenceException e)
        {
            throw new PersistenceException("Error de persistencia al crear usuario");
        }
        catch (NullPointerException e)
        {
            throw new NullPointerException("Error de entrada de dato nulo");
        }
        catch (Exception e)
        {
            throw  new Exception("Error general");
        }
    }

    public void editUser(User user) throws Exception
    {
        try
        {
            userRpository.delete(fetchAllRegisteredUser().get(0));

            userRpository.save(user);
        }
        catch (PersistenceException e)
        {
            throw new PersistenceException("Error de persistencia al editar el usuario");
        }
        catch (NullPointerException e)
        {
            throw new NullPointerException("Error de entrada de dato nulo");
        }
        catch (Exception e)
        {
            throw new Exception("Error general");
        }
    }

    public boolean ValidateUser(String email, String password)
    {
        User user = userRpository.findByEmailAndPassword(email,password);

        return (user!=null);
    }

    public List<User> fetchAllRegisteredUser()
    {
        return userRpository.findAll();
    }

}
