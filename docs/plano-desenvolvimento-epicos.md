# Plano de Desenvolvimento - AnunciosLoc
## Divisão em Épicos com Estabilidade

---

## 🎯 ÉPICO 1: Infraestrutura e Segurança Base (CRÍTICO)
**Objetivo:** Estabelecer fundação sólida para desenvolvimento seguro e colaborativo

### Sprint 1.1: Base de Dados Hospedada
**Prioridade:** CRÍTICA
- [ ] Configurar PostgreSQL em servidor remoto (Railway/Render/Supabase)
- [ ] Atualizar `application.properties` com credenciais remotas
- [ ] Criar script de migração/backup automático
- [ ] Documentar processo de conexão para equipe
- [ ] Testar conexão de múltiplos desenvolvedores

**Critério de Aceitação:** Todos desenvolvedores conectam ao mesmo BD sem perda de dados

### Sprint 1.2: Segurança JWT Robusta
**Prioridade:** ALTA
- [ ] Revisar implementação JWT no backend
- [ ] Remover UUID duplicado (manter apenas JWT)
- [ ] Implementar refresh token
- [ ] Adicionar validação de expiração no client
- [ ] Interceptor OkHttp para adicionar token automaticamente
- [ ] Tratamento de 401/403 com logout automático

**Critério de Aceitação:** Login persiste, token renova automaticamente, logout limpa sessão

### Sprint 1.3: Políticas HTTP Seguras
**Prioridade:** ALTA
- [ ] Atualizar `network_security_config.xml` com IP do servidor remoto
- [ ] Configurar HTTPS no servidor (Let's Encrypt)
- [ ] Remover cleartext após HTTPS configurado
- [ ] Validar certificados SSL

**Critério de Aceitação:** App comunica via HTTPS sem avisos de segurança

---

## 🏗️ ÉPICO 2: Core - Anúncios e Localizações (FUNDAMENTAL)
**Objetivo:** Sistema de anúncios funcionando perfeitamente com dados bem estruturados

### Sprint 2.1: Backend - Modelo de Dados Completo
**Prioridade:** ALTA
- [ ] Revisar entidades: `Announcement`, `Location`, `User`, `Policy`
- [ ] Adicionar campos: `windowStart`, `windowEnd`, `radius`, `imageUrl`
- [ ] Implementar relacionamentos JPA corretos
- [ ] Endpoints REST completos (CRUD + filtros)
- [ ] Validações de entrada (Bean Validation)
- [ ] DTOs para transferência de dados

**Critério de Aceitação:** API RESTful completa e testada no Postman

### Sprint 2.2: Android - Criar Anúncios
**Prioridade:** ALTA
- [ ] Tela de criação moderna (seguindo design guide)
- [ ] Seleção de local (lista + mapa)
- [ ] Configuração de política (Whitelist/Blacklist)
- [ ] Janela temporal (DatePicker + TimePicker)
- [ ] Validação de formulário
- [ ] Feedback visual de sucesso/erro

**Critério de Aceitação:** Anúncio criado com todos os campos salvos corretamente

### Sprint 2.3: Android - Listar e Visualizar Anúncios
**Prioridade:** ALTA
- [ ] RecyclerView com cards modernos
- [ ] Filtros: por local, data, perfil
- [ ] Tela de detalhes do anúncio
- [ ] Exibir imagem (Glide/Picasso)
- [ ] Tratamento de dados vazios/erro
- [ ] Pull-to-refresh

**Critério de Aceitação:** Anúncios aparecem bem formatados e responsivos

### Sprint 2.4: Localizações com Mapa
**Prioridade:** ALTA
- [ ] Integrar Google Maps API
- [ ] Marcar locais cadastrados no mapa
- [ ] Criar local por coordenadas (tap no mapa)
- [ ] Criar local por SSID WiFi
- [ ] Visualizar raio do local
- [ ] Listar locais próximos

**Critério de Aceitação:** Locais aparecem no mapa, criação funciona via mapa ou SSID

---

## 📍 ÉPICO 3: Geolocalização e Detecção Inteligente
**Objetivo:** Sistema de detecção de presença em locais

### Sprint 3.1: Serviço de Localização em Background
**Prioridade:** MÉDIA
- [ ] Implementar `LocationService` (Foreground Service)
- [ ] Permissões de localização (runtime)
- [ ] Rastreamento de coordenadas GPS
- [ ] Detecção de entrada/saída de locais (Geofencing)
- [ ] Otimização de bateria (só ativa perto de locais)

**Critério de Aceitação:** App detecta quando usuário entra em local cadastrado

### Sprint 3.2: Detecção de WiFi/Beacon
**Prioridade:** MÉDIA
- [ ] Scan de redes WiFi próximas
- [ ] Matching de SSID com locais cadastrados
- [ ] Integração com Termite (para testes)
- [ ] Fallback para GPS se WiFi indisponível

**Critério de Aceitação:** App detecta local por SSID WiFi configurado

---

## 🔔 ÉPICO 4: Sistema de Notificações Inteligentes
**Objetivo:** Notificações nativas, oportunas e não intrusivas

### Sprint 4.1: Notificações Push em Background
**Prioridade:** ALTA
- [ ] Implementar Firebase Cloud Messaging (FCM)
- [ ] Backend envia push quando usuário entra em local com anúncios
- [ ] Notification channels (categorias)
- [ ] Ícones e sons personalizados
- [ ] Deep linking (tap abre anúncio)

**Critério de Aceitação:** Notificação aparece na barra do sistema, mesmo com app fechado

### Sprint 4.2: Central de Notificações
**Prioridade:** MÉDIA
- [ ] Fragment com histórico de notificações
- [ ] Badge com contador não lidos
- [ ] Marcar como lida
- [ ] Limpar todas
- [ ] Cards modernos com timestamp

**Critério de Aceitação:** Usuário vê e gerencia histórico de notificações

### Sprint 4.3: Políticas de Entrega
**Prioridade:** MÉDIA
- [ ] Backend valida perfil do usuário contra política
- [ ] Whitelist: só envia se perfil match
- [ ] Blacklist: não envia se perfil match
- [ ] Log de entregas no servidor
- [ ] UI para selecionar política ao criar anúncio

**Critério de Aceitação:** Anúncios só chegam a usuários que atendem a política

---

## 🔄 ÉPICO 5: Comunicação Descentralizada (P2P)
**Objetivo:** Entrega direta via WiFi Direct (funcionalidade avançada)

### Sprint 5.1: WiFi Direct - Descoberta e Conexão
**Prioridade:** BAIXA
- [ ] Implementar WiFi Direct Manager
- [ ] Descoberta de peers próximos
- [ ] Estabelecer conexão P2P
- [ ] Testes com Termite Emulator

**Critério de Aceitação:** Dois dispositivos se descobrem e conectam

### Sprint 5.2: Transferência de Anúncios P2P
**Prioridade:** BAIXA
- [ ] Protocolo de mensagens custom
- [ ] Publicador envia anúncio diretamente
- [ ] Receptor valida e armazena localmente
- [ ] Sincronização com servidor (modo híbrido)

**Critério de Aceitação:** Anúncio transferido via WiFi Direct sem servidor

### Sprint 5.3: Sistema de Mulas (Roteamento)
**Prioridade:** BAIXA (Avançada)
- [ ] Seleção inteligente de mulas
- [ ] Hop único (um salto)
- [ ] Otimização de bateria e tráfego
- [ ] Métricas de entrega

**Critério de Aceitação:** Mula retransmite anúncio para destino final

---

## 👤 ÉPICO 6: Perfil de Usuário e Personalização
**Objetivo:** Perfil rico e gerenciamento de interesses

### Sprint 6.1: Atributos de Perfil (Chave-Valor)
**Prioridade:** MÉDIA
- [ ] Backend: armazenar pares chave-valor (Map/JSON)
- [ ] Tela de edição de perfil
- [ ] Adicionar/remover atributos customizados
- [ ] Atributos predefinidos (Profissao, Clube, Cidade)
- [ ] Validação de tipos

**Critério de Aceitação:** Usuário cria atributos customizados no perfil

### Sprint 6.2: Interesses e Preferências
**Prioridade:** MÉDIA
- [ ] Lista de categorias de interesse
- [ ] Vincular interesses a notificações
- [ ] Filtrar anúncios por interesse
- [ ] Sugestões personalizadas

**Critério de Aceitação:** Anúncios filtrados por interesses do usuário

### Sprint 6.3: Tela de Definições
**Prioridade:** BAIXA
- [ ] Configurações de notificações
- [ ] Privacidade (compartilhar localização)
- [ ] Aparência (tema escuro - futuro)
- [ ] Sobre/Versão
- [ ] Logout

**Critério de Aceitação:** Usuário configura preferências do app

---

## 🎨 ÉPICO 7: UI/UX - Polimento e Experiência
**Objetivo:** Interface moderna, intuitiva e consistente

### Sprint 7.1: Bottom Navigation Moderna
**Prioridade:** ALTA
- [ ] Implementar Bottom Navigation estilizada
- [ ] Botão central FAB (+) elevado
- [ ] 5 seções: Home, Anúncios, Criar, Notificações, Locais
- [ ] Transições suaves entre fragments
- [ ] Badges de notificações

**Critério de Aceitação:** Navegação intuitiva e visualmente atraente

### Sprint 7.2: Aplicar Design System Completo
**Prioridade:** MÉDIA
- [ ] Headers com gradiente arredondado
- [ ] Cards brancos com sombra suave
- [ ] Inputs modernos (ícones, cantos arredondados)
- [ ] Botões consistentes
- [ ] Paleta de cores aplicada
- [ ] Tipografia padronizada

**Critério de Aceitação:** Todas telas seguem design guide

### Sprint 7.3: Animações e Feedback
**Prioridade:** BAIXA
- [ ] Transições de telas
- [ ] Loading states (Shimmer)
- [ ] Toasts/Snackbars consistentes
- [ ] Animações de lista
- [ ] Empty states ilustrados

**Critério de Aceitação:** App responsivo com feedback visual claro

---

## 📜 ÉPICO 8: Termos e Políticas de Uso
**Objetivo:** Conformidade legal e transparência

### Sprint 8.1: Tela de Políticas
**Prioridade:** MÉDIA
- [ ] Criar arquivo com termos de uso
- [ ] Criar arquivo com política de privacidade
- [ ] Tela de visualização (ScrollView)
- [ ] Aceite obrigatório no primeiro acesso
- [ ] Checkbox "Li e aceito"
- [ ] Link nas configurações

**Critério de Aceitação:** Usuário aceita termos antes de usar o app

---

## 🧪 ÉPICO 9: Testes e Qualidade
**Objetivo:** Garantir estabilidade e confiabilidade

### Sprint 9.1: Testes Unitários Backend
**Prioridade:** MÉDIA
- [ ] Testes de serviços (JUnit)
- [ ] Testes de repositórios
- [ ] Testes de validação
- [ ] Cobertura > 70%

### Sprint 9.2: Testes de Integração
**Prioridade:** BAIXA
- [ ] Testes de API (MockMvc)
- [ ] Testes de segurança JWT
- [ ] Testes de políticas de entrega

### Sprint 9.3: Testes Android (Instrumentados)
**Prioridade:** BAIXA
- [ ] Testes de UI (Espresso)
- [ ] Testes de navegação
- [ ] Testes de integrações (Retrofit)

---

## 📊 Ordem de Execução Recomendada

### Fase 1: Fundação (2-3 semanas)
1. ÉPICO 1 completo → Base estável e segura
2. ÉPICO 2 (Sprints 2.1, 2.2, 2.3) → Core funcional

### Fase 2: Funcionalidades Core (3-4 semanas)
3. ÉPICO 2.4 → Mapas
4. ÉPICO 3 (Sprint 3.1, 3.2) → Geolocalização
5. ÉPICO 4 (Sprint 4.1, 4.2) → Notificações

### Fase 3: Avançadas e Polimento (2-3 semanas)
6. ÉPICO 6 → Perfil
7. ÉPICO 7 (Sprint 7.1, 7.2) → UI moderna
8. ÉPICO 4.3 → Políticas de entrega
9. ÉPICO 8 → Termos

### Fase 4: P2P e Qualidade (2-3 semanas)
10. ÉPICO 5 → WiFi Direct (se tempo permitir)
11. ÉPICO 9 → Testes
12. ÉPICO 7.3 → Animações finais

---

## ✅ Checklist de Estabilidade entre Épicos

Antes de avançar para próximo épico, garantir:
- [ ] Backend rodando sem erros
- [ ] App Android compila e roda
- [ ] Funcionalidade anterior ainda funciona
- [ ] Dados no BD consistentes
- [ ] Commit e push no Git
- [ ] README atualizado se necessário
- [ ] Equipe sincronizada

---

## 🚀 Próximos Passos Imediatos

### Ação 1: Configurar BD Remoto (hoje)
```bash
# Opção 1: Railway.app (grátis 500h/mês)
# Opção 2: Render.com (grátis com limitações)
# Opção 3: Supabase (grátis até 500MB)
```

### Ação 2: Atualizar network_security_config.xml
```xml
<domain includeSubdomains="true">seu-servidor.railway.app</domain>
```

### Ação 3: Criar Branches para Épicos
```bash
git checkout -b feature/epic1-infrastructure
git checkout -b feature/epic2-announcements
# etc.
```

---

## 📝 Notas Importantes

1. **Prioridade CRÍTICA**: Não avançar sem completar
2. **Prioridade ALTA**: Necessário para MVP
3. **Prioridade MÉDIA**: Importante mas pode aguardar
4. **Prioridade BAIXA**: Nice to have, pode ser última fase

**Estimativa Total:** 10-13 semanas (2.5-3 meses) para projeto completo
