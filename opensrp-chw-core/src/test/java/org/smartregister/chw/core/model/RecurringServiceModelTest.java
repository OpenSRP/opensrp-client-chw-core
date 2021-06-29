package org.smartregister.chw.core.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecurringServiceModelTest {
    private List<Alert> alerts;
    private Map<String, List<ServiceType>> serviceTypes;
    private List<ServiceRecord> serviceRecords;


    @Before
    public void setUp() {
        alerts = new ArrayList<>();
        serviceTypes = new LinkedHashMap<>();
        serviceRecords = new ArrayList<>();
    }
    @Test
    public void testGetAlerts(){
        RecurringServiceModel recurringServiceModel = new RecurringServiceModel(alerts, serviceTypes, serviceRecords);
        Assert.assertEquals(alerts, recurringServiceModel.getAlerts());
    }
    @Test
    public void testGetServiceTypes(){
        RecurringServiceModel recurringServiceModel = new RecurringServiceModel(alerts, serviceTypes, serviceRecords);
        Assert.assertEquals(serviceTypes, recurringServiceModel.getServiceTypes());
    }
    @Test
    public void tesGetServiceRecords(){
        RecurringServiceModel recurringServiceModel = new RecurringServiceModel(alerts, serviceTypes, serviceRecords);
        Assert.assertEquals(serviceRecords, recurringServiceModel.getServiceRecords());
    }

}