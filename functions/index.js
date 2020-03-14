// https://firebase.google.com/docs/functions/write-firebase-functions
const functions = require('firebase-functions');

exports.exampleCloudFunction = functions.https.onCall((data, context) => {

    console.log("\n---------REACHED CLOUD FUNCTION-------------\n")

// When login works
//    if (!context.auth) {
//      throw new functions.https.HttpsError('failed-precondition', 'The function must be called ' +
//          'while authenticated.');
//    }

    return {text: "Successful Cloud Function call."};
});
