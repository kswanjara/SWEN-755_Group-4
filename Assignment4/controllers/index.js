const express = require('express');
const router = express.Router();
const path = require('path');
const bcrypt = require('bcrypt');

const User = require('../models/user');

const auth = function(req, res, next) {
    if (req.session && req.session.user) {
        return next();
    } else {
        res.redirect('/login');
    }
};
const notAuth = function(req, res, next) {
    if (!(req.session && req.session.user)) {
        return next();
    } else {
        res.redirect('/');
    }
};

router.get('/', auth, (req, res) => {
    res.render('index', {
        user: req.session.user,
        role: req.session.role
    });
});

router.get('/register', notAuth, (req, res) => {
    res.render('register', {
        complete: false,
        error: false
    });
});

router.get('/login', notAuth, (req, res) => {
    res.render('login', {
        complete: false,
        error: false
    });
});

router.get('/logout', (req, res) => {
    req.session.destroy();
    res.redirect('/login');
});

router.post('/destroy', function (req, res) {
    console.log('Success!')
    req.session.destroy();
});

router.post('/register', notAuth, function (req, res) {
    User.forge({
            username: req.body.username,
            password: bcrypt.hashSync(req.body.password, 10)
        })
        .save()
        .then(function (user) {
            res.render('register', {
                complete: true,
                error: false
            });
        })
        .catch(function (err) {
            res.render('register', {
                complete: false,
                error: true
            });
        });
});

router.post('/login', notAuth, function (req, res) {
    User.where('username', req.body.username)
        .fetch()
        .then(function (user) {
            if (user) {
                bcrypt.compare(req.body.password, user.get('password'), function(err, match) {
                    if (match) {
                        // saves the user's info to the session
                        req.session.user = user.get('username');
                        req.session.role = user.get('role');

                        if(user.get('role') === "Admin"){
                            res.render('login', {
                                admin: true,
                                complete: true,
                                error: false
                            });
                        }else{
                            res.render('login', {
                                admin: false,
                                complete: true,
                                error: false
                            });
                        }
                    } else {
                        res.render('login', {
                            admin: false,
                            complete: false,
                            error: true
                        });
                    }
                });

            } else {
                res.render('login', {
                    admin: false,
                    complete: false,
                    error: true
                });
            }
        })
        .catch(function (err) {
            res.render('login', {
                admin: false,
                complete: false,
                error: true
            });
        });
});

module.exports = router;