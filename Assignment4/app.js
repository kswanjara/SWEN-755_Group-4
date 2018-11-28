const express = require('express');
const session = require('express-session');
const app = express();
const bodyParser = require('body-parser');
const exphbs = require('express-handlebars');
const port = process.env.PORT || 3000;

// Session Management
app.use(session({
    secret: 'cool_secret',
    resave: false,
    saveUninitialized: true,
    cookie: {
        expires: 10 * 1000
    }
}));


// Templates
app.engine('handlebars', exphbs({
    defaultLayout: 'main'
}));
app.set('view engine', 'handlebars');

// Allow the use of the public folder for CSS and JS
app.use(express.static('public'));

// Allow the use of JSON
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

// Include the routing from controllers
app.use(require('./controllers'));
// Run the server
app.listen(port, () => console.log(`Listening on port ${port}`));