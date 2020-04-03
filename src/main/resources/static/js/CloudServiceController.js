async function PurchaseItems(quantityData) {
    var response = await callCloudFunction("purchaseItemsFromShop", quantityData);
    return response;
}

async function cloudSaveShop(shopData) {
    var response = await callCloudFunction("addShop", shopData);
    return response;
}

async function cloudUpdateShopName(shopData) {
    var response = await callCloudFunction("changeShopName", shopData);
    return response;
}

async function cloudAddTag(shopData) {
    var response = await callCloudFunction("addTag", shopData);
    return response;
}

async function cloudRemoveTag(shopData) {
    var response = await callCloudFunction("removeTag", shopData);
    return response;
}

async function cloudAddItem(shopData) {
    var response = await callCloudFunction("addItem", shopData);
    return response;
}

async function cloudRemoveItem(shopData) {
    var response = await callCloudFunction("removeItem", shopData);
    return response;
}

async function cloudEditItem(data) {
    var response = await callCloudFunction("editItem", data);
    return response;
}

async function cloudDeleteShop(shopData) {
    var response = await callCloudFunction("deleteShop", shopData);
    return response;
}

async function cloudSaveMerchant(merchantData) {
    var response = await callCloudFunction("createMerchant", merchantData);
    return response;
}

async function cloudMerchantLogin(merchantData) {
    console.log("Inside cloud merchant login");
    var response = await callCloudFunction("merchantLogin", merchantData);
    return response;
}

async function cloudSaveCustomer(customerData) {
    var response = await callCloudFunction("createCustomer", customerData);
    return response;
}

async function cloudCustomerLogin(customerData) {
    console.log("Inside Cloud customer login");
    var response = await callCloudFunction("customerLogin", customerData);
    return response;
}

async function cloudAddToCart(data) {
    var response = await callCloudFunction("addToCart", data);
    return response;
}

async function cloudRemoveItemFromSC(data) {
    var response = await callCloudFunction("removeItemFromShoppingCart", data);
    return response;
}

// !--- PLACE ALL CLOUD SERVICE CALLS ABOVE HERE --- SHOULD NOT HAVE TO CHANGE ANYTHING BELOW ---!

document.writeln('<script type="text/javascript" src="https://www.gstatic.com/firebasejs/7.11.0/firebase-app.js"></script>');
document.writeln('<script type="text/javascript" src="https://www.gstatic.com/firebasejs/7.11.0/firebase-auth.js"></script>');
document.writeln('<script type="text/javascript" src="https://www.gstatic.com/firebasejs/7.11.0/firebase-functions.js"></script>');

function callCloudFunction(functionName, inputData) {
    var callFunction = firebase.functions().httpsCallable(functionName);
    return new Promise((resolve, reject) => {
        callFunction(inputData).then((result) => {
            resolve(result);
        }).catch((error) => {
            var code = error.code;
            var message = error.message;
            var details = error.details;
            console.log("\nCode: " + code + "\nMessage: " + message + "\nDetails: " + details);
            reject(error)
        })
    });
}

$(document).ready(() => {
    var firebaseConfig = {
        apiKey: "AIzaSyA8CjyW4zT7jL46sG4rEOcCON7SNsDN_do",
        authDomain: "engineeringlabproject.firebaseapp.com",
        databaseURL: "https://engineeringlabproject.firebaseio.com",
        projectId: "engineeringlabproject",
        storageBucket: "engineeringlabproject.appspot.com",
        messagingSenderId: "523387019136",
        appId: "1:523387019136:web:e414700e2e6bb35e372b65",
        measurementId: "G-5XBXE4QJLC"
    };
    firebase.initializeApp(firebaseConfig);
    firebase.functions();
});