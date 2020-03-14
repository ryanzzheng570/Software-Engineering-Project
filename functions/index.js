// https://firebase.google.com/docs/functions/write-firebase-functions

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const database = admin.database();

exports.exampleCloudFunction = functions.https.onCall((data, context) => {

    //    When login works
    //    if (!context.auth) {
    //      throw new functions.https.HttpsError('failed-precondition', 'The function must be called ' +
    //          'while authenticated.');
    //    }

    database.ref('/testing/exampleCloudFunction').set(data.inputData);

    var returnVal = database.ref("/testing/").once("value").then((snapshot) => {
        if(snapshot.val()) {
            return {text: snapshot.val()}
        }
        return {text: "Something went wrong!"};
    });
    return returnVal;

});
