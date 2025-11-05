### **AnunciosLoc - Sistema de Anúncios Baseado em Localização**

**Instituição:** Coordenação de Engenharia Informática, DET – ISPTEC
**Data:** 09 de Outubro de 2025

#### **1. Introdução e Objetivo**
Desenvolver uma aplicação móvel distribuída (**AnunciosLoc**) para Android (versão >= 4.0) que permite a publicação e receção de mensagens baseadas na localização do utilizador em centros urbanos. A aplicação funcionará como um sistema de "post-its" digitais, utilizando **WiFi Direct** para comunicação.

**Funcionalidade Central:** Um utilizador (ex.: Alice) pode publicar um anúncio num local específico (ex.: "Largo da Independência"). Outros utilizadores que visitem esse local recebem uma notificação e podem ler a mensagem. A localização é definida por coordenadas GPS (ex.: [lat, long, raio]) ou pela deteção de um SSID de WiFi/Beacon BLE.

#### **2. Especificação do Sistema**

**2.1. Arquitetura**
- **Cliente:** Aplicação Android.
- **Servidor Central:** Gerencia o estado do sistema (utilizadores, locais, mensagens).

**2.2. Funcionalidades de Base (F1-F6)**
- **F1.** Registar utilizador
- **F2.** Login/Logout
- **F3.** Listar/Criar/Remover locais
- **F4.** Registar/Remover anúncio
- **F5.** Visualizar anúncio
- **F6.** Editar perfil de utilizador (pares chave-valor, ex.: "club=Real Madrid")

**2.3. Mensagens e Políticas**
- As mensagens são publicadas num local e podem ter uma **política de entrega** (Whitelist ou Blacklist) baseada nos perfis dos utilizadores (ex.: enviar apenas para "Profissao=Estudante").
- Incluem uma **janela temporal** de visibilidade.
- Após recebida, a mensagem fica disponível localmente no dispositivo, mesmo que o utilizador saia do local ou o tempo expire.

**2.4. Modos de Entrega**
- **Centralizado (via servidor):** O servidor notifica os clientes quando estes reportam a sua localização.
- **Descentralizado (P2P via WiFi Direct):** O publicador entrega diretamente a mensagem a dispositivos no local de destino. *(Na versão base, apenas o publicador pode distribuir a sua mensagem).*

#### **3. Funcionalidades Avançadas (Obrigatórias)**
1.  **Roteamento de Retransmissão (Mulas):** Um nó (mula) pode transportar uma mensagem um "salto" para ajudar a entrega ao destino. O publicador deve ser inteligente na seleção de mulas para otimizar mensagens e energia.
2.  **Segurança:**
    - Comunicação cliente-servidor segura (confidencialidade e integridade).
    - Autenticação da origem das mensagens e verificação de que não foram adulteradas.

#### **4. Implementação**
- **Cliente:** Aplicação Android em Java. Uso de APIs nativas. Bibliotecas de terceiros só se aprovadas.
- **Servidor:** Aplicação autónoma em Java.
- **Ambiente de Teste:** Emulador Android SDK + Emulador **Termite** (para WiFi Direct e simulação de beacons BLE como nós WiFi). Testa-se num único computador.

#### **5. Etapas de Desenvolvimento**
1.  Desenho da GUI (wireframes).
2.  Implementação da GUI (com dados simulados).
3.  Implementação da comunicação com o servidor.
4.  Implementação da comunicação WiFi Direct (com Termite).
5.  Implementação das funcionalidades avançadas.

---
