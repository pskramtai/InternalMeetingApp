package com.InternalMeetingApp.InternalMeetingApp.Services;

import com.InternalMeetingApp.InternalMeetingApp.Common.MeetingCategory;
import com.InternalMeetingApp.InternalMeetingApp.Common.MeetingType;
import com.InternalMeetingApp.InternalMeetingApp.Models.Meeting;
import com.InternalMeetingApp.InternalMeetingApp.Repository.IMeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingServiceImpl implements IMeetingService {

    @Autowired
    IMeetingRepository repository;

    @Override
    public void createMeeting(Meeting newMeeting) {
        newMeeting.getPeople().add(newMeeting.getResponsiblePerson());
        repository.saveMeeting(newMeeting);
    }

    @Override
    public String deleteMeeting(Long person, String meetingName) {
        Meeting meeting = repository.getMeeting(meetingName);

        if(meeting == null){
            return "Meeting doesn't exist.";
        }

        if(meeting.getResponsiblePerson() == person){
            repository.deleteMeeting(meeting);
            return "Meeting deleted.";
        }

        return "Only responsible person can delete a meeting.";
    }

    @Override
    public String addPerson(String meetingName, Long person, Date timeAdded) {
        Meeting meeting = repository.getMeeting(meetingName);
        List<Meeting> meetings = repository.getAllMeetings();

        if(meeting == null){
            return "No such meeting.";
        }

        String message = meetings
                .stream()
                .anyMatch(m -> m.getPeople().contains(person)
                        && !m.getName().equals(meetingName)
                        && meeting.getStartDate().before(m.getEndDate())
                        && m.getStartDate().before(meeting.getEndDate()))
                ? "This person has a meeting at this time." : "";

        if(meeting.getPeople().contains(person)){
            message = "This person is already in this meeting.";
            return message;
        }

        meeting.getPeople().add(person);
        repository.updateMeeting(meeting, meetingName);

        return message;
    }

    @Override
    public boolean removePerson(String meetingName, Long person) {
        Meeting meeting = repository.getMeeting(meetingName);

        if(meeting == null){
            return false;
        }

        if(meeting.getResponsiblePerson() == person){
            return false;
        }

        meeting.getPeople().remove(person);
        repository.updateMeeting(meeting, meetingName);

        return true;
    }

    @Override
    public List<Meeting> filterMeetings(String description, Long responsiblePerson, MeetingCategory meetingCategory, MeetingType meetingType, Date startDate, Date endDate, Integer minAttendees, Integer maxAttendees) {
        List<Meeting> meetings = repository.getAllMeetings();

        if(description != null){
            meetings = meetings.stream().filter(m -> m.getDescription().toLowerCase().contains(description.toLowerCase())).collect(Collectors.toList());
        }

        if(responsiblePerson != null){
            meetings = meetings.stream().filter(m -> m.getResponsiblePerson().equals(responsiblePerson)).collect(Collectors.toList());
        }

        if(meetingCategory != null){
            meetings = meetings.stream().filter(m -> m.getCategory().equals(meetingCategory)).collect(Collectors.toList());
        }

        if(meetingType != null){
            meetings = meetings.stream().filter(m -> m.getType().equals(meetingType)).collect(Collectors.toList());
        }

        if(startDate != null){
            meetings = meetings.stream().filter(m -> m.getStartDate().after(startDate) || m.getStartDate().equals(startDate)).collect(Collectors.toList());
        }

        if(endDate != null){
            meetings = meetings.stream().filter(m -> m.getEndDate().before(endDate) || m.getEndDate().equals(endDate)).collect(Collectors.toList());
        }

        if(minAttendees != null){
            meetings = meetings.stream().filter(m -> m.getPeople().size() >= minAttendees).collect(Collectors.toList());
        }

        if(maxAttendees != null){
            meetings = meetings.stream().filter(m -> m.getPeople().size() <= maxAttendees).collect(Collectors.toList());
        }

        return meetings;
    }

    @Override
    public Meeting getMeeting(String meetingName) {
        return repository.getMeeting(meetingName);
    }
}
