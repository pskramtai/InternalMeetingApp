package com.InternalMeetingApp.InternalMeetingApp.Services;

import com.InternalMeetingApp.InternalMeetingApp.Common.MeetingCategory;
import com.InternalMeetingApp.InternalMeetingApp.Common.MeetingType;
import com.InternalMeetingApp.InternalMeetingApp.Models.Meeting;
import com.InternalMeetingApp.InternalMeetingApp.Repository.IMeetingRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
class MeetingServiceImplTest {

    @InjectMocks
    MeetingServiceImpl service;

    @Mock
    IMeetingRepository repository;

    private List<Meeting> meetings;
    private Meeting firstMeeting;
    private Meeting secondMeeting;
    private Meeting thirdMeeting;

    @SneakyThrows
    @BeforeEach
    void setUp() {

        List<Meeting> meetings = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Meeting firstMeeting = new Meeting("pirmas meetas", 1L, "aprasymas", MeetingCategory.CodeMonkey, MeetingType.InPerson, dateFormat.parse("2022-09-11 13:00:00"), dateFormat.parse("2022-09-11 17:00:00"));
        Meeting secondMeeting = new Meeting("antras meetas", 1L, "aprasymas", MeetingCategory.Hub, MeetingType.InPerson, dateFormat.parse("2022-09-11 12:00:00"), dateFormat.parse("2022-09-11 14:00:00"));
        Meeting thirdMeeting = new Meeting("trecias meetas", 2L, "aprasymas", MeetingCategory.CodeMonkey, MeetingType.Live, dateFormat.parse("2022-09-11 18:00:00"), dateFormat.parse("2022-09-11 19:00:00"));

        secondMeeting.getPeople().add(1L);

        meetings.add(firstMeeting);
        meetings.add(secondMeeting);
        meetings.add(thirdMeeting);

        this.meetings = meetings;
        this.firstMeeting = firstMeeting;
        this.secondMeeting = secondMeeting;
        this.thirdMeeting = thirdMeeting;
    }

    @Test
    void createMeeting() {
        Meeting newMeeting = firstMeeting;

        service.createMeeting(newMeeting);

        newMeeting.getPeople().add(newMeeting.getResponsiblePerson());

        Mockito.verify(repository, Mockito.times(1)).saveMeeting(newMeeting);
    }

    @Test
    void addPerson_WhenMeetingsIntersect_ReturnWarning(){
        Mockito.when(repository.getAllMeetings()).thenReturn(meetings);
        Mockito.when(repository.getMeeting(anyString())).thenReturn(firstMeeting);

        String message = service.addPerson("pirmas meetas", 1L, new Date(System.currentTimeMillis()));



        assertEquals("This person has a meeting at this time.", message);
    }

    @Test
    void addPerson_ReturnNoWarning(){
        Mockito.when(repository.getAllMeetings()).thenReturn(meetings);
        Mockito.when(repository.getMeeting(anyString())).thenReturn(thirdMeeting);

        String message = service.addPerson("trecias meetas", 1L, new Date(System.currentTimeMillis()));

        assertEquals("", message);
    }

    @Test
    void addPerson_WhenPersonExistsInMeeting_ReturnWarning(){
        Mockito.when(repository.getAllMeetings()).thenReturn(meetings);
        Mockito.when(repository.getMeeting(anyString())).thenReturn(secondMeeting);

        String message = service.addPerson("antras meetas", 1L, new Date(System.currentTimeMillis()));

        assertEquals("This person is already in this meeting.", message);
    }

    @Test
    void addPerson_WhenMeetingDoesntExist_ReturnErrorMessage(){
        Mockito.when(repository.getAllMeetings()).thenReturn(meetings);
        Mockito.when(repository.getMeeting(anyString())).thenReturn(null);

        String message = service.addPerson("test", 1L,new Date(System.currentTimeMillis()));

        assertEquals("No such meeting.", message);
    }

    @Test
    void deleteMeeting_WhenMeetingDoesntExist_ReturnErrorMessage(){
        Mockito.when(repository.getMeeting(anyString())).thenReturn(null);

        String message = service.deleteMeeting(2L, "test");

        assertEquals("Meeting doesn't exist.", message);
    }

    @Test
    void deleteMeeting_WhenNotResponsiblePerson_ReturnErrorMessage(){
        Mockito.when(repository.getMeeting(anyString())).thenReturn(firstMeeting);

        String message = service.deleteMeeting(2L, "test");

        assertEquals("Only responsible person can delete a meeting.", message);
    }

    @Test
    void deleteMeeting_ReturnSuccessMessage(){
        Mockito.when(repository.getMeeting(anyString())).thenReturn(firstMeeting);

        String message = service.deleteMeeting(1L, "test");

        assertEquals("Meeting deleted.", message);
    }

    @Test
    void removePerson_WhenMeetingDoesntExist_ReturnFalse(){
        Mockito.when(repository.getMeeting(anyString())).thenReturn(null);

        boolean result = service.removePerson("test", 1L);

        assertFalse(result);
    }

    @Test
    void removePerson_WhenPersonIsResponsible_ReturnFalse(){
        Mockito.when(repository.getMeeting(anyString())).thenReturn(firstMeeting);

        boolean result = service.removePerson("test", 1L);

        assertFalse(result);
    }

    @Test
    void removePerson_ReturnTrue(){
        Mockito.when(repository.getMeeting(anyString())).thenReturn(secondMeeting);

        boolean result = service.removePerson("antras meetas", 2L);

        assertTrue(result);
    }

    @Test
    void filterMeetings_WhenDescriptionGiven_ReturnFilteredByDescription(){
        Mockito.when(repository.getAllMeetings()).thenReturn(meetings);

        List<Meeting> meetings = service.filterMeetings("aprasymas", null, null, null,null,null,null,null);

        for (Meeting meeting:meetings) {
            assertTrue(meeting.getDescription().toLowerCase().contains("aprasymas"));
        }
    }

    @Test
    void filterMeetings_WhenResponsiblePersonGiven_ReturnFilteredByResponsiblePerson(){
        Mockito.when(repository.getAllMeetings()).thenReturn(meetings);

        List<Meeting> meetings = service.filterMeetings(null, 2L, null, null,null,null,null,null);

        for (Meeting meeting:meetings) {
            assertTrue(meeting.getResponsiblePerson().equals(2L));
        }
    }

    @Test
    void filterMeetings_WhenMeetingCategoryGiven_ReturnFilteredByMeetingCategory(){
        Mockito.when(repository.getAllMeetings()).thenReturn(meetings);

        List<Meeting> meetings = service.filterMeetings(null, null, MeetingCategory.CodeMonkey, null,null,null,null,null);

        for (Meeting meeting:meetings) {
            assertTrue(meeting.getCategory().equals(MeetingCategory.CodeMonkey));
        }
    }

    @Test
    void filterMeetings_WhenTypeGiven_ReturnFilteredByType(){
        Mockito.when(repository.getAllMeetings()).thenReturn(meetings);

        List<Meeting> meetings = service.filterMeetings(null, null, null, MeetingType.Live,null,null,null,null);

        for (Meeting meeting:meetings) {
            assertTrue(meeting.getType().equals(MeetingType.Live));
        }
    }

    @SneakyThrows
    @Test
    void filterMeetings_WhenStartDateGiven_ReturnMeetingsAfterStartDate(){
        Mockito.when(repository.getAllMeetings()).thenReturn(meetings);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date startDate = dateFormat.parse("2022-09-11 13:00:00");
        List<Meeting> meetings = service.filterMeetings(null, null, null, null,startDate,null,null,null);

        for (Meeting meeting:meetings) {
            assertTrue(meeting.getStartDate().after(startDate) || meeting.getStartDate().equals(startDate));
        }
    }

    @SneakyThrows
    @Test
    void filterMeetings_WhenEndDateGiven_ReturnMeetingsBeforeEndDate(){
        Mockito.when(repository.getAllMeetings()).thenReturn(meetings);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date endDate = dateFormat.parse("2022-09-11 17:00:00");
        List<Meeting> meetings = service.filterMeetings(null, null, null, null,null,endDate,null,null);

        for (Meeting meeting:meetings) {
            assertTrue(meeting.getEndDate().before(endDate) || meeting.getEndDate().equals(endDate));
        }
    }

    @Test
    void filterMeetings_WhenMinAttendeesGiven_ReturnMeetingsWithMoreAttendees(){
        Mockito.when(repository.getAllMeetings()).thenReturn(meetings);

        List<Meeting> meetings = service.filterMeetings(null, null, null, null,null,null,1,null);

        for (Meeting meeting:meetings) {
            assertTrue(meeting.getPeople().size() >= 1);
        }
    }

    @Test
    void filterMeetings_WhenMaxAttendeesGiven_ReturnMeetingsWithLessAttendees(){
        Mockito.when(repository.getAllMeetings()).thenReturn(meetings);

        List<Meeting> meetings = service.filterMeetings(null, null, null, null,null,null,null,1);

        for (Meeting meeting:meetings) {
            assertTrue(meeting.getPeople().size() <= 1);
        }
    }
}