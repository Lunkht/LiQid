const express = require('express');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const { v4: uuidv4 } = require('uuid');
const { getDb } = require('./db');

const router = express.Router();
const JWT_SECRET = process.env.JWT_SECRET || 'liqid-dev-secret-key-change-in-production';
const JWT_EXPIRES_IN = '30d';

function generateToken(userId) {
  return jwt.sign({ userId }, JWT_SECRET, { expiresIn: JWT_EXPIRES_IN });
}

function authMiddleware(req, res, next) {
  const header = req.headers.authorization;
  if (!header || !header.startsWith('Bearer ')) {
    return res.status(401).json({ error: 'Token manquant' });
  }
  try {
    const decoded = jwt.verify(header.split(' ')[1], JWT_SECRET);
    req.userId = decoded.userId;
    next();
  } catch {
    return res.status(401).json({ error: 'Token invalide ou expiré' });
  }
}

router.post('/auth/register', (req, res) => {
  try {
    const { name, email, phone, pin } = req.body;
    if (!name || !email || !phone || !pin) {
      return res.status(400).json({ error: 'Tous les champs sont requis' });
    }
    const db = getDb();
    const existing = db.prepare('SELECT id FROM users WHERE email = ?').get(email);
    if (existing) {
      return res.status(409).json({ error: 'Cet email est déjà utilisé' });
    }
    const hashedPin = bcrypt.hashSync(pin, 10);
    const userId = uuidv4();
    db.prepare('INSERT INTO users (id, name, email, phone, pin_hash) VALUES (?, ?, ?, ?, ?)')
      .run(userId, name, email, phone, hashedPin);
    const token = generateToken(userId);
    res.status(201).json({ token, user: { id: userId, name, email, phone } });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

router.post('/auth/login', (req, res) => {
  try {
    const { email, pin } = req.body;
    if (!email || !pin) {
      return res.status(400).json({ error: 'Email et PIN requis' });
    }
    const db = getDb();
    const user = db.prepare('SELECT * FROM users WHERE email = ?').get(email);
    if (!user) {
      return res.status(401).json({ error: 'Email ou PIN incorrect' });
    }
    if (!bcrypt.compareSync(pin, user.pin_hash)) {
      return res.status(401).json({ error: 'Email ou PIN incorrect' });
    }
    const token = generateToken(user.id);
    res.json({ token, user: { id: user.id, name: user.name, email: user.email, phone: user.phone } });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

router.get('/auth/me', authMiddleware, (req, res) => {
  try {
    const db = getDb();
    const user = db.prepare('SELECT id, name, email, phone FROM users WHERE id = ?').get(req.userId);
    if (!user) return res.status(404).json({ error: 'Utilisateur introuvable' });
    res.json(user);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = { router, authMiddleware };
