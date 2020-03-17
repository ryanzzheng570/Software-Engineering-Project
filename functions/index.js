// https://firebase.google.com/docs/functions/write-firebase-functions

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const database = admin.database();

// !--- PLACE ALL FUNCTIONS BELOW HERE ---!

exports.addShop2 = functions.https.onCall((data, context) => {
    var test = {
        name: "NAME"
    };

    const key = database.ref('/store/').push(test).key;

    return key;
});

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

    const key = database.ref('/store/' + SHOP_ID+ '/item/').push(item).key;
    return key;

});

exports.purchaseItems = functions.https.onCall((data, context) => {
    const SHOP_ID = data.shopID;
    const ITEM_IDS = data.itemIDs;
    const QUANTITIES = data.quantities;

    var returnVal = database.ref("/store/" + SHOP_ID).once("value").then((snapshot) => {
        if (snapshot.val()) {
            const res = snapshot.val();
            for (var id in ITEM_IDS) {
                var tempItem = res.item[ITEM_IDS[id]];
                var itemID = ITEM_IDS[id];
                var newInventoryCount = tempItem.inventory - QUANTITIES[id];
                if (newInventoryCount < 0) {
                    return { msg: "Error - Not enough inventory!" };
                } else {
                    database.ref("/store/" + SHOP_ID).child("item/" + itemID).update({
                        inventory: newInventoryCount
                    })
                }
            }
            return { msg: "Success" };
        }
        return { msg: "Something went wrong!" };
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
        if (snapshot.val()) {
            return { text: snapshot.val() }
        }
        return { text: "Something went wrong!" };
    });
    return returnVal;

});
