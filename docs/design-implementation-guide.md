# Guia de Implementação do Design Moderno

## Visão Geral
Este documento descreve as mudanças necessárias para aplicar o design do protótipo ao aplicativo Android.

## Princípios de Design

### 1. **Headers com Gradiente Arredondado**
- Gradiente: `blue-600 → blue-500 → purple-600` (135deg)
- Cantos inferiores arredondados: `40dp`
- Altura: ~40% da tela
- Elementos sobre o header: logo, título, subtítulo (todos em branco)

### 2. **Cartões (Cards)**
- Fundo: Branco puro
- Cantos: `24dp` (muito arredondado)
- Elevação: `4dp` a `8dp` (sombra suave)
- Borda: `1dp` em `gray_100` (opcional)
- Padding interno: `16dp` a `24dp`

### 3. **Inputs de Formulário**
- Altura: `56dp`
- Cantos arredondados: `12dp`
- Ícone à esquerda (Email, Lock, etc)
- Labels acima do input (fora do campo)
- Cor de foco: `blue_600`

### 4. **Botões**
- Altura: `56dp`
- Cantos arredondados: `12dp`
- Background: `blue_600` (primário), `purple_600` (secundário)
- Texto: Branco, 16sp, bold, sem ALL CAPS

### 5. **BottomNavigation**
- Fundo branco com elevação
- Cantos superiores arredondados: `24dp`
- 5 itens: Home, Anúncios, **Criar** (central elevado), Notificações, Locais
- Botão central: Círculo azul elevado (-32dp margin top), 64dp × 64dp, ícone Plus
- Itens ativos: `blue_600`, inativas: `gray_400`

### 6. **Paleta de Cores**
```xml
<!-- Primárias -->
blue_600: #2563EB
blue_500: #3B82F6
blue_700: #1D4ED8
purple_600: #9333EA
purple_500: #A855F7
orange_600: #EA580C

<!-- Neutras -->
white: #FFFFFF
gray_50: #F9FAFB
gray_100: #F3F4F6
gray_400: #9CA3AF
gray_700: #374151
gray_800: #1F2937
```

### 7. **Tipografia**
- Títulos grandes: 32sp, bold
- Títulos de card: 18sp, bold
- Texto normal: 14sp-16sp
- Labels: 14sp, bold
- Cor de texto principal: `gray_800`
- Cor de texto secundário: `gray_600`

### 8. **Espaçamentos**
- Padding de telas: `24dp` (horizontal), `16dp` (vertical)
- Espaço entre cards: `12dp` a `16dp`
- Espaço entre elementos de formulário: `24dp`

## Arquivos a Atualizar

### Drawables Necessários
✅ `bg_gradient_blue_purple.xml` - Header com gradiente
✅ `bg_gradient_blue.xml` - Header simples azul
✅ `bg_card_rounded.xml` - Card branco arredondado
✅ `ic_email.xml` - Ícone de email
✅ `ic_lock.xml` - Ícone de cadeado
- `bg_button_rounded.xml` - Botão arredondado
- `bg_bottom_nav.xml` - Bottom navigation arredondado
- `bg_icon_circle_blue.xml` - Círculo azul para ícones
- `bg_icon_circle_purple.xml` - Círculo roxo para ícones
- `bg_icon_circle_orange.xml` - Círculo laranja para ícones

### Layouts Prioritários
1. **activity_login.xml** - ✅ Atualizado
2. **activity_register.xml** - Aplicar mesmo estilo do login
3. **activity_main.xml** - Adicionar BottomNavigation estilizado
4. **fragment_profile.xml** - Header azul + cards de atributos
5. **fragment_announcements.xml** - Cards modernos
6. **fragment_locations.xml** - Cards com ícones circulares
7. **fragment_notifications.xml** - Cards de notificação

### Componentes de UI
- **Card de Anúncio**: Branco, sombra, título bold, descrição gray-600, footer com ícones
- **Card de Local**: Ícone em círculo azul, nome bold, coordenadas em gray-500
- **Card de Notificação**: Ícone colorido à esquerda, texto multilinha
- **Stats Cards**: Gradiente colorido, ícone branco, número grande, label pequena

## Próximos Passos

1. ✅ Criar todos os drawables necessários
2. Atualizar `activity_register.xml`
3. Criar `BottomNavigationView` customizado
4. Atualizar fragments com header e cards
5. Criar adapters com novo estilo de cards
6. Adicionar animações suaves (fade, slide)

## Referências
- Protótipo: `docs/Design Interface/`
- Figma: Componentes em `src/components/`
- Cores: `src/index.css` (variáveis CSS → Android colors.xml)
