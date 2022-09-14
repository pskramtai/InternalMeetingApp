package com.InternalMeetingApp.InternalMeetingApp.Repository;

import com.InternalMeetingApp.InternalMeetingApp.Common.MeetingCategory;
import com.InternalMeetingApp.InternalMeetingApp.Common.MeetingType;
import com.InternalMeetingApp.InternalMeetingApp.Models.Meeting;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MeetingRepositoryImplTest {

    @InjectMocks
    MeetingRepositoryImpl repository;

    @Mock
    FileRepository fileRepository;

    private final Gson gson = new Gson();

    private List<Meeting> meetings;
    private Meeting firstMeeting;
    private Meeting secondMeeting;
    private Meeting thirdMeeting;
    private Meeting fourthMeeting;



    @SneakyThrows
    @BeforeEach
    void setUp() {
        List<Meeting> meetings = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Meeting firstMeeting = new Meeting("pirmas meetas", 1L, "aprasymas", MeetingCategory.CodeMonkey, MeetingType.InPerson, dateFormat.parse("2022-09-11 13:00:00"), dateFormat.parse("2022-09-11 17:00:00"));
        Meeting secondMeeting = new Meeting("antras meetas", 1L, "aprasymas", MeetingCategory.Hub, MeetingType.InPerson, dateFormat.parse("2022-09-11 12:00:00"), dateFormat.parse("2022-09-11 14:00:00"));
        Meeting thirdMeeting = new Meeting("trecias meetas", 2L, "aprasymas", MeetingCategory.CodeMonkey, MeetingType.Live, dateFormat.parse("2022-09-11 18:00:00"), dateFormat.parse("2022-09-11 19:00:00"));
        Meeting fourthMeeting = new Meeting("ketvirtas meetas", 2L, "aprasymas", MeetingCategory.CodeMonkey, MeetingType.Live, dateFormat.parse("2022-09-11 18:00:00"), dateFormat.parse("2022-09-11 19:00:00"));


        secondMeeting.getPeople().add(1L);

        meetings.add(firstMeeting);
        meetings.add(secondMeeting);
        meetings.add(thirdMeeting);

        this.meetings = meetings;
        this.firstMeeting = firstMeeting;
        this.secondMeeting = secondMeeting;
        this.thirdMeeting = thirdMeeting;
        this.fourthMeeting = fourthMeeting;
    }

    @Test
    void saveMeeting() {

        List<Meeting> meetings = this.meetings;

        Mockito.when(fileRepository.readFromFile()).thenReturn(gson.toJson(meetings));

        meetings.add(fourthMeeting);

        repository.saveMeeting(fourthMeeting);

        Mockito.verify(fileRepository, Mockito.times(1)).writeToFile(gson.toJson(meetings));
    }

    @Test
    void deleteMeeting() {
        List<Meeting> meetings = this.meetings;

        Mockito.when(fileRepository.readFromFile()).thenReturn(gson.toJson(meetings));

        meetings.remove(thirdMeeting);

        repository.deleteMeeting(thirdMeeting);

        Mockito.verify(fileRepository, Mockito.times(1)).writeToFile(gson.toJson(meetings));
    }

    @Test
    void updateMeeting() {
        List<Meeting> meetings = this.meetings;

        Mockito.when(fileRepository.readFromFile()).thenReturn(gson.toJson(meetings));

        Meeting meetingToChange = meetings.get(0);
        meetingToChange.setDescription("test description");
        meetings.remove(0);
        meetings.add(meetingToChange);

        repository.updateMeeting(meetingToChange, "pirmas meetas");

        Mockito.verify(fileRepository, Mockito.times(1)).writeToFile(gson.toJson(meetings));
    }

    @Test
    void getAllMeetings() {
        List<Meeting> meetings = this.meetings;

        Mockito.when(fileRepository.readFromFile()).thenReturn(gson.toJson(meetings));

        List<Meeting> resMeetings = repository.getAllMeetings();

        for(int i = 0; i<resMeetings.size(); i++){
            assertEquals(meetings.get(i).getName(), resMeetings.get(i).getName());
        }
    }

    @Test
    void getMeeting() {
        List<Meeting> meetings = this.meetings;

        Mockito.when(fileRepository.readFromFile()).thenReturn(gson.toJson(meetings));

        Meeting testMeeting = meetings.get(0);

        assertEquals(testMeeting.getName(), repository.getMeeting("pirmas meetas").getName());
    }
}