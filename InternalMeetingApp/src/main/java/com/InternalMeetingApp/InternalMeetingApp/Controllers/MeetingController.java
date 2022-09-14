package com.InternalMeetingApp.InternalMeetingApp.Controllers;

import com.InternalMeetingApp.InternalMeetingApp.Common.MeetingCategory;
import com.InternalMeetingApp.InternalMeetingApp.Common.MeetingType;
import com.InternalMeetingApp.InternalMeetingApp.Models.Meeting;
import com.InternalMeetingApp.InternalMeetingApp.Services.IMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class MeetingController {

    @Autowired
    IMeetingService service;

    @PostMapping("/meeting")
    Meeting addMeeting(@RequestBody Meeting newMeeting){
        service.createMeeting(newMeeting);
        return newMeeting;
    }

    @PutMapping("/meeting/{meetingName}")
    String addPerson(@PathVariable String meetingName, @RequestParam Long person,
                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date timeAdded){

        String response = service.addPerson(meetingName, person, timeAdded);

        if(response.isEmpty()){
            return "Person " + person + " added to " + meetingName + " at " + timeAdded.toString();
        }
        else{
            return response;
        }
    }

    @PutMapping("/meeting/{meetingName}/{person}")
    String removePerson(@PathVariable String meetingName, @PathVariable Long person){

        if(service.removePerson(meetingName, person)){
            return "Person was removed from the meeting.";
        }
        else{
            return "Person could not be removed.";
        }
    }

    @DeleteMapping("/meeting/{meetingName}")
    String deleteMeeting(@PathVariable String meetingName, @RequestParam Long person){
        String response = service.deleteMeeting(person, meetingName);
        return response;
    }

    @GetMapping("/meetings")
    List<Meeting> getMeetings(@RequestParam(required = false) String description,
                              @RequestParam(required = false) Long responsiblePerson,
                              @RequestParam(required = false) MeetingCategory meetingCategory,
                              @RequestParam(required = false) MeetingType meetingType,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                              @RequestParam(required = false) Integer minAttendees,
                              @RequestParam(required = false) Integer maxAttendees)
    {
        List<Meeting> filteredMeetings = service.filterMeetings(description,responsiblePerson,meetingCategory,meetingType,startDate,endDate,minAttendees,maxAttendees);

        return filteredMeetings;
    }
}
