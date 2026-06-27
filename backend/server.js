const express = require('express');
const cors = require('cors');
const { v4: uuidv4 } = require('uuid');
const { getDb } = require('./db');
const { router: authRouter, authMiddleware } = require('./auth');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(express.json());

getDb();

app.use('/api', authRouter);

// ---- Health check ----
app.get('/api/health', (_req, res) => {
  res.json({ status: 'ok', message: 'Liqid API is running' });
});

// ---- Account ----
app.get('/api/account', (req, res) => {
  try {
    const db = getDb();
    const account = db.prepare('SELECT * FROM account LIMIT 1').get();
    if (!account) return res.status(404).json({ error: 'Account not found' });
    res.json(account);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ---- User ----
app.get('/api/user', (req, res) => {
  try {
    const db = getDb();
    const user = db.prepare('SELECT * FROM user_info LIMIT 1').get();
    if (!user) return res.status(404).json({ error: 'User not found' });
    res.json(user);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ---- Transactions ----
app.get('/api/transactions', (req, res) => {
  try {
    const db = getDb();
    const transactions = db.prepare('SELECT * FROM transactions ORDER BY date DESC').all();
    res.json(transactions);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/transactions', authMiddleware, (req, res) => {
  try {
    const { iban, amount, description } = req.body;
    if (!iban || !amount) {
      return res.status(400).json({ error: 'IBAN and amount are required' });
    }
    const parsedAmount = parseFloat(amount);
    if (isNaN(parsedAmount) || parsedAmount <= 0) {
      return res.status(400).json({ error: 'Invalid amount' });
    }
    const db = getDb();
    const now = new Date();
    const dateStr = `${now.getDate()} ${now.toLocaleString('fr-FR', { month: 'long' })} ${now.getHours()}:${String(now.getMinutes()).padStart(2, '0')}`;
    const ref = `VLB-${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 1000)).padStart(3, '0')}`;
    const txId = uuidv4();
    db.prepare(`INSERT INTO transactions (id, merchant_name, date, amount, type, status, category, reference, description) VALUES (?, ?, ?, ?, 'DEBIT', 'COMPLETED', 'Virement', ?, ?)`)
      .run(txId, 'Alpha Barry', dateStr, parsedAmount, ref, description || '');
    const account = db.prepare('SELECT * FROM account LIMIT 1').get();
    if (account) {
      db.prepare('UPDATE account SET balance = balance - ?, daily_change = daily_change - ? WHERE id = ?')
        .run(parsedAmount, parsedAmount, account.id);
    }
    const transaction = db.prepare('SELECT * FROM transactions WHERE id = ?').get(txId);
    res.status(201).json(transaction);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ---- Bank Cards (BankCard model) ----
app.get('/api/cards', (req, res) => {
  try {
    const db = getDb();
    const cards = db.prepare('SELECT * FROM bank_cards').all();
    cards.forEach(c => {
      c.frozen = Boolean(c.frozen);
      c.online_enabled = Boolean(c.online_enabled);
      c.contactless_enabled = Boolean(c.contactless_enabled);
      c.atm_enabled = Boolean(c.atm_enabled);
    });
    res.json(cards);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.put('/api/cards/:id', authMiddleware, (req, res) => {
  try {
    const db = getDb();
    const card = db.prepare('SELECT * FROM bank_cards WHERE id = ?').get(req.params.id);
    if (!card) return res.status(404).json({ error: 'Card not found' });
    const { frozen, online_enabled, contactless_enabled, atm_enabled } = req.body;
    db.prepare(`UPDATE bank_cards SET frozen = ?, online_enabled = ?, contactless_enabled = ?, atm_enabled = ? WHERE id = ?`)
      .run(
        frozen !== undefined ? (frozen ? 1 : 0) : card.frozen,
        online_enabled !== undefined ? (online_enabled ? 1 : 0) : card.online_enabled,
        contactless_enabled !== undefined ? (contactless_enabled ? 1 : 0) : card.contactless_enabled,
        atm_enabled !== undefined ? (atm_enabled ? 1 : 0) : card.atm_enabled,
        req.params.id
      );
    const updated = db.prepare('SELECT * FROM bank_cards WHERE id = ?').get(req.params.id);
    updated.frozen = Boolean(updated.frozen);
    updated.online_enabled = Boolean(updated.online_enabled);
    updated.contactless_enabled = Boolean(updated.contactless_enabled);
    updated.atm_enabled = Boolean(updated.atm_enabled);
    res.json(updated);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ---- Crypto Assets ----
app.get('/api/crypto', (req, res) => {
  try {
    const db = getDb();
    const assets = db.prepare('SELECT * FROM crypto_assets').all();
    res.json(assets);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ---- Card Details (Card model) ----
app.get('/api/cards/detail', (req, res) => {
  try {
    const db = getDb();
    const { accountId } = req.query;
    let cards;
    if (accountId) {
      cards = db.prepare('SELECT * FROM cards_detail WHERE account_id = ?').all(accountId);
    } else {
      cards = db.prepare('SELECT * FROM cards_detail').all();
    }
    cards.forEach(c => {
      c.contactless_enabled = Boolean(c.contactless_enabled);
      c.online_payments_enabled = Boolean(c.online_payments_enabled);
      c.atm_enabled = Boolean(c.atm_enabled);
    });
    res.json(cards);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.get('/api/cards/detail/:id', (req, res) => {
  try {
    const db = getDb();
    const card = db.prepare('SELECT * FROM cards_detail WHERE id = ?').get(req.params.id);
    if (!card) return res.status(404).json({ error: 'Card not found' });
    card.contactless_enabled = Boolean(card.contactless_enabled);
    card.online_payments_enabled = Boolean(card.online_payments_enabled);
    card.atm_enabled = Boolean(card.atm_enabled);
    res.json(card);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.put('/api/cards/detail/:id/status', authMiddleware, (req, res) => {
  try {
    const db = getDb();
    const card = db.prepare('SELECT * FROM cards_detail WHERE id = ?').get(req.params.id);
    if (!card) return res.status(404).json({ error: 'Card not found' });
    const { status } = req.body;
    if (!status) return res.status(400).json({ error: 'Status is required' });
    const validStatuses = ['ACTIVE', 'FROZEN', 'BLOCKED', 'EXPIRED'];
    if (!validStatuses.includes(status)) {
      return res.status(400).json({ error: `Invalid status. Must be one of: ${validStatuses.join(', ')}` });
    }
    db.prepare('UPDATE cards_detail SET status = ? WHERE id = ?').run(status, req.params.id);
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.listen(PORT, () => {
  console.log(`Liqid API server running on port ${PORT}`);
  console.log(`Health check: http://localhost:${PORT}/api/health`);
});
