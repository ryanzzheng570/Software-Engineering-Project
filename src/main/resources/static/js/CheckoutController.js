$(document).ready(() => {
    updateCost();
    $(".rem").click(function () {
        removeFromCartHandler($(this));
    });
})

async function removeFromCartHandler(obj) {
    var customer = $("#customerID").attr('value');
    var item = obj.val();
    if (customer && customer != "" && item && item != "") {
        showLoading();
        const resp = await cloudRemoveItemFromSC({
            customerID: customer,
            itemID: item
        });
        hideLoading();
        if (resp.data === "SUCCESS") {
            obj.closest('tr').remove()
        } else {
            if (resp.data != "") {
                alert(resp.data);
            } else {
                alert("Error: there was a problem with the transaction. Please contact a developer.");
            }
        }

    } else {
        alert("Something went wrong. Contact a developer.")
    }

}

function updateCost() {
    let totalCost = 0;

    $(".inv").each(function () {
        let quantity = $(this).val();
        let cost = $(this).closest('tr').children('td.cost').text();
        totalCost += quantity * cost;
    })
    $("#totalCost").text("Total Cost: $" + totalCost.toFixed(2));
}



function checkout() {
    var storeIDs = [];
    var itemIDs = [];
    var quantities = [];
    var submitFlag = true;
    $(".store").each(function () {
        let storeID = $(this).attr('value');
        let itemID = $(this).closest('tr').children('td.item').attr('value');
        let request = $(this).closest('tr').children('td.itemQuantity').children('input.inv').val()
        let max = $(this).closest('tr').children('td.maxInv').text();

        if (storeID && storeID != "" && itemID && itemID != "") {
            storeIDs.push(storeID);
            itemIDs.push(itemID);
        } else {
            alert("Something went wrong. Contact a developer.");
            submitFlag = false;
            return false;
        }

        if (!isNaN(request) && request > 0 && request <= (Number(max))) {
            quantities.push(request);
        } else {
            alert("Please enter valid quantities.")
            submitFlag = false;
            return false;
        }
    });
    if (submitFlag) {
        submit(storeIDs, itemIDs, quantities);
    }
}

async function submit(storeIDs, itemIDs, quantities) {
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

    showLoading();

    const resp = await PurchaseItems({
        shopIDs: storeIDs,
        itemIDs: itemIDs,
        quantities: quantities
    });
    hideLoading();
    if (resp.data === "SUCCESS") {
        alert("Thank you " + name + " for your purchase.");
        window.location = document.referrer;
    } else {
        if (resp.data && resp.data != "") {
            alert(resp.data);
        } else {
            alert("Error: there was a problem with the transaction. Please contact a developer.");
        }
    }

}