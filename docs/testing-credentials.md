# 🧪 Credenciais de Teste - AnunciosLoc

## ⚠️ IMPORTANTE - SISTEMA ATUALIZADO

**O sistema de login agora usa USERNAME (não email)**

---

## 👤 Usuários Pré-configurados

O aplicativo possui **3 usuários de teste** já cadastrados:

### 🎯 Login Rápido Recomendado

```
Username: alice
Senha: 123456
```

---

## 📋 Lista Completa de Usuários

| ID | Nome | Username | Email | Senha | Perfil |
|:--:|------|----------|-------|:-----:|--------|
| 1 | **Alice Silva** | `alice` | alice@example.com | `123456` | 🎓 Estudante<br>⚽ Benfica<br>💻 Tecnologia |
| 2 | **Bob Santos** | `bob` | bob@example.com | `123456` | 👨‍🏫 Professor<br>⚽ Porto<br>📚 Educação |
| 3 | **Carol Lima** | `carol` | carol@example.com | `123456` | 🎓 Estudante<br>⚽ Sporting<br>�� Desporto |

---

## 🔑 Fluxo de Teste

### 1️⃣ **Login com Usuário Existente**
1. Abrir aplicativo
2. **Username**: `alice` (não use email!)
3. **Senha**: `123456`
4. Clicar em "Entrar"

### 2️⃣ **Criar Nova Conta**
1. Clicar em "Criar nova conta"
2. Preencher todos os campos:
   - **Username**: mínimo 3 caracteres alfanuméricos (será usado para login!)
   - **Nome completo**
   - **Email**: válido
   - **Senha**: mínimo 6 caracteres
   - **Confirmar senha**
3. Clicar em "Registar"

### 3️⃣ **Testar Funcionalidades**

#### Perfil e Definições
- Clique no menu (3 pontos)
- Acesse "Perfil" ou "Definições"
- Agora deve funcionar sem crashes!

#### Criar Localização
1. Ir para aba "Locais"
2. Clicar no botão FAB (+)
3. Preencher nome e coordenadas

#### Criar Anúncio
1. Clicar no botão central (+)
2. Preencher informações
3. Configurar política (Whitelist/Blacklist)

---

## 🔧 Informações Técnicas

### Arquitetura MVVM
- **Repository**: `UserRepository` (Mock em memória)
- **ViewModel**: `AuthViewModel`
- **View**: `LoginActivity`, `RegisterActivity`

### Autenticação
- **Login por**: Username (não email!)
- **Chave no banco**: `usersDatabase.get(username)`
- **Senha**: Texto plano (apenas para testes mock)
- **Public Key**: Gerada automaticamente

### Persistência
- **PreferencesHelper**: Salva username do último login
- **Dados**: Armazenados em memória (perdidos ao fechar app)

---

## 🐛 Correções Recentes

✅ AndroidManifest corrigido:
- `ProfileActivity` agora em `.ui.view.`
- `ChangePasswordActivity` agora em `.ui.view.`
- `SettingsActivity` agora em `.ui.view.`

✅ Sistema de login atualizado:
- Usa **username** em vez de email
- Credenciais válidas e testadas

✅ Estrutura MVVM completa:
- Todos os models em `data/model/`
- Todas as activities em `ui/view/`
- Todos os adapters em `ui/adapter/`

---

**Última atualização**: 06 de Novembro de 2025
**Versão**: 2.0 (Sistema de username)
