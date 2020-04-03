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
            obj.closest('tr').remove();
            updateCost();
            updateTable();
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

function updateTable() {
    let count = 0;

    $(".inv").each(function () {
        count += 1;
    })

    if (count === 0) {
        $("#emptyCart").show();
        $("#cartSection").hide();
        $(".main").append("<div><h2>Your cart is empty!</h2></div>");
    }
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
    var customer = $("#customerID").attr('value');
    if (customer && customer != "") {
        if (submitFlag) {
            submit(storeIDs, itemIDs, quantities, customer);
        }
    }
}

async function submit(storeIDs, itemIDs, quantities, customer) {
    showLoading();

    const resp = await PurchaseItems({
        shopIDs: storeIDs,
        itemIDs: itemIDs,
        quantities: quantities,
        customerID: customer
    });
    hideLoading();
    if (resp.data.res) {
        alert(resp.data.str);
        window.location = document.referrer;
    } else {
        alert(resp.data);
    }

}