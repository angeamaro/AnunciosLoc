# 📚 Estrutura do Projeto AnunciosLoc

## 🎯 Arquitetura MVVM (Model-View-ViewModel)

Este projeto segue o padrão **MVVM (Model-View-ViewModel)**, que é o padrão recomendado pelo Google para aplicações Android modernas. Este padrão separa a lógica de negócio da interface do usuário, facilitando manutenção, testes e escalabilidade.

---

## 📁 Estrutura de Pastas

```
ao.co.isptec.aplm.anunciosloc/
├── data/                   # Camada de Dados
│   ├── model/             # DTOs e entidades de dados
│   ├── remote/            # APIs e serviços remotos
│   └── repository/        # Repositórios de acesso a dados
│
├── di/                    # Dependency Injection (Injeção de Dependência)
│
├── models/                # Modelos de Domínio
│
├── ui/                    # Camada de Interface do Usuário
│   ├── adapter/          # RecyclerView Adapters
│   ├── view/             # Activities e Fragments
│   │   └── fragment/     # Fragments
│   └── viewmodel/        # ViewModels (lógica de apresentação)
│
└── utils/                 # Utilitários e Helpers
```

---

## 🔍 Detalhamento de Cada Camada

### 1️⃣ **data/** - Camada de Dados

Esta camada é responsável por **buscar, armazenar e gerenciar dados**.

#### 📂 `data/model/`
**O que colocar aqui:** Classes que representam dados vindos da API (DTOs - Data Transfer Objects)

```java
// Exemplo: UserDTO.java
public class UserDTO {
    private String id;
    private String name;
    private String email;
    // Getters e Setters
}
```

**Quando usar:**
- Dados que vêm diretamente de uma API REST
- Estruturas JSON convertidas para Java/Kotlin
- Dados que precisam de serialização/deserialização

---

#### 📂 `data/remote/`
**O que colocar aqui:** Interfaces de API (Retrofit) e classes de serviços remotos

```java
// Exemplo: AnnouncementApiService.java
public interface AnnouncementApiService {
    @GET("announcements")
    Call<List<AnnouncementDTO>> getAnnouncements();
    
    @POST("announcements")
    Call<AnnouncementDTO> createAnnouncement(@Body AnnouncementDTO announcement);
}
```

**Quando usar:**
- Definir endpoints de API
- Configurar chamadas HTTP (GET, POST, PUT, DELETE)
- Implementar interceptors e autenticação

---

#### 📂 `data/repository/`
**O que colocar aqui:** Repositórios que centralizam o acesso aos dados

```java
// Exemplo: AnnouncementRepository.java
public class AnnouncementRepository {
    private AnnouncementApiService apiService;
    
    public LiveData<List<Announcement>> getAnnouncements() {
        // Lógica para buscar dados da API ou cache
    }
    
    public void createAnnouncement(Announcement announcement) {
        // Lógica para criar anúncio
    }
}
```

**Quando usar:**
- Abstrair a origem dos dados (API, banco local, cache)
- Implementar lógica de sincronização
- Gerenciar cache de dados
- Servir como única fonte de verdade para ViewModels

---

### 2️⃣ **di/** - Dependency Injection

**O que colocar aqui:** Classes de configuração de injeção de dependência (Dagger, Hilt, Koin)

```java
// Exemplo: AppModule.java
@Module
public class AppModule {
    @Provides
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build();
    }
}
```

**Quando usar:**
- Configurar dependências globais
- Prover instâncias de repositórios, APIs, etc.
- Gerenciar ciclo de vida de objetos

---

### 3️⃣ **models/** - Modelos de Domínio

**O que colocar aqui:** Classes de modelo de negócio (objetos puros da aplicação)

```java
// Exemplo: Announcement.java
public class Announcement {
    private String id;
    private String title;
    private String content;
    private User author;
    private Date createdAt;
    
    // Métodos de lógica de negócio
    public boolean isActive() {
        return endDate.after(new Date());
    }
}
```

**Quando usar:**
- Representar entidades do domínio da aplicação
- Adicionar lógica de negócio aos objetos
- Separar a estrutura de dados da API da estrutura interna

**Diferença entre `models/` e `data/model/`:**
- `data/model/`: Dados **crus da API** (estrutura JSON)
- `models/`: Objetos de **negócio** com lógica (estrutura interna)

---

### 4️⃣ **ui/** - Camada de Interface

#### 📂 `ui/adapter/`
**O que colocar aqui:** Adapters para RecyclerView e ListView

```java
// Exemplo: AnnouncementAdapter.java
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {
    private List<Announcement> announcements;
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        holder.titleTextView.setText(announcement.getTitle());
    }
}
```

**Quando usar:**
- Exibir listas de dados (anúncios, notificações, localizações)
- Implementar ViewHolders
- Gerenciar cliques em itens de lista

**✅ TODOS os adapters devem estar aqui:**
- `AnnouncementAdapter.java`
- `AnnouncementCardAdapter.java`
- `AttributeAdapter.java`
- `InterestCategoryAdapter.java`
- `InterestValueAdapter.java`
- `LocationAdapter.java`
- `NotificationAdapter.java`

---

#### 📂 `ui/view/`
**O que colocar aqui:** Activities (telas da aplicação)

```java
// Exemplo: MainActivity.java
public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        
        // Observar LiveData
        viewModel.getAnnouncements().observe(this, announcements -> {
            // Atualizar UI
        });
    }
}
```

**Quando usar:**
- Criar novas telas da aplicação
- Configurar layouts e views
- Observar dados do ViewModel
- Gerenciar navegação entre telas

**❌ NUNCA colocar aqui:**
- Lógica de negócio
- Chamadas diretas à API
- Cálculos complexos

**✅ O que DEVE estar aqui:**
- Inicialização de views (`findViewById`, ViewBinding)
- Configuração de listeners de botões
- Observação de LiveData do ViewModel
- Navegação (`startActivity`, `Intent`)
- Exibição de Toasts, Dialogs, Snackbars

**📋 Activities existentes:**
- `SplashActivity.java` - Tela inicial
- `LoginActivity.java` - Autenticação
- `RegisterActivity.java` - Cadastro
- `MainActivity.java` - Tela principal
- `MenuOptionsActivity.java` - Menu de opções
- `CreateAnnouncementActivity.java` - Criar anúncio
- `AnnouncementDetailActivity.java` - Detalhes do anúncio
- `InterestsActivity.java` - Interesses do usuário
- `InterestValuesActivity.java` - Valores de interesse
- `AddLocationActivity.java` - Adicionar localização
- `ForgotPasswordActivity.java` - Recuperar senha
- `PoliciesActivity.java` - Políticas de uso

---

#### 📂 `ui/view/fragment/`
**O que colocar aqui:** Fragments (partes reutilizáveis de UI)

```java
// Exemplo: HomeFragment.java
public class HomeFragment extends Fragment {
    private HomeViewModel viewModel;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        
        return view;
    }
}
```

**Quando usar:**
- Criar seções dentro de Activities
- Implementar navegação bottom navigation
- Reutilizar componentes de UI

**📋 Fragments existentes:**
- `HomeFragment.java` - Tela inicial (navegação)
- `AnnouncementsFragment.java` - Lista de anúncios
- `LocationsFragment.java` - Localizações
- `NotificationsFragment.java` - Notificações
- `ProfileFragment.java` - Perfil do usuário
- `BottomNavigationFragment.java` - Gerenciador de navegação inferior

---

#### 📂 `ui/viewmodel/`
**O que colocar aqui:** ViewModels (lógica de apresentação)

```java
// Exemplo: AnnouncementViewModel.java
public class AnnouncementViewModel extends ViewModel {
    private AnnouncementRepository repository;
    private MutableLiveData<List<Announcement>> announcements;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<String> errorMessage;
    
    public AnnouncementViewModel() {
        repository = new AnnouncementRepository();
        announcements = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
    }
    
    public void loadAnnouncements() {
        isLoading.setValue(true);
        repository.getAnnouncements(new Callback() {
            @Override
            public void onSuccess(List<Announcement> data) {
                announcements.setValue(data);
                isLoading.setValue(false);
            }
            
            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
                isLoading.setValue(false);
            }
        });
    }
    
    public LiveData<List<Announcement>> getAnnouncements() {
        return announcements;
    }
}
```

**Quando usar:**
- Buscar dados do repositório
- Processar dados antes de exibir
- Gerenciar estados da UI (loading, error, success)
- Validar inputs do usuário
- Formatar dados para exibição

**✅ O que DEVE estar aqui:**
- LiveData para expor dados à UI
- Métodos para buscar/atualizar dados
- Lógica de formatação e validação
- Gerenciamento de estado

**❌ NUNCA colocar aqui:**
- Referências a Views (TextView, Button, etc.)
- Referências a Context (usar AndroidViewModel se necessário)
- Manipulação direta de UI

---

### 5️⃣ **utils/** - Utilitários

**O que colocar aqui:** Classes auxiliares e helpers

```java
// Exemplo: DateUtils.java
public class DateUtils {
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }
    
    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale.getDefault());
        return sdf.format(date);
    }
}
```

**Quando usar:**
- Funções de formatação (datas, números, texto)
- Validações reutilizáveis (email, telefone)
- Conversões de dados
- Constantes globais
- Helpers de rede, permissões, etc.

**Exemplos comuns:**
- `DateUtils.java` - Formatação de datas
- `ValidationUtils.java` - Validações
- `NetworkUtils.java` - Verificar conectividade
- `PermissionUtils.java` - Gerenciar permissões
- `Constants.java` - Constantes globais

---

## 🔄 Fluxo de Dados (MVVM)

```
┌─────────────┐
│   Activity  │ (UI Layer)
│  /Fragment  │
└──────┬──────┘
       │ Observa LiveData
       ▼
┌─────────────┐
│  ViewModel  │ (Presentation Layer)
└──────┬──────┘
       │ Busca dados
       ▼
┌─────────────┐
│ Repository  │ (Data Layer)
└──────┬──────┘
       │ Chama API
       ▼
┌─────────────┐
│ API Service │ (Network Layer)
└─────────────┘
```

### Exemplo Prático: Criar um Anúncio

1. **Activity** (`CreateAnnouncementActivity.java`):
   ```java
   btnSave.setOnClickListener(v -> {
       Announcement announcement = new Announcement();
       announcement.setTitle(titleEditText.getText().toString());
       viewModel.createAnnouncement(announcement);
   });
   
   viewModel.getAnnouncementCreated().observe(this, success -> {
       if (success) {
           Toast.makeText(this, "Anúncio criado!", Toast.LENGTH_SHORT).show();
           finish();
       }
   });
   ```

2. **ViewModel** (`AnnouncementViewModel.java`):
   ```java
   public void createAnnouncement(Announcement announcement) {
       if (!validateAnnouncement(announcement)) {
           errorMessage.setValue("Preencha todos os campos");
           return;
       }
       
       isLoading.setValue(true);
       repository.createAnnouncement(announcement, new Callback() {
           @Override
           public void onSuccess() {
               announcementCreated.setValue(true);
               isLoading.setValue(false);
           }
       });
   }
   ```

3. **Repository** (`AnnouncementRepository.java`):
   ```java
   public void createAnnouncement(Announcement announcement, Callback callback) {
       AnnouncementDTO dto = convertToDTO(announcement);
       apiService.createAnnouncement(dto).enqueue(new Callback<AnnouncementDTO>() {
           @Override
           public void onResponse(Call<AnnouncementDTO> call, Response<AnnouncementDTO> response) {
               callback.onSuccess();
           }
       });
   }
   ```

4. **API Service** (`AnnouncementApiService.java`):
   ```java
   @POST("announcements")
   Call<AnnouncementDTO> createAnnouncement(@Body AnnouncementDTO announcement);
   ```

---

## 📝 Regras e Boas Práticas

### ✅ DO (Fazer)

1. **Separação de Responsabilidades**
   - Activity/Fragment: **apenas UI**
   - ViewModel: **lógica de apresentação**
   - Repository: **acesso a dados**
   - Model: **lógica de negócio**

2. **Nomenclatura Consistente**
   - Activities: `NomeActivity.java` (ex: `LoginActivity.java`)
   - Fragments: `NomeFragment.java` (ex: `HomeFragment.java`)
   - ViewModels: `NomeViewModel.java` (ex: `LoginViewModel.java`)
   - Adapters: `NomeAdapter.java` (ex: `AnnouncementAdapter.java`)
   - Repositories: `NomeRepository.java` (ex: `UserRepository.java`)

3. **Use LiveData para Comunicação**
   - ViewModel expõe LiveData
   - Activity/Fragment observa LiveData
   - Nunca passar callbacks diretamente

4. **Um ViewModel por Activity/Fragment**
   - Não compartilhar ViewModels desnecessariamente
   - Usar ViewModelProvider para criar instâncias

5. **Repositórios como Singleton**
   - Usar `getInstance()` para acesso global
   - Centralizar acesso aos dados

### ❌ DON'T (Não Fazer)

1. **NÃO colocar lógica de negócio em Activities/Fragments**
   ```java
   // ❌ ERRADO
   public class MainActivity extends AppCompatActivity {
       void loadData() {
           // NÃO fazer chamadas de API aqui!
           apiService.getData().enqueue(...);
       }
   }
   
   // ✅ CORRETO
   public class MainActivity extends AppCompatActivity {
       void loadData() {
           viewModel.loadData(); // Delegar ao ViewModel
       }
   }
   ```

2. **NÃO referenciar Views em ViewModels**
   ```java
   // ❌ ERRADO
   public class MainViewModel extends ViewModel {
       private TextView textView; // NUNCA!
   }
   
   // ✅ CORRETO
   public class MainViewModel extends ViewModel {
       private MutableLiveData<String> text; // Usar LiveData
   }
   ```

3. **NÃO misturar camadas**
   - Activity não deve chamar API diretamente
   - ViewModel não deve chamar Repository de outra feature
   - Adapter não deve ter lógica de negócio

4. **NÃO criar pastas duplicadas**
   - Todos adapters em `ui/adapter/`
   - Todas activities em `ui/view/`
   - Todos fragments em `ui/view/fragment/`

---

## 🆕 Como Adicionar Nova Funcionalidade

### Exemplo: Adicionar Feature de "Comentários"

1. **Criar Model** (`models/Comment.java`):
   ```java
   public class Comment {
       private String id;
       private String content;
       private User author;
       private Date createdAt;
   }
   ```

2. **Criar DTO** (`data/model/CommentDTO.java`):
   ```java
   public class CommentDTO {
       private String id;
       private String content;
       private String authorId;
       private long createdAt;
   }
   ```

3. **Criar API Service** (`data/remote/CommentApiService.java`):
   ```java
   public interface CommentApiService {
       @GET("comments/{announcementId}")
       Call<List<CommentDTO>> getComments(@Path("announcementId") String id);
   }
   ```

4. **Criar Repository** (`data/repository/CommentRepository.java`):
   ```java
   public class CommentRepository {
       public void getComments(String announcementId, Callback callback) {
           // Implementação
       }
   }
   ```

5. **Criar ViewModel** (`ui/viewmodel/CommentViewModel.java`):
   ```java
   public class CommentViewModel extends ViewModel {
       private MutableLiveData<List<Comment>> comments;
       private CommentRepository repository;
       
       public void loadComments(String announcementId) {
           // Implementação
       }
   }
   ```

6. **Criar Adapter** (`ui/adapter/CommentAdapter.java`):
   ```java
   public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
       // Implementação
   }
   ```

7. **Criar Activity** (`ui/view/CommentsActivity.java`):
   ```java
   public class CommentsActivity extends AppCompatActivity {
       private CommentViewModel viewModel;
       private CommentAdapter adapter;
       
       @Override
       protected void onCreate(Bundle savedInstanceState) {
           // Configurar UI e observar ViewModel
       }
   }
   ```

8. **Registrar no AndroidManifest.xml**:
   ```xml
   <activity
       android:name=".ui.view.CommentsActivity"
       android:exported="false" />
   ```

---

## 🔧 Ferramentas e Bibliotecas Recomendadas

- **Retrofit**: Chamadas de API
- **Gson**: Conversão JSON
- **LiveData**: Observação de dados
- **ViewModel**: Gerenciamento de estado
- **RecyclerView**: Listas
- **Material Design**: Componentes de UI
- **Glide/Picasso**: Carregamento de imagens

---

## 📞 Dúvidas Frequentes

**P: Onde coloco validação de formulários?**  
R: No **ViewModel**. Exemplo: `LoginViewModel.validateEmail()`

**P: Onde coloco formatação de datas?**  
R: Em **utils/DateUtils.java**

**P: Posso chamar a API diretamente da Activity?**  
R: **NÃO!** Sempre use ViewModel → Repository → API Service

**P: Onde salvo dados em cache?**  
R: No **Repository**, usando SharedPreferences ou Room Database

**P: Como compartilhar dados entre Fragments?**  
R: Use um ViewModel compartilhado da Activity pai

---

## 📚 Recursos Adicionais

- [Documentação oficial MVVM](https://developer.android.com/topic/architecture)
- [Guia de Arquitetura Android](https://developer.android.com/topic/architecture/intro)
- [LiveData Overview](https://developer.android.com/topic/libraries/architecture/livedata)
- [ViewModel Overview](https://developer.android.com/topic/libraries/architecture/viewmodel)

---

## 👥 Contribuindo

Ao adicionar código ao projeto, sempre siga esta estrutura:

1. ✅ Coloque o código na pasta correta
2. ✅ Siga o padrão MVVM
3. ✅ Use nomenclatura consistente
4. ✅ Documente código complexo
5. ✅ Teste suas alterações

---

**Última atualização:** 06/11/2025  
**Versão do documento:** 1.0  
**Autor:** Equipa AnunciosLoc
