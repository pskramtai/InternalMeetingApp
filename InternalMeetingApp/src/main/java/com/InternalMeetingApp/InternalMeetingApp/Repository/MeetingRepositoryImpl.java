package com.InternalMeetingApp.InternalMeetingApp.Repository;

import com.InternalMeetingApp.InternalMeetingApp.Models.Meeting;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MeetingRepositoryImpl implements IMeetingRepository {

    @Autowired
    private FileRepository fileRepository;

    @Override
    public void saveMeeting(Meeting newMeeting) {
        Gson gson = new Gson();
        List<Meeting> meetings = getAllMeetings();

        meetings.add(newMeeting);

        fileRepository.writeToFile(gson.toJson(meetings));
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        List<Meeting> meetings = getAllMeetings();
        Gson gson = new Gson();

        meetings = meetings.stream().filter(m -> !m.getName().equals(meeting.getName())).collect(Collectors.toList());

        fileRepository.writeToFile(gson.toJson(meetings));
    }

    @Override
    public void updateMeeting(Meeting meeting, String meetingName) {
        List<Meeting> meetings = getAllMeetings();
        Gson gson = new Gson();

        Optional<Meeting> meetingToChange = meetings.stream().filter(m -> m.getName().equals(meetingName)).findFirst();

        if(meetingToChange.isEmpty()){
            return;
        }

        meetings.remove(meetingToChange.get());
        meetings.add(meeting);

        fileRepository.writeToFile(gson.toJson(meetings));
    }

    @Override
    public List<Meeting> getAllMeetings() {
        Gson gson = new Gson();
        String json = fileRepository.readFromFile();

        return gson.fromJson(json, new TypeToken<ArrayList<Meeting>>(){}.getType());
    }

    @Override
    public Meeting getMeeting(String meetingName) {
        List<Meeting> meetings = getAllMeetings();

        Optional<Meeting> meeting = meetings.stream().filter(m -> m.getName().equals(meetingName)).findFirst();

        return meeting.orElse(null);

    }


}
