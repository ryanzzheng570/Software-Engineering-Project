function addLoadingAnimation() {
    $("head").prepend("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/Loading.css\">");

    var loadingDiv = document.createElement("div");
    loadingDiv.id = "loading";

    var imgElem = document.createElement("img");
    imgElem.id = "loading-image";
    imgElem.src = "https://media1.giphy.com/media/3oEjI6SIIHBdRxXI40/giphy.gif?cid=790b7611d01a9f717c5f04cee7232ee824a926f97937dad0&rid=giphy.gif";
    imgElem.alt = "Loading...";

    loadingDiv.append(imgElem);

    $("body").append(loadingDiv);
}

function hideLoading() {
    $('#loading').hide();
}

function showLoading() {
    $('#loading').show();
}

$(document).ready(function () {
    addLoadingAnimation();
    hideLoading();
})