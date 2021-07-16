package org.smartregister.chw.core.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VaccineTaskModelTest {
    private String vaccineGroupName;
    private VaccineGroup groupMap;
    private DateTime anchorDate;
    private List<Alert> alerts;
    private List<Vaccine> vaccines;
    private Map<String, Date> receivedVaccines;
    private List<Map<String, Object>> scheduleList;
    private ArrayList<VaccineWrapper> notGivenVaccine;
    private VaccineTaskModel vaccineTaskModel;

    @Before
    public void setUp(){
       vaccineGroupName = "some-vaccine-group";
       groupMap = new VaccineGroup();
       DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
       anchorDate = formatter.parseDateTime("01/01/2021");
       alerts = new ArrayList<>();
       vaccines = new ArrayList<>();
       notGivenVaccine = new ArrayList<VaccineWrapper>();
       vaccineTaskModel = new VaccineTaskModel();
       scheduleList = new ArrayList<>();
       receivedVaccines = new HashMap<>();
    }

    @Test
    public void testVaccineGroupNameSetterAndGetter(){
        vaccineTaskModel.setVaccineGroupName(vaccineGroupName);
        Assert.assertEquals(vaccineGroupName, vaccineTaskModel.getVaccineGroupName());
    }

    @Test
    public void testAnchorDateSetterAndGetter(){
        vaccineTaskModel.setAnchorDate(anchorDate);
        Assert.assertEquals(anchorDate,vaccineTaskModel.getAnchorDate());
    }

    @Test
    public void testGroupMapSetterAndGetters(){
        vaccineTaskModel.setGroupMap(groupMap);
        Assert.assertEquals(groupMap, vaccineTaskModel.getGroupMap());
    }

    @Test
    public void testGetAlertsMapDoesNotReturnNull(){
        Assert.assertNotNull(vaccineTaskModel.getAlertsMap());
    }

    @Test
    public void testVaccineNotGivenSetterAndGetter(){
        vaccineTaskModel.setNotGivenVaccine(notGivenVaccine);
        Assert.assertEquals(notGivenVaccine,vaccineTaskModel.getNotGivenVaccine());
    }

    @Test
    public void testVaccineScheduleListGetterAndSetter(){
        vaccineTaskModel.setScheduleList(scheduleList);
        Assert.assertEquals(scheduleList, vaccineTaskModel.getScheduleList());
    }

    @Test
    public void testReceivedVaccinesSetterAndGetter(){
        vaccineTaskModel.setReceivedVaccines(receivedVaccines);
        Assert.assertEquals(receivedVaccines, vaccineTaskModel.getReceivedVaccines());
    }

    @Test
    public void testAlertsSetterAndGetter(){
        vaccineTaskModel.setAlerts(alerts);
        Assert.assertEquals(alerts, vaccineTaskModel.getAlerts());
    }

    @Test
    public void testVaccinesSetterAndGetter(){
        vaccineTaskModel.setVaccines(vaccines);
        Assert.assertEquals(vaccines, vaccineTaskModel.getVaccines());
    }

}
