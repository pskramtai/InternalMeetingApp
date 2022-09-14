package com.InternalMeetingApp.InternalMeetingApp.Models;

import com.InternalMeetingApp.InternalMeetingApp.Common.MeetingCategory;
import com.InternalMeetingApp.InternalMeetingApp.Common.MeetingType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Meeting {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private Long responsiblePerson;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private MeetingCategory category;

    @Getter @Setter
    private MeetingType type;

    @Getter @Setter
    private Date startDate;

    @Getter @Setter
    private Date endDate;

    @Getter @Setter
    private List<Long> people;

    public Meeting(String name, Long responsiblePerson, String description, MeetingCategory category, MeetingType type, Date startDate, Date endDate) {
        this.name = name;
        this.responsiblePerson = responsiblePerson;
        this.description = description;
        this.category = category;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.people = new ArrayList<>();
    }
}
