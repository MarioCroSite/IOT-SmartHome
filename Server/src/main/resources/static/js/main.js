
document.addEventListener("DOMContentLoaded", function(event) {

    let ui = new LiveData.ui();
    let uiAdapter = new LiveData.uiAdapter(ui);
    let infiniteLoader = new LiveData.infiniteLoader(uiAdapter);

    let delayMs = parseInt(document.getElementById("infiniteLoaderDelay").value) * 1000;
    infiniteLoader.start(delayMs);



    HistoryData.setCurrentChart("temp");
    HistoryData.setCurrentInterval("Daily");
    HistoryData.load();

});