$(document).ready(() => {
    addItemIDs();
    updateCost();
})

function addItemIDs() {
    const ids_str = $("#itemIDs").text();
    const ids = ids_str.split("$");
    let counter = 0;
    let isFirstRow = true;
    $("#cartTable tr").each(function () {
        if (isFirstRow) {
            isFirstRow = false;
        } else {
            $(this).append('<input name=item value="' + ids[counter] + '" type=hidden /></td>');
            counter++;
        }
    })
}

function updateCost() {
    let isFirstRow = true;
    let totalCost = 0;

    $("#cartTable tr").each(function () {
        if (isFirstRow) {
            isFirstRow = false;
        } else {
            let dataCells = $(this).find('td');
            let quantity;
            $(this).closest('tr').find("input").each(function () {
                if (!isNaN(this.value)) {
                    quantity = this.value;
                }
            });
            let cost = dataCells[3].innerHTML;

            totalCost += quantity * cost;
        }
    })
    $("#totalCost").text("Total Cost: " + totalCost.toFixed(2));
}

function checkout() {
    let isFirstRow = true;
    let itemIDs = [];
    let quantities = [];

    $("#cartTable tr").each(function () {
        if (isFirstRow) {
            isFirstRow = false;
        } else {
            $(this).closest('tr').find("input").each(function () {
                if (isNaN(this.value)) {
                    itemIDs.push(this.value);
                }
                if (!isNaN(this.value)) {
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
        alert("Please enter a name!");
        return;
    }
    if (ccNum == "") {
        alert("Please enter a credit card number!");
        return;
    }

    const resp = await PurchaseItems({
        shopID: aStoreID,
        itemIDs: itemIDs,
        quantities: quantities
    });

    if (resp.data == "Success") {
        alert("Thank you " + name + " for your purchase.");
        window.history.back();
    } else {
        alert("Error: there was a problem with the transaction");
    }
}