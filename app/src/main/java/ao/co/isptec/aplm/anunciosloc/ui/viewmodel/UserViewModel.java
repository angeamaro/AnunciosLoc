package ao.co.isptec.aplm.anunciosloc.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import ao.co.isptec.aplm.anunciosloc.data.model.User;

public class UserViewModel extends ViewModel {
    
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();

    public LiveData<List<User>> getUsers() {
        return users;
    }

    // A lógica para carregar usuários agora deve vir da API.
}
