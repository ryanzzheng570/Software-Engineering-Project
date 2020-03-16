// https://firebase.google.com/docs/functions/write-firebase-functions

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const database = admin.database();

// !--- PLACE ALL FUNCTIONS BELOW HERE ---!

exports.getItemsFromStoreByIds = functions.https.onCall((data, context) => {

    const SHOP_ID = data.shopID;
    const ITEM_IDS = data.itemIDs;

    var returnVal = database.ref("/store/" + SHOP_ID).once("value").then((snapshot) => {
        if (snapshot.val()) {
            const res = snapshot.val();
            var retItems = [];
            for (var id in ITEM_IDS) {
                var temp = res[ITEM_IDS[id]];
                temp.id = ITEM_IDS[id];
                retItems.push(temp)
            }
            return { items: retItems, name: res.name }
        }
        return { data: "Something went wrong!" };
    });
    return returnVal;

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

    database.ref('/store/' + SHOP_ID).push(item);

});

exports.purchaseItems = functions.https.onCall((data, context) => {
    const SHOP_ID = data.shopID;
    const ITEM_IDS = data.itemIDs;
    const QUANTITIES = data.quantities;

    var returnVal = database.ref("/store/" + SHOP_ID).once("value").then((snapshot) => {
        if (snapshot.val()) {
            const res = snapshot.val();
            for (var id in ITEM_IDS) {
                var tempItem = res[ITEM_IDS[id]];
                var itemID = ITEM_IDS[id];
                var newInventoryCount = tempItem.inventory - QUANTITIES[id];
                if (newInventoryCount < 0) {
                    return { msg: "Error - Not enough inventory!" };
                } else {
                    database.ref("/store/" + SHOP_ID).child(itemID).update({
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

exports.getItemsFromStore = functions.https.onCall((data, context) => {

    const SHOP_ID = data.shopID;

    var returnVal = database.ref("/store/" + SHOP_ID).once("value").then((snapshot) => {
        if (snapshot.val()) {
            const res = snapshot.val();
            var retItems = [];
            var retTags = [];
            for (var item in res) {
                if (res[item].name) {
                    var temp = res[item];
                    temp.id = item;
                    retItems.push(temp)
                }
            }
            for (var tag in res.tag) {
                retTags.push(res.tag[tag])
            }
            return { items: retItems, name: res.name, tags: retTags }
        }
        return { data: "Something went wrong!" };
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
