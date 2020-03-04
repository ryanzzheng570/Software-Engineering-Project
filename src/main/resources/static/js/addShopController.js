var tagKey = "TAG"

var tagCounter = 0;

$(document).ready(function() {
    $("#addShopForm").submit(function(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }

        console.log($("#addShopForm").serialize());

        $.ajax({
            url: "http://localhost:8181/addShop?" + $("#addShopForm").serialize(),
            type: "POST",
            dataType: "json"
        }).then(function(data) {
            console.log(data);
            var newRow = "";

            newRow += "<tr>";
            newRow += "<td>" + data.id + "</td>";
            newRow += "<td>" + data.shopName + "</td>";

            var tags = [];
            for (var tagInd = 0; tagInd < data.tags.length; tagInd++){
                var currTagName = data.tags[tagInd].tagName;
                console.log(currTagName);
                tags.push(currTagName);
            }

            newRow += "<td>" + tags + "</td>";
            newRow += "</tr>";

            $("#shopIdTable").append(newRow);
        })
    })

    $("#addTag").click(function(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }

        //$("#shopInfoTable").append("<tr><td>Tag Value</td><td><input name=\"" + tagKey + "_" + tagCounter + "\" type=\"text\"></td></tr>");
        $("#shopInfoTable").append("<tr><td>Tag Value</td><td><input name=\"tag\" type=\"text\"></td></tr>");

        tagCounter += 1;
    })
});