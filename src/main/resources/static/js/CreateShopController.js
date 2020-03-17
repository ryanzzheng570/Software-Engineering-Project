var createShopFormId = "#createShopForm";

async function saveStoreToDb(shopData) {
    console.log(shopData);
    const resp = await cloudSaveShop({});
    console.log(resp.data);
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
        $.ajax({
            url: "/addShop?" + $(createShopFormId).serialize(),
            type: "POST",
            dataType: "json"
        }).then(function(data) {
            console.log("HERE");
            saveStoreToDb(data);
//            window.location.href = '/goToEditShopPage?shopId=' + data.id;
        });
    }
}

$(document).ready(function() {
    $(createShopFormId).submit(function(e) {
        createShopFormHandler(e);
    });
});