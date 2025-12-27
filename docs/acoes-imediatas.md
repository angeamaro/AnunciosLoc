# Ações Imediatas - Começar Agora!

## 🚨 PRIORIDADE 1: Base de Dados Hospedada (1-2 horas)

### Opção Recomendada: Railway.app

**1. Criar conta no Railway:**
```
https://railway.app (login com GitHub)
```

**2. Criar novo PostgreSQL:**
- Clicar em "New Project"
- Selecionar "Provision PostgreSQL"
- Copiar credenciais geradas

**3. Atualizar `application.properties`:**
```properties
spring.datasource.url=jdbc:postgresql://containers-us-west-xxx.railway.app:5432/railway
spring.datasource.username=postgres
spring.datasource.password=seu-password-gerado
```

**4. Testar conexão:**
```bash
cd AnunciosLoc-project_Backend/anunciosloc
./mvnw spring-boot:run
```

✅ **Sucesso:** Backend conecta sem "Connection refused"

---

## 🔒 PRIORIDADE 2: Configurar HTTPS ou Permitir HTTP (30 min)

### Opção Temporária (Desenvolvimento):

**1. Atualizar `network_security_config.xml`:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">192.168.1.14</domain>
        <domain includeSubdomains="true">containers-us-west-xxx.railway.app</domain>
        <domain includeSubdomains="true">10.0.2.2</domain>
    </domain-config>
</network-security-config>
```

**2. Atualizar `RetrofitClient.java`:**
```java
private static final String BASE_URL = "https://seu-backend.railway.app/";
// ou temporariamente: http://192.168.1.14:8080/
```

**3. Rebuild do app:**
```bash
./gradlew clean assembleDebug
```

---

## 🎯 PRIORIDADE 3: Corrigir JWT (1 hora)

### Backend: Simplificar Autenticação

**1. Revisar `JwtUtil.java`:**
- Remover UUID se duplicado
- Garantir que gera token com username
- Validação de expiração correta

**2. Criar `JwtAuthenticationFilter.java`:**
```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) {
        String token = extractToken(request);
        if (token != null && jwtUtil.validateToken(token)) {
            // Autenticar usuário
        }
        filterChain.doFilter(request, response);
    }
}
```

**3. Android: Salvar Token no SharedPreferences:**
```java
// Após login bem-sucedido:
SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
prefs.edit()
     .putString("token", response.getToken())
     .putString("username", response.getUsername())
     .apply();
```

**4. Adicionar Interceptor no Retrofit:**
```java
OkHttpClient client = new OkHttpClient.Builder()
    .addInterceptor(chain -> {
        Request original = chain.request();
        String token = getToken(); // Do SharedPreferences
        if (token != null) {
            Request request = original.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();
            return chain.proceed(request);
        }
        return chain.proceed(original);
    })
    .build();
```

---

## 📱 PRIORIDADE 4: Criar Anúncio - Dados Corretos (2 horas)

### Backend: Endpoint Completo

**`AnnouncementController.java`:**
```java
@PostMapping("/announcements")
public ResponseEntity<Announcement> create(@Valid @RequestBody AnnouncementDTO dto) {
    Announcement announcement = announcementService.create(dto);
    return ResponseEntity.ok(announcement);
}

@GetMapping("/announcements")
public ResponseEntity<List<Announcement>> getByLocation(@RequestParam Long locationId) {
    return ResponseEntity.ok(announcementService.findByLocation(locationId));
}
```

### Android: Tela de Criação

**`CreateAnnouncementActivity.java`:**
```java
// Campos necessários:
EditText titleInput, descriptionInput;
Spinner locationSpinner; // Lista de locais cadastrados
Button selectDateButton, selectTimeButton;
RadioGroup policyGroup; // Whitelist/Blacklist/None
Button submitButton;

// Ao submeter:
AnnouncementRequest request = new AnnouncementRequest(
    title,
    description,
    locationId,
    startDate,
    endDate,
    policyType
);

apiService.createAnnouncement(request).enqueue(new Callback<>() {
    @Override
    public void onResponse(Call<AnnouncementResponse> call, Response<AnnouncementResponse> response) {
        if (response.isSuccessful()) {
            Toast.makeText(this, "Anúncio criado!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
});
```

---

## 🗺️ PRIORIDADE 5: Integrar Google Maps (1 hora)

**1. Obter API Key:**
```
https://console.cloud.google.com
→ Enable Maps SDK for Android
→ Criar credencial
```

**2. Adicionar ao `AndroidManifest.xml`:**
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="SUA_API_KEY_AQUI"/>
```

**3. Adicionar dependência no `build.gradle`:**
```gradle
implementation 'com.google.android.gms:play-services-maps:18.2.0'
implementation 'com.google.android.gms:play-services-location:21.0.1'
```

**4. Criar `LocationsMapFragment.java`:**
```java
@Override
public void onMapReady(GoogleMap googleMap) {
    this.map = googleMap;
    
    // Adicionar marcadores dos locais:
    for (Location loc : locations) {
        map.addMarker(new MarkerOptions()
            .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
            .title(loc.getName()));
    }
}
```

---

## 🔔 PRIORIDADE 6: Notificações FCM (2 horas)

**1. Configurar Firebase:**
- Ir para https://console.firebase.google.com
- Adicionar app Android
- Baixar `google-services.json` → colocar em `app/`

**2. Adicionar dependências:**
```gradle
// build.gradle (project)
classpath 'com.google.gms:google-services:4.4.0'

// build.gradle (app)
implementation 'com.google.firebase:firebase-messaging:23.3.1'
apply plugin: 'com.google.gms.google-services'
```

**3. Criar `MyFirebaseMessagingService.java`:**
```java
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        
        showNotification(title, body);
    }
    
    private void showNotification(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true);
            
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}
```

**4. Registrar no `AndroidManifest.xml`:**
```xml
<service
    android:name=".MyFirebaseMessagingService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT"/>
    </intent-filter>
</service>
```

---

## 📋 Checklist de Hoje

- [ ] BD hospedado no Railway/Render
- [ ] Backend conecta e roda sem erro
- [ ] App Android compila
- [ ] Consegue fazer login
- [ ] Token JWT salvo
- [ ] network_security_config permite HTTP/HTTPS do servidor
- [ ] Testar criar anúncio (mesmo que dados fictícios)

---

## 🆘 Se Tiver Problemas

### Problema: "Connection refused" no backend
**Solução:** PostgreSQL não está rodando. Use Railway/Render.

### Problema: "CLEARTEXT not permitted"
**Solução:** Atualizar `network_security_config.xml` com domínio correto.

### Problema: "401 Unauthorized"
**Solução:** Token não está sendo enviado. Verificar interceptor.

### Problema: App crash ao abrir
**Solução:** Verificar logs no Logcat, geralmente falta permissão ou dependência.

---

## 📞 Próxima Reunião de Equipe

**Agenda:**
1. Revisar BD hospedado (todos conectam?)
2. Testar fluxo completo: Login → Criar Anúncio → Listar
3. Definir responsáveis por cada épico
4. Criar branches no Git para cada feature

**Meta da Semana:**
- ÉPICO 1 completo (BD + JWT + HTTPS)
- ÉPICO 2 Sprint 2.1 e 2.2 (Criar/Listar anúncios básicos)
