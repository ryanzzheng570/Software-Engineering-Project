var createShopFormId = "#createShopForm";

async function saveStoreToDb(shopData) {
    const resp = await cloudSaveShop(shopData);
    var shopId = resp.data;
    return shopId;
}

function createShopFormHandler(e) {
    if (e.preventDefault) {
        e.preventDefault();
    }

    var info = $(createShopFormId).serializeArray();
    var infoJson = {};

    for (var i = 0; i < info.length; i++){
        var curr = info[i];
        infoJson[curr["name"]] = curr["value"];
    }

    if (infoJson["shopName"] === "") {
        alert("Please enter a store name!");
    } else {
        showLoading();
        saveStoreToDb(infoJson).then(function(data) {
            console.log(data);
            return $.ajax({
                url: "/addShop?" + $(createShopFormId).serialize() + "&setId=" + data,
                type: "POST",
                dataType: "json"
            });
        }).then(function(data){
            hideLoading();
            window.location.href = '/goToEditShopPage?shopId=' + data.id;
        });
    }
}

$(document).ready(function() {
    $(createShopFormId).submit(function(e) {
        createShopFormHandler(e);
    });
});