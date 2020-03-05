$(document).ready(function() {
    $("#searchShopForm").submit(function (e) {
        e.preventDefault();

        $.ajax({
            url: "http://localhost:8181/search?" + $("#searchShopForm").serialize(),
            type: "POST",
            dataType: "json"
        }).then(function (data) {
            for (let i = 0; i < data.length; i++) {
                document.write("Shop ID: " + data[i].id + " , Shop Name: " + data[i].shopName + ", ");
                document.write("Shop tags: ");
                for(let j = 0; j < data[i].tags.length; j++) {
                    document.write(data[i].tags[j].tagName);
                }
                document.write("<br>");
            }
        })
    })
})