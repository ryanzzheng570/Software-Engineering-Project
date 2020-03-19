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

// !--- PLACE ALL SERVICES BELOW HERE ---!

function addShop(shopName, mode = '') {
    if (!ValidateString(shopName)) {
        return "Sorry, invalid input was entered!";
    }
    return database.ref(mode + '/store/').push({
        name: shopName
    }).key;
}

function deleteShop(shopID, mode = '') {
    if (!ValidateString(shopID)) {
        return "Sorry, invalid input was entered!";
    }
    return database.ref(mode + '/store/' + shopID).remove();
}

function changeShopName(shopID, shopName, mode = '') {
    if (!ValidateString(shopID) || !ValidateString(shopName)) {
        return "Sorry, invalid input was entered!";
    }
    return database.ref(mode + '/store/' + shopID).update({
        name: shopName
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
    return database.ref(mode + '/store/' + shopID + "/item/" + itemID).remove();
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

// !--- PLACE ALL PRODUCTION ENDPOINTS WITH THE TEST ENDPOINTS BELOW HERE ---!
exports.addShop = functions.https.onCall((data, context) => {
    return addShop(data.shopName);
});
exports.addShopTest = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(addShop(request.body.shopName, TEST_MODE));
    });
});

exports.deleteShop = functions.https.onCall((data, context) => {
    return deleteShop(data.shopId);
});
exports.deleteShopTest = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(deleteShop(request.body.shopID, TEST_MODE));
    });
});

exports.changeShopName = functions.https.onCall((data, context) => {
    return changeShopName(data.shopId, data.shopName)
});
exports.changeShopNameTest = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(changeShopName(request.body.shopID, request.body.shopName, TEST_MODE));
    });
});

exports.addTag = functions.https.onCall((data, context) => {
    return addTagToShop(data.shopId, data.tagName);
});
exports.addTagTest = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(addTag(request.body.shopID, request.body.tagName, TEST_MODE));
    });
});

exports.removeTag = functions.https.onCall((data, context) => {
    return removeTagFromShop(data.shopId, data.tagId);
});
exports.removeTagTest = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(removeTagFromShop(request.body.shopID, request.body.tagID, TEST_MODE));
    });
});

exports.addItem = functions.https.onCall((data, context) => {
    const itemData = {
        url: data.url,
        altText: data.altText,
        name: data.itemName,
        cost: data.cost,
        inventory: data.inventory
    };
    return addItemToShop(data.shopId, itemData);
});
exports.addItemTest = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        const itemData = {
            url: request.body.url,
            altText: request.body.altText,
            name: request.body.itemName,
            cost: request.body.cost,
            inventory: request.body.inventory
        };
        response.status(200).send(addItemToShop(request.body.shopID, itemData, TEST_MODE));
    });
});

exports.removeItem = functions.https.onCall((data, context) => {
    return removeItemFromShop(data.shopId, data.itemId);
});
exports.removeItemTest = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(removeItemFromShop(request.body.shopID, request.body.itemID, TEST_MODE));
    });
});

exports.purchaseItemsFromShop = functions.https.onCall((data, context) => {
    return purchaseItemFromShop(data.shopID, data.itemIDs, data.quantities);
});
exports.purchaseItemsTest = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(purchaseItemFromShop(request.body.shopID, request.body.itemIDs, request.body.quantities, TEST_MODE));
    });
});

exports.createMerchant = functions.https.onCall((data, context) => {
    return createMerchant(data.userName, data.password, data.email, data.contactPhoneNumber);
});
exports.createMerchantTest = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(createMerchant(request.body.userName, request.body.password, request.body.email, request.body.contactPhoneNumber, TEST_MODE));
    });
});

exports.createCustomer = functions.https.onCall((data, context) => {
    return createCustomer(data.userName, data.password, data.email, data.address, data.phoneNumber, data.note);
});
exports.createCustomerTest = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(createCustomer(request.body.userName, request.body.password, request.body.email, request.body.address, request.body.phoneNumber, request.body.note, TEST_MODE));
    });
});

// !--- PLACE ALL HELPER FUNCTIONS BELOW HERE ---!

function encrypt(text) {
    let cipher = crypto.createCipheriv('aes-256-cbc', Buffer.from(key), iv);
    let encrypted = cipher.update(text);
    encrypted = Buffer.concat([encrypted, cipher.final()]);
    return encrypted.toString('hex');
}

function decrypt(text) {
    let iv = Buffer.from(text.iv, 'hex');
    let encryptedText = Buffer.from(text.encryptedData, 'hex');
    let decipher = crypto.createDecipheriv('aes-256-cbc', Buffer.from(key), iv);
    let decrypted = decipher.update(encryptedText);
    decrypted = Buffer.concat([decrypted, decipher.final()]);
    return decrypted.toString();
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