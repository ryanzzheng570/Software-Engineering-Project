var merchantInfoFormId = "#merchantInfoForm";

async function createMerchantInDb(merchantData) {
    const resp = await cloudSaveMerchant(merchantData);
    var merchantId = resp.data;
    return merchantId;
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
            console.log(data);
            return $.ajax({
                url: "/addNewMerchant?" + $(merchantInfoFormId).serialize() + "&setId=" + data,
                type: "POST",
                dataType: "json"
            });
        }).then(function (data) {
            window.location.href = '/';
            hideLoading();
        });
    }
}

$(document).ready(function () {
    $(merchantInfoFormId).submit(function (e) {
        createMerchantAccountHandler(e);
    });
});