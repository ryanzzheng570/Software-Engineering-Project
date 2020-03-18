var editNameFormId = "#editNameForm";
var addTagFormId = "#addTagForm";
var addItemFormId = "#addItemForm";

async function asyncUpdateShopName(formData) {
    const resp = await cloudUpdateShopName(formData);
}

async function asyncAddTag(formData) {
    const resp = await cloudAddTag(formData);
    var tagId = resp.data;
    return tagId;
}

async function asyncRemoveTag(formData) {
    const resp = await cloudRemoveTag(formData);
}

async function asyncAddItem(formData) {
    const resp = await cloudAddItem(formData);
    var itemId = resp.data;
    return itemId;
}

async function asyncRemoveItem(formData) {
    const resp = await cloudRemoveItem(formData);
}

function editShopNameHandler(e){
    if (e.preventDefault) {
        e.preventDefault();
    }

    var info = $(editNameFormId).serializeArray();
    var infoJson = {};

    for (var i = 0; i < info.length; i++){
        var curr = info[i];
        infoJson[curr["name"]] = curr["value"];
    }

    if (infoJson["shopName"] === "") {
        alert("Please enter a store name!");
    } else {
        asyncUpdateShopName(infoJson).then(function(data) {
            console.log(data);
            return $.ajax({
               url: "/changeShopName?" + $(editNameFormId).serialize(),
               type: "POST",
               dataType: "json"
             })
        }).then(function(data){
            $(editNameFormId).value = data.shopName;
        });
    }
}

function removeTag(btn, shopId, tagId) {
    var infoJson = {
        shopId: shopId,
        tagId: tagId
    };

    asyncRemoveTag(infoJson).then(function(data) {
        console.log(data);
        return $.ajax({
            url: "/removeTag?shopId=" + shopId + "&tagId=" + tagId,
            type: "POST",
            dataType: "json"
        })
    }).then(function(data){
        var row = btn.parentNode.parentNode;
        row.parentNode.removeChild(row);

        if (data.tags.length === 0) {
            $("#noTagDiv").css("display", "block");
            $("#nonEmptyTagDiv").css("display", "none");
        }
    });
}

function addTagHandler(e){
    if (e.preventDefault) {
        e.preventDefault();
    }

    var info = $(addTagFormId).serializeArray();
    var infoJson = {};

    for (var i = 0; i < info.length; i++){
        var curr = info[i];
        infoJson[curr["name"]] = curr["value"];
    }

    if (infoJson["tagName"] === "") {
        alert("Tag names must be non-empty!");
    } else {
        asyncAddTag(infoJson).then(function(data) {
            console.log(data);
            return $.ajax({
                url: "/addTag?" + $(addTagFormId).serialize() + "&setId=" + data,
                type: "POST",
                dataType: "json"
            })
        }).then(function(data){
            $("#noTagDiv").css("display", "none");
            $("#nonEmptyTagDiv").css("display", "block");

            var newRow = document.createElement("tr");

            var nameElem = document.createElement("td");
            var nameText = document.createTextNode(data.tagName)
            nameElem.appendChild(nameText);
            newRow.appendChild(nameElem);

            var remButtonBox = document.createElement("td");
            var remButton = document.createElement("input");
            remButton.type = "button";
            remButton.value = "Remove Tag";
            remButton.addEventListener('click', function() {
                removeTag(this, infoJson["shopId"], data.id);
            });
            remButtonBox.appendChild(remButton);
            newRow.appendChild(remButtonBox);

            document.getElementById("tagTable").appendChild(newRow);
        });
    }
}

function removeItem(btn, shopId, itemId) {
    infoJson = {
        shopId: shopId,
        itemId: itemId
    };

    asyncRemoveItem(infoJson).then(function(data) {
        console.log(data);
        return $.ajax({
            url: "/removeItem?shopId=" + shopId + "&itemId=" + itemId,
            type: "POST",
            dataType: "json"
        })
    }).then(function(data){
        var row = btn.parentNode.parentNode;
        row.parentNode.removeChild(row);

        if (data.items.length === 0) {
            $("#noItemDiv").css("display", "block");
            $("#nonEmptyItemDiv").css("display", "none");
        }
    });
}

function addItemHandler(e){
    console.log("ADDING ITEM");
    if (e.preventDefault) {
        e.preventDefault();
    }

    var info = $(addItemFormId).serializeArray();
    var infoJson = {};

    for (var i = 0; i < info.length; i++){
        var curr = info[i];
        infoJson[curr["name"]] = curr["value"];
    }

    if (infoJson["itemName"] === "") {
        alert("Please enter an item name!");
    } else if (infoJson["inventory"] === "") {
        alert("Please enter a quantity value!");
    } else if (infoJson["cost"] === "") {
        alert("Please enter a cost value!");
    } else {
        asyncAddItem(infoJson).then(function(data) {
            console.log(data);
            return $.ajax({
                url: "/addItem?" + $(addItemFormId).serialize() + "&setId=" + data,
                type: "POST",
                dataType: "json"
            })
        }).then(function(data){
            $("#noItemDiv").css("display", "none");
            $("#nonEmptyItemDiv").css("display", "block");

            var newRow = document.createElement("tr");

            var nameElem = document.createElement("td");
            var nameText = document.createTextNode(data.itemName)
            nameElem.appendChild(nameText);
            newRow.appendChild(nameElem);

            var imgElem = document.createElement("td");
            var images = data.images;
            if (images.length > 0) {
                var linkRef = document.createElement("a");
                linkRef.href = images[0].url;

                var imgRef = document.createElement("img");
                imgRef.src = images[0].url;
                imgRef.altText = images[0].altText;
                imgRef.style = "width:200px;height:200px;border:0;";

                linkRef.appendChild(imgRef);
                imgElem.appendChild(linkRef);
            } else {
                var noImgText = document.createTextNode("(NO IMAGE)");
                imgElem.appendChild(noImgText);
            }
            newRow.appendChild(imgElem);

            var invElem = document.createElement("td");
            var invText = document.createTextNode(data.inventory)
            invElem.appendChild(invText);
            newRow.appendChild(invElem);

            var costElem = document.createElement("td");
            var costText = document.createTextNode(data.cost)
            costElem.appendChild(costText);
            newRow.appendChild(costElem);

            var remButtonBox = document.createElement("td");
            var remButton = document.createElement("input");
            remButton.type = "button";
            remButton.value = "Remove Item";
            remButton.addEventListener('click', function() {
                removeItem(this, infoJson["shopId"], data.id);
            });
            remButtonBox.appendChild(remButton);
            newRow.appendChild(remButtonBox);

            document.getElementById("itemTable").appendChild(newRow);
        });
    }
}

$(document).ready(function() {
    $(editNameFormId).submit(function(e) {
        editShopNameHandler(e);
    });

    $(addTagFormId).submit(function(e) {
        addTagHandler(e);
    });

    $(addItemFormId).submit(function(e) {
        addItemHandler(e);
    });

    $(".removeTagButton").on('click', function() {
        tagId = this.id.replace("TAG_", "");
        removeTag(this, $("#shopId").val(), tagId);
    });

    $(".removeItemButton").on('click', function() {
        tagId = this.id.replace("ITEM_", "");
        removeItem(this, $("#shopId").val(), tagId);
    });
});