# 🧪 Credenciais de Teste - AnunciosLoc

## 👤 Usuários Pré-configurados

O aplicativo possui **3 usuários de teste** já cadastrados no sistema mock para facilitar os testes:

### 🎯 Login Rápido Recomendado

```
Email: alice@example.com
Senha: 123456
```

---

## 📋 Lista Completa de Usuários

| ID | Nome | Email | Senha | Username | Perfil |
|:--:|------|-------|:-----:|----------|--------|
| 1 | **Alice Silva** | alice@example.com | `123456` | alice | 🎓 Estudante<br>⚽ Benfica<br>💻 Tecnologia |
| 2 | **Bob Santos** | bob@example.com | `123456` | bob | 👨‍🏫 Professor<br>⚽ Porto<br>📚 Educação |
| 3 | **Carol Lima** | carol@example.com | `123456` | carol | 🎓 Estudante<br>⚽ Sporting<br>🏃 Desporto |

---

## 🔑 Fluxo de Teste

### 1️⃣ **Login com Usuário Existente**
1. Abrir aplicativo
2. Inserir: `alice@example.com`
3. Senha: `123456`
4. Clicar em "Entrar"

### 2️⃣ **Criar Nova Conta**
1. Clicar em "Criar nova conta"
2. Preencher todos os campos:
   - Username: mínimo 3 caracteres alfanuméricos
   - Nome completo
   - Email válido
   - Senha: mínimo 6 caracteres
   - Confirmar senha
3. Clicar em "Registar"

### 3️⃣ **Criar Localização**
1. Login no app
2. Ir para aba "Locais"
3. Clicar no botão FAB (+)
4. Preencher:
   - Nome do local
   - Latitude: ex: -8.8383 (Luanda)
   - Longitude: ex: 13.2344
   - Raio: 100-10000 metros
5. Salvar

### 4️⃣ **Criar Anúncio**
1. Ir para aba "Anúncios"
2. Clicar no botão FAB (+)
3. Preencher:
   - Título
   - Conteúdo
   - Selecionar localização
   - Definir janela temporal (opcional)
   - Escolher política de entrega
4. Publicar

### 5️⃣ **Ver Detalhes do Anúncio**
1. Na lista de anúncios, clicar em qualquer card
2. Ver informações completas
3. Se for o autor, pode editar ou excluir

### 6️⃣ **Editar Perfil**
1. Ir para aba "Perfil"
2. Clicar no FAB (+) para adicionar atributos
3. Inserir chave e valor (ex: `hobby: futebol`)
4. Atributos são usados nas políticas de entrega

---

## 🎯 Cenários de Teste de Políticas

### Política: **Whitelist**
```
Anúncio: "Evento de Tecnologia"
Whitelist: profissao=Estudante, interesse=Tecnologia
```
✅ Alice recebe (Estudante + Tecnologia)
❌ Bob não recebe (Professor + Educação)

### Política: **Blacklist**
```
Anúncio: "Evento Desportivo"
Blacklist: clube=Benfica
```
❌ Alice não recebe (Benfica)
✅ Bob recebe (Porto)
✅ Carol recebe (Sporting)

### Política: **Todos**
```
Anúncio: "Aviso Geral"
Política: EVERYONE
```
✅ Todos recebem

---

## ⚠️ Observações Importantes

- 📝 **Backend Mockado:** Todos os dados são armazenados apenas em memória
- 🔄 **Reset:** Fechar o app limpa todos os dados criados durante a sessão
- 🚫 **Sem Servidor Real:** Não há comunicação de rede real
- ⏱️ **Delays Simulados:** Thread.sleep() simula latência de rede (500-800ms)
- 📍 **GPS Mock:** Localizações são simuladas, não usa GPS real
- 📶 **WiFi Direct Mock:** Comunicação P2P não implementada nesta versão

---

## 🐛 Troubleshooting

### Problema: App fecha logo após splash screen
**Causa:** Erro ao carregar fragments ou inicializar MainActivity  
**Solução:** ✅ **Corrigido v2!**
- Adicionado try-catch em TODOS os fragments (onCreateView)
- SplashActivity simplificado com melhor tratamento de erros
- Fallback para view vazia se fragment falhar
- Validação robusta de sessão e usuário
- Se algo falhar, sempre redireciona para Login com segurança

### Problema: App fecha após login
**Causa:** Falha ao restaurar sessão ou inicializar MainActivity  
**Solução:** ✅ **Corrigido v1!**
- Try-catch em MainActivity e SplashActivity
- Restauração de sessão com validação de email
- Fallback automático para LoginActivity em caso de erro

### Problema: App fecha após registro
**Causa:** Campo `username` vs `name` inconsistente  
**Solução:** ✅ **Corrigido!** - Usa `user.getName()` consistentemente

### Problema: Não consigo fazer login
**Causa:** Email ou senha incorretos  
**Solução:** Usar credenciais da tabela acima (email completo + senha 123456)

### Problema: Locais não aparecem
**Causa:** Nenhum local criado ainda  
**Solução:** Criar um novo local usando o FAB na aba "Locais"

### Problema: Anúncios não aparecem
**Causa:** Nenhum anúncio publicado ainda  
**Solução:** Criar um novo anúncio usando o FAB na aba "Anúncios"

---

## 📊 Estrutura de Dados Mock

### Localizações Iniciais
Nenhuma localização pré-configurada. Usuário deve criar.

### Anúncios Iniciais
3 anúncios de exemplo criados automaticamente:
1. "Bem-vindo ao AnunciosLoc!" (Alice)
2. "Evento de Tecnologia no ISPTEC" (Bob)
3. "Treino de Futebol" (Carol)

### Notificações Iniciais
5 notificações mockadas para cada usuário:
- "Novo anúncio próximo de você"
- "Você entrou em uma área com anúncios"
- "Mensagem recebida via WiFi Direct"

---

**Última Atualização:** 04/11/2025  
**Versão da Aplicação:** 1.0.0 (Build Debug)
