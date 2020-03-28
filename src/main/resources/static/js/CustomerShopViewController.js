$(document).ready(() => {
    $(".a2c").click(function () {
        createAddToCartHandler($(this).val());
    });
});

async function createAddToCartHandler(itemID) {
    const STORE = $("#storeID").val();
    const ITEM = itemID;
    const CUSTOMER = $("#customerID").val();
    showLoading();

    const RESP = await cloudAddToCart({
        customerID: CUSTOMER,
        shopID: STORE,
        itemID: ITEM,
    });

    if (RESP.data.resp) {
        alert(RESP.data.str);
    }
    hideLoading();
}