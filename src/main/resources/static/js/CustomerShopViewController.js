$(document).ready(function () {
    // Will use this when adding stores to database is setup
    //   const STORE_ID = document.getElementById("shopID").childNodes[1].innerText;
    const STORE_ID = "-M2QECi8-MSD1yp8jzA9";
    loadPage(STORE_ID);
})

async function loadPage(aStoreID) {
    const resp = await GetItemsFromStore({
        shopID: aStoreID
    });
    const items = resp.data.items;
    const tags = resp.data.tags;
    document.title = resp.data.name;
    $('#pageTitle').html('<h1 align="center">Welcome to the online store for ' + resp.data.name + '</h1>');

    var itemHTML = "";

    if (items.length === 0) {
        $('#noItems').html('<h2>Sorry, the merchant did not add items to their store yet!</h2>');
        $('shoppingCart').css('display', 'none');

    } else {
        itemHTML += '<h2>Items</h2><table><tr><th>Name</th><th>Image</th><th>Remaining</th><th>Cost</th><th>Checkout</th></tr>';
        $('#shoppingCart').css('display', 'inline');
    }

    for (var id in items) {
        itemHTML += '<tr><td style="text-align:center" name="itemName">' + items[id].name + '</td>';

        if (items[id].url === "") {
            itemHTML += '<td style="text-align:center">(NO IMAGE)</td>';
        } else {
            itemHTML += '<td style="text-align:center"><a href="' + items[id].url + '"><img src="' + items[id].url + '" alt="' + items[id].altText + '"style="width:200px;height:200px;border:0;"></a>>/td>';
        }

        itemHTML += '<td style="text-align:center" name="itemInventory">' + items[id].inventory + '</td>';

        itemHTML += '<td style="text-align:center">$' + items[id].cost + '</td>';

        if (items[id].inventory != 0) {
            itemHTML += '<td style="text-align:center"><input name=item value="' + items[id].id + '" type=checkbox /></td>';
        } else {
            itemHTML += '<td style="text-align:center"><input name=item value="' + items[id].id + '" type=checkbox disabled /></td>';
        }

        itemHTML += '</tr>'

    }
    itemHTML += '</table><td style="text-align:center"><input name=store value="' + aStoreID + '" type=hidden /></td>';

    $('#itemSection').html(itemHTML);

    var tagHTML = '';
    if (tags.length != 0) {
        tagHTML += '<h5>Tag(s)</h5><h6>'
        for (var t in tags) {
            tagHTML += '&nbsp&nbsp&nbsp' + tags[t];
        }
        tagHTML += '</h6>';
    }
    $('#tagSection').html(tagHTML);

}