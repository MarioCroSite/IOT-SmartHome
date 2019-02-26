package com.algebra.iot.server.scheduled;

import com.algebra.iot.server.dao.model.AirMeasurement;
import com.algebra.iot.server.dao.model.AirMeasurementAverages;
import com.algebra.iot.server.dao.model.PlugMeasurement;
import com.algebra.iot.server.dao.model.PlugMeasurementAverages;
import com.algebra.iot.server.dao.repo.AirMeasurementAveragesRepository;
import com.algebra.iot.server.dao.repo.AirMeasurementRepository;
import com.algebra.iot.server.dao.repo.PlugMeasurementAveragesRepository;
import com.algebra.iot.server.dao.repo.PlugMeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
public class ScheduledMeasurementCalculator {

    @Autowired
    AirMeasurementRepository airMeasurementRepository;

    @Autowired
    PlugMeasurementRepository plugMeasurementRepository;

    @Autowired
    AirMeasurementAveragesRepository airMeasurementAveragesRepository;

    @Autowired
    PlugMeasurementAveragesRepository plugMeasurementAveragesRepository;

    @Scheduled(cron = "0 0 * ? * *")
    public void calculateHourlyAverageAirMeasurements() throws Exception {
        Set<AirMeasurement> measurements=airMeasurementRepository.findByDateBetween(new Date().getTime()-3600000 ,new Date().getTime());

        if(measurements.isEmpty()){
            airMeasurementAveragesRepository.save(new AirMeasurementAverages(null,null,null,"HOURLY",new Date().getTime(),1));
            return;
        }

        Double tempSum=0.0;
        Double co2Sum=0.0;
        Double humSum=0.0;
        for (AirMeasurement m:measurements) {
            tempSum+=m.getTemparature();
            co2Sum+=m.getCO2();
            humSum+=m.getRelativeHumidity();
        }

        AirMeasurementAverages averages=new AirMeasurementAverages(co2Sum/measurements.size(),
                tempSum/measurements.size(),
                humSum/measurements.size(),
                "HOURLY",
                new Date().getTime(),
                1);

        airMeasurementAveragesRepository.save(averages);
    }

    @Scheduled(cron = "0 0 * ? * *")
    public void calculateHourlyAveragePlugMeasurements() throws Exception {
        Set<PlugMeasurement> measurements=plugMeasurementRepository.findByDateBetween(new Date().getTime()-3600000 ,new Date().getTime());

        if(measurements.isEmpty()){
            plugMeasurementAveragesRepository.save(new PlugMeasurementAverages(null, new Date().getTime(),"HOURLY",null));
            return;
        }

        Double powerSum=0.0;
        for (PlugMeasurement m:measurements) {
            powerSum+=m.getPower();
        }

        PlugMeasurementAverages averages=new PlugMeasurementAverages(powerSum/measurements.size(),
                new Date().getTime(),
                "HOURLY",
                null);

        plugMeasurementAveragesRepository.save(averages);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void calculateDailyAverageAirMeasurements() throws Exception {
        Set<AirMeasurement> measurements=airMeasurementRepository.findByDateBetween(new Date().getTime()-86400000,new Date().getTime());

        if(measurements.isEmpty()){
            airMeasurementAveragesRepository.save(new AirMeasurementAverages(null,null,null,"DAILY",new Date().getTime(),1));
            return;
        }

        Double tempSum=0.0;
        Double co2Sum=0.0;
        Double humSum=0.0;
        for (AirMeasurement m:measurements) {
            tempSum+=m.getTemparature();
            co2Sum+=m.getCO2();
            humSum+=m.getRelativeHumidity();
        }

        AirMeasurementAverages averages=new AirMeasurementAverages(co2Sum/measurements.size(),
                tempSum/measurements.size(),
                humSum/measurements.size(),
                "DAILY",
                new Date().getTime(),
                null);

        airMeasurementAveragesRepository.save(averages);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void calculateDailyAveragePlugMeasurements() throws Exception {
        PlugMeasurement measurement=plugMeasurementRepository.findFirstByOrderByIdDesc();
        Set<PlugMeasurement> measurements=plugMeasurementRepository.findByDateBetween(new Date().getTime()-86400000,new Date().getTime());

        if(measurements.isEmpty()){
            plugMeasurementAveragesRepository.save(new PlugMeasurementAverages(null, new Date().getTime(),"DAILY",null));
            return;
        }

        Double powerSum=0.0;
        for (PlugMeasurement m:measurements) {
            powerSum+=m.getPower();
        }

        PlugMeasurementAverages averages=new PlugMeasurementAverages(powerSum/measurements.size(),
                measurement.getDate(),
                "DAILY",
                measurement.getGateway());

        plugMeasurementAveragesRepository.save(averages);
    }

}
