var merchantInfoFormId = "#merchantInfoForm";

async function createMerchantInDb(merchantData) {
    const resp = await cloudSaveMerchant(merchantData);
    return resp.data;
}

async function merchantLogin(shopData) {
    const resp = await cloudMerchantLogin(shopData);
    return resp;
}

function createMerchantAccountHandler(e) {
    if (e.preventDefault) {
        e.preventDefault();
    }

    var info = $(merchantInfoFormId).serializeArray();
    var infoJson = {};

    for (var i = 0; i < info.length; i++) {
        var curr = info[i];
        infoJson[curr["name"]] = curr["value"];
    }

    if (infoJson["userName"] === "") {
        alert("Please enter a user name!");
    } else if (infoJson["password"] === "") {
        alert("Please enter a password!");
    } else if (infoJson["email"] === "") {
        alert("Please enter an email!");
    } else if (infoJson["contactPhoneNumber"] === "") {
        alert("Please enter a phone number!");
    } else {
        showLoading();
        createMerchantInDb(infoJson).then(function (data) {
            if (data.res) {
                return $.ajax({
                    url: "/addNewMerchant?" + $(merchantInfoFormId).serialize() + "&setId=" + data.str,
                    type: "POST",
                    dataType: "json"
                });
            } else {
                alert(data.str);
                return false;
            }

        }).then(function (data) {
            if (data) {
                showLoading();

                var loginJson = {};
                loginJson["userName"] = infoJson["userName"];
                loginJson["password"] = infoJson["password"];
                merchantLogin(loginJson).then(function (data) {
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
            else {
                hideLoading();
            }
        });
    }
}

$(document).ready(function () {
    $(merchantInfoFormId).submit(function (e) {
        createMerchantAccountHandler(e);
    });
});