A estrutura de um projeto Android utilizando a **arquitetura MVVM (Model–View–ViewModel)** é organizada de forma a separar claramente as responsabilidades entre as camadas — **UI, lógica de apresentação e dados**.

---

### 🏗 Estrutura típica de pastas

```
app/
 └── src/
      └── main/
           ├── java/
           │    └── com.example.seuprojeto/
           │         ├── data/
           │         │    ├── model/
           │         │    │    └── User.kt
           │         │    ├── repository/
           │         │    │    └── UserRepository.kt
           │         │    └── remote/
           │         │         └── ApiService.kt
           │         │
           │         ├── domain/                  ← opcional (para Clean Architecture)
           │         │    └── usecase/
           │         │         └── GetUserUseCase.kt
           │         │
           │         ├── ui/
           │         │    ├── view/
           │         │    │    ├── MainActivity.kt
           │         │    │    └── fragments/
           │         │    │         └── UserFragment.kt
           │         │    └── viewmodel/
           │         │         └── UserViewModel.kt
           │         │
           │         ├── di/                      ← injeção de dependências (Hilt/Koin)
           │         │    └── AppModule.kt
           │         │
           │         ├── utils/                   ← extensões e helpers
           │         │    └── Constants.kt
           │         │
           │         └── App.kt                   ← classe Application
           │
           └── res/
                ├── layout/
                │    └── fragment_user.xml
                ├── values/
                │    └── strings.xml
                └── drawable/
```

---

### ⚙️ Fluxo de dados simplificado

```
UI (Activity/Fragment)
      ↓
ViewModel (lógica de apresentação)
      ↓
Repository (intermediário entre domínio e dados)
      ↓
Data Source (API / BD local)
```

---

### 🧩 Explicação das camadas

#### **1. View (UI Layer)**

* Contém as Activities, Fragments e, se usar Compose, os Composables.
* Apenas observa o estado do ViewModel.
* Não contém lógica de negócio.

Exemplo:

```kotlin
class UserFragment : Fragment() {
    private val viewModel: UserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.users.observe(viewLifecycleOwner) { users ->
            // Atualiza a UI
        }
        viewModel.loadUsers()
    }
}
```

---

#### **2. ViewModel**

* Mantém o estado da UI e lida com a lógica de apresentação.
* Faz chamadas ao Repository e expõe dados via `LiveData`, `Flow` ou `StateFlow`.

```kotlin
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    fun loadUsers() {
        viewModelScope.launch {
            _users.value = repository.getUsers()
        }
    }
}
```

---

#### **3. Repository**

* Camada intermediária entre o ViewModel e as fontes de dados.
* Decide de onde buscar (API, BD local, cache).

```kotlin
class UserRepository @Inject constructor(
    private val api: ApiService,
    private val dao: UserDao
) {
    suspend fun getUsers(): List<User> {
        val response = api.getUsers()
        dao.insertAll(response)
        return response
    }
}
```

---

#### **4. Data Layer**

* Inclui **APIs**, **DAOs**, **Models** e **entidades** do banco local.
* Usa Room, Retrofit, etc.

```kotlin
interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}

@Entity
data class User(
    @PrimaryKey val id: Int,
    val name: String
)
```

---
