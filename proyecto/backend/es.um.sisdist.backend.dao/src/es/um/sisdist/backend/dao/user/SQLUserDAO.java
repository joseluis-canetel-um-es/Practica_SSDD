/**
 *
 */
package es.um.sisdist.backend.dao.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Supplier;

import es.um.sisdist.backend.dao.models.User;
import es.um.sisdist.backend.dao.utils.Lazy;

/**
 * @author dsevilla
 *
 */
public class SQLUserDAO implements IUserDAO
{
    Supplier<Connection> conn;

    public SQLUserDAO()
    {
    	conn = Lazy.lazily(() -> 
    	{
    		try
    		{
    			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();

    			// Si el nombre del host se pasa por environment, se usa aquí.
    			// Si no, se usa localhost. Esto permite configurarlo de forma
    			// sencilla para cuando se ejecute en el contenedor, y a la vez
    			// se pueden hacer pruebas locales
    			String sqlServerName = Optional.ofNullable(System.getenv("SQL_SERVER")).orElse("localhost");
    			String dbName = Optional.ofNullable(System.getenv("DB_NAME")).orElse("ssdd");
    			return DriverManager.getConnection(
                    "jdbc:mysql://" + sqlServerName + "/" + dbName + "?user=root&password=root");
    		} catch (Exception e)
    		{
    			// TODO Auto-generated catch block
    			e.printStackTrace();
            
    			return null;
    		}
    	});
    }

    @Override
    public Optional<User> getUserById(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<User> getUserByEmail(String id)
    {
        PreparedStatement stm;
        try
        {
            stm = conn.get().prepareStatement("SELECT * from users WHERE email = ?");
            stm.setString(1, id);
            ResultSet result = stm.executeQuery();
            if (result.next())
                return createUser(result);
        } catch (SQLException e)
        {
            // Fallthrough
        }
        return Optional.empty();
    }

    private Optional<User> createUser(ResultSet result)
    {
        try
        {
            return Optional.of(new User(result.getString(1), // id
                    result.getString(2), // email
                    result.getString(3), // pwhash
                    result.getString(4), // name
                    result.getString(5), // token
                    result.getInt(6))); // visits
        } catch (SQLException e)
        {
            return Optional.empty();
        }
    }

	@Override
	public boolean addUser(String email, String passwordHash, String name) {
		PreparedStatement insert;
		
		try {
			
			String token = generateUserToken(email, passwordHash, name);
			int visits = 0;
			
			insert = conn.get().prepareStatement("INSERT INTO users (email, password_hash, name, token, visits) VALUES (?, ?, ?, ?, ?)");
			insert.setString(1, email);
			insert.setString(2, passwordHash);
			insert.setString(3, name);
			insert.setString(4, token);
			insert.setInt(5, visits);
			
			insert.executeUpdate();
			return true;
			
		} catch (SQLException e) {}
		
		return false;
	}
	
	private String generateUserToken(String email, String passwordHash, String name) {
		
		String userToken = User.calculateMd5( email + passwordHash + name + UUID.randomUUID().toString().replace("-", "") );
		return userToken;
		
	}
}