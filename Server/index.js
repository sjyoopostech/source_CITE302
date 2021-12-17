var express = require('express');
var fs = require('fs');
var bodyParser = require('body-parser');
var app = express();
app.use(bodyParser.urlencoded({extended:true}));
app.use(bodyParser.json());

// server-client connection
var server = app.listen(3001, function() {
	var host = server.address().address;
	var port = server.address().port;
	console.log('Running at %s:%s', host, port);
});

// mysql (database)
var mysql = require('mysql');

// server-database connection
// server-database connection
var database_restaurant = mysql.createConnection({
    host : 'localhost',
    user : 'root',
    password : 'XXXXXXXX',
    port : 3306,
    database : 'cite302projectdatabase'
  });

database_restaurant.connect();

// main page
app.get('/', function(req, res) {
	res.send('CITE302 Project Server');
});

////////////////// Deprecated //////////////////////
app.get('/show_restaurant', function(req, res) {
	var string = "select * from restaurant";
	database_restaurant.query(string, function(err, data) {
		if (err) console.log(err);
		else {
			console.log(string);

			var container = new Object();
			container.restaurant = data;
			res.send(JSON.stringify(container));
		}
	});
});

// restaurant_all
// prayerroom_all
// restaurant_simple (south, north, west, east)
// prayerroom_simple (south, north, west, east)
// restaurant_one (id)
// prayerroom_one (id)

app.get('/restaurant_all', function(req, res) {
	var string = "select * from data_restaurant";
	database_restaurant.query(string, function(err, data) {
		if (err) console.log(err);
		else {
			console.log(string);
			res.send(data);
		}
	});
});

app.get('/prayerroom_all', function(req, res) {
	var string = "select * from data_prayerroom";
	database_restaurant.query(string, function(err, data) {
		if (err) console.log(err);
		else {
			console.log(string);
			res.send(data);
		}
	});
});

app.get('/restaurant_simple', function(req, res) {
	var north = req.query.north;
	var south = req.query.south;
	var west = req.query.west;
	var east = req.query.east;
	var string = "select ID, name_english, type_muslimfriendly, type_food1, type_food2, type_food3, lat, lng from data_restaurant";
	string += " where lat>"+south + " and lat<"+north + " and lng>"+west + " and lng<"+east;
	database_restaurant.query(string, function(err, data) {
		if (err) console.log(err);
		else {
			console.log(string);
			res.send(data);
		}
	});
});

app.get('/prayerroom_simple', function(req, res) {
	var north = req.query.north;
	var south = req.query.south;
	var west = req.query.west;
	var east = req.query.east;
	var string = "select ID, name_english, type_prayerroom, lat, lng from data_prayerroom";
	string += " where lat>"+south + " and lat<"+north + " and lng>"+west + " and lng<"+east;
	database_restaurant.query(string, function(err, data) {
		if (err) console.log(err);
		else {
			console.log(string);
			res.send(data);
		}
	});
});

app.get('/restaurant_one', function(req, res) {
	var id = req.query.id;
	var string = "select * from data_restaurant where ID=" + id;
	database_restaurant.query(string, function(err, data) {
		if (err) console.log(err);
		else {
			console.log(string);
			res.send(data);
		}
	});
});

app.get('/prayerroom_one', function(req, res) {
	var id = req.query.id;
	var string = "select * from data_prayerroom where ID=" + id;
	database_restaurant.query(string, function(err, data) {
		if (err) console.log(err);
		else {
			console.log(string);
			res.send(data);
		}
	});
});