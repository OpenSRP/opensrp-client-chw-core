package org.smartregister.chw.core.dao;

import android.content.Context;

import net.sqlcipher.MatrixCursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.core.domain.NotificationRecord;
import org.smartregister.repository.Repository;

public class ChwNotificationDaoTest extends ChwNotificationDao {

    @Mock
    private Repository repository;

    @Mock
    private SQLiteDatabase database;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setRepository(repository);
    }

    @Test
    public void getSickChildFollowUpRecord() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"full_name", "cg_full_name", "village", "diagnosis", "results", "visit_date"});
        matrixCursor.addRow(new Object[]{"Raila Odinga", "Ida Odinga", "Bondo", "Has corona", "Positive Corona", "2020-05-21 17:16:31"});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.any(), Mockito.any());
        NotificationRecord sickChildFollowUpRecord = ChwNotificationDao.getSickChildFollowUpRecord("123456");
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertNotNull(sickChildFollowUpRecord);
        Assert.assertEquals(sickChildFollowUpRecord.getCareGiverName(), "Ida Odinga");
        Assert.assertEquals(sickChildFollowUpRecord.getDiagnosis(), "Has corona");
        Assert.assertEquals(sickChildFollowUpRecord.getVisitDate(), "21-05-2020");
        Assert.assertEquals(sickChildFollowUpRecord.getResults(), "Positive Corona");
        Assert.assertEquals(sickChildFollowUpRecord.getClientName(), "Raila Odinga");
        Assert.assertEquals(sickChildFollowUpRecord.getVillage(), "Bondo");
    }

    @Test
    public void getAncPncDangerSignsOutcomeRecord() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"full_name", "village", "danger_signs_present", "action_taken", "visit_date"});
        matrixCursor.addRow(new Object[]{"Martha Karua", "Kiambaa", "Fistula", "Referred", "2020-05-21 17:16:31"});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.any(), Mockito.any());
        NotificationRecord ancPncDangerSignsOutcomeRecord = ChwNotificationDao.getAncPncDangerSignsOutcomeRecord("123456", "ec_pregnancy_outcome");
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertNotNull(ancPncDangerSignsOutcomeRecord);
        Assert.assertEquals(ancPncDangerSignsOutcomeRecord.getActionTaken(), "Referred");
        Assert.assertEquals(ancPncDangerSignsOutcomeRecord.getVisitDate(), "21-05-2020");
        Assert.assertEquals(ancPncDangerSignsOutcomeRecord.getDangerSigns(), "Fistula");
        Assert.assertEquals(ancPncDangerSignsOutcomeRecord.getClientName(), "Martha Karua");
        Assert.assertEquals(ancPncDangerSignsOutcomeRecord.getVillage(), "Kiambaa");
    }

    @Test
    public void getMalariaFollowUpRecord() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"full_name", "village", "action_taken", "results", "visit_date"});
        matrixCursor.addRow(new Object[]{"General Mwathethe", "Matuga", "Referred", "Malaria positive", "2020-05-21 17:16:31"});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.any(), Mockito.any());
        NotificationRecord ancPncDangerSignsOutcomeRecord = ChwNotificationDao.getMalariaFollowUpRecord("123456");
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertNotNull(ancPncDangerSignsOutcomeRecord);
        Assert.assertEquals(ancPncDangerSignsOutcomeRecord.getActionTaken(), "Referred");
        Assert.assertEquals(ancPncDangerSignsOutcomeRecord.getVisitDate(), "21-05-2020");
        Assert.assertEquals(ancPncDangerSignsOutcomeRecord.getResults(), "Malaria positive");
        Assert.assertEquals(ancPncDangerSignsOutcomeRecord.getClientName(), "General Mwathethe");
        Assert.assertEquals(ancPncDangerSignsOutcomeRecord.getVillage(), "Matuga");
    }

    @Test
    public void getFamilyPlanningRecord() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"full_name", "village", "method", "visit_date"});
        matrixCursor.addRow(new Object[]{"Moses Kuria", "Nyeri", "Male castration", "2020-05-21 17:16:31"});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.any(), Mockito.any());
        NotificationRecord familyPlanningRecord = ChwNotificationDao.getFamilyPlanningRecord("123456");
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertNotNull(familyPlanningRecord);
        Assert.assertEquals(familyPlanningRecord.getVisitDate(), "21-05-2020");
        Assert.assertEquals(familyPlanningRecord.getMethod(), "Male castration");
        Assert.assertEquals(familyPlanningRecord.getClientName(), "Moses Kuria");
        Assert.assertEquals(familyPlanningRecord.getVillage(), "Nyeri");
    }

    @Test
    public void isMarkedAsDone() {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"count"});
        matrixCursor.addRow(new Object[]{"0"});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.any(), Mockito.any());
        Context context = Mockito.mock(Context.class);
        Mockito.doReturn("Sick Child").when(context).getString(Mockito.anyInt());
        boolean markedAsDone = ChwNotificationDao.isMarkedAsDone(context, "123456", "Sick Child");
        Assert.assertFalse(markedAsDone);
    }

    @Test
    public void getHivTBOutcomeRecord() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"full_name", "village","problem", "action_taken", "test_results", "visit_date"});
        matrixCursor.addRow(new Object[]{"General Mwathethe", "Matuga", "Coughing alot","Referred", "TB positive", "2020-05-21 17:16:31"});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.any(), Mockito.any());
        NotificationRecord tbOutcomeNotificationRecord = ChwNotificationDao.getHivTBOutcomeRecord("123456","ec_hiv_outcome");
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertNotNull(tbOutcomeNotificationRecord);
        Assert.assertEquals(tbOutcomeNotificationRecord.getActionTaken(), "Referred");
        Assert.assertEquals(tbOutcomeNotificationRecord.getVisitDate(), "21-05-2020");
        Assert.assertEquals(tbOutcomeNotificationRecord.getResults(), "TB positive");
        Assert.assertEquals(tbOutcomeNotificationRecord.getClientName(), "General Mwathethe");
        Assert.assertEquals(tbOutcomeNotificationRecord.getVillage(), "Matuga");
    }

    @Test
    public void getNotYetDoneReferrals() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"full_name", "dob","village","notification_date", "phone_number", "base_entity_id"});
        matrixCursor.addRow(new Object[]{"General Mwathethe", "1990-05-21 00:00:00","Matuga", "2020-05-21 17:16:31","0798000000", "123456"});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.any(), Mockito.any());
        NotificationRecord notYetDoneReferralNotificationRecord = ChwNotificationDao.getNotYetDoneReferral("123456");
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertNotNull(notYetDoneReferralNotificationRecord);
        Assert.assertEquals(notYetDoneReferralNotificationRecord.getClientName(), "General Mwathethe");
        Assert.assertEquals(notYetDoneReferralNotificationRecord.getVillage(), "Matuga");
        Assert.assertEquals(notYetDoneReferralNotificationRecord.getClientDateOfBirth(), "1990-05-21 00:00:00");
        Assert.assertEquals(notYetDoneReferralNotificationRecord.getNotificationDate(), "2020-05-21 17:16:31");
    }
}