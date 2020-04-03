var merchantLoginFormId = "#merchantLoginForm";
var customerLoginFormId = "#customerLoginForm";

async function merchantLogin(shopData) {
    const resp = await cloudMerchantLogin(shopData);
    return resp;
}

async function customerLogin(cartData) {
    const resp = await cloudCustomerLogin(cartData);
    return resp;
}

function merchantLoginHandler(e) {
    if (e.preventDefault) {
        e.preventDefault();
    }

    var info = $(merchantLoginFormId).serializeArray();
    var infoJson = {};

    for (var i = 0; i < info.length; i++) {
        var curr = info[i];
        infoJson[curr["name"]] = curr["value"];
    }

    if (infoJson["userName"] === "") {
        alert("Please enter a user name!");
    } else if (infoJson["password"] === "") {
        alert("Please enter a password!");
    } else {
        showLoading();
        merchantLogin(infoJson).then(function (data) {
            data = data.data;
            if (data === null) {
                alert("Error: Invalid login.");
                return {
                    id: null
                };
            } else {
                var id = data.id;
                var name = data.name;
                var shops = [];
                if ("shops" in data) {
                    var tempShops = data["shops"];
                    for (var shopKey in tempShops) {
                        shops.push(tempShops[shopKey]);
                    }
                }

                data = {
                    id: id,
                    shops: shops,
                    userName: name
                };

                var callStr = "/loginAsMerchant?" + $.param(data);
                return $.ajax({
                    url: callStr,
                    type: "POST",
                    dataType: "json"
                });
            }
        }).then(function (data) {
            hideLoading();
            if (data.id !== null) {
                window.location.href = '/goToMerchantMenuPage';
            }
        });
    }
}

function customerLoginHandler(e) {
    if(e.preventDefault) {
        e.preventDefault();
    }

    var info = $(customerLoginFormId).serializeArray();
    var infoJson = {};

    for(var i=0; i<info.length; i++) {
        var curr = info[i];
        infoJson[curr["name"]] = curr["value"];
    }

    if(infoJson["userName"] === "") {
        alert("Please enter a user name!");
    }
    else if (infoJson["password"] === "") {
        alert("Please enter a password!");
    }
    else {
        showLoading();
        customerLogin(infoJson).then(function (data) {
            data = data.data;
            if(data === null) {
                alert("Error: Invalid login.");
                return {
                    id: null
                };
            }
            else {
                var id = data.id;
                var name = data.name;
                var cart = [];
                if("shoppingCart" in data) {
                    var tempCarts = data["cart"];
                    for (var cartKey in tempCarts) {
                        cart.push(tempCarts[cartKey]);
                    }
                }

                data = {
                    id: id,
                    cart: cart,
                    userName: name
                }

                var callStr = "/loginAsCustomer?" + $.param(data);
                return $.ajax({
                    url: callStr,
                    type: "POST",
                    dataType: "json"
                });
            }
        }).then(function (data) {
            hideLoading();
            if(data.id !== null) {
                window.location.href ='/';
             }
        });
    }
}

$(document).ready(function () {
    $(merchantLoginFormId).submit(function (e) {
        merchantLoginHandler(e);
    });

    $(customerLoginFormId).submit(function (e) {
        customerLoginHandler(e);
    });
});