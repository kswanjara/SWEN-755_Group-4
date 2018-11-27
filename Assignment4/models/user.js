const db = require('./database');

module.exports = db.Model.extend({
    tableName: 'users'
});