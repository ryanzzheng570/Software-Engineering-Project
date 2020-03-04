$(document).ready(function() {
    $("#searchShopForm").submit(function (e) {
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
        }).then(function (data) {
            $("#shopList").append("<tr><td>" + shop.id + "</td></tr>");
        })
    })
}