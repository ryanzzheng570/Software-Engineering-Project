$(document).ready(function() {
    $("#searchShopForm").submit(function (e) {
        e.preventDefault();

        $.ajax({
            url: "/search?" + $("#searchShopForm").serialize(),
            type: "POST",
            dataType: "json"
        }).then(function (data) {
            $("#results").empty();
            for (let i = 0; i < data.length; i++) {
                let tags = "";
                for(let j = 0; j < data[i].tags.length; j++) {
                    tags += data[i].tags[j].tagName;
                }
                let str = "Shop ID: " + data[i].id + ", Shop Name: " + data[i].shopName + ", Shop Tags: " + tags;

                $("#results").append(`<p>${str}</p>`);
            }
        })
    })
})