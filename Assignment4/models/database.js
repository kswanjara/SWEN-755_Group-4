var knex = require('knex')({
    client: 'pg',
    connection: {
        host: 'baasu.db.elephantsql.com',
        user: 'ddzzfwmt',
        password: 'zQvWNanIxJSS99-3UrGD3DaiyolmRmTH',
        database: 'ddzzfwmt',
        charset: 'utf8'
    }
});

module.exports = require('bookshelf')(knex);