
var request = require('request');
var options = {
  'method': 'POST',
  'url': 'https://json.freeastrologyapi.com/planets',
  'headers': {
    'Content-Type': 'application/json',
    'x-api-key': 'SL08OU7QDr34yTmL5JTc37aMM9aktIHK3QXw3b2L'
  },
  body: JSON.stringify({
    "year": 1992,
    "month": 10,
    "date": 7,
    "hours": 0,
    "minutes": 20,
    "seconds": 0,
    "latitude": 39.46975,
    "longitude": -0.37739,
    "timezone": 1,
    "settings": {
      "observation_point": "topocentric",
      "ayanamsha": "lahiri"
    }
  })

};
request(options, function (error, response) {
  if (error) throw new Error(error);
  console.log(response.body);
});
