$(document).ready(function() {
    $("#searchShopForm").submit(function (e) {
        if (e.preventDefault) {
            e.preventDefault();
        }

        $.ajax({
            url: "http://localhost:8181/search" + $("#searchShopForm").serialize(),
            type: "POST",
            dataType: "json"
        }).then(function (data) {
            console.log("data", data);
            for (row in data) {
                document.write("Shop ID:" + row.id);
            }
        })
    })
})