$(document).ready(function() {
    $("#searchShopForm").submit(function (e) {
        e.preventDefault();

        $.ajax({
            url: "/search?" + $("#searchShopForm").serialize(),
            type: "POST",
            dataType: "json"
        }).then(function (data) {
            $("#results").empty();
            if (data.length == 0) {
                $("#results").append('<p>Sorry, no shops were found</p>');
            }
            else {
                for (let i = 0; i < data.length; i++) {
                    let tags = "";
                    for(let j = 0; j < data[i].tags.length; j++) {
                        tags += data[i].tags[j].tagName;
                    }
                    let str = "Shop ID: " + data[i].id + ", Shop Name: " + data[i].shopName + ", Shop Tags: " + tags;

                    let shopUrl = `/goToShop?shopId=${data[i].id}`;
                    let button = `<input type="button" value="View Shop as Customer" onclick="location.href='${shopUrl}'"></input>`;
                    $("#results").append(`<p style="display: inline-block">${str}</p>${button}</br>`);
                }
            }
        })
    })
})