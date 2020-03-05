var globals = {
    tagCounter: 0
};

function resetForm() {
    $("#addShopForm table").remove();

    var tableElem = document.createElement("table");
    tableElem.id = "shopInfoTable";

    var initialRow = document.createElement("tr");

    var nameCell = document.createElement("td");
    var nameText = document.createTextNode("Shop Name");
    nameCell.appendChild(nameText);
    initialRow.appendChild(nameCell);

    var inputCell = document.createElement("td");
    var inputBody = document.createElement("input");
    inputBody.name = "shopName";
    inputBody.type = "text";
    inputCell.appendChild(inputBody);
    initialRow.appendChild(inputCell);

    tableElem.appendChild(initialRow);

    var elem = document.getElementById("addShopForm");
    elem.insertBefore(tableElem, elem.firstChild);
}

$(document).ready(function() {
    resetForm();

    $("#addShopForm").submit(function(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }

        var info = $("#addShopForm").serializeArray();
        var infoJson = {};

        for (var i = 0; i < info.length; i++){
            var curr = info[i];
            infoJson[curr["name"]] = curr["value"];
        }

        if (infoJson["shopName"] === "") {
            alert("Please enter a store name!");
        } else {
            $.ajax({
                url: "http://localhost:8181/addShop?" + $("#addShopForm").serialize(),
                type: "POST",
                dataType: "json"
            }).then(function(data) {
                var newRow = document.createElement("tr");

                var idCell = document.createElement("td");
                var idText = document.createTextNode(data.id);
                idCell.appendChild(idText);
                newRow.appendChild(idCell);

                var nameCell = document.createElement("td");
                var nameText = document.createTextNode(data.shopName);
                nameCell.appendChild(nameText);
                newRow.appendChild(nameCell);

                var tags = [];
                for (var tagInd = 0; tagInd < data.tags.length; tagInd++){
                    var currTagName = data.tags[tagInd].tagName;
                    tags.push(currTagName);
                }

                var tagCell = document.createElement("td");
                var tagText = document.createTextNode("[" + tags + "]");
                tagCell.appendChild(tagText);
                newRow.appendChild(tagCell);

                var merchantViewCell = document.createElement("td");
                var merchantButton = document.createElement("button");
                merchantButton.type = "submit";
                merchantButton.name = "shopId";
                merchantButton.value = data.id;
                merchantButton.innerText = "Go to Shop";
                merchantViewCell.appendChild(merchantButton);
                newRow.appendChild(merchantViewCell);

                $("#shopIdTable").append(newRow);
            });
        }
    });

    $("#addTag").click(function(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }

        var rowId = "TAG_" + globals.tagCounter;

        var row = document.createElement("tr");
        row.id = rowId;

        var nameBox = document.createElement("td");
        var nameText = document.createTextNode("Tag Value");
        nameBox.appendChild(nameText);
        row.appendChild(nameBox);

        var valBox = document.createElement("td");
        var inputText = document.createElement("input");
        inputText.type = "text";
        inputText.name = "tag";
        valBox.appendChild(inputText);
        row.appendChild(valBox);

        var buttonBox = document.createElement("td");
        var inputButton = document.createElement("input");
        inputButton.type = "button";
        inputButton.value = "Remove Tag";
        inputButton.addEventListener('click', function() {
            var row = document.getElementById(rowId);
            row.parentNode.removeChild(row);
        });
        buttonBox.appendChild(inputButton);
        row.appendChild(buttonBox);

        document.getElementById("shopInfoTable").appendChild(row);

        globals.tagCounter += 1;
    });

    $("#resetForm").click(function(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }

        resetForm();
    });
});