function addShoppingCartLink() {
    sessionStorage.setItem('token', '-M3JTMoy32z1v1Makgq1');

    var scLink = document.createElement("a");
    var scText = document.createTextNode("Your Shopping Cart");
    if (sessionStorage.getItem("token") && sessionStorage.getItem("token") != "") {
        scLink.href = "/goToCart?userID=" + sessionStorage.getItem("token");
    } else {
        scLink.href = "/login";
    }
    scLink.appendChild(scText);
    return scLink;
}

function addSidebar() {
    $("head").prepend("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/SidebarStyles.css\">");
    $("body").wrapInner("<div class='main' />");

    var sidebarDiv = document.createElement("div");
    sidebarDiv.className = "sidenav";

    //     Disable the login link since it's not fully implemented
    //     var loginLink = document.createElement("a");
    //     var loginText = document.createTextNode("User Login");
    //     loginLink.href = "/login";
    //     loginLink.appendChild(loginText);
    //     sidebarDiv.appendChild(loginLink);

    var signUpLink = document.createElement("a");
    var signUpText = document.createTextNode("Sign Up");
    signUpLink.href = "/signUp";
    signUpLink.appendChild(signUpText);
    sidebarDiv.appendChild(signUpLink);

    var searchLink = document.createElement("a");
    var searchText = document.createTextNode("Search");
    searchLink.href = "/search";
    searchLink.appendChild(searchText);
    sidebarDiv.appendChild(searchLink);

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

    var createShopLink = document.createElement("a");
    var createShopText = document.createTextNode("Create Shop");
    createShopLink.href = "/goToAddShopPage";
    createShopLink.appendChild(createShopText);
    sidebarDiv.appendChild(createShopLink);

    sidebarDiv.appendChild(addShoppingCartLink());

    $("body").prepend(sidebarDiv);
}

$(document).ready(function () {
    addSidebar();
})