$(document).ready(function() {
    $("#addShopForm").submit(function(e) {
        console.log("HERE");
        if (e.preventDefault) {
            e.preventDefault();
        }

        $.ajax({
            url: "http://localhost:8181/addShop",
            type: "POST",
            dataType: "json"
        }).then(function(data) {
            $("#shopIdTable").append("<tr><td>" + data.id + "</td></tr>");
        })
    })

    $("#searchShopForm").submit(function(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }

        $.ajax({
            url: "http://localhost:8181/searchForShops",
            type: "POST",
            dataType: "json"
        }).then(function(data) {
            for(shop in data) {
                $("#shopList").append("<tr><td>" + data.id + "</td></tr>");
            }
        })
    })

});