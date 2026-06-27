const Database = require('better-sqlite3');
const path = require('path');

const DB_PATH = path.join(__dirname, 'liqid.db');

let db;

function getDb() {
  if (!db) {
    db = new Database(DB_PATH);
    db.pragma('journal_mode = WAL');
    db.pragma('foreign_keys = ON');
    initTables();
  }
  return db;
}

function initTables() {
  db.exec(`
    CREATE TABLE IF NOT EXISTS account (
      id TEXT PRIMARY KEY,
      balance REAL NOT NULL,
      currency TEXT NOT NULL,
      daily_change REAL NOT NULL DEFAULT 0
    );

    CREATE TABLE IF NOT EXISTS user_info (
      id TEXT PRIMARY KEY,
      name TEXT NOT NULL,
      initials TEXT NOT NULL,
      phone TEXT NOT NULL,
      plan TEXT NOT NULL DEFAULT 'Standard'
    );

    CREATE TABLE IF NOT EXISTS transactions (
      id TEXT PRIMARY KEY,
      merchant_name TEXT NOT NULL,
      date TEXT NOT NULL,
      amount REAL NOT NULL,
      type TEXT NOT NULL CHECK(type IN ('DEBIT', 'CREDIT')),
      status TEXT NOT NULL CHECK(status IN ('PENDING', 'COMPLETED', 'FAILED')),
      category TEXT NOT NULL DEFAULT '',
      reference TEXT NOT NULL,
      description TEXT NOT NULL DEFAULT ''
    );

    CREATE TABLE IF NOT EXISTS bank_cards (
      id TEXT PRIMARY KEY,
      card_number TEXT NOT NULL,
      cardholder_name TEXT NOT NULL,
      expiry_date TEXT NOT NULL,
      plan TEXT NOT NULL,
      scheme TEXT NOT NULL,
      frozen INTEGER NOT NULL DEFAULT 0,
      online_enabled INTEGER NOT NULL DEFAULT 1,
      contactless_enabled INTEGER NOT NULL DEFAULT 1,
      atm_enabled INTEGER NOT NULL DEFAULT 1
    );

    CREATE TABLE IF NOT EXISTS crypto_assets (
      id TEXT PRIMARY KEY,
      name TEXT NOT NULL,
      symbol TEXT NOT NULL,
      price REAL NOT NULL,
      change_24h REAL NOT NULL,
      value REAL NOT NULL,
      amount REAL NOT NULL
    );

    CREATE TABLE IF NOT EXISTS users (
      id TEXT PRIMARY KEY,
      name TEXT NOT NULL,
      email TEXT UNIQUE NOT NULL,
      phone TEXT NOT NULL,
      pin_hash TEXT NOT NULL,
      created_at TEXT DEFAULT (datetime('now'))
    );

    CREATE TABLE IF NOT EXISTS cards_detail (
      id TEXT PRIMARY KEY,
      account_id TEXT NOT NULL,
      card_number TEXT NOT NULL,
      cardholder_name TEXT NOT NULL,
      expiry_month INTEGER NOT NULL,
      expiry_year INTEGER NOT NULL,
      type TEXT NOT NULL CHECK(type IN ('PHYSICAL', 'VIRTUAL')),
      status TEXT NOT NULL CHECK(status IN ('ACTIVE', 'FROZEN', 'BLOCKED', 'EXPIRED')),
      scheme TEXT NOT NULL CHECK(scheme IN ('VISA', 'MASTERCARD')),
      contactless_enabled INTEGER NOT NULL DEFAULT 1,
      online_payments_enabled INTEGER NOT NULL DEFAULT 1,
      atm_enabled INTEGER NOT NULL DEFAULT 1,
      spending_limit REAL NOT NULL DEFAULT 0
    );
  `);
}

module.exports = { getDb };
