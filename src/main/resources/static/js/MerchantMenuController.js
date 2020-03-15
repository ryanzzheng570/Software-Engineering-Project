function deleteShop(btn, shopId) {
    var callStr = "/deleteShop?shopId=" + shopId;

    $.ajax({
        url: callStr,
        type: "POST",
        dataType: "json"
    }).then(function(data) {
        var row = btn.parentNode.parentNode;
        row.parentNode.removeChild(row);

        console.log(data);

        if (data) {
            $("#noShopDiv").css("display", "block");
            $("#nonEmptyShopDiv").css("display", "none");
        }
    });
}

$(document).ready(function() {
    $(".deleteShopButton").on('click', function() {
        shopId = this.id.replace("SHOP_", "");
        deleteShop(this, shopId);
    });
});