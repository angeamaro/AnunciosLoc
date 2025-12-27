# Configuração do Backend - AnunciosLoc

## 🔧 Configuração das Variáveis de Ambiente

### Método 1: Arquivo .env (Recomendado para Desenvolvimento)

1. Copie o arquivo de exemplo:
```bash
cp .env.example .env
```

2. Edite o arquivo `.env` com suas credenciais reais

3. Execute o backend:
```bash
./mvnw spring-boot:run
```

**Nota:** O Spring Boot não carrega arquivos `.env` automaticamente. Você precisa usar uma das opções abaixo.

### Método 2: Variáveis de Ambiente do Sistema

**Linux/Mac:**
```bash
export DATABASE_URL=
export DATABASE_USERNAME=
export DATABASE_PASSWORD=
./mvnw spring-boot:run
```

**Windows (PowerShell):**
```powershell
$env:DATABASE_URL=
$env:DATABASE_USERNAME=
$env:DATABASE_PASSWORD=
./mvnw spring-boot:run
```

### Método 3: IDE (IntelliJ/Eclipse)

**IntelliJ IDEA:**
1. Run → Edit Configurations
2. Environment Variables → Adicione:
   - `DATABASE_URL`
   - `DATABASE_USERNAME`
   - `DATABASE_PASSWORD`

**Eclipse:**
1. Run → Run Configurations
2. Environment Tab → New
3. Adicione as variáveis

### Método 4: Linha de Comando (Único Comando)

```bash
./mvnw spring-boot:run \
  -Dspring-boot.run.arguments="\
    --DATABASE_URL= ''\
    --DATABASE_USERNAME='' \
    --DATABASE_PASSWORD=''"
```

## 🔒 Segurança

⚠️ **NUNCA faça commit do arquivo `.env` com credenciais reais!**

- ✅ `.env` está no `.gitignore`
- ✅ Compartilhe apenas `.env.example` (sem credenciais)
- ✅ Use variáveis de ambiente em produção (Railway, Heroku, etc.)

## 🗄️ Credenciais Atuais

### Banco Neon (Produção)
- Host: `ep-gentle-night-abh88026-pooler.eu-west-2.aws.neon.tech`
- Database: `neondb`
- User: `neondb_owner`
- Password: Veja com o time ou no `.env`

### Banco Local (Desenvolvimento)
- Host: `localhost:5432`
- Database: `anunciosloc`
- User: `anuncios_user`
- Password: `Anunciosloc@g02`
