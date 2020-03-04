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
            url: "http://localhost:8181/searchForShops" + $("#searchShopForm").serialize(),
            type: "POST",
            data: {
                "searchField": $("#searchField").val()
            },
            dataType: "json"
        }).then(function(data) {
                $("#shopList").append("<tr><td>" + shop.id + "</td></tr>");
        })
    })

});