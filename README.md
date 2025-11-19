# 📱 AnunciosLoc

**AnunciosLoc** é uma aplicação móvel Android que permite aos utilizadores publicar e receber **anúncios baseados em localização**.  
Suporta comunicação **centralizada (via servidor)** e **descentralizada (via WiFi Direct)** — permitindo entrega de mensagens mesmo quando os utilizadores estão offline.

---

## 🎯 Objetivo do Projeto

Desenvolver uma aplicação móvel distribuída que melhora a interação comunitária através de **mensagens baseadas em localização**, permitindo que utilizadores partilhem e recebam anúncios vinculados a localizações geográficas específicas.

---

## 🧩 Funcionalidades Principais

### Funcionalidades Base
- 🧍‍♂️ **F1:** Registo de utilizador
- 🔑 **F2:** Login / Logout
- 📍 **F3:** Listar, criar e remover localizações
- 📰 **F4:** Registar e remover anúncios
- 👀 **F5:** Visualizar anúncios
- ✏️ **F6:** Editar perfil de utilizador (pares chave-valor: "clube=Benfica", "profissao=Estudante")
- 🔔 **Notificações:** Alertas baseados em localização
- ☁️ **Modo Centralizado:** Comunicação via servidor principal
- 📶 **Modo Descentralizado:** Comunicação peer-to-peer via WiFi Direct

### Funcionalidades Avançadas
- 🚚 **Roteamento por Retransmissão (Mulas):**  
  Acelera a entrega de mensagens permitindo que dispositivos intermediários selecionados (mulas) transportem mensagens para os seus destinos.

- 🛡️ **Camada de Segurança:**  
  Garante comunicação encriptada e autenticação de mensagens para prevenir adulteração, interceção ou ataques de injeção.

- 🎯 **Políticas de Entrega:**  
  - **Everyone:** Anúncio disponível para todos
  - **Whitelist:** Apenas utilizadores com atributos específicos recebem (ex: "interesse=Tecnologia")
  - **Blacklist:** Utilizadores com atributos específicos são bloqueados

---

## 🏗️ Arquitetura do Sistema

### Padrão MVVM (Model-View-ViewModel)

```
app/src/main/java/ao/co/isptec/aplm/anunciosloc/
├── data/
│   ├── model/              # Modelos de dados
│   │   ├── User.java
│   │   ├── Announcement.java
│   │   ├── Location.java
│   │   ├── InterestCategory.java
│   │   └── PolicyFilter.java
│   └── repository/         # Repositórios (acesso a dados)
│       ├── UserRepository.java
│       ├── AnnouncementRepository.java
│       └── LocationRepository.java
├── ui/
│   ├── view/              # Activities (Views)
│   │   ├── SplashActivity.java
│   │   ├── MainActivity.java
│   │   ├── LoginActivity.java
│   │   ├── RegisterActivity.java
│   │   ├── HomeActivity.java
│   │   ├── CreateAnnouncementActivity.java
│   │   ├── AnnouncementDetailActivity.java
│   │   ├── ProfileActivity.java
│   │   ├── SettingsActivity.java
│   │   ├── ConfigurePolicyActivity.java
│   │   └── ...
│   ├── adapter/           # RecyclerView Adapters
│   │   ├── AnnouncementAdapter.java
│   │   ├── LocationAdapter.java
│   │   ├── ProfileAttributeAdapter.java
│   │   └── ...
│   └── viewmodel/         # ViewModels (lógica de negócio)
│       ├── UserViewModel.java
│       ├── AnnouncementViewModel.java
│       └── LocationViewModel.java
└── utils/                 # Classes utilitárias
    ├── Constants.java
    ├── SessionManager.java
    └── ...
```

### Componentes do Sistema
- 🖥️ **Servidor Central:** Aplicação Java standalone que gere utilizadores, mensagens e políticas de entrega
- 📲 **Cliente Android:** Aplicação Java que permite interação e troca de mensagens
- 🧪 **Ambiente de Testes:** Android Emulator + Termite WiFi Direct (para GPS, mobilidade e simulação P2P)

---

## ⚙️ Tecnologias Utilizadas

- 💻 **Linguagem:** Java 8+
- 📱 **Framework:** Android SDK (API 21+)
- 🏛️ **Arquitetura:** MVVM (Model-View-ViewModel)
- 🎨 **UI Components:** Material Design 3
- 🗄️ **Persistência Local:** SharedPreferences
- 🌐 **Comunicação:** WiFi Direct, REST API (planejado)
- 🧩 **Simuladores:** Android Emulator & Termite WiFi Direct
- 🗺️ **APIs:** GPS, WiFi, Bluetooth para deteção de localização e proximidade

---

## 📦 Recursos Principais

### Material Design 3
- Cards com elevação e cantos arredondados
- SwitchMaterial para toggles
- MaterialToolbar para navegação
- Cores consistentes com paleta personalizada

### Gestão de Permissões
- ✅ Localização (GPS)
- ✅ Notificações
- ✅ WiFi
- ✅ Bluetooth

### Sistema de Perfil de Utilizador
Os utilizadores podem definir atributos personalizados no seu perfil:
- **interesse:** Tecnologia, Desporto, Música, Arte, Ciência, Culinária
- **profissao:** Estudante, Professor, Engenheiro, Médico, Empresário
- **clube:** Benfica, Porto, Sporting, 1º de Agosto, Petro de Luanda
- **faixa_etaria:** 18-24, 25-34, 35-44, 45+
- **cidade:** Luanda, Benguela, Huambo

---

## 🧪 Testes e Credenciais

### 👤 Utilizadores de Teste (Mock)

A aplicação possui 3 utilizadores pré-configurados para testes rápidos:

| Email | Senha | Nome | Atributos de Perfil |
|-------|-------|------|---------------------|
| `alice@example.com` | `password123` | Alice Silva | interesse=Tecnologia, profissao=Estudante, clube=Benfica |
| `bob@example.com` | `password123` | Bob Santos | interesse=Educação, profissao=Professor, clube=Porto |
| `carol@example.com` | `password123` | Carol Lima | interesse=Desporto, profissao=Estudante, clube=Sporting |

**Login Rápido Recomendado:**
- Email: `alice@example.com`
- Senha: `password123`

> ⚠️ **Nota:** Este é um projeto académico com backend mockado. Todos os dados são simulados e armazenados apenas em memória.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
- ☕ JDK 8 ou superior
- 📱 Android Studio Arctic Fox ou superior
- 🤖 Android SDK (API 21+)
- 📲 Dispositivo Android ou Emulador

### Passos de Instalação

1. **Clone o repositório:**
```bash
git clone https://github.com/angeamaro/AnunciosLoc.git
cd AnunciosLoc
```

2. **Abra o projeto no Android Studio:**
```bash
# No terminal do Android Studio
File > Open > Selecione a pasta AnunciosLoc
```

3. **Configure o arquivo local.properties:**
```properties
sdk.dir=/caminho/para/seu/Android/Sdk
```

4. **Sincronize o Gradle:**
```bash
./gradlew sync
```

5. **Execute a aplicação:**
```bash
./gradlew installDebug
# ou use o botão Run no Android Studio
```

---

## 📱 Funcionalidades Implementadas

### ✅ Autenticação
- [x] Tela de Splash com animação
- [x] Login com validação
- [x] Registo de novos utilizadores
- [x] Gestão de sessão (SessionManager)
- [x] Logout

### ✅ Gestão de Anúncios
- [x] Criar anúncios com título, conteúdo, localização e janela temporal
- [x] Listar anúncios disponíveis
- [x] Visualizar detalhes de anúncios
- [x] Configurar políticas de entrega (Everyone, Whitelist, Blacklist)
- [x] Filtros baseados em atributos de perfil

### ✅ Gestão de Localizações
- [x] Criar localizações (GPS + WiFi SSID)
- [x] Listar localizações disponíveis
- [x] Remover localizações

### ✅ Perfil e Definições
- [x] Editar perfil de utilizador
- [x] Adicionar atributos personalizados (pares chave-valor)
- [x] Configurar permissões (Localização, Notificações, WiFi)
- [x] Ativar/Desativar modo MULA

### ✅ Interface de Utilizador
- [x] Design Material 3
- [x] Navegação intuitiva
- [x] Feedback visual consistente
- [x] Cards com fundo personalizado
- [x] Animações suaves

---

## 🎨 Guia de Design

### Paleta de Cores
```xml
<!-- Cores Primárias -->
<color name="blue_primary">#1E88E5</color>
<color name="orange_accent">#FF6F00</color>

<!-- Cores de Fundo -->
<color name="background_color">#F5F5F5</color>
<color name="card_background">#FFFFFF</color>

<!-- Cores de Texto -->
<color name="text_primary">#212121</color>
<color name="text_secondary">#757575</color>

<!-- Outras -->
<color name="divider">#E0E0E0</color>
```

### Componentes UI
- **Cards:** BorderRadius 12-16dp, Elevation 2-4dp
- **Botões:** BorderRadius 12dp, Padding 12-16dp
- **Texto:** Sans-serif, 12-28sp
- **Ícones:** 20-24dp, Tint baseado no tema

---

## 📚 Documentação Adicional

Para mais detalhes sobre a arquitetura e implementação, consulte:
- 📄 [Estrutura do Projeto](docs/estrutura-projeto.md)
- 📄 [Guia de Design e Implementação](docs/design-implementation-guide.md)
- 📄 [Enunciado do Projeto](docs/enunciado.md)

---

## 👩‍💻 Autores

- **Ângela Amaro** - [GitHub](https://github.com/angeamaro)
- **Adriana Mazanga**
- **Raquel Da Gama**

---

## 🏫 Instituição

**ISPTEC - Instituto Superior Politécnico de Tecnologias e Ciências**  
Coordenação de Engenharia Informática  
Departamento de Engenharia e Tecnologias (DET)

**Disciplina:** Aplicações Móveis  
**Ano Letivo:** 2024/2025  
**Data:** Outubro - Dezembro 2025

---

## 🧾 Licença

Este projeto foi desenvolvido para fins académicos no âmbito da disciplina de **Aplicações Móveis** no **ISPTEC**.  
Todos os direitos reservados © 2025 — *Não destinado a uso comercial.*

---

## 📞 Contacto

Para questões ou sugestões sobre o projeto:
- 📧 Email: angela.amaro@isptec.ao
- 🐙 GitHub: [@angeamaro](https://github.com/angeamaro)

---

## ⭐ Agradecimentos

Agradecemos aos professores e colegas do ISPTEC pelo apoio e orientação durante o desenvolvimento deste projeto.

---

**Desenvolvido com ❤️ em Angola 🇦🇴**
