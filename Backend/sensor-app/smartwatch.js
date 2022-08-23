"use strict";

const fs = require("fs");
const parse = require("csv-parse");
const axios = require('axios');

const userID = 9;

function getNumberOfHoursFromString(hoursString){
  const [hours, minutes] = hoursString.split(':');
  return parseFloat((+hours) + minutes / 60);
}

let records = [];
let currentRecord = 0;

const url = "http://localhost:5000/api/Gateway/PostVitals";
const parser = parse.parse({ columns: true }, function (err, recs) {
  records = recs;
  //console.log(recs)
});

fs.createReadStream("./Blood_Pressure.csv").pipe(parser);

const interval = setInterval(() => {
  const timestamp = new Date().getTime();
  const params = {
    sys: records[currentRecord].Sys,
    dias: records[currentRecord].Dias,
    pulse: records[currentRecord].Pulse,
    userID,
    timestamp,
  };
  currentRecord = (currentRecord + 1) % records.length;

  axios.post(url, params)
      .then((res) => {
          console.log(params)
          console.log(`Status: ${res.status}`);
          console.log('Body: ', res.data);
      }).catch((err) => {
          console.error(err);
      });
}, 7000);

let sleepRecords = [];
let currentSleepRecord = 0;

const sleepDataUrl = "http://localhost:5000/api/Gateway/PostSleepHours";
const sleepDataParser = parse.parse({columns: true}, function(err, recs) {
  sleepRecords = recs;
});

fs.createReadStream("./sleepdata.csv").pipe(sleepDataParser);

const sleepInterval = setInterval(() => {
  const sleepParams = {
    sleepHours: getNumberOfHoursFromString(sleepRecords[currentSleepRecord].SleepHours).toString(),
    timestamp: sleepRecords[currentSleepRecord].Start,
    userID,
  };
  console.log(sleepParams)

  currentSleepRecord = (currentSleepRecord + 1) % sleepRecords.length;

  axios.post(sleepDataUrl, sleepParams)
    .then((res) => {
        console.log(sleepParams)
        console.log(`Status: ${res.status}`);
        console.log('Body: ', res.data);
    }).catch((err) => {
      console.error(err);
  });
}, 5000);
