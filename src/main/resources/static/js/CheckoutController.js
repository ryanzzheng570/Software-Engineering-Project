$(document).ready(() => {
    // This is where you're going to have to do most of the HTML file based on the DB
    // const STORE_ID = "-M2QECi8-MSD1yp8jzA9";
    // loadPage(STORE_ID);
    updateCost();
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
                if(!isNaN(this.value)) {
                    quantity = this.value;
                    console.log("quantity", quantity);
                }
            });
            let cost = dataCells[3].innerHTML;
            console.log("cost", cost);

            totalCost += quantity * cost;
        }
    })
    $("#totalCost").text("Total Cost: " + totalCost.toFixed(2));
}

// async function loadPage(aStoreID) {
//     const ids_str = $("#itemIds").text();
//     const ids = ids_str.split("$");
//
//     const resp = await GetItemsFromStoreByIds({
//         shopID: aStoreID,
//         itemIDs: ids
//     });
//
//     const items = resp.data.items;
//
//     let cartHTML = "";
//     if (items.length === 0) {
//         $('#noItems').html('<h2>Nothing in cart</h2>');
//     } else {
//         cartHTML += "<table id='cartTable'><tr><th>Name</th><th>Quantity</th><th>Inventory Remaining</th><th>Individual Cost</th></tr>";
//     }
//
//     for (let item in items) {
//         cartHTML += '<tr><td style="text-align:center" name="itemName">' + items[item].name + '</td>';
//         cartHTML += '<td style="text-align:center" name="itemQuantity"><input name="quantity" type="number" min="1" max="' + items[item].inventory + '" value="1" onchange="updateCost()"/></td>';
//         cartHTML += '<td style="text-align:center" name="itemInventory">' + items[item].inventory + '</td>';
//         cartHTML += '<td style="text-align:center">$' + items[item].cost + '</td>';
//         cartHTML += '<input name=item value="' + items[item].id + '" type=hidden /></td>';
//     }
//     cartHTML +=  '</table><td style="text-align:center"><input id="storeID" name=store value="' + aStoreID + '" type=hidden /></td>'
//     $('#cartSection').html(cartHTML);
//
//     //Calculate cost using default quantity
//     updateCost();
// }

function checkout() {
    let isFirstRow = true;
    let itemIDs = [];
    let quantities = [];

    $("#cartTable tr").each(function(){
        if (isFirstRow) {
            isFirstRow = false;
        }
        else {
            $(this).closest('tr').find("input").each(function() {
                if(isNaN(this.value)) {
                    console.log("itemId:", this.value);
                    itemIDs.push(this.value);
                }
                else if(!isNaN(this.value)) {
                    console.log("quantity:", this.value);
                    quantities.push(this.value);
                }
            });
        }
    })
    let storeId = $("#storeID").val();
    submit(storeId, itemIDs, quantities);
}

async function submit(aStoreID, itemIDs, quantities) {
    let ccNum = $("#ccNum").val();
    let name = $("#paymentName").val();

    if (name == "") {
        alert ("Please enter a name!");
        return;
    }
    if (ccNum == "") {
        alert ("Please enter a credit card number!");
        return;
    }
    console.log("itemIDS", itemIDs);
    console.log("quantities", quantities);

    // const resp = await PurchaseItems({
    //     shopID: aStoreID,
    //     itemIDs: itemIDs,
    //     quantities: quantities
    // });
    // console.log(resp.data)
    //
    // if (resp.data.data == "Success") {
    //     alert ("Thank you " + name + " for your purchase.");
    //     window.history.back();
    // } else {
    //     alert ("Error: there was a problem with the transaction");
    // }
}