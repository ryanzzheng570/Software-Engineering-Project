var merchantInfoFormId = "#merchantInfoForm";

async function createMerchantInDb(merchantData) {
    const resp = await cloudSaveMerchant(merchantData);
    return resp.data;
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
                window.location.href = '/';
            }
            hideLoading();
        });
    }
}

$(document).ready(function () {
    $(merchantInfoFormId).submit(function (e) {
        createMerchantAccountHandler(e);
    });
});