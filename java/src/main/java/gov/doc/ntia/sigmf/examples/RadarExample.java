package gov.doc.ntia.sigmf.examples;

import gov.doc.ntia.sigmf.*;
import gov.doc.ntia.sigmf.ext.annotation.algorithm.FrequencyDomainDetection;
import gov.doc.ntia.sigmf.ext.annotation.environment.Environment;
import gov.doc.ntia.sigmf.ext.annotation.sensor.CalibrationAnnotation;
import gov.doc.ntia.sigmf.ext.annotation.sensor.SensorAnnotation;
import gov.doc.ntia.sigmf.ext.global.core.Antenna;
import gov.doc.ntia.sigmf.ext.global.core.HardwareSpec;
import gov.doc.ntia.sigmf.ext.global.core.Measurement;
import gov.doc.ntia.sigmf.ext.global.sensor.*;

import java.util.ArrayList;
import java.util.Calendar;

public class RadarExample  implements Example {


    @Override
    public MetaDoc getExample() {
        MetaDoc metaDoc = new MetaDoc();
        Global global = ExampleUtils.getGlobal(Calendar.getInstance().getTime());
        global.setExtensions(getExtensions());
        Sensor sensor = getSensor();
        global.setSensor(getSensor());
        global.setMeasurement(getMeasurement());
        metaDoc.setGlobal(global);
        metaDoc.setAnnotations(getAnnotations());
        metaDoc.setCaptures(getCaptures());
        return metaDoc;
    }

    public static Measurement getMeasurement(){
        Measurement measurement = new Measurement();
        measurement.setCenterFrequency(3.5501875E9);
        measurement.setLowFrequency(3.45021875E9);
        measurement.setHighFrequency(3.65015625E9);
        measurement.setDomain("frequency");
        measurement.setMeasurementType("scan");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018,02, 01, 07, 01, 00);
        measurement.setStartTime(calendar.getTime());
        calendar.add(Calendar.MILLISECOND, 30);
        measurement.setEndTime(calendar.getTime());
        return measurement;
    }

    public static Extensions getExtensions(){
        Extensions extensions = new Extensions();
        Extension sensorExtension = new Extension();
        sensorExtension.setName("ntia-sensor");
        sensorExtension.setVersion("v1.0.0");

        Extension algorithmExtension = new Extension();
        algorithmExtension.setName("ntia-slgorithm");
        algorithmExtension.setVersion("v1.0.0");

        Extension envExtension = new Extension();
        envExtension.setName("ntia-environment");
        envExtension.setVersion("v1.0.0");

        extensions.addExtension(sensorExtension);
        extensions.addExtension(algorithmExtension);
        extensions.addExtension(envExtension);
        return extensions;
    }

    public static Sensor getSensor(){
        Sensor sensor = new Sensor();
        sensor.setId("Radar_Sensor_1");
        sensor.setAntenna(getAntenna());
        sensor.setPreselector(getPreselector());
        return sensor;
    }

    private static Antenna getAntenna(){
        Antenna antenna = new Antenna();
        HardwareSpec antSpec = new HardwareSpec();
        antSpec.setModel("ARA BSB-26");
        antSpec.setDescription("RF antenna ideally suited for reception of signals on the horizon for nautical and broadband surveillance applications");
        antenna.setAntennaSpec(antSpec);
        antenna.setType("omni-directional");
        antenna.setLowFrequency(2.0E9);
        antenna.setHighFrequency(6.0E9);
        antenna.setGain(0d);
        antenna.setPolarization("slant");
        antenna.setCrossPolarDiscrimination(13d);
        antenna.setHorizontalBeamWidth(360d);
        antenna.setVerticalBeamWidth(68.38);
        antenna.setVoltageStandingWaveRatio(2d);
        antenna.setCableLoss(.62);
        antenna.setSteerable(Boolean.FALSE);
        antenna.setAzimuthAngle(90d);
        antenna.setElevationAngle(0d);
        antenna.setMobile(Boolean.FALSE);
        return antenna;

    }

    private static Preselector getPreselector(){
        Preselector preselector = new Preselector();
        HardwareSpec calSourceSpec = new HardwareSpec();
        calSourceSpec.setId("MY53400510");
        calSourceSpec.setModel("Keysight 346B");
        calSourceSpec.setSupplementalInformation("https://www.keysight.com/en/pd-1000001299%3Aepsg%3Apro-pn-346B/noise-source-10-mhz-to-18-ghz-nominal-enr-15-db?cc=US&lc=eng");

        CalSource calSource = new CalSource();
        calSource.setCalSourceSpec(calSourceSpec);

        preselector.setCalSource(calSource);
        Amplifier amplifier = new Amplifier();
        HardwareSpec lnaSpec = new HardwareSpec();
        lnaSpec.setId("1904043");
        lnaSpec.setModel("MITEQ AFS3-02000400-30-25P-6");
        lnaSpec.setSupplementalInformation("https://nardamiteq.com/docs/MITEQ_Amplifier-AFS.JS_c41.pdf");
        amplifier.setAmplifierSpec(lnaSpec);
        amplifier.setNoiseFigure(2.76);
        amplifier.setGain(30.61);
        amplifier.setMaxPower(13d);

        Filter filter  = new Filter();
        HardwareSpec filterSpec = new HardwareSpec();
        filterSpec.setId("13FV40-00014");
        filterSpec.setModel("K&L 13FV40-3550/U200-o/o");
        filterSpec.setSupplementalInformation("http://www.klfilterwizard.com/klfwpart.aspx?FWS=1112001&PN=13FV40-3550%2fU200-O%2fO");
        filter.setFilterSpec(filterSpec);

        filter.setHighFrequencyPassband(3670000000d);
        filter.setLowFrequencyPassband(3430000000d);
        filter.setHighFrequencyStopband(3710000000d);
        filter.setLowFrequencyStopband(3390000000d);

        Filter[] filters = new Filter[1];
        filters[0] = filter;
        preselector.setFilters(filters);

        Amplifier[] amplifiers = new Amplifier[1];
        amplifiers[0] = amplifier;
        preselector.setAmplifiers(amplifiers);

        RFPath path1 = new RFPath();
        path1.setFilterId("13FV40-00014");
        path1.setAmplifierId("1904043");
        path1.setCalSourceId("Calibrated noise source");

        RFPath[] paths = new RFPath[1];
        paths[0] = path1;

        preselector.setRfPaths(paths);
        return preselector;

    }

    private static ArrayList<Capture> getCaptures(){
        ArrayList<Capture> captures = new ArrayList<>();
        Capture capture = new Capture();
        capture.setFrequency(3.5501875E9);
        Calendar calendar = Calendar.getInstance();
        //"2018-02-01T07:01:00.000001Z"
        calendar.set(2018,02, 01, 07, 01, 00);
        capture.setDateTime(calendar.getTime());
        capture.setSampleStart(0);
        captures.add(capture);

        return captures;
    }

    private static ArrayList<Annotation> getAnnotations(){
        ArrayList<Annotation> annotations = new ArrayList<>();
        CalibrationAnnotation calibrationAnnotation = new CalibrationAnnotation();
        calibrationAnnotation.setSampleStart(0l);
        calibrationAnnotation.setComment("Calibration is done every 6 hours.");
        calibrationAnnotation.setNoiseFigureSensor(9.892);
        calibrationAnnotation.setMeanNoisePowerSensor(-92.21948908296943);
        calibrationAnnotation.setTemperature(18.556);
        calibrationAnnotation.setGainPreselector(25.931);
        annotations.add(calibrationAnnotation);

        SensorAnnotation sensorAnnotation = new SensorAnnotation();
        sensorAnnotation.setAttenuationSettingSigan(6.0);
        sensorAnnotation.setOverload(false);
        sensorAnnotation.setRfPathIndex(0);

        Environment environment = new Environment();
        environment.setCategory("Outside. Coastal");
        environment.setSampleStart(0l);
        annotations.add(environment);

        FrequencyDomainDetection fdd = new FrequencyDomainDetection();
        fdd.setDetector("fft_max_power");
        fdd.setNumberOfFfts(458);
        fdd.setNumberOfSamplesInFft(64);
        fdd.setWindow("Gauss-top");
        fdd.setEquivalentNoiseBandwidth(962500.0);
        fdd.setFrequencyStep(437500.0);

        annotations.add(fdd );
        return annotations;
    }



}
