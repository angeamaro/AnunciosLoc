package ao.co.isptec.aplm.anunciosloc.data.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ao.co.isptec.aplm.anunciosloc.data.model.User;

/**
 * Repositório mock para gerenciar usuários
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
     * Inicializa dados mockados
     */
    private void initializeMockData() {
        // Usuário de teste 1
        User user1 = new User("1", "alice", "alice@example.com", "Alice Silva");
        user1.setPassword("123456");
        user1.addProfileAttribute("profissao", "Estudante");
        user1.addProfileAttribute("clube", "Benfica");
        user1.addProfileAttribute("interesse", "Tecnologia");
        usersDatabase.put(user1.getEmail(), user1);
        
        // Usuário de teste 2
        User user2 = new User("2", "bob", "bob@example.com", "Bob Santos");
        user2.setPassword("123456");
        user2.addProfileAttribute("profissao", "Professor");
        user2.addProfileAttribute("clube", "Porto");
        user2.addProfileAttribute("interesse", "Educação");
        usersDatabase.put(user2.getEmail(), user2);
        
        // Usuário de teste 3
        User user3 = new User("3", "carol", "carol@example.com", "Carol Lima");
        user3.setPassword("123456");
        user3.addProfileAttribute("profissao", "Estudante");
        user3.addProfileAttribute("clube", "Sporting");
        user3.addProfileAttribute("interesse", "Desporto");
        usersDatabase.put(user3.getEmail(), user3);
    }
    
    /**
     * Realiza login
     */
    public User login(String email, String password) {
        User user = usersDatabase.get(email);
        if (user != null && user.getPassword().equals(password)) {
            user.setLastLoginAt(System.currentTimeMillis());
            currentUser = user;
            return user;
        }
        return null;
    }
    
    /**
     * Registra novo usuário
     */
    public User register(String username, String email, String password, String name) {
        if (usersDatabase.containsKey(email)) {
            return null; // Email já existe
        }
        
        String userId = UUID.randomUUID().toString();
        User newUser = new User(userId, username, email, name);
        newUser.setPassword(password);
        newUser.setLastLoginAt(System.currentTimeMillis());
        
        usersDatabase.put(email, newUser);
        currentUser = newUser;
        return newUser;
    }
    
    /**
     * Obtém o usuário atual logado
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Define o usuário atual
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    /**
     * Faz logout
     */
    public void logout() {
        currentUser = null;
    }
    
    /**
     * Obtém usuário por ID
     */
    public User getUserById(String userId) {
        for (User user : usersDatabase.values()) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Obtém usuário por email
     */
    public User getUserByEmail(String email) {
        return usersDatabase.get(email);
    }
    
    /**
     * Atualiza perfil do usuário
     */
    public boolean updateUserProfile(User user) {
        if (user == null || user.getEmail() == null) {
            return false;
        }
        
        usersDatabase.put(user.getEmail(), user);
        if (currentUser != null && currentUser.getId().equals(user.getId())) {
            currentUser = user;
        }
        return true;
    }
    
    /**
     * Adiciona atributo ao perfil do usuário atual
     */
    public boolean addProfileAttribute(String key, String value) {
        try {
            if (currentUser == null) {
                return false;
            }
            
            if (key == null || value == null || key.trim().isEmpty() || value.trim().isEmpty()) {
                return false;
            }
            
            currentUser.addProfileAttribute(key, value);
            return updateUserProfile(currentUser);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Remove atributo do perfil do usuário atual
     */
    public boolean removeProfileAttribute(String key) {
        try {
            if (currentUser == null) {
                return false;
            }
            
            if (key == null || key.trim().isEmpty()) {
                return false;
            }
            
            currentUser.removeProfileAttribute(key);
            return updateUserProfile(currentUser);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Verifica se email já existe
     */
    public boolean emailExists(String email) {
        return usersDatabase.containsKey(email);
    }
    
    /**
     * Recuperação de senha (mock - apenas retorna sucesso)
     */
    public boolean recoverPassword(String email) {
        return usersDatabase.containsKey(email);
    }
    
    /**
     * Lista todos os usuários (para testes)
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(usersDatabase.values());
    }
}
