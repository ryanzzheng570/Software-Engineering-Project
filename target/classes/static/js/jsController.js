$(document).ready(function() {
    $("#addForm").submit(function(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }

        $.ajax({
            url: "http://localhost:8181/addAddressBook",
            type: "POST",
            dataType: "json"
        }).then(function(data) {
            $("#bookIdTable").append("<tr><td>" + data.id + "</td></tr>");
        })
    })

    $("#viewForm").submit(function(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }

        $.ajax({
            url: "http://localhost:8181/viewAddressBookJson?" + $("#viewForm").serialize(),
            dataType: "json"
        }).then(function(data) {
            $("#viewBookBuddies tbody").empty();

            $(".viewBookId").text("Id: " + data.id);

            $(".needsBookId").val(data.id);

            $("#viewAddressBookDivId").removeAttr("style");

            var buddies = data.myBuddies;
            for(var i = 0; i < buddies.length; i++) {
                var currBuddy = buddies[i];

                var toAdd = "<tr>";
                toAdd += "<td>" + currBuddy.id + "</td>";
                toAdd += "<td>" + currBuddy.name + "</td>";
                toAdd += "<td>" + currBuddy.phoneNumber + "</td>";
                toAdd += "<td>" + currBuddy.address + "</td>";
                toAdd += "</tr>";

                $("#viewBookBuddies").append(toAdd);
            }
        })
    })

    $("#addBuddyForm").submit(function(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }

        $.ajax({
            url: "http://localhost:8181/addBuddy?" + $("#addBuddyForm").serialize(),
            type: "POST",
            dataType: "json"
        }).then(function(data) {
            var toAdd = "<tr>";
            toAdd += "<td>" + data.id + "</td>";
            toAdd += "<td>" + data.name + "</td>";
            toAdd += "<td>" + data.phoneNumber + "</td>";
            toAdd += "<td>" + data.address + "</td>";
            toAdd += "</tr>";

            $("#viewBookBuddies tbody").append(toAdd);
        })
    })

    $("#removeBuddyForm").submit(function(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }

        $.ajax({
            url: "http://localhost:8181/removeBuddy?" + $("#removeBuddyForm").serialize(),
            type: "POST",
            dataType: "json"
        }).then(function(data) {
            var ind = -1;
            $("#viewBookBuddies tbody td:first-child").each(function(i, row) {
                console.log(i);
                console.log(row);
                console.log($(this).text());
                console.log(data);

                if (parseInt($(this).text()) === data) {
                    ind = i
                }
            })

            console.log(ind);
            if (ind != -1){
                $("#viewBookBuddies tbody tr:eq(" + ind + ")").remove();
            }
        })
    })
});