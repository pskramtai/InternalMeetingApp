package com.InternalMeetingApp.InternalMeetingApp.Services;



import com.InternalMeetingApp.InternalMeetingApp.Common.MeetingCategory;
import com.InternalMeetingApp.InternalMeetingApp.Common.MeetingType;
import com.InternalMeetingApp.InternalMeetingApp.Models.Meeting;

import java.util.Date;
import java.util.List;

public interface IMeetingService {
    void createMeeting(Meeting newMeeting);
    String deleteMeeting(Long person, String meetingName);
    String addPerson(String meetingName, Long personId, Date timeAdded);
    boolean removePerson(String meetingName, Long personId);
    List<Meeting> filterMeetings(String description, Long responsiblePerson, MeetingCategory meetingCategory, MeetingType meetingType, Date startDate, Date endDate, Integer minAttendees, Integer maxAttendees);
    Meeting getMeeting(String meetingName);
}
