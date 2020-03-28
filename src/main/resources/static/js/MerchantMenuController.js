async function asyncDeleteShop(formData) {
    const resp = await cloudDeleteShop(formData);
}

function deleteShop(btn, shopId) {
    var infoJson = {
        shopId: shopId,
        userId: $("#currUserId").val()
    };

    showLoading();
    asyncDeleteShop(infoJson).then(function (data) {
        console.log(data);
        return $.ajax({
            url: "/deleteShop?shopId=" + shopId,
            type: "POST",
            dataType: "json"
        })
    }).then(function (data) {
        var row = btn.parentNode.parentNode;
        row.parentNode.removeChild(row);

        console.log(data);

        if (data) {
            $("#noShopDiv").css("display", "block");
            $("#nonEmptyShopDiv").css("display", "none");
        }
        hideLoading();
    });
}

function editShop(shopId) {
    window.location.href = '/goToEditShopPage?shopId=' + shopId;
}

function viewAsCustomer(shopId) {
    window.location.href = '/goToShopCustomerView?shopId=' + shopId;
}

$(document).ready(function () {
    $(".deleteShopButton").on('click', function () {
        shopId = this.parentNode.parentNode.id.replace("SHOP_", "");
        deleteShop(this, shopId);
    });

    $(".viewAsCustButton").on('click', function () {
        shopId = this.parentNode.parentNode.id.replace("SHOP_", "");
        viewAsCustomer(shopId);
    });

    $(".editShopButton").on('click', function () {
        shopId = this.parentNode.parentNode.id.replace("SHOP_", "");
        editShop(shopId);
    });
});