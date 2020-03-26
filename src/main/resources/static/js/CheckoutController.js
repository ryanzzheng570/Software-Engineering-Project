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
            cost = cost.replace("$", "");

            totalCost += quantity * cost;
        }
    })
    $("#totalCost").text("Total Cost: " + totalCost.toFixed(2));
}

function checkout() {
    let isFirstRow = true;
    let itemIDs = [];
    let quantities = [];
    let noErrors = true;

    $("#cartTable tr").each(function () {
        if (isFirstRow) {
            isFirstRow = false;
        } else {
            $(this).closest('tr').find("input").each(function () {
                if (isNaN(this.value)) {
                    itemIDs.push(this.value);
                }
                if (!isNaN(this.value)) {
                    let maxValue = parseInt($(this)[0].max);
                    if (this.value == "" || isNaN(this.value) || parseInt(this.value) > maxValue || parseInt(this.value) < 1) {
                        alert("Error - Please enter a valid quantity!");
                        noErrors = false;
                        return;
                    }
                    quantities.push(this.value);
                }
            });
        }
    })
    if (noErrors === true) {
        let storeId = $("#storeID").val();
        submit(storeId, itemIDs, quantities);
    }
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

    var checkoutData = {
        storeId: aStoreID,
        itemIds: itemIDs,
        quantities: quantities
    };

    console.log(checkoutData);

    showLoading();

    var resp = await PurchaseItems({
        shopID: aStoreID,
        itemIDs: itemIDs,
        quantities: quantities
    });

    console.log(resp.data);

    if (resp.data == "Success") {
        $.ajax({
            url: "/checkout?" + $.param(checkoutData),
            type: "POST",
            dataType: "json"
        });

        hideLoading();

        alert("Thank you " + name + " for your purchase.");

        window.location = document.referrer;
    } else {
        hideLoading();
        alert("Error: there was a problem with the transaction");
    }

}