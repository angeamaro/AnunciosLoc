### 🧩 **Resumo do Projeto — AnunciosLoc**

**AnunciosLoc** é uma aplicação móvel Android que permite aos utilizadores **criar, visualizar e receber anúncios baseados na localização**.
O sistema combina **comunicação centralizada (via servidor)** e **descentralizada (via WiFi Direct)**, permitindo a partilha de informações locais mesmo sem conexão à Internet.

---

### ⚙️ **Objetivo**

Facilitar a comunicação entre pessoas próximas, promovendo a interação comunitária através de anúncios geolocalizados, com uma interface simples, acessível e funcional.

---

### 💻 **Tecnologias Utilizadas**

* **Linguagem:** Java
* **Framework:** Android SDK
* **Backend:** Servidor Java Standalone
* **Comunicação:** WiFi Direct + GPS
* **IDE:** Android Studio
* **Gradle (KTS)** para gestão de dependências
* **Ferramentas de Teste:** Android Emulator, Termite

---

### 🎨 **Paleta de Cores**

| Nome           | Hex     | Uso                                 |
| -------------- | ------- | ----------------------------------- |
| **Black**      | #000000 | Texto e contrastes                  |
| **White**      | #FFFFFF | Fundo e áreas limpas                |
| **Blue 600**   | #2563EB | Ações principais, botões e destaque |
| **Blue 500**   | #3B82F6 | Links, ícones e gradientes          |
| **Purple 600** | #9333EA | Detalhes e elementos de interação   |
| **Orange 600** | #EA580C | Alertas e feedback                  |
| **Gray BG**    | #F9FAFB | Fundo neutro e conforto visual      |

🔹 *Foco visual:* tons frios e profissionais (azul e branco) para transmitir **confiança, clareza e tecnologia**, com contrastes sutis em **preto e roxo** para sofisticação e acessibilidade.

---

### 📱 **Fluxo de Navegação (App Flow)**

1. **Tela de Apresentação (Splash)** →
2. **Login / Registro / Redefinir Senha** →
3. **Tela Principal (Main)** →
4. **Gerir Locais / Adicionar / Listar Locais** →
5. **Gerir Anúncios / Criar / Visualizar / Detalhes** →
6. **Notificações / Definições / Políticas / Perfil**

---

### 🧠 **Regras de Usabilidade**

* Interface **simples, responsiva e funcional**
* Navegação intuitiva com **ícones familiares**
* Uso coerente da paleta e espaçamento uniforme
* Feedback visual em todas as ações (cliques, carregamentos)
* Texto legível, contraste alto, design acessível
* Layout modular para fácil expansão de funcionalidades

---

### 🧱 **Estrutura do Projeto**

O projeto segue o padrão **modular Android**:

```
AnunciosLoc/
├── app/
│   ├── src/main/java/...      # Código-fonte principal
│   ├── src/main/res/layout/   # Layouts XML
│   ├── res/drawable/          # Ícones e fundos
│   ├── res/values/            # Cores, temas, strings
│   └── AndroidManifest.xml
├── build.gradle.kts
├── gradle/                    # Configurações de build
└── settings.gradle.kts
```

---
