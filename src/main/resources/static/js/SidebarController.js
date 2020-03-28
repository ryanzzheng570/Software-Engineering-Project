function addCustomerInfo(sidebarDiv) {
    var isCustomer = $("#isCustomer").attr('value');
    var username = $("#username").attr('value');
    if (username && username != "") {
        var type = "Merchant"
        if (isCustomer) {
            type = "Customer"
        }
        var h2 = document.createElement("H2");
        var t2 = document.createTextNode(type);
        h2.appendChild(t2);
        sidebarDiv.appendChild(h2);
        var h3 = document.createElement("H3");
        var t3 = document.createTextNode(username);
        h3.appendChild(t3);
        sidebarDiv.appendChild(h3);
    }
}

function addSidebar() {
    $("head").prepend("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/SidebarStyles.css\">");
    $("body").wrapInner("<div class='main' />");

    var sidebarDiv = document.createElement("div");
    sidebarDiv.className = "sidenav";

    addCustomerInfo(sidebarDiv);

    var homeLink = document.createElement("a");
    var homeText = document.createTextNode("Home");
    homeLink.href = "/";
    homeLink.appendChild(homeText);
    sidebarDiv.appendChild(homeLink);

    var merchantMenuLink = document.createElement("a");
    var merchantMenuText = document.createTextNode("Merchant Menu");
    merchantMenuLink.href = "/goToMerchantMenuPage";
    merchantMenuLink.appendChild(merchantMenuText);
    sidebarDiv.appendChild(merchantMenuLink);

    var searchLink = document.createElement("a");
    var searchText = document.createTextNode("Search");
    searchLink.href = "/search";
    searchLink.appendChild(searchText);
    sidebarDiv.appendChild(searchLink);

    var scLink = document.createElement("a");
    var scText = document.createTextNode("Your Cart");
    scLink.href = "/goToCart"
    scLink.appendChild(scText);
    sidebarDiv.appendChild(scLink);

    var loginLink = document.createElement("a");
    var loginText = document.createTextNode("User Login");
    loginLink.href = "/login";
    loginLink.appendChild(loginText);
    sidebarDiv.appendChild(loginLink);

    var signUpLink = document.createElement("a");
    var signUpText = document.createTextNode("Sign Up");
    signUpLink.href = "/signUp";
    signUpLink.appendChild(signUpText);
    sidebarDiv.appendChild(signUpLink);


    $("body").prepend(sidebarDiv);
}

$(document).ready(function () {
    addSidebar();
})