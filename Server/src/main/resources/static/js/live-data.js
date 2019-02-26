var LiveData = new function()  {
    this.ui = function () {
        this.gauges = new function () {
            this.gauge_co2 = new JustGage({
                id: "gauge-co2",
                min: 0,
                max: 2500,
                title: "CO₂",
                label: "ppm",
                decimals: 0,
                relativeGaugeSize: true,
                donut: false
            });
            this.gauge_temp = new JustGage({
                id: "gauge-temp",
                min: -20,
                max: 50,
                title: "Temperature",
                label: "°C",
                decimals: 1,
                relativeGaugeSize: true,
                donut: false
            });
            this.gauge_rel_humidity = new JustGage({
                id: "rel-humidity",
                min: 0,
                max: 100,
                title: "Rel. humidity",
                label: "%",
                decimals: 1,
                relativeGaugeSize: true,
                donut: false
            });
            this.gauge_power = new JustGage({
                id: "power-consumption",
                min: 0,
                max: 1000,
                title: "Power consumption",
                label: "W",
                decimals: 0,
                relativeGaugeSize: true,
                donut: false
            });

            this.setValues = (function (obj) {
                if (obj) {
                    if(obj.air && obj.air.data){
                        this.gauge_co2.refresh(obj.air.data.co2);
                        this.gauge_temp.refresh(obj.air.data.temparature);
                        this.gauge_rel_humidity.refresh(obj.air.data.relativeHumidity);
                    }
                    if(obj.plug && obj.plug.data){
                        this.gauge_power.refresh(obj.plug.data.power);
                    }
                }
                else {
                    alert("Received data has invalid format");
                }
            }).bind(this);
        };

        this.deviceStateSwitcher =  (function (buttonId, gatewayId, deviceName) {
            this.btn = document.getElementById(buttonId);

            this.gatewayId = gatewayId;
            this.deviceName = deviceName;

            this.startLabel = "Start";
            this.stopLabel = "Stop";
            this.stateStarted = true;
            this.stateStopped = false;

            this.setState = (function (state) {
                if(state === this.stateStarted){
                    this.btn.value = this.stopLabel;
                    this.btn.style.backgroundColor = "red";
                }
                else if(state === this.stateStopped){
                    this.btn.value = this.startLabel;
                    this.btn.style.backgroundColor = "green";
                }
            }).bind(this);

            this.onStarted = (function (responseText) {
                this.setState(this.stateStarted);
                this.btn.disabled = false;
            }).bind(this);

            this.onStopped = (function (responseText) {
                this.setState(this.stateStopped);
                this.btn.disabled = false;
            }).bind(this);

            this.onError = (function (responseText, responseCode) {
                alert("Error while trying to change device state. Status: " + responseCode);
                this.btn.disabled = false;
            }).bind(this);

            this.btn.addEventListener("click", (function () {
                if(!this.btn.disabled){
                    this.btn.disabled = true;
                    this.btn.style.backgroundColor = "orange";

                    if(this.btn.value == this.startLabel){
                        this.btn.value = "Starting...";
                        httpGetAsync(
                            "/api/gateway/" + this.gatewayId + "/" + this.deviceName + "/state/true",
                            this.onStarted,
                            this.onError
                        );
                    }
                    else if(this.btn.value == this.stopLabel){
                        this.btn.value = "Stopping...";
                        httpGetAsync(
                            "/api/gateway/" + this.gatewayId + "/" + this.deviceName + "/state/false",
                            this.onStopped,
                            this.onError
                        );
                    }
                }
            }).bind(this));
        });

        this.airStateSwitcher = new this.deviceStateSwitcher("startStopAir", "1", "air");
        this.plugStateSwitcher = new this.deviceStateSwitcher("startStopPlug", "1", "plug");

        this.update = (function (responseText) {
            let obj = JSON.parse(responseText);
            if (obj) {
                // update state button
                this.airStateSwitcher.setState(obj.air.state);
                this.plugStateSwitcher.setState(obj.plug.state);
                // TODO
                // update speedometers
                this.gauges.setValues(obj);
            }
            else {
                alert("Received data is empty!");
            }
        }).bind(this);

        this.showError = (function (responseText, responseCode) {
            if(responseCode == 0) {
                alert("Lost connection with a server.\nClick OK to reload.");
                location.reload(true);
            }
            else{
                alert("Error while receiving sensors info. Status code: " + responseCode);
            }
        })
    };

    this.uiAdapter = function (ui) {
        this.ui = ui;
        this.load = (function () {
            httpGetAsync(
                "/api/measurements/latest",
                this.ui.update,
                this.ui.showError
            );
        }).bind(this);
    }

    this.infiniteLoader = function (uiAdapter) {
        this.interval = null;
        this.uiAdapter = uiAdapter;

        this.create = (function (delay) {
            console.log("Creating interval with delay of " + delay + " ms");
            // immediately load so we don't wait for interval trigger
            this.uiAdapter.load();
            return setInterval((function () {
                this.uiAdapter.load();
            }).bind(this), delay);
        }).bind(this);

        this.stop = function (){
            if(this.interval){
                console.log("clearing interval");
                clearInterval(this.interval);
            }
        }

        this.start = (function (delay) {
            this.stop();
            this.interval = this.create(delay);
        }).bind(this);
    }

}