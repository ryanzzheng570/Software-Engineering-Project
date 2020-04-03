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
    if (!ValidateString(shopName) || !ValidateString(merchantId)) {
        return "Sorry, invalid input was entered!";
    }

    var shopNameToUse = shopName.trim();

    const returnVal = database.ref(mode + "/store").once("value").then((snapshot) => {
        if (snapshot.val()) {
            const storeData = snapshot.val();
            for (var storeID in storeData) {
                if (storeData[storeID].shopName === shopNameToUse) {
                    return true;
                }
            }
        }
        return false;
    }).then((res) => {
        if (res) {
            return {
                exists: true,
                res: "Sorry, that store name is already taken."
            };
        } else {
            const storeKey = database.ref(mode + '/store/').push({
                shopName: shopNameToUse
            }).key;
            database.ref(mode + "/users/merchants/" + merchantId + "/shops").push(storeKey);
            return {
                exists: false,
                res: storeKey
            };
        }
    });
    return returnVal;
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

    var shopNameToUse = shopName.trim();

    const returnVal = database.ref(mode + "/store").once("value").then((snapshot) => {
        if (snapshot.val()) {
            const storeData = snapshot.val();
            for (var storeID in storeData) {
                if (storeData[storeID].shopName === shopNameToUse) {

                    return true;
                }
            }
        }

        return false;
    }).then((res) => {
        if (res) {
            return {
                exists: true,
                res: "Sorry, that store name is already taken."
            };
        } else {
            const key = database.ref(mode + '/store/' + shopID).update({
                shopName: shopNameToUse
            }).key;
            return {
                exists: false,
                res: key
            };
        }
    });
    return returnVal;
}

function addTagToShop(shopID, tag, mode = '') {
    if (!ValidateString(shopID) || !ValidateString(tag)) {
        return "Sorry, invalid input was entered!";
    }
    const tagToAdd = tag.trim();
    const returnVal = database.ref(mode + "/store/" + shopID + "/tag").once("value").then((snapshot) => {
        if (snapshot.val()) {
            const tags = snapshot.val();
            for (var id in tags) {
                if (tags[id] === tagToAdd) {
                    return {
                        res: false,
                        str: "Sorry, a tag with that name already exists."
                    };
                }
            }
            const key = database.ref(mode + '/store/' + shopID + "/tag").push(tagToAdd).key;
            return {
                res: true,
                str: key
            }
        } else {
            const key = database.ref(mode + '/store/' + shopID + "/tag").push(tagToAdd).key;
            return {
                res: true,
                str: key
            }
        }
    });
    return returnVal;
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
    const returnVal = database.ref(mode + "/store/" + shopID + "/item").once("value").then((snapshot) => {
        if (snapshot.val()) {
            const itemData = snapshot.val();
            for (var itemID in itemData) {
                if (itemData[itemID].name === itemInfo.name) {
                    return true;
                }
            }
        }
        return false;
    }).then((res) => {
        if (res) {
            return {
                exists: true,
                res: "Sorry, that item name is already taken."
            };
        } else {
            const key = database.ref(mode + '/store/' + shopID + "/item").push(itemInfo).key;
            return {
                exists: false,
                res: key
            };
        }
    });
    return returnVal;
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

    const retVal = database.ref(mode + "/users/merchants/" + merchantID + "/shops/").once("value").then((snapshot) => {
        var validChange = false;
        var ssv = snapshot.val();
        if (ssv) {
            for (var shopKey in ssv) {
                if (ssv[shopKey] === shopID) {
                    validChange = true;
                    break;
                }
            }
        }

        return validChange;
    }).then((res) => {
        if (res) {
            const temp = database.ref(mode + "/store/" + shopID + "/item/").once("value").then((snapshot) => {
                const items = snapshot.val();
                for (var id in items) {
                    if ((items[id].name === itemInfo.name) && (itemID !== id)) {
                        return {
                            res: false,
                            str: "Sorry, you already have an item with that name."
                        }
                    }
                }
                database.ref(mode + '/store/' + shopID + "/item/" + itemID).update(itemInfo);
                return {
                    res: true
                }

            });
            return temp;
        } else {
            return {
                res: false,
                str: "Sorry, that change was not valid."
            }
        }
    });

    return retVal;

}

function purchaseItemFromShop(shopIDs, itemIDs, quantities, customerID, mode = '') {
    const SHOP_IDS = shopIDs;
    const ITEM_IDS = itemIDs;
    const QUANTITIES = quantities;
    const CUSTOMER = customerID
    if (!ValidateString(CUSTOMER) || !ValidateArray(SHOP_IDS, ValidateString) || !ValidateArray(ITEM_IDS, ValidateString) || !ValidateArray(QUANTITIES, ValidateNumber)) {
        return "Sorry, invalid input was entered!";
    }
    var returnVal = database.ref(mode + "/store/").once("value").then((snapshot) => {
        if (snapshot.val()) {
            const res = snapshot.val();
            for (var check in SHOP_IDS) {
                var tempItem = res[SHOP_IDS[check]].item[ITEM_IDS[check]];

                var newInventoryCount = tempItem.inventory - QUANTITIES[check];
                if (newInventoryCount < 0) {
                    return "Item " + tempItem.name + " from " + res[SHOP_IDS[check]].shopName + " could not be bought. Please double check the inventory, and if you think this is a mistake please contact a developer.";
                }
            }

            for (var id in SHOP_IDS) {
                var newTempItem = res[SHOP_IDS[id]].item[ITEM_IDS[id]];
                var newItemID = ITEM_IDS[id];

                var officialInvCount = newTempItem.inventory - QUANTITIES[id];
                database.ref(mode + "/store/" + SHOP_IDS[id]).child("item/" + newItemID).update({
                    inventory: officialInvCount
                })
            }
            return database.ref(mode + "/users/customers/" + CUSTOMER).once("value").then((snapshot) => {
                if (snapshot.val()) {
                    var name = snapshot.val().userName;
                    var address = snapshot.val().address;
                    if (mode !== TEST_MODE) {
                        address = decrypt(address);
                    }
                    return {
                        res: true,
                        str: "Thank you for your purchase " + name + "! We will send the item(s) to " + address + "."
                    };
                } else {
                    return ERROR_CONSTANT;
                }
            });

        }
        return ERROR_CONSTANT;

    });
    return returnVal;
}

function createMerchant(username, password, email, phoneNumber, mode = '') {
    if (!ValidateString(username) || !ValidateString(password) || !ValidateString(email) || !ValidateString(phoneNumber)) {
        return "Invalid username, password, email or phone number was entered.";
    }
    const nameToUse = username.trim();
    const encryptedPassword = encrypt(password);
    const encryptedEmail = encrypt(email);
    const encryptedPhoneNumber = encrypt(phoneNumber);
    const retVal = database.ref(mode + "/users/merchants/").once("value").then((snapshot) => {
        const merchants = snapshot.val();
        for (var id in merchants) {
            if (merchants[id].userName === nameToUse) {
                return {
                    res: false,
                    str: "Sorry, that username is already in use."
                }
            }
        }
        const key = database.ref(mode + '/users/merchants').push({
            userName: nameToUse,
            password: encryptedPassword,
            email: encryptedEmail,
            phoneNum: encryptedPhoneNumber
        }).key;
        return {
            res: true,
            str: key
        }
    });
    return retVal;
}

function merchantLogin(username, password, mode = '') {
    if (!ValidateString(username) || !(ValidateString(password))) {
        return "Invalid username or password was entered.";
    }
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
    if (!ValidateString(username) || !ValidateString(password) || !ValidateString(email) || !ValidateString(address) || !ValidateString(phoneNumber)) {
        return "Invalid username, password, email, address or phone number was entered.";
    }
    const nameToUse = username.trim();
    const encryptedPassword = encrypt(password);
    const encryptedEmail = encrypt(email);
    const encryptedAddress = encrypt(address);
    const encryptedPhoneNumber = encrypt(phoneNumber);

    const retVal = database.ref(mode + "/users/customers/").once("value").then((snapshot) => {
        const customers = snapshot.val();
        for (var id in customers) {
            if (customers[id].userName === nameToUse) {
                return {
                    res: false,
                    str: "Sorry, that username is already in use."
                }
            }
        }
        const key = database.ref(mode + '/users/customers').push({
            userName: nameToUse,
            password: encryptedPassword,
            email: encryptedEmail,
            address: encryptedAddress,
            phoneNum: encryptedPhoneNumber,
            note: note
        }).key;
        return {
            res: true,
            str: key
        }
    });
    return retVal;
}

function customerLogin(username, password, mode='') {
    if(!ValidateString(username) || !(ValidateString(password))) {
        return "Invalid username or password was entered.";
    }

    password = encrypt(password);
    var retVal = database.ref(mode + "/users/customers").once("value").then((snapshot) => {
        var ssv = snapshot.val();
        for(var customerId in ssv) {
            var currData = ssv[customerId];

            var checkUsername = currData["userName"];
            var checkPass = currData["password"];

            if((checkUsername === username) && (checkPass === password)) {
                var cart = [];
                if("shoppingCart" in currData) {
                    cart = currData["shoppingCart"];
                }

                return {
                    id: customerId,
                    cart: cart,
                    name: checkUsername
                };
            }
        }

        return null;
    });

    return retVal;
}

function addItemToShoppingCart(customerID, shopID, itemID, mode = "") {
    if (!ValidateString(customerID) || !ValidateString(shopID) || !ValidateString(itemID)) {
        return "Incorrect customer, shop or user was entered.";
    }
    var returnVal = database.ref(mode + '/users/customers/' + customerID + '/shoppingCart/').once("value").then((snapshot) => {
        if (snapshot.val()) {
            const SC = snapshot.val();
            for (var item in SC) {
                if (SC[item].itemID === itemID) {
                    return {
                        resp: true,
                        str: "That item is already in your shopping cart!"
                    }
                }
            }
        }

        return database.ref(mode + '/users/customers/' + customerID + '/shoppingCart/').push({
            itemID: itemID,
            shopID: shopID
        }).key;

    });
    return returnVal;
}

function remItemFromSC(customer, itemToRemove, mode = "") {
    if (!ValidateString(customer) || !ValidateString(itemToRemove)) {
        return "Incorrect customer or item was entered.";
    }
    var returnVal = database.ref(mode + '/users/customers/' + customer + '/shoppingCart/').once("value").then((snapshot) => {
        if (snapshot.val()) {
            const SC = snapshot.val();
            for (var item in SC) {
                if (SC[item].itemID === itemToRemove) {
                    database.ref(mode + '/users/customers/' + customer + '/shoppingCart/' + item).remove();
                    return SUCCESS_CONSTANT;
                }
            }
            return ERROR_CONSTANT;
        } else {
            return ERROR_CONSTANT;
        }

    });
    return returnVal;
}

// !--- PLACE ALL PRODUCTION ENDPOINTS WITH THE TEST ENDPOINTS BELOW HERE ---!
exports.removeItemFromShoppingCart = functions.https.onCall((data, context) => {
    return remItemFromSC(data.customerID, data.itemID);
});
exports.testRemoveItemFromSC = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(remItemFromSC(request.body.userID, request.body.itemID, TEST_MODE));
    });
});

exports.addToCart = functions.https.onCall((data, context) => {
    return addItemToShoppingCart(data.customerID, data.shopID, data.itemID);
});
exports.testAddToCart = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(addItemToShoppingCart(request.body.customerID, request.body.shopID, request.body.itemID, TEST_MODE));
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
    var name = data.itemName.trim();
    const itemData = {
        url: data.url,
        altText: data.altText,
        name: name,
        cost: data.cost,
        inventory: inventory
    };
    return addItemToShop(data.shopId, itemData);
});
exports.testAddItem = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        const numInventory = Number(request.body.inventory);
        const name = request.body.itemName.trim();
        const itemData = {
            url: request.body.url,
            altText: request.body.altText,
            name: name,
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
    var name = data.itemName.trim();
    const itemData = {
        url: data.url,
        altText: data.altText,
        name: name,
        cost: cost,
        inventory: inventory
    };

    return editItemInShop(shopId, itemId, merchantId, itemData);
});
exports.testEditItem = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        const numInventory = Number(request.body.inventory);
        const name = request.body.itemName.trim();
        const itemData = {
            url: request.body.url,
            altText: request.body.altText,
            name: name,
            cost: request.body.cost,
            inventory: numInventory
        };
        response.status(200).send(editItemInShop(request.body.shopID, request.body.itemId, request.body.merchantId, itemData, TEST_MODE));
    });
});

exports.purchaseItemsFromShop = functions.https.onCall((data, context) => {
    return purchaseItemFromShop(data.shopIDs, data.itemIDs, data.quantities, data.customerID);
});
exports.testPurchaseItems = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        const items = request.body.itemIDs.split(', ');
        const stores = request.body.shopIDs.split(', ');
        const quantities = request.body.quantities.split(', ');
        var qties = [];
        for (var a in quantities) {
            qties.push(Number(quantities[a]));
        }
        response.status(200).send(purchaseItemFromShop(stores, items, qties, request.body.customerID, TEST_MODE));
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

exports.customerLogin = functions.https.onCall((data, context) => {
    return customerLogin(data.userName, data.password);
});

exports.testCustomerLogin = functions.https.onRequest((request, response) => {
    cors(request, response, () => {
        response.status(200).send(createCustomer(request.body.userName, request.body.password, TEST_MODE));
    });
})

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
        if (number < 0) return false;
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