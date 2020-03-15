$(document).ready(function() {

    const STORE_ID = "-M2QECi8-MSD1yp8jzA9";
    loadItems(STORE_ID);

})

async function loadItems(aStoreID) {
    const resp = await GetItemsFromStore({shopID: aStoreID});
    const items = resp.data.items;
    const tags = resp.data.tags;
    document.title = resp.data.name;
    $('#pageTitle').html('<h1 align="center">Welcome to the online store for ' + resp.data.name + '</h1>');

    var itemHTML = "";

    if(items.length === 0) {
        $('#noItems').html('<h2>Sorry, the merchant did not add items to their store yet!</h2>');
    } else {
        itemHTML += '<h2>Items</h2><table><tr><th>Name</th><th>Image</th><th>Quantity</th><th>Cost</th><th>Checkout</th></tr>';
    }

    for(var id in items) {

        itemHTML += '<tr><td style="text-align:center" name="itemName">' + items[id].name + '</td>';

        if (items[id].url === "") {
            itemHTML += '<td style="text-align:center">(NO IMAGE)</td>';
        } else {
            itemHTML += '<td style="text-align:center"><a href="' + items[id].url + '"><img src="' + items[id].url + '" alt="' + items[id].altText + '"style="width:200px;height:200px;border:0;"></a>>/td>';
        }

        itemHTML += '<td style="text-align:center" name="itemInventory">' + items[id].inventory + '</td>';

        itemHTML += '<td style="text-align:center">$' + items[id].cost + '</td>';

        itemHTML += '</tr>'

    }
    itemHTML += '</table>'

    $('#itemSection').html(itemHTML);

    var tagHTML = '';
    if(tags.length != 0) {
        tagHTML += '<h5>Tag(s)</h5><h6>'
        for(var t in tags) {
            tagHTML += '&nbsp&nbsp&nbsp' + tags[t];
        }
        tagHTML += '</h6>';
    }
    $('#tagSection').html(tagHTML);

}


