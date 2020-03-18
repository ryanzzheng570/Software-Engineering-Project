// https://firebase.google.com/docs/functions/write-firebase-functions

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const database = admin.database();
const cors = require('cors')({
    origin: true
});
const TEST_MODE = '/test';

// !--- PLACE ALL SERVICES BELOW HERE ---!

function addShop(shopName, mode = '') {
    return database.ref(mode + '/store/').push({
        name: shopName
    }).key;
}

function deleteShop(shopID, mode = '') {
    return database.ref(mode + '/store/' + shopID).remove();
}

function changeShopName(shopID, shopName, mode = '') {
    return database.ref(mode + '/store/' + shopID).update({
        name: shopName
    }).key;
}

function addTagToShop(shopID, tag, mode = '') {
    return database.ref(mode + '/store/' + shopID + "/tag").push(tag).key;
}

function removeTagFromShop(shopID, tagID, mode = '') {
    return database.ref(mode + '/store/' + shopID + "/tag/" + tagID).remove();
}

function addItemToShop(shopID, itemInfo, mode = '') {
    return database.ref(mode + '/store/' + shopID + "/item").push(itemInfo).key;
}

function removeItemFromShop(shopID, itemID, mode = '') {
    return database.ref(mode + '/store/' + shopID + "/item/" + itemID).remove();
}

function purchaseItemFromShop(shopID, itemID, quantities, mode = '') {
    const SHOP_ID = shopID;
    const ITEM_IDS = itemID;
    const QUANTITIES = quantities;

    var returnVal = database.ref(mode + "/store/" + SHOP_ID).once("value").then((snapshot) => {
        if (snapshot.val()) {
            const res = snapshot.val();
            for (var id in ITEM_IDS) {
                var tempItem = res.item[ITEM_IDS[id]];
                var itemID = ITEM_IDS[id];
                var newInventoryCount = tempItem.inventory - QUANTITIES[id];
                if (newInventoryCount < 0) {
                    return "Error - Not enough inventory!";

                } else {
                    database.ref(mode + "/store/" + SHOP_ID).child("item/" + itemID).update({
                        inventory: newInventoryCount
                    })
                }
            }
            return "Success";

        }
        return "Something went wrong!"

    });
    return returnVal;
}

// !--- PLACE ALL PRODUCTION ENDPOINTS WITH THE TEST ENDPOINTS RIGHT UNDER ---!
exports.addShop = functions.https.onCall((data, context) => {
    return addShop(data.shopName);
});

exports.deleteShop = functions.https.onCall((data, context) => {
    return deleteShop(data.shopId);
});

exports.changeShopName = functions.https.onCall((data, context) => {
    return changeShopName(data.shopId, data.shopName)
});

exports.addTag = functions.https.onCall((data, context) => {
    return addTagToShop(data.shopId, data.tagName);
});

exports.removeTag = functions.https.onCall((data, context) => {
    return removeTagFromShop(data.shopId, data.tagId);
});

exports.addItem = functions.https.onCall((data, context) => {
    var itemData = {
        url: data.url,
        altText: data.altText,
        name: data.itemName,
        cost: data.cost,
        inventory: data.inventory
    };
    return addItemToShop(data.shopID, itemData);
});

exports.removeItem = functions.https.onCall((data, context) => {
    return removeItemFromShop(data.shopId, data.itemId);
});

exports.purchaseItems = functions.https.onCall((data, context) => {
    return purchaseItemFromShop(data.shopID, data.itemIDs, data.quantities);
});

// !--- PLACE ALL TESTING ENDPOINTS BELOW HERE ---!

exports.test = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send({
            test: request.body.test
        });
    });
});