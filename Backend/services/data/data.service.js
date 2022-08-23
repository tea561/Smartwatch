"use strict"

const express = require("express");
const swaggerUi = require('swagger-ui-express');
const swaggerDocument = require('../../swagger.json');
const Influx = require('influx');
const bodyParser = require('body-parser');

module.exports = {
	name: 'data',
	settings: {
		port: 3333 
	},
	methods: {
		initRoutes(app) {
			app.get("/getVitals", this.getVitals);
			app.get("/getPulseForDay", this.getPulseForDay);
			app.get("/getPulseForWeek", this.getPulseForWeek);
			app.get("/getSysPressureForDay", this.getSysPressureForDay);
			app.get("/getSysPressureForWeek", this.getSysPressureForWeek);
			app.get("/getDiasPressureForDay", this.getDiasPressureForDay);
			app.get("/getDiasPressureForWeek", this.getDiasPressureForWeek);
			app.get("/getSleepHoursForDay", this.getSleepHoursForDay);
			app.get("/getSleepHoursForWeek", this.getSleepHoursForWeek);
			app.get("/getCaloriesForDay", this.getCaloriesForDay);
			app.get("/getCaloriesForWeek", this.getCaloriesForWeek);
			app.get("/getStepsForWeek", this.getStepsForWeek);
			app.post("/postVitals", this.postVitals);
			app.post("/postSleepHours", this.postSleepHours);
			app.post("/postSteps", this.postSteps);
			app.post("/postCalories", this.postCalories);
			app.put("/updateVitals", this.putVitals);
			app.delete("/deleteVitals", this.deleteVitals);
			app.delete("/deleteSleepHours", this.deleteSleepHours);
		},
		async getVitals(req, res) {
			try {
				const sysPressure = await this.influx.query(
					`select last(*) from "sys-pressure" where userID='${req.query.userID}'`
				);
				if (sysPressure[0] == undefined) {
					res.status(404);
					res.send(`There is no entry for user with id = ${req.query.userID}.`);
					return null;
				}
				const diasPressure = await this.influx.query(
					`select last(*) from "dias-pressure" where userID='${req.query.userID}'`
				);
				const pulse = await this.influx.query(
					`select last(*) from "pulse" where userID='${req.query.userID}'`
				);
				res.send({
					sys: sysPressure[0].last_value,
					dias: diasPressure[0].last_value,
					pulse: pulse[0].last_value,
					userID: req.query.userID,
					timestamp: new Date(sysPressure[0].time).getTime()
				});
			}
			catch(err){
				console.log(err);
				res.status(500).send(err);
			}
		},
		async getPulseForDay(req, res) {
			try {
				const pulseArr = await this.influx.query(
					`select * from pulse where time > now() - 24h and userID ='${req.query.userID}'`
				)
				if(pulseArr == [])
				{
					res.status(404);
					res.send(`There is no entry for user with id = ${req.query.userID}.`);
					return null;
				}
				console.log(pulseArr)
				res.send(pulseArr)
			}
			catch(err){
				console.log(err);
				res.status(500).send(err)
			}
		},
		async getPulseForWeek(req, res) {
			try {
				const weekArr = await this.influx.query(
					`select mean(value) from pulse where time > now() - 7d and userID='${req.query.userID}' group by time(1d) `
				)
				if(weekArr == [])
				{
					res.status(404);
					res.send(`There is no entry for user with id = ${req.query.userID}.`);
					return null;
				}
				var resultArr = [];
				weekArr.forEach(element => {
					var newElement = {value: null, time: null};
					newElement.value = element.mean
					newElement.time = element.time
					resultArr.push(newElement)
				});
				console.log(weekArr)
				res.send(resultArr)
			}
			catch(err){
				console.log(err);
				res.status(500).send(err)
			}
		},
		async getSysPressureForDay(req, res) {
			try{
				const sysPressureArr = await this.influx.query(
					`select * from "sys-pressure" where time > now() - 24h and userID ='${req.query.userID}'`
				)
				if(sysPressureArr == [])
				{
					res.status(404);
					res.send(`There is no entry for user with id = ${req.query.userID}.`);
					return null;
				}
				console.log(sysPressureArr)
				res.send(sysPressureArr)
			}
			catch(err){
				console.log(err);
				res.status(500).send(err)
			}
		},
		async getSysPressureForWeek(req, res) {
			try {
				const weekArr = await this.influx.query(
					`select mean(value) from "sys-pressure" where time > now() - 7d and userID='${req.query.userID}' group by time(1d) `
				)
				if(weekArr == [])
				{
					res.status(404);
					res.send(`There is no entry for user with id = ${req.query.userID}.`);
					return null;
				}
				var resultArr = [];
				weekArr.forEach(element => {
					var newElement = {value: null, time: null};
					newElement.value = element.mean
					newElement.time = element.time
					resultArr.push(newElement)
				});
				console.log(weekArr)
				res.send(resultArr)
			}
			catch(err){
				console.log(err);
				res.status(500).send(err)
			}
		},
		async getDiasPressureForDay(req, res) {
			try{
				const diasPressureArr = await this.influx.query(
					`select * from "dias-pressure" where time > now() - 24h and userID ='${req.query.userID}'`
				)
				if(diasPressureArr == [])
				{
					res.status(404);
					res.send(`There is no entry for user with id = ${req.query.userID}.`);
					return null;
				}
				console.log(diasPressureArr)
				res.send(diasPressureArr)
			}
			catch(err){
				console.log(err);
				res.status(500).send(err)
			}
		},
		async getDiasPressureForWeek(req, res) {
			try {
				const weekArr = await this.influx.query(
					`select mean(value) from "dias-pressure" where time > now() - 7d and userID='${req.query.userID}' group by time(1d) `
				)
				if(weekArr == [])
				{
					res.status(404);
					res.send(`There is no entry for user with id = ${req.query.userID}.`);
					return null;
				}
				var resultArr = [];
				weekArr.forEach(element => {
					var newElement = {value: null, time: null};
					newElement.value = element.mean
					newElement.time = element.time
					resultArr.push(newElement)
				});
				console.log(weekArr)
				res.send(resultArr)
			}
			catch(err){
				console.log(err);
				res.status(500).send(err)
			}
		},
		async getSleepHoursForDay(req, res) {
			try{
				const sleepHours = await this.influx.query(
					`select sum(value) from "sleep-hours" where time > now() - 24h and userID ='${req.query.userID}'`
				)
				//console.log(sleepHours)
				if(sleepHours == [])
				{
					res.status(404);
					res.send(`There is no entry for user with id = ${req.query.userID}.`);
					return null;
				}
				var resultArr = []
				sleepHours.forEach(element => {
					var newElement = {sleepHours: null, timestamp: null};
					newElement.sleepHours = element.sum
					newElement.timestamp = element.time
					resultArr.push(newElement)
				})
				res.send(resultArr)
			}
			catch(err){
				console.log(err);
				res.status(500).send(err)
			}
		},
		async getSleepHoursForWeek(req, res) {
			try {
				const weekArr = await this.influx.query(
					`select sum(value) from "sleep-hours" where time > now() - 7d and userID='${req.query.userID}' group by time(1d)`
				)
				
				if(weekArr == [])
				{
					res.status(404);
					res.send(`There is no entry for user with id = ${req.query.userID}.`);
					return null;
				}
				var resultArr = [];
				weekArr.forEach(element => {
					var newElement = {sleepHours: null, timestamp: null};
					newElement.sleepHours = element.sum
					newElement.timestamp = element.time
					resultArr.push(newElement)
				});
				console.log(weekArr)
				res.send(resultArr)
			}
			catch(err){
				console.log(err);
				res.status(500).send(err)
			}
		},
		async getCaloriesForDay(req, res) {
			try{
				const sleepHours = await this.influx.query(
					`select sum(value) from "${req.body.param}" where time > now() - 24h and userID ='${req.query.userID}'`
				)
				console.log(sleepHours)
				if(sleepHours == [])
				{
					res.status(404);
					res.send(`There is no entry for user with id = ${req.query.userID}.`);
					return null;
				}
				var resultArr = []
				sleepHours.forEach(element => {
					var newElement = {value: null, time: null};
					newElement.value = element.sum
					newElement.timestamp = element.time
					resultArr.push(newElement)
				})
				res.send(resultArr)
			}
			catch(err){
				console.log(err);
				res.status(500).send(err)
			}
		},
		async getCaloriesForWeek(req, res) {
			try {
				const weekArr = await this.influx.query(
					`select sum(value) from "calories" where time > now() - 7d and userID='${req.query.userID}' group by time(1d)`
				)
				
				if(weekArr == [])
				{
					res.status(404);
					res.send(`There is no entry for user with id = ${req.query.userID}.`);
					return null;
				}
				var resultArr = [];
				weekArr.forEach(element => {
					var newElement = {value: null, time: null};
					newElement.value = element.sum
					newElement.timestamp = element.time
					resultArr.push(newElement)
				});
				console.log(weekArr)
				res.send(resultArr)
			}
			catch(err){
				console.log(err);
				res.status(500).send(err)
			}
		},
		async getStepsForWeek(req, res) {
			try {
				const weekArr = await this.influx.query(
					`select sum(value) from "steps" where time > now() - 7d and userID='${req.query.userID}' group by time(1d)`
				)
				
				if(weekArr == [])
				{
					res.status(404);
					res.send(`There is no entry for user with id = ${req.query.userID}.`);
					return null;
				}
				var resultArr = [];
				weekArr.forEach(element => {
					var newElement = {value: null, time: null};
					newElement.value = element.sum
					newElement.time = element.time
					resultArr.push(newElement)
				});
				console.log(weekArr)
				res.send(resultArr)
			}
			catch(err){
				console.log(err);
				res.status(500).send(err)
			}
		},
		async postVitals(req, res) {
			if (req.body.userID == undefined 
				|| req.body.sys == undefined 
				|| req.body.dias == undefined 
				|| req.body.pulse == undefined) {
					res.status(400).send("Post parameters not defined.");
					return null;
			}
			try {
				console.log(req.body);
				this.influx.writePoints([{
					measurement: 'sys-pressure',
					tags: {
						userID: req.body.userID
					},
					fields: {
						value: req.body.sys
					},
					time: req.body.timestamp
				},
				{
					measurement: 'dias-pressure',
					tags: {
						userID: req.body.userID
					},
					fields: {
						value: req.body.dias
					},
					time: req.body.timestamp
				},
				{
					measurement: 'pulse',
					tags: {
						userID: req.body.userID
					},
					fields: {
						value: req.body.pulse
					},
					time: req.body.timestamp
				}]);
				res.send(true);
			}
			catch(err) {
				console.log(err);
				res.status(500).send(err);
			}
		},
		async postSleepHours(req,res) {
			if (req.body.userID == undefined 
				|| req.body.sleepHours == undefined) {
					res.status(400).send("Post parameters not defined.");
					return null;
			}
			try {
				this.influx.writePoints([{
					measurement: 'sleep-hours',
					tags: {
						userID: req.body.userID
					},
					fields: {
						value: req.body.sleepHours
					},
					timestamp: new Date(req.body.timestamp)
				}]);
				res.send(true);
			}
			catch(err) {
				console.log(err);
				res.status(500).send(err);
			}
		},
		async postCalories(req,res) {
			if (req.body.userID == undefined 
				|| req.body.calories == undefined) {
					res.status(400).send("Post parameters not defined.");
					return null;
			}
			try {
				this.influx.writePoints([{
					measurement: 'calories',
					tags: {
						userID: req.body.userID
					},
					fields: {
						value: req.body.calories
					},
					timestamp: new Date().getTime()
				}]);
				res.send(true);
			}
			catch(err) {
				console.log(err);
				res.status(500).send(err);
			}
		},
		async postSteps(req,res) {
			if (req.body.userID == undefined 
				|| req.body.value == undefined) {
					res.status(400).send("Post parameters not defined.");
					return null;
			}
			try {
				console.log("POST STEPS!!! AAAAAAA")
				console.log(req.body)
				this.influx.writePoints([{
					measurement: 'steps',
					tags: {
						userID: req.body.userID
					},
					fields: {
						value: req.body.value
					},
					timestamp: new Date(req.body.time).getTime() * 1000
				}]);
				res.send(true);
			}
			catch(err) {
				console.log(err);
				res.status(500).send(err);
			}
		},
		async putVitals(req, res) {
			if (req.body.userID == undefined 
				|| req.body.sys == undefined 
				|| req.body.dias == undefined 
				|| req.body.pulse == undefined) {
					res.status(400).send("Put parameters not defined.");
					return null;
			}
			try {
				const measurements = ["sys-pressure", "dias-pressure", "pulse"];
				measurements.forEach(async (m) => {
					this.influx.query(
						`delete from "${m}" where userID='${req.query.userID}'`
					);
				});
				this.influx.writePoints([{
					measurement: 'sys-pressure',
					tags: {
						userID: req.body.userID
					},
					fields: {
						value: req.body.sys
					},
					time: req.body.timestamp
				},
				{
					measurement: 'dias-pressure',
					tags: {
						userID: req.body.userID
					},
					fields: {
						value: req.body.dias
					},
					time: req.body.timestamp
				},
				{
					measurement: 'pulse',
					tags: {
						userID: req.body.userID
					},
					fields: {
						value: req.body.pulse
					},
					time: req.body.timestamp
				}]);

				res.send(true);
			}
			catch(err) {
				console.log(err);
				res.status(500).send(err);
			}
		},
		async deleteVitals(req, res) {
			try {
				const measurements = ["sys-pressure", "dias-pressure", "pulse"];
				measurements.forEach((m) => {
					this.influx.query(
						`delete from "${m}" where userID='${req.query.userID}'`
					);
				});
				res.send(true);
			}
			catch(err) {
				console.log(err);
				res.status(500).send(err);
			}
		},
		async deleteSleepHours(req, res) {
			try{
				this.influx.query(
					`delete from "sleep-hours" where userID='${req.query.userID}'`
				);
				res.send(true);
			}
			catch(err){
				console.log(err);
				res.status(500).send(err);
			}
		}
	},
	created() {
        const app = express();
        app.use(bodyParser.urlencoded({ extended: false }));
        app.use(bodyParser.json());
		app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument));
        app.listen(this.settings.port);
        this.initRoutes(app);
		this.influx = new Influx.InfluxDB({
			host: process.env.INFLUXDB_HOST || 'influx',
			database: process.env.INFLUXDB_DATABASE || 'vitals',
			username: process.env.ADMIN_USER || 'admin',
			password: process.env.ADMIN_PASSWORD || 'admin',
			schema: [
				{
					measurement: 'sys-pressure',
					fields: {
						value: Influx.FieldType.FLOAT,
					},
					tags: ['userID'],
				},
				{
					measurement: 'dias-pressure',
					fields: {
						value: Influx.FieldType.FLOAT,
					},
					tags: ['userID'],
				},
				{
					measurement: 'pulse',
					fields: {
						value: Influx.FieldType.INTEGER
					},
					tags: ['userID']
				}, 
				{
					measurement: 'sleep-hours',
					fields: {
						value: Influx.FieldType.FLOAT
					},
					tags: ['userID'],
				}, 
				{
					measurement: 'calories',
					fields: {
						value: Influx.FieldType.FLOAT
					},
					tags: ['userID'],
				},
				{
					measurement: 'steps',
					fields: {
						value: Influx.FieldType.INTEGER
					},
					tags: ['userID']
				}
			]
		});
		this.influx.getDatabaseNames().then((names) => {
			if (!names.includes('vitals')) {
				return this.influx.createDatabase('vitals');
			}
			return null;
		});
        this.app = app;
		console.log("Data Service listening on port 3333.");
    }
}