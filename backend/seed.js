const { getDb } = require('./db');
const bcrypt = require('bcryptjs');

function seed() {
  const db = getDb();

  const accountCount = db.prepare('SELECT COUNT(*) as count FROM account').get().count;
  if (accountCount > 0) {
    console.log('Database already seeded.');
    return;
  }

  const insertAccount = db.prepare('INSERT INTO account (id, balance, currency, daily_change) VALUES (?, ?, ?, ?)');
  const insertUser = db.prepare('INSERT INTO user_info (id, name, initials, phone, plan) VALUES (?, ?, ?, ?, ?)');
  const insertAuthUser = db.prepare('INSERT INTO users (id, name, email, phone, pin_hash) VALUES (?, ?, ?, ?, ?)');
  const insertTx = db.prepare('INSERT INTO transactions (id, merchant_name, date, amount, type, status, category, reference, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)');
  const insertCard = db.prepare('INSERT INTO bank_cards (id, card_number, cardholder_name, expiry_date, plan, scheme, frozen, online_enabled, contactless_enabled, atm_enabled) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)');
  const insertCrypto = db.prepare('INSERT INTO crypto_assets (id, name, symbol, price, change_24h, value, amount) VALUES (?, ?, ?, ?, ?, ?, ?)');
  const insertCardDetail = db.prepare('INSERT INTO cards_detail (id, account_id, card_number, cardholder_name, expiry_month, expiry_year, type, status, scheme, contactless_enabled, online_payments_enabled, atm_enabled, spending_limit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)');

  const transaction = db.transaction(() => {
    insertAccount.run('acc_1', 0.0, 'EUR', 0.0);

    insertUser.run('user_1', 'Lunkht', 'L', '+224 6X XX XX XX', 'Standard');
    insertAuthUser.run('user_1', 'Lunkht', 'lunkht@liqid.app', '+224 6X XX XX XX', bcrypt.hashSync('000000', 10));

    insertTx.run('tx_1', 'Netflix', '24 juin 14:32', 15.99, 'DEBIT', 'COMPLETED', 'Abonnements', 'NFLX-2025-06-001', 'Abonnement mensuel');
    insertTx.run('tx_2', 'Amazon', '23 juin 09:15', 89.90, 'DEBIT', 'COMPLETED', 'Shopping', 'AMZ-2025-06-002', 'Commande #AMZ-28471');
    insertTx.run('tx_3', 'Virement reçu', '22 juin 16:45', 500.00, 'CREDIT', 'COMPLETED', 'Transfert', 'TRF-2025-06-003', 'Remboursement prêt');
    insertTx.run('tx_4', 'Uber', '21 juin 22:30', 12.50, 'DEBIT', 'PENDING', 'Transport', 'UBR-2025-06-004', 'Trajet Aéroport Centre');
    insertTx.run('tx_5', 'Spotify', '21 juin 00:00', 9.99, 'DEBIT', 'COMPLETED', 'Abonnements', 'SPF-2025-06-005', 'Abonnement mensuel');

    insertCard.run('card_1', '4721', 'Lunkht', '09/28', 'STANDARD', 'visa', 0, 1, 1, 1);
    insertCard.run('card_2', '8902', 'Lunkht', '03/30', 'PREMIUM', 'mastercard', 0, 1, 1, 0);

    insertCrypto.run('crypto_1', 'Bitcoin', 'BTC', 42150.00, 2.4, 1250.00, 0.0297);
    insertCrypto.run('crypto_2', 'Ethereum', 'ETH', 2850.00, -1.2, 950.00, 0.3333);
    insertCrypto.run('crypto_3', 'Solana', 'SOL', 145.00, 5.8, 580.00, 4.0);
    insertCrypto.run('crypto_4', 'Cardano', 'ADA', 0.45, -0.5, 225.00, 500.0);

    insertCardDetail.run('card_001', 'acc_001', '4521', 'John Doe', 12, 2027, 'PHYSICAL', 'ACTIVE', 'VISA', 1, 1, 1, 5000.00);
    insertCardDetail.run('card_002', 'acc_001', '7890', 'John Doe', 6, 2028, 'VIRTUAL', 'ACTIVE', 'MASTERCARD', 0, 1, 0, 1000.00);
  });

  transaction();
  console.log('Database seeded successfully.');
}

module.exports = { seed };

// Allow running directly: node seed.js
if (require.main === module) {
  seed();
}
