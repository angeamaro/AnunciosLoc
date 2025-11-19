package ao.co.isptec.aplm.anunciosloc.data.repository;

import ao.co.isptec.aplm.anunciosloc.data.model.User;

/**
 * Repositório simplificado para gerenciar a sessão do usuário atual na aplicação.
 */
public class UserRepository {
    
    private static UserRepository instance;
    private User currentUser;
    
    private UserRepository() {}
    
    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
    
    /**
     * Obtém o usuário atual logado.
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Define o usuário atual após o login.
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    /**
     * Faz logout, limpando o usuário atual.
     */
    public void logout() {
        currentUser = null;
    }
}
