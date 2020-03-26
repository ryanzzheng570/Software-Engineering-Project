var merchantLoginFormId = "#merchantLoginForm";

async function merchantLogin(shopData) {
    const resp = await cloudSaveShop(shopData);
    return resp;
}

function merchantLoginHandler(e) {
    if (e.preventDefault) {
        e.preventDefault();
    }

    var info = $(merchantLoginFormId).serializeArray();
    var infoJson = {};

    for (var i = 0; i < info.length; i++){
        var curr = info[i];
        infoJson[curr["name"]] = curr["value"];
    }

    if (infoJson["userName"] === "") {
        alert("Please enter a user name!");
    } else if (infoJson["password"] === "") {
        alert("Please enter a password!");
    } else {
        showLoading();
        merchantLogin(infoJson).then(function(data) {
            data = data.data;
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
        }).then(function(data){
            hideLoading();
            if (data.id === null) {
                alert("Error: Invalid login.");
            } else {
                window.location.href = '/goToMerchantMenuPage';
            }
        });
    }
}

$(document).ready(function() {
    $(merchantLoginFormId).submit(function(e) {
        merchantLoginHandler(e);
    });
});