package ao.co.isptec.aplm.anunciosloc.data.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ao.co.isptec.aplm.anunciosloc.data.model.User;

/**
 * Repositório mock para gerenciar usuários, alinhado com o novo modelo User.
 */
public class UserRepository {
    
    private static UserRepository instance;
    private final Map<String, User> usersDatabase;
    private User currentUser;
    
    private UserRepository() {
        usersDatabase = new HashMap<>();
        initializeMockData();
    }
    
    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
    
    /**
     * Inicializa dados mockados com a nova estrutura de User.
     */
    private void initializeMockData() {
        // Usuários de teste
        User user1 = new User("alice", "123456");
        user1.setId(1L);
        usersDatabase.put(user1.getUsername(), user1);
        
        User user2 = new User("bob", "123456");
        user2.setId(2L);
        usersDatabase.put(user2.getUsername(), user2);
    }
    
    /**
     * Realiza login com username e password.
     */
    public User login(String username, String password) {
        User user = usersDatabase.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return user;
        }
        return null;
    }
    
    /**
     * Registra novo usuário apenas com username e password.
     */
    public User register(String username, String password) {
        if (usersDatabase.containsKey(username)) {
            return null; // Username já existe
        }
        
        User newUser = new User(username, password);
        newUser.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE); // Gera um ID longo aleatório
        
        usersDatabase.put(username, newUser);
        return newUser;
    }
    
    /**
     * Obtém o usuário atual logado.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Faz logout.
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Obtém usuário por username.
     */
    public User getUserByUsername(String username) {
        return usersDatabase.get(username);
    }

    /**
     * Verifica se username já existe.
     */
    public boolean usernameExists(String username) {
        return usersDatabase.containsKey(username);
    }

    /**
     * Lista todos os usuários (para testes).
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(usersDatabase.values());
    }
}
