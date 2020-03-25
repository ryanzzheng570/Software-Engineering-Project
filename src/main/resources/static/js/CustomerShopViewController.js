$(document).ready(() => {
    checkboxClicked();
})

function checkboxClicked(){
    var items = document.getElementsByName("item");
    document.getElementById("shoppingCart").disabled = true;

    for (var i = 0; i < items.length; i++) {
        //If at least 1 item is selected, you can add to cart
        if (items[i].checked == true) {
            document.getElementById("shoppingCart").disabled = false;
        }
    }
}