$(document).ready(function () {
    $("#searchShopForm").submit(function (e) {
        e.preventDefault();

        showLoading();

        $.ajax({
            url: "/search?" + $("#searchShopForm").serialize(),
            type: "POST",
            dataType: "json"
        }).then(function (data) {
            $("#results").empty();
            if (data.length == 0) {
                $("#results").append('<p>Sorry, no shops were found</p>');
            } else {
                $("#results").append('<table id="results-table"><tr><th>Shop Name</th><th>Shop Tags</th><th></th></tr>');
                for (let i = 0; i < data.length; i++) {
                    let tags = "[";
                    for (let j = 0; j < data[i].tags.length; j++) {
                        tags += data[i].tags[j].tagName;
                        if (j != data[i].tags.length - 1) {
                            tags += ", ";
                        }
                    }
                    tags += "]";

                    let shopUrl = "/goToShopCustomerView?shopId=" + data[i].id;

                    let button = "<input "
                    button += "type=\"button\" ";
                    button += "value=\"View Shop as Customer\" ";
                    button += "style=\"margin-left: 20px\" ";
                    button += "onclick=\"location.href='" + shopUrl + "'\"";
                    button += ">";
                    button += "</input>";

                    let tr = "<tr>";
                    tr += "<td style=\"text-align:center\">" + data[i].shopName + "</td>";
                    tr += "<td style=\"text-align:center\">" + tags + "</td>";
                    tr += "<td style=\"text-align:center\">" + button + "</td></tr>";
                    tr += "</tr>";

                    $("#results-table").append(tr);
                }
            }

            hideLoading();
        })
    })
})