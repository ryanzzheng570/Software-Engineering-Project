$(document).ready(() => {
    $(".a2c").click(function() {
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

    console.log(RESP.data);

    //    if (resp.data == "Success") {
    //        $.ajax({
    //            url: "/checkout?" + $.param(checkoutData),
    //            type: "POST",
    //            dataType: "json"
    //        });
    //
    //        hideLoading();
    //
    //        alert("Thank you " + name + " for your purchase.");
    //
    //        window.history.back();
    //    } else {
    //        hideLoading();
    //        alert("Error: there was a problem with the transaction");
    //    }
        hideLoading();
}