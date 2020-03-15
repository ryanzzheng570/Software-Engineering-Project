// https://firebase.google.com/docs/functions/write-firebase-functions

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const database = admin.database();

// !--- PLACE ALL FUNCTIONS BELOW HERE ---!

exports.addItemToStore = functions.https.onCall((data, context) => {

    const SHOP_ID = data.shopID;
    const URL = data.url;
    const ALT_TEXT = data.altText;
    const ITEM_NAME = data.itemName;
    const ITEM_COST = data.itemCost;
    const ITEM_INV = data.inventory;

    var item = {
        name: ITEM_NAME,
        cost: ITEM_COST,
        inventory: ITEM_INV,
        url: URL,
        altText: ALT_TEXT,
    };

    database.ref('/store/' + SHOP_ID).push(item);

});

exports.getItemsFromStore = functions.https.onCall((data, context) => {

    const SHOP_ID = data.shopID;

    var returnVal = database.ref("/store/" + SHOP_ID).once("value").then((snapshot) => {
            if(snapshot.val()) {
                const res = snapshot.val();
                var retItems = [];
                var retTags = [];
                for(var item in res) {
                    if(res[item].name) {
                        var temp = res[item];
                        temp.id = item;
                        retItems.push(temp)
                    }
                }
                for(var tag in res.tag) {
                    retTags.push(res.tag[tag])
                }
                return {items: retItems, name: res.name, tags: retTags}
            }
            return {data: "Something went wrong!"};
        });
    return returnVal;

});


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
