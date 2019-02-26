HistoryData = new function(){

    this.currentChartName = null;
    this.currentInterval = null;

    this.createChart = function(containerElementId, labels, dataPoints){

        new Chart(document.getElementById(containerElementId), {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    data: dataPoints,
                    label: "Label",
                    borderColor: "#3e95cd",
                    fill: false
                }]
            },
            options: {
                legend: {
                    display: false
                },
                title: {
                    display: false,
                }
            }
        });

    };

    this.days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

    this.getWeekDayNamesFromDate = function(date){
        let lastDay = date.getDay();

        let dayNames = [];

        for(let i = lastDay; i>=0; i--){
            dayNames.push(this.days[i]);
        }

        for(let i=6; i>lastDay; i--){
            dayNames.push(this.days[i]);
        }

        return dayNames.reverse();
    };

    this.getMonthDaysFromDate = function(date){
        let dates = [];

        let copiedDate = new Date(date.getTime());
        let numberOfGroups = 6;

        let daysInGroup = 30/numberOfGroups;
        for(let i=0; i<numberOfGroups; i++){
            let dayInMonth = copiedDate.getDate();
            let month = copiedDate.getMonth() + 1;

            dates.push(dayInMonth.toString() + "." + month.toString() + ".");
            dates.push("");
            dates.push("");
            dates.push("");
            dates.push("");

            // subtract n days
            copiedDate.setDate(copiedDate.getDate()-daysInGroup);
        }

        return dates.reverse();
    };

    this.getHoursFromDate = function(date){
        let hours = [];

        let copiedDate = new Date(date.getTime());

        for(let i=0; i<24; i++){
            hours.push(copiedDate.getHours());
            // subtract n days
            copiedDate.setHours(copiedDate.getHours()-1);
        }

        return hours.reverse();
    };

    this.setData = function(data){
        let obj = JSON.parse(data);

        let dateNow = new Date();

        this.labelsDaily = this.getHoursFromDate(dateNow);
        this.labelsWeekly = this.getWeekDayNamesFromDate(dateNow);
        this.labelsMonthly = this.getMonthDaysFromDate(dateNow);

        this.tempDaily = [];
        this.tempWeekly = [];
        this.tempMonthly = [];

        this.co2Daily = [];
        this.co2Weekly = [];
        this.co2Monthly = [];

        this.relHumidityDaily = [];
        this.relHumidityWeekly = [];
        this.relHumidityMonthly = [];

        this.powerDaily = [];
        this.powerWeekly = [];
        this.powerMonthly = [];


        // region DAILY
        for(let i=0; i<obj.air.daily.length; i++){
            this.tempDaily.push(obj.air.daily[i].averageTemperature);
            this.co2Daily.push(obj.air.daily[i].averageCO2);
            this.relHumidityDaily.push(obj.air.daily[i].averageRelativeHumidity);
        }
        for(let i=0; i<obj.plug.daily.length; i++){
            this.powerDaily.push(obj.plug.daily[i].averagePower);
        }
        // endregion


        // region WEEKLY
        for(let i=0; i<obj.air.weekly.length; i++){
            this.tempWeekly.push(obj.air.weekly[i].averageTemperature);
            this.co2Weekly.push(obj.air.weekly[i].averageCO2);
            this.relHumidityWeekly.push(obj.air.weekly[i].averageRelativeHumidity);
        }
        for(let i=0; i<obj.plug.weekly.length; i++){
            this.powerWeekly.push(obj.plug.weekly[i].averagePower);
        }
        // endregion


        // region MONTHLY
        for(let i=0; i<obj.air.monthly.length; i++){
            this.tempMonthly.push(obj.air.monthly[i].averageTemperature);
            this.co2Monthly.push(obj.air.monthly[i].averageCO2);
            this.relHumidityMonthly.push(obj.air.monthly[i].averageRelativeHumidity);
        }
        for(let i=0; i<obj.plug.monthly.length; i++){
            this.powerMonthly.push(obj.plug.monthly[i].averagePower);
        }
        // endregion

        if(this.currentChartName && this.currentInterval){
            this.display();
        }
    }.bind(this);

    this.display = function(){
        console.log("Displaying " + this.currentInterval + " " + this.currentChartName)
        this.createChart("historyChart", this["labels" + this.currentInterval], this[this.currentChartName + this.currentInterval]);
    };

    this.setCurrentChart = function(chartName){
        if(this.currentChartName !== chartName){
            this.currentChartName = chartName;
            if(this.currentChartName && this.currentInterval){
                this.display();
            }

        }
    };

    this.setCurrentInterval = function(interval){
        if(this.currentInterval !== interval){
            this.currentInterval = interval;
            if(this.currentChartName && this.currentInterval){
                this.display();
            }
        }
    };


    this.error = function(error){
        alert("Error while loading history charts: " + error);
    };

    this.load = function(){
        console.log("Loading history data");
        httpGetAsync(
            "/api/measurements/averages",
            this.setData,
            this.error
        );
    }


};