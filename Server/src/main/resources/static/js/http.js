function httpGetAsync(theUrl, callbackStatusOk, callbackStatusOther) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.readyState === 4) {
            if (xmlHttp.status === 200) {
                callbackStatusOk(xmlHttp.responseText);
            }
            else {
                callbackStatusOther(xmlHttp.responseText, xmlHttp.status)
            }
        }
    };
    xmlHttp.open("GET", theUrl, true); // true for asynchronous
    xmlHttp.send(null);
}