-- schema.sql - Banco de Dados AnunciosLoc
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabela: users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Tabela: sessions
CREATE TABLE sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    session_token TEXT UNIQUE NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Tabela: profile_keys (chaves públicas)
CREATE TABLE profile_keys (
    id SERIAL PRIMARY KEY,
    key_name VARCHAR(50) UNIQUE NOT NULL
);

-- Tabela: user_profiles
CREATE TABLE user_profiles (
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    key_id INTEGER NOT NULL REFERENCES profile_keys(id) ON DELETE CASCADE,
    value TEXT NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    PRIMARY KEY (user_id, key_id)
);

-- Tabela: locations
CREATE TABLE locations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    type VARCHAR(10) CHECK (type IN ('GPS', 'WIFI')) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    radius INTEGER,
    wifi_ssids TEXT[],
    created_by INTEGER NOT NULL REFERENCES users(id),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    CONSTRAINT valid_location CHECK (
        (type = 'GPS' AND latitude IS NOT NULL AND longitude IS NOT NULL AND radius IS NOT NULL AND wifi_ssids IS NULL)
        OR
        (type = 'WIFI' AND wifi_ssids IS NOT NULL AND array_length(wifi_ssids, 1) > 0 AND latitude IS NULL)
    )
);

-- Tabela: messages
CREATE TABLE messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    content TEXT NOT NULL,
    location_id INTEGER NOT NULL REFERENCES locations(id) ON DELETE CASCADE,
    publisher_id INTEGER NOT NULL REFERENCES users(id),
    delivery_mode VARCHAR(20) CHECK (delivery_mode IN ('centralized', 'decentralized')) NOT NULL,
    policy_type VARCHAR(20) CHECK (policy_type IN ('whitelist', 'blacklist', 'none')) DEFAULT 'none',
    time_start TIMESTAMPTZ,
    time_end TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ,
    CONSTRAINT valid_time_window CHECK (time_start IS NULL OR time_end IS NULL OR time_start < time_end)
);

-- Tabela: message_policies
CREATE TABLE message_policies (
    message_id UUID NOT NULL REFERENCES messages(id) ON DELETE CASCADE,
    key_id INTEGER NOT NULL REFERENCES profile_keys(id),
    value TEXT NOT NULL,
    PRIMARY KEY (message_id, key_id, value)
);

-- Tabela: received_messages
CREATE TABLE received_messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    message_id UUID NOT NULL REFERENCES messages(id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    received_at TIMESTAMPTZ DEFAULT NOW(),
    read_at TIMESTAMPTZ,
    UNIQUE(message_id, user_id)
);