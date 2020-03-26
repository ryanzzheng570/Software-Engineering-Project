$(document).ready(() => {
    $(".a2c").click(function () {
        createAddToCartHandler($(this).val());
    });
});

async function createAddToCartHandler(itemID) {
    const STORE = $("#storeID").val();
    const ITEM = itemID;
    showLoading();

    const RESP = await cloudAddToCart({
        shopID: STORE,
        itemID: ITEM,
    });

    if (RESP.data != "SUCCESS") {
        alert(RESP.data);
    }
    hideLoading();
}