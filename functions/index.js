// https://firebase.google.com/docs/functions/write-firebase-functions
const functions = require('firebase-functions');

exports.exampleCloudFunction = functions.https.onRequest((request, response) => {
    console.log("\n---------REACHED CLOUD FUNCTION-------------\n")
    response.send({returnData: "Hello from Firebase!"});
});
