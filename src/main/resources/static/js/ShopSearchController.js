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
                $("#results").append('<table id="results-table"><tr><th>Shop ID</th><th>Shop Name</th><th>Shop Tags</th><th></th></tr>');
                for (let i = 0; i < data.length; i++) {
                    let tags = "[";
                    for(let j = 0; j < data[i].tags.length; j++) {
                        tags += data[i].tags[j].tagName;
                        if (j != data[i].tags.length - 1) {
                            tags += ", ";
                        }
                    }
                    tags += "]";
                    let shopUrl = `/goToShop?shopId=${data[i].id}`;
                    let button = `<input type="button" value="View Shop as Customer" style="margin-left: 20px" onclick="location.href='${shopUrl}'"></input>`;
                    let tr = "<tr><td>" + data[i].id + "</td><td>" + data[i].shopName + "</td><td>" + tags + "</td><td>" + button + "</td></tr>";
                    $("#results-table").append(tr);
                }
            }
        })
    })
})