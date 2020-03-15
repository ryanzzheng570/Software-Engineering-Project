$(document).ready(() => {
    // This is where you're going to have to do most of the HTML file based on the DB
    const STORE_ID = "-M2QECi8-MSD1yp8jzA9";
    loadPage(STORE_ID);
})

function updateCost() {
    let isFirstRow = true;
    let totalCost = 0;

    $("#cartTable tr").each(function(){
        if (isFirstRow) {
            isFirstRow = false;
        }
        else {
            let dataCells = $(this).find('td');
            let quantity;
            $(this).closest('tr').find("input").each(function() {
                quantity = this.value;
            });
            let cost = dataCells[3].innerHTML.substr(1);
            totalCost += quantity * cost;
        }
    })
    $("#totalCost").text("Total Cost: " + totalCost.toFixed(2));
}

async function loadPage(aStoreID) {
    const ids_str = $("#itemIds").text();
    const ids = ids_str.split("$");

    const resp = await GetItemsFromStoreByIds({
        shopID: aStoreID,
        itemIDs: ids
    });

    const items = resp.data.items;

    let cartHTML = "";
    if (items.length === 0) {
        $('#noItems').html('<h2>Nothing in cart</h2>');
    } else {
        cartHTML += "<table id='cartTable'><tr><th>Name</th><th>Quantity</th><th>Inventory Remaining</th><th>Individual Cost</th></tr>";
    }

    for (let item in items) {
        cartHTML += '<tr><td style="text-align:center" name="itemName">' + items[item].name + '</td>';
        cartHTML += '<td style="text-align:center" name="itemQuantity"><input type="number" min="1" max="' + items[item].inventory + '" value="1" onchange="updateCost()"/></td>';
        cartHTML += '<td style="text-align:center" name="itemInventory">' + items[item].inventory + '</td>';
        cartHTML += '<td style="text-align:center">$' + items[item].cost + '</td>';
    }
    $('#cartSection').html(cartHTML);

    //Calculate cost using default quantity
    updateCost();
}
