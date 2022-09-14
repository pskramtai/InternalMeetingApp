package com.InternalMeetingApp.InternalMeetingApp.Repository;

import com.InternalMeetingApp.InternalMeetingApp.Models.Meeting;

import java.util.List;

public interface IMeetingRepository {
    void saveMeeting(Meeting newMeeting);
    void deleteMeeting(Meeting meeting);
    void updateMeeting(Meeting meeting, String meetingName);
    List<Meeting> getAllMeetings();
    Meeting getMeeting(String meetingName);
}
