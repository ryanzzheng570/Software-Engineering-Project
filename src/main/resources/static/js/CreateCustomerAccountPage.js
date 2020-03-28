var customerInfoFormId = "#customerInfoForm";

async function createCustomerInDb(customerData) {
    const resp = await cloudSaveCustomer(customerData);
    var customerId = resp.data;
    return customerId;
}

function createCustomerAccountHandler(e) {
    if (e.preventDefault) {
        e.preventDefault();
    }

    var info = $(customerInfoFormId).serializeArray();
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
    } else if (infoJson["address"] === "") {
        alert("Please enter an address!");
    } else if (infoJson["phoneNumber"] === "") {
        alert("Please enter a phone number!");
    } else if (infoJson["note"] === "") {
        alert("Please enter a note!");
    } else {
        showLoading();
        createCustomerInDb(infoJson).then(function (data) {
            console.log(data);
            return $.ajax({
                url: "/createCustomerAccount?" + $(customerInfoFormId).serialize() + "&setId=" + data,
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
    $(customerInfoFormId).submit(function (e) {
        createCustomerAccountHandler(e);
    });
});