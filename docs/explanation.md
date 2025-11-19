# 📋 Documentação Técnica - AnunciosLoc

## Índice
1. [Visão Geral do Sistema](#visão-geral-do-sistema)
2. [Arquitetura e Padrões](#arquitetura-e-padrões)
3. [Bibliotecas e Dependências](#bibliotecas-e-dependências)
4. [Threading e Operações Assíncronas](#threading-e-operações-assíncronas)
5. [Estruturas de Dados](#estruturas-de-dados)
6. [Implementação das Telas](#implementação-das-telas)
7. [Comunicação Cliente-Servidor](#comunicação-cliente-servidor)
8. [Gerenciamento de Estado](#gerenciamento-de-estado)

---

## 1. Visão Geral do Sistema

O **AnunciosLoc** é uma aplicação Android nativa desenvolvida em **Java 11** que implementa um sistema de anúncios baseado em localização com políticas de entrega personalizadas. O sistema utiliza o padrão **MVVM (Model-View-ViewModel)** para separação de responsabilidades e gerenciamento reativo de estado.

### Características Técnicas Principais
- **Linguagem**: Java 11
- **SDK Mínimo**: API 24 (Android 7.0 Nougat)
- **SDK Target**: API 36
- **Build System**: Gradle 8.13.0 com Kotlin DSL
- **Arquitetura**: MVVM com LiveData
- **Threading**: Thread manual + Handler (sem Coroutines/RxJava)

---

## 2. Arquitetura e Padrões

### 2.1 Padrão MVVM

A aplicação segue rigorosamente o padrão **MVVM** com as seguintes camadas:

```
┌─────────────────────────────────────────┐
│          VIEW LAYER (UI)                │
│  Activities, Fragments, Adapters        │
└──────────────┬──────────────────────────┘
               │ observa LiveData
               ▼
┌─────────────────────────────────────────┐
│       VIEWMODEL LAYER                   │
│  Lógica de apresentação + Estado UI     │
└──────────────┬──────────────────────────┘
               │ requisita dados
               ▼
┌─────────────────────────────────────────┐
│       REPOSITORY LAYER                  │
│  Gerenciamento de dados (mock/cache)    │
└──────────────┬──────────────────────────┘
               │ acessa
               ▼
┌─────────────────────────────────────────┐
│       DATA SOURCE (MOCK)                │
│  In-memory database (HashMap)           │
└─────────────────────────────────────────┘
```

### 2.2 Separação de Responsabilidades

#### **View (Activity/Fragment)**
- **Responsabilidade**: Renderização da UI, captura de eventos do usuário
- **NÃO faz**: Lógica de negócio, chamadas de API, processamento de dados
- **Comunicação**: Apenas observa LiveData do ViewModel e dispara ações

#### **ViewModel**
- **Responsabilidade**: Lógica de apresentação, gerenciamento de estado UI, validações
- **NÃO faz**: Manipulação direta de Views, Context dependente
- **Comunicação**: Expõe LiveData para View, interage com Repository

#### **Repository**
- **Responsabilidade**: Fonte única de verdade, gerenciamento de dados, cache
- **NÃO faz**: Lógica de apresentação, manipulação de UI
- **Comunicação**: Implementa operações CRUD, atualmente com dados mock

### 2.3 Padrões Implementados

#### **Singleton Pattern**
Utilizado em Repositories para garantir única instância:

```java
public class UserRepository {
    private static UserRepository instance;
    
    private UserRepository() {
        // Construtor privado
    }
    
    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
}
```

**Implementado em:**
- `UserRepository`
- `AnnouncementRepository`
- `LocationRepository`
- `NotificationRepository`

#### **Observer Pattern**
Implementado via **LiveData** do Android Architecture Components:

```java
// No ViewModel
private MutableLiveData<List<Announcement>> announcements = new MutableLiveData<>();

public LiveData<List<Announcement>> getAnnouncements() {
    return announcements;
}

// Na View
viewModel.getAnnouncements().observe(this, announcementList -> {
    adapter.submitList(announcementList);
});
```

#### **Builder Pattern**
Usado na configuração de componentes complexos:

```java
AlertDialog dialog = new AlertDialog.Builder(context)
    .setTitle("Título")
    .setMessage("Mensagem")
    .setPositiveButton("OK", (d, w) -> {})
    .create();
```

---

## 3. Bibliotecas e Dependências

### 3.1 Bibliotecas Core

#### **AndroidX Libraries**
```gradle
implementation(libs.appcompat)           // 1.6.1 - AppCompat
implementation(libs.material)            // 1.10.0 - Material Design 3
implementation(libs.activity)            // 1.8.0 - Activity KTX
implementation(libs.constraintlayout)    // 2.1.4 - ConstraintLayout
```

**Uso:**
- **appcompat**: Compatibilidade retroativa com versões antigas do Android
- **material**: Componentes Material Design 3 (MaterialButton, MaterialCardView, Chip, etc.)
- **activity**: Activity Result API para navegação segura entre Activities
- **constraintlayout**: Layouts flexíveis e performáticos

#### **Google Play Services**
```gradle
implementation("com.google.android.gms:play-services-location:21.0.1")
implementation("com.google.android.gms:play-services-maps:18.2.0")
```

**Uso:**
- **location**: FusedLocationProviderClient para obtenção de localização GPS
- **maps**: GoogleMap, MapFragment para exibição de mapas

### 3.2 Bibliotecas de Testes

```gradle
testImplementation(libs.junit)                      // 4.13.2
androidTestImplementation(libs.ext.junit)           // 1.1.5
androidTestImplementation(libs.espresso.core)       // 3.5.1
```

### 3.3 Componentes Android Architecture

Embora não explicitamente declaradas como dependências (incluídas no AndroidX), a aplicação utiliza:

#### **LiveData**
Observable data holder class que respeita o ciclo de vida:

```java
private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

public LiveData<Boolean> getIsLoading() {
    return isLoading;
}
```

**Vantagens:**
- Observação lifecycle-aware
- Não causa memory leaks
- Atualização automática da UI

#### **ViewModel**
Sobrevive a mudanças de configuração (rotação de tela):

```java
public class AnnouncementViewModel extends ViewModel {
    // Estado preservado em rotações
    private MutableLiveData<List<Announcement>> announcements;
}
```

### 3.4 Bibliotecas NÃO Utilizadas (mas comuns)

A aplicação **NÃO utiliza**:
- ❌ **Retrofit**: Não há chamadas REST reais (dados mock)
- ❌ **Gson/Moshi**: Serialização JSON não necessária
- ❌ **Glide/Picasso**: Carregamento de imagens externas
- ❌ **Room Database**: Persistência local não implementada
- ❌ **Dagger/Hilt**: Injeção de dependências
- ❌ **Coroutines/RxJava**: Threading manual com Thread/Handler

---

## 4. Threading e Operações Assíncronas

### 4.1 Estratégia de Threading

A aplicação utiliza **threading manual** com `Thread` e `Handler/postValue()` ao invés de Coroutines ou RxJava.

#### **Padrão Implementado**

```java
public void loadAnnouncements() {
    isLoading.setValue(true);
    
    new Thread(() -> {
        try {
            // Simula delay de rede
            Thread.sleep(500);
            
            // Operação em background
            List<Announcement> data = repository.getAllAnnouncements();
            
            // Atualiza LiveData na Main Thread
            announcements.postValue(data);
        } catch (InterruptedException e) {
            errorMessage.postValue("Erro ao carregar dados");
        } finally {
            isLoading.postValue(false);
        }
    }).start();
}
```

### 4.2 Threading nos ViewModels

Todos os ViewModels seguem o padrão:

#### **Thread Principal (Main/UI Thread)**
- Atualização de Views
- `setValue()` do LiveData
- Event listeners

#### **Background Thread**
- Operações de Repository
- Simulação de delays de rede
- Processamento de dados

### 4.3 Sincronização de Threads

#### **postValue() vs setValue()**

```java
// setValue() - Deve ser chamado na Main Thread
isLoading.setValue(true);

// postValue() - Pode ser chamado de qualquer thread
announcements.postValue(data);
```

### 4.4 Exemplo Completo: CreateAnnouncement

```java
public void createAnnouncement(Announcement announcement) {
    isLoading.setValue(true);
    errorMessage.setValue(null);
    
    new Thread(() -> {
        try {
            Thread.sleep(800); // Simula latência de rede
            
            Announcement created = announcementRepository.createAnnouncement(announcement);
            
            if (created != null) {
                announcementCreated.postValue(true);
                operationSuccess.postValue(true);
                loadAnnouncements(); // Recarrega lista
            } else {
                errorMessage.postValue("Erro ao criar anúncio");
                operationSuccess.postValue(false);
            }
        } catch (InterruptedException e) {
            errorMessage.postValue("Erro ao criar anúncio");
            operationSuccess.postValue(false);
        } finally {
            isLoading.postValue(false);
        }
    }).start();
}
```

### 4.5 Gerenciamento de Recursos

#### **Memory Leaks Prevention**
- ✅ ViewModels não mantêm referências a Context
- ✅ LiveData respeita lifecycle (auto-cleanup)
- ✅ Threads não mantêm referências fortes a Activities

#### **Thread Lifecycle**
- Threads são criadas on-demand
- Não há pool de threads reutilizável
- Threads terminam após conclusão da tarefa

---

## 5. Estruturas de Dados

### 5.1 Estruturas de Dados no Cliente

#### **In-Memory Storage (HashMap)**

Todos os Repositories utilizam `HashMap` para simular banco de dados:

```java
public class AnnouncementRepository {
    private final Map<String, Announcement> announcementsDatabase;
    
    private AnnouncementRepository() {
        announcementsDatabase = new HashMap<>();
        initializeMockData();
    }
}
```

**Características:**
- **Tempo de busca**: O(1) por ID
- **Persistência**: Dados voláteis (perdidos ao fechar app)
- **Thread-safety**: Não implementada (acesso single-threaded simulado)

#### **SharedPreferences**

Usado para dados simples e configurações:

```java
SharedPreferences prefs = context.getSharedPreferences("AppSettings", MODE_PRIVATE);
prefs.edit()
    .putBoolean("notifications_enabled", true)
    .putString("user_id", userId)
    .apply(); // Assíncrono
```

**Armazenado:**
- Estado de switches (GPS, WiFi, Notificações, Mula Mode)
- IDs de anúncios salvos (Set<String>)
- Sessão do usuário (user_id, session_token)
- Atributos de perfil (Map<String, String> serializado)

### 5.2 Modelos de Dados (Models)

#### **User**
```java
public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String photoUrl;
    private String publicKey;
    private Map<String, String> profileAttributes; // Interesses dinâmicos
    private long createdAt;
}
```

**Estrutura de Dados:**
- `profileAttributes`: HashMap<String, String>
  - Exemplo: `{"interesse": "Tecnologia", "profissao": "Estudante"}`
  - Usado para filtragem de políticas

#### **Announcement**
```java
public class Announcement {
    private String id;
    private String title;
    private String content;
    private String locationId;
    private String authorId;
    
    // Janela temporal
    private Date startDate;
    private Date endDate;
    
    // Política de entrega
    private String deliveryPolicy; // WHITELIST, BLACKLIST, EVERYONE
    private List<PolicyRule> policyRules;
    
    private long createdAt;
    private String status; // ACTIVE, EXPIRED, DRAFT
}
```

**Estrutura de Dados:**
- `policyRules`: ArrayList<PolicyRule>
- Cada `PolicyRule`: `{"attributeName": "interesse", "attributeValue": "Tecnologia"}`

#### **Location**
```java
public class Location {
    private String id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String createdBy;
    private long createdAt;
}
```

#### **PolicyFilter**
```java
public class PolicyFilter implements Serializable {
    private Map<String, String> attributes;
    
    public boolean matches(User user) {
        Map<String, String> userAttrs = user.getProfileAttributes();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String userValue = userAttrs.get(entry.getKey());
            if (!entry.getValue().equals(userValue)) {
                return false;
            }
        }
        return true;
    }
}
```

**Algoritmo de Matching:**
- Percorre todos os atributos do filtro
- Compara com atributos do usuário
- Retorna `true` apenas se TODOS os atributos coincidem

### 5.3 Estruturas de Dados no Servidor (Mock)

Atualmente, o servidor é **simulado** através de dados mock nos Repositories:

```java
private void initializeMockData() {
    // Usuário mock
    User user1 = new User("1", "alice", "alice@example.com", "Alice Silva");
    user1.setPassword("password123");
    user1.getProfileAttributes().put("interesse", "Tecnologia");
    user1.getProfileAttributes().put("profissao", "Estudante");
    usersDatabase.put(user1.getId(), user1);
    
    // Anúncio mock
    Announcement ann1 = new Announcement("1", "Workshop de IA", "...", "1", "1");
    ann1.setDeliveryPolicy(Constants.POLICY_WHITELIST);
    ann1.addPolicyRule(new PolicyRule("interesse", "Tecnologia"));
    announcementsDatabase.put(ann1.getId(), ann1);
}
```

**Dados Mock Inicializados:**
- 5 usuários com diferentes perfis
- 10+ anúncios com variadas políticas
- 5 localizações em Luanda
- 15+ notificações de diferentes tipos

### 5.4 Coleções Utilizadas

| Tipo | Uso | Implementação |
|------|-----|---------------|
| `HashMap<String, T>` | Armazenamento principal (ID → Objeto) | `java.util.HashMap` |
| `ArrayList<T>` | Listas de objetos | `java.util.ArrayList` |
| `LinkedHashMap<String, String>` | Preservar ordem de inserção (Interesses) | `java.util.LinkedHashMap` |
| `HashSet<String>` | IDs únicos (anúncios salvos) | `java.util.HashSet` |

---

## 6. Implementação das Telas

### 6.1 SplashActivity

**Arquivo**: `SplashActivity.java`  
**Layout**: `activity_splash.xml`

#### **Propósito**
Tela inicial exibida durante 2.5 segundos com logo da aplicação.

#### **Implementação Técnica**

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    
    new Handler(Looper.getMainLooper()).postDelayed(() -> {
        Intent intent;
        if (preferencesHelper.isUserLoggedIn()) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }, Constants.SPLASH_DELAY); // 2500ms
}
```

**Threading:**
- `Handler` + `postDelayed()` para delay na Main Thread
- Não bloqueia UI
- Verifica sessão via SharedPreferences

**Design:**
- ImageView com logo centralizado
- Background gradiente azul-roxo
- Sem animações complexas

---

### 6.2 LoginActivity

**Arquivo**: `LoginActivity.java`  
**Layout**: `activity_login.xml`  
**ViewModel**: `AuthViewModel.java`

#### **Componentes UI**
```xml
<EditText android:id="@+id/editUsername" />
<EditText android:id="@+id/editPassword" android:inputType="textPassword" />
<Button android:id="@+id/btnLogin" />
<TextView android:id="@+id/txtRegister" />
<TextView android:id="@+id/txtForgotPassword" />
<ProgressBar android:id="@+id/progressBar" />
```

#### **Fluxo de Autenticação**

```java
private void attemptLogin() {
    String username = editUsername.getText().toString().trim();
    String password = editPassword.getText().toString().trim();
    
    // Validação
    if (!ValidationUtils.isValidUsername(username)) {
        editUsername.setError("Usuário inválido");
        return;
    }
    
    // Chama ViewModel
    authViewModel.login(username, password);
}
```

**No ViewModel:**
```java
public void login(String username, String password) {
    isLoading.setValue(true);
    
    new Thread(() -> {
        try {
            Thread.sleep(1000); // Simula latência
            User user = userRepository.authenticate(username, password);
            
            if (user != null) {
                authenticatedUser.postValue(user);
                loginSuccess.postValue(true);
            } else {
                errorMessage.postValue("Credenciais inválidas");
                loginSuccess.postValue(false);
            }
        } catch (InterruptedException e) {
            errorMessage.postValue("Erro ao fazer login");
        } finally {
            isLoading.postValue(false);
        }
    }).start();
}
```

#### **Observação de Estado**

```java
authViewModel.getLoginSuccess().observe(this, success -> {
    if (success != null && success) {
        User user = authViewModel.getAuthenticatedUser().getValue();
        preferencesHelper.saveUser(user);
        
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
});
```

**Características:**
- Validação em tempo real
- Loading state com ProgressBar
- Mensagens de erro via Snackbar
- Navegação para RegisterActivity/ForgotPasswordActivity

---

### 6.3 RegisterActivity

**Arquivo**: `RegisterActivity.java`  
**Layout**: `activity_register.xml`  
**ViewModel**: `AuthViewModel.java`

#### **Validações Implementadas**

```java
private void attemptRegister() {
    String username = editUsername.getText().toString().trim();
    String password = editPassword.getText().toString().trim();
    String confirmPassword = editConfirmPassword.getText().toString().trim();
    
    clearErrors();
    
    if (!ValidationUtils.isValidUsername(username)) {
        editUsername.setError("Mínimo 3 caracteres");
        return;
    }
    
    if (!ValidationUtils.isValidPassword(password)) {
        editPassword.setError("Mínimo 6 caracteres");
        return;
    }
    
    if (!password.equals(confirmPassword)) {
        editConfirmPassword.setError("Senhas não coincidem");
        return;
    }
    
    authViewModel.register(username, password);
}
```

**Classe de Validação:**
```java
public class ValidationUtils {
    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 3;
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
```

---

### 6.4 MainActivity

**Arquivo**: `MainActivity.java`  
**Layout**: `activity_main_new.xml`

#### **Estrutura**
```
MainActivity (Container)
├── BottomNavigationView
│   ├── nav_home (AnnouncementsFragment)
│   ├── nav_create (CreateAnnouncementActivity)
│   ├── nav_notifications (NotificationsFragment)
│   └── nav_locations (LocationsFragment)
└── FrameLayout (fragment_container)
```

#### **Fragment Management**

```java
private void loadFragment(Fragment fragment, String tag) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment existingFragment = fragmentManager.findFragmentByTag(tag);
    
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    
    // Esconde todos os fragments
    for (Fragment f : fragmentManager.getFragments()) {
        if (f != null && f.isAdded()) {
            transaction.hide(f);
        }
    }
    
    // Mostra ou adiciona o fragment
    if (existingFragment != null) {
        transaction.show(existingFragment);
    } else {
        transaction.add(R.id.fragment_container, fragment, tag);
    }
    
    transaction.commit();
}
```

**Vantagens:**
- Fragments não são recriados desnecessariamente
- Estado preservado em navegações
- Melhor performance

#### **Bottom Navigation Handler**

```java
bottomNavigationView.setOnItemSelectedListener(item -> {
    Fragment fragment = null;
    String tag = "";
    
    int itemId = item.getItemId();
    if (itemId == R.id.nav_home) {
        fragment = new AnnouncementsFragment();
        tag = "home";
    } else if (itemId == R.id.nav_create) {
        Intent intent = new Intent(this, CreateAnnouncementActivity.class);
        startActivity(intent);
        return true;
    } else if (itemId == R.id.nav_notifications) {
        fragment = new NotificationsFragment();
        tag = "notifications";
    } else if (itemId == R.id.nav_locations) {
        fragment = new LocationsFragment();
        tag = "locations";
    }
    
    if (fragment != null) {
        loadFragment(fragment, tag);
        return true;
    }
    
    return false;
});
```

#### **Back Press Handling**

```java
private void setupBackPressHandler() {
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                finish();
            }
        }
    };
    getOnBackPressedDispatcher().addCallback(this, callback);
}
```

---

### 6.5 AnnouncementsFragment

**Arquivo**: `AnnouncementsFragment.java`  
**Layout**: `fragment_announcements.xml`  
**ViewModel**: `AnnouncementViewModel`

#### **Funcionalidades**
- Listagem de anúncios (Todos/Guardados)
- Pesquisa com filtro em tempo real
- Sistema de tabs (TabLayout)
- RecyclerView com CardView

#### **Implementação de Tabs**

```java
private void setupTabs() {
    tabLayout.addTab(tabLayout.newTab().setText("💾 Guardados"));
    tabLayout.addTab(tabLayout.newTab().setText("🌍 Todos"));
    
    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            currentTab = tab.getPosition() == 0 ? "saved" : "all";
            filterAnnouncements();
        }
        
        @Override
        public void onTabUnselected(TabLayout.Tab tab) {}
        
        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    });
}
```

#### **Sistema de Pesquisa**

```java
editSearch.addTextChangedListener(new TextWatcher() {
    @Override
    public void afterTextChanged(Editable s) {
        searchQuery = s.toString().toLowerCase();
        filterAnnouncements();
    }
    
    // ...outros métodos
});
```

```java
private void filterAnnouncements() {
    List<Announcement> filtered = new ArrayList<>();
    
    for (Announcement ann : allAnnouncements) {
        // Filtro por tab
        if (currentTab.equals("saved")) {
            if (!savedAnnouncementIds.contains(ann.getId())) {
                continue;
            }
        }
        
        // Filtro por pesquisa
        if (!searchQuery.isEmpty()) {
            if (!ann.getTitle().toLowerCase().contains(searchQuery) &&
                !ann.getContent().toLowerCase().contains(searchQuery)) {
                continue;
            }
        }
        
        filtered.add(ann);
    }
    
    adapter.submitList(filtered);
    updateEmptyState(filtered.isEmpty());
}
```

#### **RecyclerView Setup**

```java
recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
recyclerView.setHasFixedSize(true);

adapter = new AnnouncementCardAdapter(new AnnouncementCardAdapter.OnAnnouncementClickListener() {
    @Override
    public void onAnnouncementClick(Announcement announcement) {
        Intent intent = new Intent(getActivity(), AnnouncementDetailActivity.class);
        intent.putExtra(AnnouncementDetailActivity.EXTRA_ANNOUNCEMENT_ID, announcement.getId());
        startActivity(intent);
    }
    
    @Override
    public void onSaveClick(Announcement announcement) {
        toggleSaveAnnouncement(announcement.getId());
    }
});

recyclerView.setAdapter(adapter);
```

---

### 6.6 CreateAnnouncementActivity

**Arquivo**: `CreateAnnouncementActivity.java`  
**Layout**: `activity_create_announcement.xml`  
**ViewModels**: `AnnouncementViewModel`, `LocationViewModel`

#### **Componentes UI Complexos**

##### **Date/Time Pickers**

```java
private void setupDateTimePickers() {
    editStartDate.setOnClickListener(v -> showDatePicker(true));
    editEndDate.setOnClickListener(v -> showDatePicker(false));
    editStartTime.setOnClickListener(v -> showTimePicker(true));
    editEndTime.setOnClickListener(v -> showTimePicker(false));
}

private void showDatePicker(boolean isStartDate) {
    Calendar calendar = Calendar.getInstance();
    
    DatePickerDialog datePickerDialog = new DatePickerDialog(
        this,
        (view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(selectedDate.getTime());
            
            if (isStartDate) {
                editStartDate.setText(formattedDate);
                startDateTimestamp = selectedDate.getTimeInMillis();
            } else {
                editEndDate.setText(formattedDate);
                endDateTimestamp = selectedDate.getTimeInMillis();
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    );
    
    datePickerDialog.show();
}
```

##### **AutoCompleteTextView para Localizações**

```java
private void loadLocations() {
    locationViewModel.loadLocations();
}

private void observeLocations() {
    locationViewModel.getLocations().observe(this, locationList -> {
        if (locationList != null) {
            locations = locationList;
            
            List<String> locationNames = new ArrayList<>();
            for (Location loc : locationList) {
                locationNames.add(loc.getName());
            }
            
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                locationNames
            );
            
            spinnerLocation.setAdapter(adapter);
        }
    });
}
```

#### **Sistema de Políticas**

##### **RadioGroup para Política**

```java
radioGroupPolicy.setOnCheckedChangeListener((group, checkedId) -> {
    if (checkedId == R.id.radioEveryone) {
        selectedPolicy = Constants.DELIVERY_POLICY_EVERYONE;
        btnConfigurePolicy.setVisibility(View.GONE);
    } else if (checkedId == R.id.radioWhitelist) {
        selectedPolicy = Constants.DELIVERY_POLICY_WHITELIST;
        btnConfigurePolicy.setVisibility(View.VISIBLE);
    } else if (checkedId == R.id.radioBlacklist) {
        selectedPolicy = Constants.DELIVERY_POLICY_BLACKLIST;
        btnConfigurePolicy.setVisibility(View.VISIBLE);
    }
});
```

##### **Activity Result API para Configuração**

```java
private ActivityResultLauncher<Intent> configurePolicyLauncher;

// No onCreate()
configurePolicyLauncher = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            PolicyFilter filter = (PolicyFilter) result.getData()
                .getSerializableExtra(ConfigurePolicyActivity.EXTRA_POLICY_FILTER);
            
            if (filter != null && !filter.isEmpty()) {
                policyFilter = filter;
                String message = selectedPolicy.equals(Constants.DELIVERY_POLICY_WHITELIST)
                    ? "Whitelist: " + filter.toString()
                    : "Blacklist: " + filter.toString();
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
);

// Ao clicar no botão
btnConfigurePolicy.setOnClickListener(v -> {
    Intent intent = new Intent(this, ConfigurePolicyActivity.class);
    configurePolicyLauncher.launch(intent);
});
```

#### **Criação do Anúncio**

```java
private void saveAnnouncement() {
    // Validações
    String title = editTitle.getText().toString().trim();
    String content = editContent.getText().toString().trim();
    
    if (title.isEmpty() || content.isEmpty()) {
        Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        return;
    }
    
    // Cria anúncio via ViewModel
    announcementViewModel.createAnnouncement(
        title,
        content,
        selectedLocationId,
        startDateTimestamp,
        endDateTimestamp,
        selectedPolicy,
        policyFilter
    );
}
```

**No ViewModel:**
```java
public void createAnnouncement(String title, String content, String locationId, 
                               long startDate, long endDate, String deliveryPolicy, 
                               PolicyFilter policyFilter) {
    isLoading.setValue(true);
    
    new Thread(() -> {
        try {
            Thread.sleep(1000);
            
            // Converte PolicyFilter em PolicyRules
            List<PolicyRule> policyRules = new ArrayList<>();
            if (policyFilter != null) {
                for (Map.Entry<String, String> entry : policyFilter.getAttributes().entrySet()) {
                    PolicyRule rule = new PolicyRule();
                    rule.setAttributeName(entry.getKey());
                    rule.setAttributeValue(entry.getValue());
                    policyRules.add(rule);
                }
            }
            
            // Cria objeto Announcement
            Announcement announcement = new Announcement();
            announcement.setTitle(title);
            announcement.setContent(content);
            announcement.setLocationId(locationId);
            announcement.setStartDate(new Date(startDate));
            announcement.setEndDate(new Date(endDate));
            announcement.setDeliveryPolicy(deliveryPolicy);
            announcement.setPolicyRules(policyRules);
            
            // Define autor
            User currentUser = userRepository.getCurrentUser();
            if (currentUser != null) {
                announcement.setAuthorId(currentUser.getId());
                announcement.setAuthorName(currentUser.getName());
            }
            
            // Salva no repository
            Announcement created = announcementRepository.createAnnouncement(announcement);
            
            if (created != null) {
                announcementCreated.postValue(true);
                operationSuccess.postValue(true);
            } else {
                errorMessage.postValue("Erro ao criar anúncio");
                operationSuccess.postValue(false);
            }
        } catch (InterruptedException e) {
            errorMessage.postValue("Erro ao criar anúncio");
        } finally {
            isLoading.postValue(false);
        }
    }).start();
}
```

---

### 6.7 ConfigurePolicyActivity

**Arquivo**: `ConfigurePolicyActivity.java`  
**Layout**: `activity_configure_policy.xml`  
**Adapter**: `ProfileAttributeAdapter.java`

#### **RecyclerView com Checkboxes**

```java
private void setupRecyclerView() {
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    
    adapter = new ProfileAttributeAdapter(availableAttributes);
    recyclerView.setAdapter(adapter);
}

private void loadAvailableAttributes() {
    availableAttributes = new ArrayList<>();
    
    // Interesses
    availableAttributes.add(new ProfileAttributeAdapter.ProfileAttribute("interesse", "Tecnologia"));
    availableAttributes.add(new ProfileAttributeAdapter.ProfileAttribute("interesse", "Desporto"));
    availableAttributes.add(new ProfileAttributeAdapter.ProfileAttribute("interesse", "Música"));
    
    // Profissões
    availableAttributes.add(new ProfileAttributeAdapter.ProfileAttribute("profissao", "Estudante"));
    availableAttributes.add(new ProfileAttributeAdapter.ProfileAttribute("profissao", "Engenheiro"));
    availableAttributes.add(new ProfileAttributeAdapter.ProfileAttribute("profissao", "Professor"));
    
    // Clubes
    availableAttributes.add(new ProfileAttributeAdapter.ProfileAttribute("clube", "Benfica"));
    availableAttributes.add(new ProfileAttributeAdapter.ProfileAttribute("clube", "1º de Agosto"));
    
    // Faixa Etária
    availableAttributes.add(new ProfileAttributeAdapter.ProfileAttribute("faixa_etaria", "18-24"));
    availableAttributes.add(new ProfileAttributeAdapter.ProfileAttribute("faixa_etaria", "25-34"));
    
    // Cidade
    availableAttributes.add(new ProfileAttributeAdapter.ProfileAttribute("cidade", "Luanda"));
    availableAttributes.add(new ProfileAttributeAdapter.ProfileAttribute("cidade", "Benguela"));
}
```

#### **ProfileAttributeAdapter**

```java
public class ProfileAttributeAdapter extends RecyclerView.Adapter<ProfileAttributeAdapter.ViewHolder> {
    
    public static class ProfileAttribute {
        private String key;
        private String value;
        private boolean isSelected;
        
        public ProfileAttribute(String key, String value) {
            this.key = key;
            this.value = value;
            this.isSelected = false;
        }
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileAttribute attribute = attributes.get(position);
        
        holder.textKey.setText(attribute.getKey());
        holder.textValue.setText(attribute.getValue());
        holder.checkbox.setChecked(attribute.isSelected());
        
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            attribute.setSelected(isChecked);
        });
    }
    
    public Map<String, String> getSelectedAttributes() {
        Map<String, String> selected = new HashMap<>();
        for (ProfileAttribute attr : attributes) {
            if (attr.isSelected()) {
                selected.put(attr.getKey(), attr.getValue());
            }
        }
        return selected;
    }
}
```

#### **Retorno do Resultado**

```java
btnSave.setOnClickListener(v -> {
    Map<String, String> selectedAttributes = adapter.getSelectedAttributes();
    
    if (selectedAttributes.isEmpty()) {
        Toast.makeText(this, "Selecione pelo menos um atributo", Toast.LENGTH_SHORT).show();
        return;
    }
    
    PolicyFilter filter = new PolicyFilter(selectedAttributes);
    
    Intent resultIntent = new Intent();
    resultIntent.putExtra(EXTRA_POLICY_FILTER, filter);
    setResult(RESULT_OK, resultIntent);
    finish();
});
```

---

### 6.8 SettingsActivity

**Arquivo**: `SettingsActivity.java`  
**Layout**: `activity_settings.xml`

#### **Material Design 3 Layout**

```xml
<!-- Header com 200dp -->
<com.google.android.material.appbar.MaterialToolbar
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:background="@color/blue_primary" />

<!-- Cards para cada seção -->
<com.google.android.material.card.MaterialCardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardCornerRadius="12dp"
    app:cardElevation="2dp">
    
    <com.google.android.material.switchmaterial.SwitchMaterial
        android:id="@+id/switchNotifications"
        android:text="Notificações" />
</com.google.android.material.card.MaterialCardView>
```

#### **Gerenciamento de Permissões**

```java
private void setupListeners() {
    switchLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
        if (isChecked) {
            requestLocationPermission();
        } else {
            saveSettings();
        }
    });
    
    switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
        if (isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission();
        } else {
            saveSettings();
        }
    });
}

private void requestLocationPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
            REQUEST_CODE_LOCATION);
    }
}

@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                       @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    
    if (requestCode == REQUEST_CODE_LOCATION) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            tvLocationStatus.setText("Localização ativa");
            saveSettings();
        } else {
            switchLocation.setChecked(false);
            tvLocationStatus.setText("Permissão negada");
        }
    }
}
```

#### **Persistência com SharedPreferences**

```java
private void saveSettings() {
    SharedPreferences.Editor editor = prefs.edit();
    editor.putBoolean(KEY_LOCATION, switchLocation.isChecked());
    editor.putBoolean(KEY_WIFI, switchWiFi.isChecked());
    editor.putBoolean(KEY_NOTIFICATIONS, switchNotifications.isChecked());
    editor.putBoolean(KEY_MULA_MODE, switchMulaMode.isChecked());
    editor.apply(); // Assíncrono
    
    Toast.makeText(this, "Configurações salvas", Toast.LENGTH_SHORT).show();
}

private void loadSettings() {
    switchLocation.setChecked(prefs.getBoolean(KEY_LOCATION, false));
    switchWiFi.setChecked(prefs.getBoolean(KEY_WIFI, false));
    switchNotifications.setChecked(prefs.getBoolean(KEY_NOTIFICATIONS, true));
    switchMulaMode.setChecked(prefs.getBoolean(KEY_MULA_MODE, false));
}
```

---

### 6.9 InterestsActivity

**Arquivo**: `InterestsActivity.java`  
**Layout**: `activity_interests.xml`

#### **Sistema de Chave-Valor Dinâmico**

```java
private LinkedHashMap<String, String> pares = new LinkedHashMap<>();

private void setupListeners() {
    btnSalvar.setOnClickListener(v -> {
        String chave = editChave.getText().toString().trim();
        String valor = editValor.getText().toString().trim();
        
        if (!chave.isEmpty() && !valor.isEmpty()) {
            pares.put(chave, valor);
            atualizarLista();
            editChave.setText("");
            editValor.setText("");
        }
    });
}

private void atualizarLista() {
    listaPares.removeAllViews();
    
    for (Map.Entry<String, String> entry : pares.entrySet()) {
        Chip chip = new Chip(this);
        chip.setText(entry.getKey() + ": " + entry.getValue());
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            pares.remove(entry.getKey());
            atualizarLista();
        });
        listaPares.addView(chip);
    }
}
```

**Componentes:**
- `EditText` para chave e valor
- `ChipGroup` para exibir pares
- `Chip` com ícone de fechar para remover

**Uso:**
- Adicionar atributos personalizados ao perfil
- Exemplo: "Profissão: Engenheiro", "Clube: Benfica"

---

## 7. Comunicação Cliente-Servidor

### 7.1 Arquitetura Atual (Mock)

Atualmente, **NÃO há comunicação real** com servidor. Todos os dados são **mockados** nos Repositories.

```
┌──────────────────────┐
│  Activity/Fragment   │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│     ViewModel        │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│    Repository        │
│  (In-Memory Mock)    │
└──────────────────────┘
```

### 7.1.1 Como Funciona a "API Falsa" (Mock)

Imagine que você tem uma loja, mas ao invés de ir buscar produtos num armazém real, você tem uma caixa de amostras na sua mesa. É exatamente isso que fazemos no projeto atual.

#### **O que é um Mock?**

Um **Mock** é uma "imitação" de dados reais. É como se fosse um jogo de faz-de-conta onde simulamos que estamos a receber informações de um servidor, mas na verdade os dados estão guardados dentro da própria aplicação.

#### **Porque Usar Mock?**

1. **Desenvolvimento Rápido**: Não precisamos esperar que o servidor esteja pronto
2. **Testes Fáceis**: Podemos testar a aplicação sem internet
3. **Sem Custos**: Não gastamos dinheiro com servidores enquanto desenvolvemos
4. **Demonstração**: Podemos mostrar a aplicação funcionando em qualquer lugar

#### **Onde Estão os Dados Mock?**

Os dados ficam guardados numa estrutura chamada `HashMap`, que é como um armário com gavetas etiquetadas:

```java
// Exemplo: Armário de Usuários
private Map<String, User> usersDatabase = new HashMap<>();

// Adicionar um usuário (como colocar algo numa gaveta)
User alice = new User("1", "alice", "alice@example.com", "Alice Silva");
usersDatabase.put("1", alice);  // Gaveta "1" tem a Alice

// Buscar um usuário (como abrir uma gaveta)
User usuario = usersDatabase.get("1");  // Abre a gaveta "1" e pega a Alice
```

#### **Como São Criados os Dados de Teste**

No momento em que a aplicação inicia, o Repository cria dados falsos:

```java
private void initializeMockData() {
    // Criar 5 usuários de exemplo
    User alice = new User("1", "alice", "alice@example.com", "Alice Silva");
    alice.setPassword("password123");
    alice.getProfileAttributes().put("interesse", "Tecnologia");
    usersDatabase.put("1", alice);
    
    User bob = new User("2", "bob", "bob@example.com", "Bob Santos");
    bob.setPassword("password123");
    bob.getProfileAttributes().put("interesse", "Desporto");
    usersDatabase.put("2", bob);
    
    // ... mais 3 usuários
    
    // Criar 10 anúncios de exemplo
    Announcement ann1 = new Announcement("1", "Workshop de IA", 
        "Aprenda sobre Inteligência Artificial", "1", "1");
    announcementsDatabase.put("1", ann1);
    
    // ... mais 9 anúncios
}
```

É como se estivéssemos a preparar uma loja de brinquedos antes de abrir: colocamos produtos nas prateleiras para que os clientes possam ver e comprar.

#### **Simulando Latência de Rede**

Para tornar a simulação mais realista, fazemos a aplicação "esperar" um pouco, como se estivesse realmente a comunicar com um servidor distante:

```java
public void loadAnnouncements() {
    new Thread(() -> {
        try {
            Thread.sleep(500);  // Espera meio segundo (simula internet lenta)
            
            List<Announcement> data = announcementsDatabase.values();
            announcements.postValue(data);  // Mostra os dados
        } catch (InterruptedException e) {
            // Se algo correr mal
        }
    }).start();
}
```

Quando você clica para ver anúncios, a aplicação espera 500 milissegundos (meio segundo) antes de mostrar os dados. Isso faz parecer que realmente foi buscar informações num servidor na internet!

### 7.2 Preparação para Retrofit (Futura)

A arquitetura está preparada para integração futura com Retrofit:

#### **O que é Retrofit?**

Imagine que você quer encomendar uma pizza por telefone. O Retrofit é como ter um assistente que:
1. Faz a chamada para você
2. Diz exactamente o que quer (pizza, tamanho, ingredientes)
3. Espera pela resposta
4. Traz-lhe a pizza quando chega

No mundo da programação, o **Retrofit** é uma biblioteca que facilita a comunicação entre a aplicação Android e um servidor na internet. Ele transforma pedidos complicados em chamadas simples de código.

#### **Como o Retrofit Funciona?**

```java
// 1. Definimos o que queremos pedir (Interface)
public interface AnnouncementApiService {
    // "Quero buscar todos os anúncios"
    @GET("announcements")
    Call<List<Announcement>> getAnnouncements();
    
    // "Quero buscar um anúncio específico pelo ID"
    @GET("announcements/{id}")
    Call<Announcement> getAnnouncementById(@Path("id") String id);
    
    // "Quero criar um novo anúncio"
    @POST("announcements")
    Call<Announcement> createAnnouncement(@Body Announcement announcement);
    
    // "Quero atualizar um anúncio existente"
    @PUT("announcements/{id}")
    Call<Announcement> updateAnnouncement(
        @Path("id") String id, 
        @Body Announcement announcement
    );
    
    // "Quero apagar um anúncio"
    @DELETE("announcements/{id}")
    Call<Void> deleteAnnouncement(@Path("id") String id);
}
```

**Explicação simples de cada anotação:**

- **@GET**: "Vou buscar informações" (como abrir um livro para ler)
- **@POST**: "Vou enviar informações novas" (como escrever numa folha em branco)
- **@PUT**: "Vou atualizar informações existentes" (como corrigir algo que já escreveu)
- **@DELETE**: "Vou apagar informações" (como usar uma borracha)
- **@Path("id")**: "Substitua {id} pelo número que eu der"
- **@Body**: "Envie estas informações completas no pedido"

#### **Configuração do Retrofit**

Antes de usar, precisamos "montar" o Retrofit, como montar um puzzle:

```java
// Criar o Retrofit (como construir o nosso assistente telefónico)
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.anunciosloc.ao/")  // Endereço do servidor
    .addConverterFactory(GsonConverterFactory.create())  // Tradutor de JSON
    .build();

// Criar o serviço (dar instruções ao assistente)
AnnouncementApiService apiService = retrofit.create(AnnouncementApiService.class);
```

**O que significa cada parte:**

1. **baseUrl**: É como o endereço da pizzaria. Todas as chamadas vão começar com este endereço.
2. **GsonConverterFactory**: É o "tradutor". O servidor fala em JSON (uma linguagem de dados), e o Gson traduz para objetos Java que conseguimos usar.
3. **build()**: "Pronto, está montado!"

#### **Como Usar o Retrofit no Repository**

Agora podemos fazer pedidos ao servidor:

```java
public class AnnouncementRepository {
    private AnnouncementApiService apiService;
    
    // Buscar todos os anúncios
    public void getAnnouncements(Callback<List<Announcement>> callback) {
        // Fazer o pedido ao servidor
        apiService.getAnnouncements().enqueue(new Callback<List<Announcement>>() {
            
            @Override
            public void onResponse(Call<List<Announcement>> call, 
                                 Response<List<Announcement>> response) {
                // Se o servidor respondeu com sucesso
                if (response.isSuccessful()) {
                    List<Announcement> anuncios = response.body();
                    callback.onSuccess(anuncios);  // "Aqui estão os anúncios!"
                } else {
                    callback.onError("Erro: " + response.code());  // "Algo correu mal"
                }
            }
            
            @Override
            public void onFailure(Call<List<Announcement>> call, Throwable t) {
                // Se não conseguimos falar com o servidor (sem internet, etc.)
                callback.onError("Sem conexão: " + t.getMessage());
            }
        });
    }
    
    // Criar um novo anúncio
    public void createAnnouncement(Announcement announcement, 
                                   Callback<Announcement> callback) {
        apiService.createAnnouncement(announcement).enqueue(
            new Callback<Announcement>() {
                @Override
                public void onResponse(Call<Announcement> call, 
                                     Response<Announcement> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        callback.onError("Não foi possível criar o anúncio");
                    }
                }
                
                @Override
                public void onFailure(Call<Announcement> call, Throwable t) {
                    callback.onError("Erro de conexão");
                }
            }
        );
    }
}
```

**Fluxo de Comunicação Completo:**

```
1. Usuário clica no botão "Criar Anúncio"
2. Activity chama ViewModel
3. ViewModel chama Repository
4. Repository usa Retrofit para fazer pedido HTTP
5. Retrofit converte o Anúncio para JSON
6. Envia para o servidor pela internet
7. Servidor processa e responde com JSON
8. Retrofit converte JSON de volta para Anúncio
9. Repository recebe a resposta
10. ViewModel atualiza o LiveData
11. Activity observa a mudança e mostra ao usuário
```

É como uma cadeia de pessoas passando uma bola - cada um tem seu papel!

### 7.3 Estrutura de Dados no Servidor (Planejado)

#### **Como Funciona um Servidor de Verdade?**

Pense num servidor como uma biblioteca gigante:
- **Base de Dados**: As estantes com livros (onde ficam guardadas as informações)
- **API**: O bibliotecário (que busca e organiza os livros para você)
- **Aplicação Android**: O leitor (você, que pede livros ao bibliotecário)

#### **Base de Dados - Onde Guardar as Informações**

Uma base de dados é como um conjunto de tabelas (tipo Excel) onde guardamos informações organizadas.

##### **Tabela de Utilizadores (users)**

Esta tabela guarda informações sobre quem usa a aplicação:

```sql
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,           -- Número único do utilizador
    username VARCHAR(50) UNIQUE NOT NULL, -- Nome de utilizador (ex: "alice")
    email VARCHAR(100) UNIQUE NOT NULL,   -- Email (ex: "alice@example.com")
    password_hash VARCHAR(255) NOT NULL,  -- Senha encriptada (segura)
    name VARCHAR(100),                    -- Nome completo (ex: "Alice Silva")
    phone_number VARCHAR(20),             -- Telefone (ex: "+244 923 456 789")
    photo_url VARCHAR(255),               -- Link da foto de perfil
    public_key TEXT,                      -- Chave de encriptação
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Data de registo
);
```

**Explicação simples:**
- **VARCHAR(50)**: Texto com no máximo 50 caracteres
- **PRIMARY KEY**: Identificador único (como o número do Bilhete de Identidade)
- **UNIQUE**: Não pode haver dois iguais (como duas pessoas não podem ter o mesmo email)
- **NOT NULL**: Campo obrigatório (tem que preencher)
- **TIMESTAMP**: Data e hora

**Exemplo de dados guardados:**

| id | username | email | name | phone_number |
|----|----------|-------|------|--------------|
| 1 | alice | alice@example.com | Alice Silva | +244 923 111 222 |
| 2 | bob | bob@example.com | Bob Santos | +244 923 333 444 |

##### **Tabela de Atributos de Perfil (user_attributes)**

Esta tabela guarda os interesses e características de cada utilizador:

```sql
CREATE TABLE user_attributes (
    id INT AUTO_INCREMENT PRIMARY KEY,    -- Número automático
    user_id VARCHAR(36),                  -- A quem pertence este atributo
    attribute_key VARCHAR(50),            -- Nome do atributo (ex: "interesse")
    attribute_value VARCHAR(100),         -- Valor (ex: "Tecnologia")
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_attribute (user_id, attribute_key)
);
```

**Explicação simples:**
- **AUTO_INCREMENT**: O número aumenta sozinho (1, 2, 3, 4...)
- **FOREIGN KEY**: Liga esta tabela à tabela de utilizadores (como uma seta a apontar)
- **ON DELETE CASCADE**: Se apagar o utilizador, apaga também os seus atributos

**Exemplo de dados guardados:**

| id | user_id | attribute_key | attribute_value |
|----|---------|---------------|-----------------|
| 1 | 1 | interesse | Tecnologia |
| 2 | 1 | profissao | Estudante |
| 3 | 1 | clube | Benfica |
| 4 | 2 | interesse | Desporto |
| 5 | 2 | profissao | Engenheiro |

##### **Tabela de Anúncios (announcements)**

Esta tabela guarda os anúncios criados:

```sql
CREATE TABLE announcements (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,          -- Título do anúncio
    content TEXT NOT NULL,                -- Descrição completa
    location_id VARCHAR(36),              -- Onde foi publicado
    author_id VARCHAR(36),                -- Quem criou
    start_date TIMESTAMP,                 -- Quando começa a ser válido
    end_date TIMESTAMP,                   -- Quando expira
    delivery_policy ENUM('EVERYONE', 'WHITELIST', 'BLACKLIST') DEFAULT 'EVERYONE',
    status ENUM('ACTIVE', 'EXPIRED', 'DRAFT') DEFAULT 'ACTIVE',
    view_count INT DEFAULT 0,             -- Quantas vezes foi visto
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (location_id) REFERENCES locations(id),
    FOREIGN KEY (author_id) REFERENCES users(id)
);
```

**Explicação simples:**
- **TEXT**: Texto longo (sem limite de caracteres)
- **ENUM**: Só pode ser um dos valores da lista
- **DEFAULT 0**: Se não especificar, usa 0
- **ON UPDATE CURRENT_TIMESTAMP**: Atualiza a data automaticamente quando houver mudanças

##### **Tabela de Regras de Política (policy_rules)**

Esta tabela guarda os filtros de cada anúncio (quem pode ver):

```sql
CREATE TABLE policy_rules (
    id INT AUTO_INCREMENT PRIMARY KEY,
    announcement_id VARCHAR(36),          -- A que anúncio pertence
    attribute_name VARCHAR(50),           -- Nome do filtro (ex: "interesse")
    attribute_value VARCHAR(100),         -- Valor necessário (ex: "Tecnologia")
    FOREIGN KEY (announcement_id) REFERENCES announcements(id) ON DELETE CASCADE
);
```

**Exemplo prático:**

Imagina um anúncio sobre um Workshop de Programação:

**Tabela announcements:**
| id | title | delivery_policy | author_id |
|----|-------|-----------------|-----------|
| 101 | Workshop de IA | WHITELIST | 1 |

**Tabela policy_rules:**
| id | announcement_id | attribute_name | attribute_value |
|----|-----------------|----------------|-----------------|
| 1 | 101 | interesse | Tecnologia |
| 2 | 101 | profissao | Estudante |

**Resultado**: Só utilizadores que tenham `interesse=Tecnologia` E `profissao=Estudante` vão receber este anúncio.

#### **Relacionamento Entre Tabelas**

Imagine as tabelas como famílias ligadas por parentesco:

```
        users (Pais)
          |
    ┌─────┴─────┐
    |           |
user_attributes  announcements (Filhos)
                     |
              policy_rules (Netos)
```

- Um **User** pode ter vários **user_attributes**
- Um **User** pode criar vários **announcements**
- Um **Announcement** pode ter várias **policy_rules**
- Se apagar um **User**, apagam-se também os seus **announcements** e **attributes** (CASCADE)

#### **API Endpoints (REST)**

**O que é um Endpoint?**

Um endpoint é como um balcão de atendimento específico. Numa repartição pública:
- **Balcão 1**: Emitir Bilhetes de Identidade
- **Balcão 2**: Renovar Passaportes
- **Balcão 3**: Pagar Multas

Na nossa API, cada endpoint faz uma coisa específica:

##### **Endpoints de Autenticação**

Estes são para entrar, sair e registar na aplicação:

```
POST /api/auth/login
```
**O que faz:** Fazer login (entrar na aplicação)

**Como usar:**
```json
Enviar:
{
  "username": "alice",
  "password": "password123"
}

Receber:
{
  "success": true,
  "user": {
    "id": "1",
    "username": "alice",
    "email": "alice@example.com",
    "name": "Alice Silva"
  },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Explicação:** É como mostrar o seu Bilhete de Identidade na entrada de um edifício. Se estiver correcto, dão-lhe um cartão de acesso (token).

---

```
POST /api/auth/register
```
**O que faz:** Criar uma conta nova

**Como usar:**
```json
Enviar:
{
  "username": "carlos",
  "email": "carlos@example.com",
  "password": "senha123",
  "name": "Carlos Mendes"
}

Receber:
{
  "success": true,
  "message": "Utilizador criado com sucesso",
  "user_id": "6"
}
```

---

```
POST /api/auth/logout
```
**O que faz:** Fazer logout (sair da aplicação)

**Como usar:**
```json
Enviar:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}

Receber:
{
  "success": true,
  "message": "Sessão terminada"
}
```

---

```
GET /api/auth/me
```
**O que faz:** Ver informações sobre quem está logado

**Como usar:**
```json
Enviar: (apenas o token no header)
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

Receber:
{
  "id": "1",
  "username": "alice",
  "email": "alice@example.com",
  "name": "Alice Silva",
  "profileAttributes": {
    "interesse": "Tecnologia",
    "profissao": "Estudante"
  }
}
```

##### **Endpoints de Anúncios**

Estes são para gerir os anúncios:

```
GET /api/announcements
```
**O que faz:** Listar todos os anúncios disponíveis

**Como usar:**
```json
Receber:
[
  {
    "id": "101",
    "title": "Workshop de IA",
    "content": "Aprenda sobre Inteligência Artificial",
    "locationName": "ISPTEC",
    "authorName": "Alice Silva",
    "startDate": "2025-11-10T09:00:00",
    "endDate": "2025-11-10T17:00:00",
    "deliveryPolicy": "WHITELIST",
    "status": "ACTIVE"
  },
  {
    "id": "102",
    "title": "Promoção de Verão",
    "content": "Descontos em todas as lojas!",
    "locationName": "Belas Shopping",
    "authorName": "Bob Santos",
    "endDate": "2025-11-30T23:59:59",
    "deliveryPolicy": "EVERYONE",
    "status": "ACTIVE"
  }
]
```

---

```
GET /api/announcements/{id}
```
**O que faz:** Ver detalhes de um anúncio específico

**Exemplo:** `GET /api/announcements/101`

```json
Receber:
{
  "id": "101",
  "title": "Workshop de IA",
  "content": "Aprenda sobre Inteligência Artificial. Inscrições limitadas!",
  "locationId": "1",
  "locationName": "ISPTEC",
  "authorId": "1",
  "authorName": "Alice Silva",
  "startDate": "2025-11-10T09:00:00",
  "endDate": "2025-11-10T17:00:00",
  "deliveryPolicy": "WHITELIST",
  "policyRules": [
    {
      "attributeName": "interesse",
      "attributeValue": "Tecnologia"
    }
  ],
  "status": "ACTIVE",
  "viewCount": 47,
  "createdAt": "2025-11-01T10:30:00"
}
```

---

```
POST /api/announcements
```
**O que faz:** Criar um novo anúncio

**Como usar:**
```json
Enviar:
{
  "title": "Aula de Yoga Gratuita",
  "content": "Venha relaxar connosco. Todos os níveis bem-vindos!",
  "locationId": "3",
  "startDate": "2025-11-15T07:00:00",
  "endDate": "2025-11-15T08:30:00",
  "deliveryPolicy": "EVERYONE"
}

Receber:
{
  "success": true,
  "message": "Anúncio criado com sucesso",
  "announcement": {
    "id": "103",
    "title": "Aula de Yoga Gratuita",
    "status": "ACTIVE",
    "createdAt": "2025-11-07T14:30:00"
  }
}
```

---

```
PUT /api/announcements/{id}
```
**O que faz:** Atualizar um anúncio existente

**Exemplo:** `PUT /api/announcements/103`

```json
Enviar:
{
  "title": "Aula de Yoga GRÁTIS - Vagas Limitadas",
  "content": "Venha relaxar connosco. APENAS 20 VAGAS!"
}

Receber:
{
  "success": true,
  "message": "Anúncio atualizado com sucesso"
}
```

---

```
DELETE /api/announcements/{id}
```
**O que faz:** Apagar um anúncio

**Exemplo:** `DELETE /api/announcements/103`

```json
Receber:
{
  "success": true,
  "message": "Anúncio apagado com sucesso"
}
```

---

```
GET /api/announcements/filtered
```
**O que faz:** Buscar anúncios filtrados pelas minhas características

**Como usar:**
```json
Enviar: (o servidor vê os atributos do utilizador logado)

Receber: (apenas anúncios que o utilizador pode ver)
[
  {
    "id": "101",
    "title": "Workshop de IA",
    ...
  }
]
```

##### **Endpoints de Localizações**

```
GET /api/locations
```
**O que faz:** Listar todas as localizações

```json
Receber:
[
  {
    "id": "1",
    "name": "ISPTEC",
    "address": "Talatona, Luanda",
    "latitude": -8.9094,
    "longitude": 13.1842
  },
  {
    "id": "2",
    "name": "Marginal de Luanda",
    "address": "Avenida 4 de Fevereiro",
    "latitude": -8.8159,
    "longitude": 13.2306
  }
]
```

---

```
GET /api/locations/nearby?lat={latitude}&lng={longitude}&radius={metros}
```
**O que faz:** Encontrar localizações perto de mim

**Exemplo:** `GET /api/locations/nearby?lat=-8.9094&lng=13.1842&radius=5000`

(Buscar localizações num raio de 5km)

```json
Receber:
[
  {
    "id": "1",
    "name": "ISPTEC",
    "distance": 0
  },
  {
    "id": "5",
    "name": "Belas Shopping",
    "distance": 2300
  }
]
```

##### **Endpoints de Utilizadores**

```
GET /api/users/{id}
```
**O que faz:** Ver perfil de um utilizador

```
PUT /api/users/{id}
```
**O que faz:** Atualizar o meu perfil

```
GET /api/users/{id}/attributes
```
**O que faz:** Ver os atributos de perfil

```
POST /api/users/{id}/attributes
```
**O que faz:** Adicionar novo atributo ao perfil

**Exemplo:**
```json
Enviar:
{
  "attribute_key": "clube",
  "attribute_value": "Benfica"
}

Receber:
{
  "success": true,
  "message": "Atributo adicionado com sucesso"
}
```

#### **Como o Servidor Processa os Pedidos**

Imagine este fluxo quando você quer criar um anúncio:

```
1. Você preenche o formulário na aplicação Android
   ↓
2. Aplicação envia pedido POST para /api/announcements
   ↓
3. Servidor recebe o pedido
   ↓
4. Servidor verifica se você está autenticado (token válido?)
   ↓
5. Servidor valida os dados (título não está vazio? data válida?)
   ↓
6. Servidor guarda na base de dados
   ↓
7. Servidor envia resposta de sucesso
   ↓
8. Aplicação mostra mensagem "Anúncio criado!"
```

#### **Códigos de Resposta HTTP**

O servidor responde com números que indicam o resultado:

| Código | Significado | Exemplo |
|--------|-------------|---------|
| **200** | OK - Tudo correu bem | Anúncio carregado com sucesso |
| **201** | Criado - Novo recurso criado | Novo anúncio criado |
| **400** | Pedido Inválido - Algo está errado nos dados | Campo título está vazio |
| **401** | Não Autorizado - Precisa fazer login | Token inválido ou expirado |
| **403** | Proibido - Não tem permissão | Tentar apagar anúncio de outra pessoa |
| **404** | Não Encontrado - Não existe | Anúncio com ID 999 não existe |
| **500** | Erro no Servidor - Problema técnico | Base de dados caiu |

**Exemplo prático:**

```java
// Tentar criar anúncio sem estar logado
Response: 401 Unauthorized
{
  "error": "Token não fornecido. Por favor, faça login."
}

// Tentar criar anúncio com título vazio
Response: 400 Bad Request
{
  "error": "O campo 'title' é obrigatório"
}

// Criar anúncio com sucesso
Response: 201 Created
{
  "success": true,
  "announcement": { ... }
}
```

---

## 7.4 Formato de Dados: JSON

#### **O que é JSON?**

JSON significa "JavaScript Object Notation". É uma forma de escrever informações que tanto humanos como computadores conseguem ler facilmente.

Pense no JSON como uma lista de compras bem organizada:

```json
{
  "nome": "Alice Silva",
  "idade": 22,
  "email": "alice@example.com",
  "interesses": ["Tecnologia", "Música", "Cinema"],
  "ativo": true
}
```

**Regras do JSON:**
1. Usa **chaves {}** para agrupar informações
2. Usa **colchetes []** para listas
3. Separa com **vírgulas**
4. Texto entre **"aspas"**
5. Números sem aspas
6. Valores verdadeiro/falso: `true` ou `false`

#### **Exemplo Real: Criar um Anúncio**

**O que a aplicação envia (JSON):**
```json
{
  "title": "Workshop de Programação",
  "content": "Aprenda Python do zero! Gratuito para estudantes.",
  "locationId": "1",
  "startDate": "2025-11-15T14:00:00",
  "endDate": "2025-11-15T18:00:00",
  "deliveryPolicy": "WHITELIST",
  "policyRules": [
    {
      "attributeName": "interesse",
      "attributeValue": "Tecnologia"
    },
    {
      "attributeName": "profissao",
      "attributeValue": "Estudante"
    }
  ]
}
```

**O que o servidor responde (JSON):**
```json
{
  "success": true,
  "message": "Anúncio criado com sucesso",
  "announcement": {
    "id": "104",
    "title": "Workshop de Programação",
    "content": "Aprenda Python do zero! Gratuito para estudantes.",
    "locationId": "1",
    "locationName": "ISPTEC",
    "authorId": "1",
    "authorName": "Alice Silva",
    "startDate": "2025-11-15T14:00:00",
    "endDate": "2025-11-15T18:00:00",
    "deliveryPolicy": "WHITELIST",
    "policyRules": [
      {
        "attributeName": "interesse",
        "attributeValue": "Tecnologia"
      },
      {
        "attributeName": "profissao",
        "attributeValue": "Estudante"
      }
    ],
    "status": "ACTIVE",
    "viewCount": 0,
    "createdAt": "2025-11-07T15:30:00",
    "updatedAt": "2025-11-07T15:30:00"
  }
}
```

#### **Como o Android Converte JSON em Objetos Java**

O **Gson** (biblioteca) faz a "mágica" de converter:

**JSON → Objeto Java:**
```java
// JSON recebido do servidor
String json = "{\"id\":\"1\",\"username\":\"alice\",\"email\":\"alice@example.com\"}";

// Converter para objeto User
Gson gson = new Gson();
User user = gson.fromJson(json, User.class);

// Agora podemos usar:
String nome = user.getUsername();  // "alice"
String email = user.getEmail();    // "alice@example.com"
```

**Objeto Java → JSON:**
```java
// Objeto User
User user = new User("1", "alice", "alice@example.com", "Alice Silva");

// Converter para JSON
Gson gson = new Gson();
String json = gson.toJson(user);

// Resultado:
// {"id":"1","username":"alice","email":"alice@example.com","name":"Alice Silva"}
```

---

## 7.5 Autenticação e Segurança

#### **Como Funciona o Login Seguro?**

Imagine que está a entrar num clube exclusivo:

**1. Mostrar Identidade (Login)**
```
Você: "Olá, sou a Alice, senha: password123"
Porteiro (Servidor): "Deixe-me verificar..." [consulta base de dados]
Porteiro: "Confirmado! Aqui está a sua pulseira VIP (token)"
```

**2. Usar a Pulseira (Token)**
```
Você: [mostra pulseira no bar]
Barman: "Pulseira válida! O que deseja pedir?"
```

**3. Pulseira Expira**
```
Você: [mostra pulseira antiga]
Segurança: "Esta pulseira expirou há 2 dias. Precisa renovar na entrada."
```

#### **Implementação Técnica**

**Passo 1: Fazer Login**

```java
// Aplicação Android envia
POST /api/auth/login
{
  "username": "alice",
  "password": "password123"
}

// Servidor responde
{
  "success": true,
  "user": {
    "id": "1",
    "username": "alice",
    "name": "Alice Silva"
  },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxIiwidXNlcm5hbWUiOiJhbGljZSIsImlhdCI6MTYzMDAwMDAwMCwiZXhwIjoxNjMwMDg2NDAwfQ.XYZ123"
}
```

**Passo 2: Guardar o Token**

```java
// Guardar token no SharedPreferences
SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
prefs.edit()
    .putString("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    .putBoolean("is_logged_in", true)
    .apply();
```

**Passo 3: Usar o Token em Pedidos**

```java
// Todas as chamadas à API incluem o token
GET /api/announcements
Headers:
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

```java
// No Retrofit
@GET("announcements")
Call<List<Announcement>> getAnnouncements(
    @Header("Authorization") String token
);

// Usar
String token = prefs.getString("token", "");
apiService.getAnnouncements("Bearer " + token);
```

#### **O que é um Token JWT?**

JWT significa "JSON Web Token". É como uma pulseira de festival que tem informações codificadas:

```
Pulseira:    eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

Decodificado:
{
  "userId": "1",
  "username": "alice",
  "iat": 1630000000,    // Emitido em (timestamp)
  "exp": 1630086400     // Expira em (timestamp - 24h depois)
}
```

**Vantagens do Token:**
- ✅ Servidor não precisa guardar sessões
- ✅ Pode verificar se é válido sem consultar base de dados
- ✅ Expira automaticamente (segurança)
- ✅ Contém informações do utilizador

#### **Encriptação de Senhas**

**NUNCA guardar senhas em texto simples!**

❌ **Errado:**
```sql
INSERT INTO users (username, password) VALUES ('alice', 'password123');
```

✅ **Correcto (com BCrypt):**
```java
// No servidor, ao registar utilizador
String senha = "password123";
String senhaEncriptada = BCrypt.hashpw(senha, BCrypt.gensalt());
// Resultado: "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"

// Guardar na base de dados
INSERT INTO users (username, password_hash) 
VALUES ('alice', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
```

**Ao fazer login:**
```java
// Utilizador envia: password123
String senhaEnviada = "password123";

// Buscar hash da base de dados
String hashGuardado = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

// Verificar se coincide
boolean senhaCorreta = BCrypt.checkpw(senhaEnviada, hashGuardado);
// true = pode entrar | false = senha errada
```

**Porquê BCrypt é seguro:**
1. Mesmo que alguém roube a base de dados, não consegue ver as senhas reais
2. É impossível reverter (não dá para "desencriptar")
3. Mesmo senhas iguais geram hashes diferentes
4. É lento de propósito (dificulta ataques de força bruta)

#### **HTTPS - Comunicação Encriptada**

Todos os dados entre a aplicação e o servidor devem viajar encriptados:

```
SEM HTTPS (INSEGURO):
Aplicação → Internet → Servidor
  "alice:password123" (qualquer pessoa pode ler!)

COM HTTPS (SEGURO):
Aplicação → [ENCRIPTADO] → Servidor
  "8f3k2jd9sk2md..." (ninguém consegue ler!)
```

No Retrofit, usar sempre `https://`:
```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.anunciosloc.ao/")  // ← https, não http!
    .build();
```

---

## 7.6 Resumo Visual da Arquitetura API

```
┌─────────────────────────────────────────────────────────┐
│                   APLICAÇÃO ANDROID                      │
│  ┌───────────┐   ┌────────────┐   ┌─────────────────┐  │
│  │  Activity │→→→│  ViewModel │→→→│   Repository    │  │
│  │   (UI)    │   │  (Lógica)  │   │ (Dados + API)   │  │
│  └───────────┘   └────────────┘   └────────┬────────┘  │
│                                             │           │
└─────────────────────────────────────────────┼───────────┘
                                              │
                                              │ Retrofit
                                              │ (HTTP/JSON)
                                              ▼
┌─────────────────────────────────────────────────────────┐
│                    SERVIDOR (API)                        │
│  ┌──────────────┐   ┌──────────────┐   ┌─────────────┐ │
│  │   Endpoints  │→→→│  Controller  │→→→│  Database   │ │
│  │ /api/users   │   │   (Lógica)   │   │   (MySQL)   │ │
│  │ /api/announ..│   │              │   │             │ │
│  └──────────────┘   └──────────────┘   └─────────────┘ │
└─────────────────────────────────────────────────────────┘
```

**Fluxo Completo de um Pedido:**

1. **Usuário** clica em "Criar Anúncio"
2. **Activity** captura os dados do formulário
3. **ViewModel** valida e prepara os dados
4. **Repository** chama o Retrofit
5. **Retrofit** converte objeto Java → JSON
6. **Retrofit** envia HTTP POST para o servidor
7. **Servidor** recebe JSON
8. **Servidor** valida token de autenticação
9. **Servidor** processa e guarda na base de dados
10. **Servidor** responde com JSON (sucesso ou erro)
11. **Retrofit** converte JSON → objeto Java
12. **Repository** retorna resposta ao ViewModel
13. **ViewModel** atualiza LiveData
14. **Activity** observa mudança e atualiza UI
15. **Usuário** vê mensagem "Anúncio criado com sucesso!"

**Tempo estimado:** 200-500ms (dependendo da velocidade da internet)

---

## 8. Gerenciamento de Estado

### 8.1 LiveData Pattern

Toda comunicação View ↔ ViewModel é feita via **LiveData**:

```java
// No ViewModel
private MutableLiveData<List<Announcement>> announcements = new MutableLiveData<>();
private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
private MutableLiveData<String> errorMessage = new MutableLiveData<>();

// Na View
viewModel.getAnnouncements().observe(this, list -> {
    adapter.submitList(list);
});

viewModel.getIsLoading().observe(this, isLoading -> {
    progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
});

viewModel.getErrorMessage().observe(this, error -> {
    if (error != null && !error.isEmpty()) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
});
```

### 8.2 Single Source of Truth

Os **Repositories** são a fonte única de verdade:

```java
public class AnnouncementRepository {
    private Map<String, Announcement> announcementsDatabase = new HashMap<>();
    
    public List<Announcement> getAllAnnouncements() {
        return new ArrayList<>(announcementsDatabase.values());
    }
    
    public Announcement createAnnouncement(Announcement announcement) {
        announcement.setId(UUID.randomUUID().toString());
        announcementsDatabase.put(announcement.getId(), announcement);
        return announcement;
    }
}
```

### 8.3 Estado de Configuração

Salvo em **SharedPreferences** para persistência:

```java
public class PreferencesHelper {
    private static final String PREFS_NAME = "AnunciosLocPrefs";
    
    public void saveUser(User user) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }
    
    public boolean isUserLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
```

---

## 9. Considerações de Segurança

### 9.1 Senhas

**Atualmente**: Senhas armazenadas em **plain text** (apenas mock)

**Produção**: Deve usar **BCrypt/Argon2** para hashing:

```java
// No servidor
String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

// Verificação
boolean isValid = BCrypt.checkpw(inputPassword, hashedPassword);
```

### 9.2 Comunicação

**Planejado**: HTTPS com certificado SSL/TLS para todas as requisições.

### 9.3 Tokens de Sessão

**Planejado**: JWT (JSON Web Tokens) para autenticação stateless:

```java
@Headers("Authorization: Bearer {token}")
@GET("announcements")
Call<List<Announcement>> getAnnouncements();
```

---

## 10. Performance e Otimizações

### 10.1 RecyclerView

- **ViewHolder Pattern**: Reutilização de views
- **DiffUtil**: Cálculo eficiente de diferenças em listas (não implementado atualmente)
- **setHasFixedSize(true)**: Otimização quando tamanho é fixo

### 10.2 Memory Management

- **WeakReference**: Para callbacks que referenciam Context
- **LiveData**: Limpeza automática de observers
- **ViewModel**: Sobrevive a rotações sem recriar dados

### 10.3 Threading

- Background threads para operações pesadas
- Main thread apenas para UI
- Sem blocking de UI thread

---

## 11. Testes e Debugging

### 11.1 Logs

Uso de `Log` do Android:

```java
private static final String TAG = "AnnouncementViewModel";

Log.d(TAG, "Loading announcements...");
Log.e(TAG, "Error: " + e.getMessage());
```

### 11.2 Dados de Teste

Credenciais mockadas para testes:

| Username | Password | Perfil |
|----------|----------|--------|
| alice | password123 | Interesse: Tecnologia |
| bob | password123 | Interesse: Desporto |
| carol | password123 | Profissão: Estudante |

---

## 12. Próximos Passos Técnicos

### 12.1 Implementações Pendentes

1. **Retrofit**: Integração com API REST real
2. **Room Database**: Persistência local com SQLite
3. **WorkManager**: Sincronização em background
4. **WiFi Direct**: Comunicação P2P com Termite
5. **BLE Beacons**: Detecção de proximidade
6. **Dagger/Hilt**: Injeção de dependências
7. **Coroutines**: Threading moderno
8. **DataStore**: Substituir SharedPreferences

### 12.2 Melhorias de Arquitetura

1. **Clean Architecture**: Adicionar camada de Use Cases
2. **DiffUtil**: Otimizar RecyclerView updates
3. **Paging 3**: Paginação de listas grandes
4. **Navigation Component**: Navegação type-safe
5. **ViewBinding**: Substituir findViewById

---

## Conclusão

O **AnunciosLoc** implementa uma arquitetura sólida e escalável utilizando MVVM, LiveData e padrões modernos do Android. A separação clara de responsabilidades e o uso de threading manual garantem controle total sobre o comportamento da aplicação, enquanto a estrutura prepara o projeto para futuras integrações com serviços reais de backend e comunicação P2P.

A documentação técnica aqui apresentada serve como referência completa para desenvolvedores que desejam entender, manter ou expandir o sistema AnunciosLoc.
