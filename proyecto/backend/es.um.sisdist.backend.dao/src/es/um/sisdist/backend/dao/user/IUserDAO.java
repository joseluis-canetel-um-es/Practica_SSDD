package es.um.sisdist.backend.dao.user;

import java.util.Optional;

import es.um.sisdist.backend.dao.models.User;

public interface IUserDAO
{
    public Optional<User> getUserById(String id);

    public Optional<User> getUserByEmail(String email);
    
    public void addVisits(String email); // incrementa el numero de visitas
    public boolean insertUser(String email, String name, String password);
    public boolean deleteUser(String email);
    public boolean updateUser(User user);
 
}
