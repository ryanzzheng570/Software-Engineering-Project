var editNameFormId = "#editNameForm";
var addTagFormId = "#addTagForm";
var addItemFormId = "#addItemForm";

async function asyncUpdateShopName(formData) {
    const resp = await cloudUpdateShopName(formData);
    return resp.data;
}

async function asyncAddTag(formData) {
    const resp = await cloudAddTag(formData);
    return resp.data;
}

async function asyncRemoveTag(formData) {
    const resp = await cloudRemoveTag(formData);
}

async function asyncAddItem(formData) {
    const resp = await cloudAddItem(formData);
    return resp.data;
}

async function asyncEditItem(formData) {
    const resp = await cloudEditItem(formData);
    return resp.data;
}

async function asyncRemoveItem(formData) {
    const resp = await cloudRemoveItem(formData);
}

function editShopNameHandler(e) {
    if (e.preventDefault) {
        e.preventDefault();
    }

    var info = $(editNameFormId).serializeArray();
    var infoJson = {};

    for (var i = 0; i < info.length; i++) {
        var curr = info[i];
        infoJson[curr["name"]] = curr["value"];
    }

    if (infoJson["shopName"] === "") {
        alert("Please enter a store name!");
    } else {
        showLoading();
        asyncUpdateShopName(infoJson).then(function (data) {
            if (data.exists) {
                alert(data.res);
            } else {
                return $.ajax({
                    url: "/changeShopName?" + $(editNameFormId).serialize(),
                    type: "POST",
                    dataType: "json"
                })
            }
        }).then(function (data) {
            hideLoading();
            if (data && data.shopName) {
                $(editNameFormId).value = data.shopName;
            }
        });
    }
}

function removeTag(btn, shopId, tagId) {
    var infoJson = {
        shopId: shopId,
        tagId: tagId
    };

    showLoading();
    asyncRemoveTag(infoJson).then(function (data) {
        return $.ajax({
            url: "/removeTag?shopId=" + shopId + "&tagId=" + tagId,
            type: "POST",
            dataType: "json"
        })
    }).then(function (data) {
        hideLoading();
        var row = btn.parentNode.parentNode;
        row.parentNode.removeChild(row);

        if (data.tags.length === 0) {
            $("#noTagDiv").css("display", "block");
            $("#nonEmptyTagDiv").css("display", "none");
        }
    });
}

function addTagHandler(e) {
    if (e.preventDefault) {
        e.preventDefault();
    }

    var info = $(addTagFormId).serializeArray();
    var infoJson = {};

    for (var i = 0; i < info.length; i++) {
        var curr = info[i];
        infoJson[curr["name"]] = curr["value"];
    }

    if (infoJson["tagName"] === "") {
        alert("Tag names must be non-empty!");
    } else {
        showLoading();
        asyncAddTag(infoJson).then(function (data) {
            if (data.res) {
                return $.ajax({
                    url: "/addTag?" + $(addTagFormId).serialize() + "&setId=" + data.str,
                    type: "POST",
                    dataType: "json"
                })
            } else {
                alert(data.str);
            }

        }).then(function (data) {
            hideLoading();
            if (data) {
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
                remButton.addEventListener('click', function () {
                    removeTag(this, infoJson["shopId"], data.id);
                });
                remButtonBox.appendChild(remButton);
                newRow.appendChild(remButtonBox);

                document.getElementById("tagTable").appendChild(newRow);
            }

        });
    }
}



function removeItem(btn, shopId, itemId) {
    infoJson = {
        shopId: shopId,
        itemId: itemId
    };

    showLoading();
    asyncRemoveItem(infoJson).then(function (data) {
        return $.ajax({
            url: "/removeItem?shopId=" + shopId + "&itemId=" + itemId,
            type: "POST",
            dataType: "json"
        })
    }).then(function (data) {
        hideLoading();
        var row = btn.parentNode.parentNode;
        row.parentNode.removeChild(row);

        if (data.items.length === 0) {
            $("#noItemDiv").css("display", "block");
            $("#nonEmptyItemDiv").css("display", "none");
        }
    });
}

function editItemHandler(btn) {
    var shopId = $("#shopId").val();
    var merchantId = $("#currUserId").val();
    var rowId = btn.parentNode.parentNode.id;
    var itemId = rowId.replace("ITEM_", "");

    var row = $('#' + rowId);
    var itemData = {};
    $("td input", row).each(function () {
        var name = $(this).attr('name')
        var val = $(this).val();
        itemData[name] = val;
    });

    if (itemData["itemName"] === "") {
        alert("Please enter an item name!");
        return;
    } else if (itemData["inventory"] === "") {
        alert("Please enter a quantity value!");
        return;
    } else if (itemData["cost"] === "") {
        alert("Please enter a cost value!");
        return;
    }

    if (isNaN(itemData["inventory"] || Number.isInteger(itemData["inventory"]) || itemData["inventory"] < 0)) {
        alert("Please enter a whole number quantity!");
        return;
    } else if (isNaN(itemData["cost"]) || itemData["cost"] < 0) {
        alert("Please enter a valid cost value!");
        return;
    }

    var callStr = "/editItem?";
    itemData["shopId"] = shopId;
    itemData["itemId"] = itemId;
    itemData["merchantId"] = merchantId;

    showLoading();
    asyncEditItem(itemData).then(function (data) {
        if (data.res) {
            return $.ajax({
                url: callStr + $.param(itemData),
                type: "POST",
                dataType: "json"
            })
        } else {
            alert(data.str);
        }

    }).then(function (data) {
        hideLoading();
        if (data) {
            if (data.items.length === 0) {
                $("#noItemDiv").css("display", "block");
                $("#nonEmptyItemDiv").css("display", "none");
            }
            window.location.href = '/goToEditShopPage?shopId=' + data.id;
        }
    });
}

function addItemHandler(e) {
    if (e.preventDefault) {
        e.preventDefault();
    }

    var info = $(addItemFormId).serializeArray();
    var infoJson = {};

    for (var i = 0; i < info.length; i++) {
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
        showLoading();
        asyncAddItem(infoJson).then(function (data) {
            if (data.exists) {
                alert(data.res);
            } else {
                return $.ajax({
                    url: "/addItem?" + $(addItemFormId).serialize() + "&setId=" + data.res,
                    type: "POST",
                    dataType: "json"
                })
            }
        }).then(function (data) {
            hideLoading();
            if (data) {
                $("#noItemDiv").css("display", "none");
                $("#nonEmptyItemDiv").css("display", "block");

                var newRow = document.createElement("tr");

                newRow.id = "ITEM_" + data.id;

                var nameElem = document.createElement("td");
                var nameText = document.createElement("input");
                nameText.type = "text";
                nameText.value = data.itemName;
                nameText.name = "itemName";
                nameElem.appendChild(nameText);
                newRow.appendChild(nameElem);

                var imgElem = document.createElement("td");
                var images = data.images;
                var url = "";
                var altText = "";
                if (images.length > 0) {
                    url = images[0].url;
                    altText = images[0].altText;

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

                var urlElem = document.createElement("td");
                var urlText = document.createElement("input");
                urlText.type = "text";
                urlText.value = url;
                urlText.name = "url";
                urlElem.appendChild(urlText);
                newRow.appendChild(urlElem);

                var altTextElem = document.createElement("td");
                var altTextText = document.createElement("input");
                altTextText.type = "text";
                altTextText.value = altText;
                altTextText.name = "altText";
                altTextElem.appendChild(altTextText);
                newRow.appendChild(altTextElem);

                var invElem = document.createElement("td");
                var invText = document.createElement("input");
                invText.type = "number";
                invText.step = "1";
                invText.min = "0";
                invText.value = data.inventory;
                invText.name = "inventory";
                invElem.appendChild(invText);
                newRow.appendChild(invElem);

                var costElem = document.createElement("td");
                var costText = document.createElement("input");
                costText.type = "number";
                costText.step = "0.01";
                costText.min = "0";
                costText.value = data.cost;
                costText.name = "cost";
                costElem.appendChild(costText);
                newRow.appendChild(costElem);

                var remButtonBox = document.createElement("td");
                var remButton = document.createElement("input");
                remButton.type = "button";
                remButton.value = "Remove Item";
                remButton.addEventListener('click', function () {
                    removeItem(this, infoJson["shopId"], data.id);
                });
                remButtonBox.appendChild(remButton);
                newRow.appendChild(remButtonBox);

                var editButtonBox = document.createElement("td");
                var editButton = document.createElement("input");
                editButton.type = "button";
                editButton.value = "Save Changes";
                editButton.addEventListener('click', function () {
                    editItemHandler(this);
                });
                editButtonBox.appendChild(editButton);
                newRow.appendChild(editButtonBox);

                document.getElementById("itemTable").appendChild(newRow);
            }
        });
    }
}

$(document).ready(function () {
    $(editNameFormId).submit(function (e) {
        editShopNameHandler(e);
    });

    $(addTagFormId).submit(function (e) {
        addTagHandler(e);
    });

    $(addItemFormId).submit(function (e) {
        addItemHandler(e);
    });

    $(".removeTagButton").on('click', function () {
        tagId = this.id.replace("TAG_", "");
        removeTag(this, $("#shopId").val(), tagId);
    });

    $(".removeItemButton").on('click', function () {
        tagId = this.parentNode.parentNode.id.replace("ITEM_", "");
        removeItem(this, $("#shopId").val(), tagId);
    });

    $(".saveChangesButton").on('click', function () {
        editItemHandler(this);
    });
});