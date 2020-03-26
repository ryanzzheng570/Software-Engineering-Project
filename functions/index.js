// Cloud Function Link: https://firebase.google.com/docs/functions/write-firebase-functions
// Encrypt and Decrypt Link: https://codeforgeek.com/encrypt-and-decrypt-data-in-node-js/ 

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const database = admin.database();
const cors = require('cors')({
    origin: true
});
const TEST_MODE = '/test';
const crypto = require('crypto');
const algorithm = 'aes-256-cbc';
const key = crypto.randomBytes(32);
const iv = crypto.randomBytes(16);
const SUCCESS_CONSTANT = "SUCCESS";
const ERROR_CONSTANT = "ERROR - Please contact the developer";

// !--- PLACE ALL SERVICES BELOW HERE ---!

function addShop(shopName, merchantId, mode = '') {
    if (!ValidateString(shopName) || !ValidateString(merchantId))  {
        return "Sorry, invalid input was entered!";
    }

    var storeKey = database.ref(mode + '/store/').push({
                           shopName: shopName
                       }).key;

    database.ref(mode + "/users/merchants/" + merchantId + "/shops").push(storeKey);

    return storeKey;
}

function deleteShop(shopID, merchantId, mode = '') {
    if (!ValidateString(shopID) || !ValidateString(merchantId)) {
        return "Sorry, invalid input was entered!";
    }

    var path = mode + '/users/merchants/' + merchantId + "/shops/";
    var retVal = database.ref(path).once("value").then((snapshot) => {
        var ssv = snapshot.val();

        var found = false;
        for (var shopKey in ssv) {
            if (ssv[shopKey] === shopID) {
                database.ref(path + shopKey).remove();
                found = true;
                break;
            }
        }

        return found;
    });

    retVal = database.ref(mode + '/store/' + shopID).remove();

    return retVal;
}

function changeShopName(shopID, shopName, mode = '') {
    if (!ValidateString(shopID) || !ValidateString(shopName)) {
        return "Sorry, invalid input was entered!";
    }
    return database.ref(mode + '/store/' + shopID).update({
        shopName: shopName
    }).key;
}

function addTagToShop(shopID, tag, mode = '') {
    if (!ValidateString(shopID) || !ValidateString(tag)) {
        return "Sorry, invalid input was entered!";
    }
    return database.ref(mode + '/store/' + shopID + "/tag").push(tag).key;
}

function removeTagFromShop(shopID, tagID, mode = '') {
    if (!ValidateString(shopID) || !ValidateString(tagID)) {
        return "Sorry, invalid input was entered!";
    }
    return database.ref(mode + '/store/' + shopID + "/tag/" + tagID).remove();
}

function addItemToShop(shopID, itemInfo, mode = '') {
    if (!ValidateString(shopID) || !ValidateString(itemInfo.name) || !ValidateNumber(itemInfo.cost) && !ValidateNumber(itemInfo.inventory)) {
        return "Sorry, invalid input was entered!";
    }
    return database.ref(mode + '/store/' + shopID + "/item").push(itemInfo).key;
}

function removeItemFromShop(shopID, itemID, mode = '') {
    if (!ValidateString(shopID) || !ValidateString(itemID)) {
        return "Sorry, invalid input was entered!";
    }
    return database.ref(mode + '/store/' + shopID + "/item/" + itemID).remove();
}

function editItemInShop(shopID, itemID, merchantID, itemInfo, mode = '') {
    if (!ValidateString(shopID) || !ValidateString(itemID) || !ValidateString(merchantID) ||
        !ValidateString(itemInfo.name) || !ValidateNumber(itemInfo.cost) && !ValidateNumber(itemInfo.inventory)) {
        return "Sorry, invalid input was entered!";
    }

    for (var key in itemInfo) {
        if (itemInfo[key] === undefined) {
            itemInfo[key] = "";
        }
    }

    var retVal = database.ref(mode + "/users/merchants/" + merchantID + "/shops/").once("value").then((snapshot) => {
        var validChange = false;
        var ssv = snapshot.val();
        if (ssv) {
            for (var shopKey in ssv) {
                if (ssv[shopKey] === shopID) {
                    database.ref(mode + '/store/' + shopID + "/item/" + itemID).update(itemInfo);
                    validChange = true;
                    break;
                }
            }
        }

        return validChange;
    });

    return retVal;

}

function purchaseItemFromShop(shopID, itemID, quantities, mode = '') {
    const SHOP_ID = shopID;
    const ITEM_IDS = itemID;
    const QUANTITIES = quantities;
    if (!ValidateString(SHOP_ID) || !ValidateArray(ITEM_IDS, ValidateString) || !ValidateArray(QUANTITIES, ValidateNumber)) {
        return "Sorry, invalid input was entered!";
    }
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

function createMerchant(username, password, email, phoneNumber, mode = '') {
    const encryptedPassword = encrypt(password);
    const encryptedEmail = encrypt(email);
    const encryptedPhoneNumber = encrypt(phoneNumber);
    return database.ref(mode + '/users/merchants').push({
        userName: username,
        password: encryptedPassword,
        email: encryptedEmail,
        phoneNum: encryptedPhoneNumber
    }).key;
}

function merchantLogin(username, password, mode = '') {
    password = encrypt(password);
    var retVal = database.ref(mode + "/users/merchants").once("value").then((snapshot) => {
        var ssv = snapshot.val();
        for (var merchantId in ssv) {
            var currData = ssv[merchantId];

            var checkUsername = currData["userName"];
            var checkPass = currData["password"];

            if ((checkUsername === username) && (checkPass === password)) {
                var shops = [];
                if ("shops" in currData) {
                    shops = currData["shops"];
                }
                return {
                    id: merchantId,
                    shops: shops,
                    name: checkUsername
                };
            }
        }

        return null;
    });
    return retVal;
}

function createCustomer(username, password, email, address, phoneNumber, note, mode = '') {
    const encryptedPassword = encrypt(password);
    const encryptedEmail = encrypt(email);
    const encryptedAddress = encrypt(address);
    const encryptedPhoneNumber = encrypt(phoneNumber);
    return database.ref(mode + '/users/customers').push({
        userName: username,
        password: encryptedPassword,
        email: encryptedEmail,
        address: encryptedAddress,
        phoneNum: encryptedPhoneNumber,
        note: note
    }).key;
}

function addItemToShoppingCart(customerID, shopID, itemID, mode = "") {
    var returnVal = database.ref(mode + '/users/customers/' + customerID + '/shoppingCart/').once("value").then((snapshot) => {
        if (snapshot.val()) {
            const SC = snapshot.val();
            for (var item in SC) {
                if (SC[item].itemID === itemID) {
                    return "That item is already in your shopping cart!"
                }
            }
            database.ref(mode + '/users/customers/' + customerID + '/shoppingCart/').push({
                itemID: itemID,
                shopID: shopID
            }).key;
            return SUCCESS_CONSTANT;

        } else {
            database.ref(mode + '/users/customers/' + customerID + '/shoppingCart/').push({
                itemID: itemID,
                shopID: shopID
            }).key;
            return SUCCESS_CONSTANT;
        }

    });
    return returnVal;
}

// !--- PLACE ALL PRODUCTION ENDPOINTS WITH THE TEST ENDPOINTS BELOW HERE ---!
exports.addToCart = functions.https.onCall((data, context) => {
    return addItemToShoppingCart(data.customerID, data.shopID, data.itemID);
});
exports.testAddToCart = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(addItemToShoppingCart("testCustomerID","testShopID", "testItemID", TEST_MODE));
    });
});
exports.addShop = functions.https.onCall((data, context) => {
    return addShop(data.shopName, data.userId);
});
exports.testAddShop = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(addShop(request.body.shopName, request.body.userId, TEST_MODE));
    });
});

exports.deleteShop = functions.https.onCall((data, context) => {
    return deleteShop(data.shopId, data.userId);
});
exports.testDeleteShop = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(deleteShop(request.body.shopID, request.body.userId, TEST_MODE));
    });
});

exports.changeShopName = functions.https.onCall((data, context) => {
    return changeShopName(data.shopId, data.shopName)
});
exports.testChangeShopName = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(changeShopName(request.body.shopID, request.body.shopName, TEST_MODE));
    });
});

exports.addTag = functions.https.onCall((data, context) => {
    return addTagToShop(data.shopId, data.tagName);
});
exports.testAddTag = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(addTagToShop(request.body.shopID, request.body.tagName, TEST_MODE));
    });
});

exports.removeTag = functions.https.onCall((data, context) => {
    return removeTagFromShop(data.shopId, data.tagId);
});
exports.testRemoveTag = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(removeTagFromShop(request.body.shopID, request.body.tagID, TEST_MODE));
    });
});

exports.addItem = functions.https.onCall((data, context) => {
    var inventory = parseInt(data.inventory);
    var cost = parseFloat(data.cost);
    const itemData = {
        url: data.url,
        altText: data.altText,
        name: data.itemName,
        cost: data.cost,
        inventory: inventory
    };
    return addItemToShop(data.shopId, itemData);
});
exports.testAddItem = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        const numInventory = Number(request.body.inventory);
        const itemData = {
            url: request.body.url,
            altText: request.body.altText,
            name: request.body.itemName,
            cost: request.body.cost,
            inventory: numInventory
        };
        response.status(200).send(addItemToShop(request.body.shopID, itemData, TEST_MODE));
    });
});

exports.removeItem = functions.https.onCall((data, context) => {
    return removeItemFromShop(data.shopId, data.itemId);
});
exports.testRemoveItem = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(removeItemFromShop(request.body.shopID, request.body.itemID, TEST_MODE));
    });
});

exports.editItem = functions.https.onCall((data, context) => {
    var itemId = data.itemId;
    var shopId = data.shopId;
    var merchantId = data.merchantId;

    var inventory = parseInt(data.inventory);
    var cost = parseFloat(data.cost);
    const itemData = {
        url: data.url,
        altText: data.altText,
        name: data.itemName,
        cost: cost,
        inventory: inventory
    };

    return editItemInShop(shopId, itemId, merchantId, itemData);
});
exports.editItemTest = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        const numInventory = Number(request.body.inventory);
        const itemData = {
            url: request.body.url,
            altText: request.body.altText,
            name: request.body.itemName,
            cost: request.body.cost,
            inventory: numInventory
        };
        response.status(200).send(editItemInShop(request.body.shopID, request.body.itemId, request.body.merchantId, itemData, TEST_MODE));
    });
});

exports.purchaseItemsFromShop = functions.https.onCall((data, context) => {
    return purchaseItemFromShop(data.shopID, data.itemIDs, data.quantities);
});
exports.testPurchaseItems = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        const items = request.body.itemIDs.split(', ');
        const quantities = request.body.quantities.split(', ');
        var qties = [];
        for (var a in quantities) {
            qties.push(Number(quantities[a]));
        }
        response.status(200).send(purchaseItemFromShop(request.body.shopID, items, qties, TEST_MODE));
    });
});

exports.createMerchant = functions.https.onCall((data, context) => {
    return createMerchant(data.userName, data.password, data.email, data.contactPhoneNumber);
});
exports.testCreateMerchant = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(createMerchant(request.body.userName, request.body.password, request.body.email, request.body.contactPhoneNumber, TEST_MODE));
    });
});

exports.merchantLogin = functions.https.onCall((data, context) => {
    return merchantLogin(data.userName, data.password);
});
exports.testMerchantLogin = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(merchantLogin(request.body.userName, request.body.password, TEST_MODE));
    });
});

exports.createCustomer = functions.https.onCall((data, context) => {
    return createCustomer(data.userName, data.password, data.email, data.address, data.phoneNumber, data.note);
});
exports.testCreateCustomer = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(createCustomer(request.body.userName, request.body.password, request.body.email, request.body.address, request.body.phoneNumber, request.body.note, TEST_MODE));
    });
});

// !--- PLACE ALL HELPER FUNCTIONS BELOW HERE ---!

const ENCRYPTION_PASSWORD = "password";
const ENCRYPTION_METHOD = 'aes-128-cbc';
const UNENCRYPTED_FORM = "utf8";
const ENCRYPTED_FORM = "hex";

function encrypt(text) {
    var key = crypto.createCipher(ENCRYPTION_METHOD, ENCRYPTION_PASSWORD);
    var encryptedVal = key.update(text, UNENCRYPTED_FORM, ENCRYPTED_FORM)
    encryptedVal += key.final(ENCRYPTED_FORM);
    return encryptedVal;
}

function decrypt(text) {
    var key = crypto.createDecipher(ENCRYPTION_METHOD, ENCRYPTION_PASSWORD);
    var unencryptedVal = key.update(text, ENCRYPTED_FORM, UNENCRYPTED_FORM)
    unencryptedVal += key.final(UNENCRYPTED_FORM);
    return unencryptedVal;
}

function ValidateString(string) {
    if (string && string !== "") {
        return true;
    }
    return false;
}

function ValidateNumber(number) {
    if (number && !isNaN(number)) {
        return true;
    }
    return false;
}

function ValidateArray(array, type) {
    for (var item in array) {
        if (!type(array[item])) {
            return false;
        }
    }
    return true;
}