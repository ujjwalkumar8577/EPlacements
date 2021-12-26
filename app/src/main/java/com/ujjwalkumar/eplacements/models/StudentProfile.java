package com.ujjwalkumar.eplacements.models;

import java.lang.reflect.Field;

public class StudentProfile {
    public String reg_no, name, course, branch, dob, email, skype_id, linkedin_id, gender, category,
    residential_status, guardian, present_address, permanent_address, maritial_status, state, country,
    photo, resume, status, remarks, school_10, board_10, school_12, board_12, project_title, project_desc,
    intern_title, intern_desc;
    public boolean physically_challenged;
    public int credits, year_10, year_12, backlogs;
    public float percent_10;
    public float percent_12;
    public float[] spi;
    public float cpi;

    public StudentProfile(String reg_no, String name, String course, String branch, String dob, String email, String skype_id, String linkedin_id, String gender, String category, String residential_status, String guardian, String present_address, String permanent_address, String maritial_status, String state, String country, String photo, String resume, String status, String remarks, String school_10, String board_10, String school_12, String board_12, String project_title, String project_desc, String intern_title, String intern_desc, boolean physically_challenged, int credits, int year_10, int year_12, int backlogs, float percent_10, float percent_12, float[] spi, float cpi) {
        this.reg_no = reg_no;
        this.name = name;
        this.course = course;
        this.branch = branch;
        this.dob = dob;
        this.email = email;
        this.skype_id = skype_id;
        this.linkedin_id = linkedin_id;
        this.gender = gender;
        this.category = category;
        this.residential_status = residential_status;
        this.guardian = guardian;
        this.present_address = present_address;
        this.permanent_address = permanent_address;
        this.maritial_status = maritial_status;
        this.state = state;
        this.country = country;
        this.photo = photo;
        this.resume = resume;
        this.status = status;
        this.remarks = remarks;
        this.school_10 = school_10;
        this.board_10 = board_10;
        this.school_12 = school_12;
        this.board_12 = board_12;
        this.project_title = project_title;
        this.project_desc = project_desc;
        this.intern_title = intern_title;
        this.intern_desc = intern_desc;
        this.physically_challenged = physically_challenged;
        this.credits = credits;
        this.year_10 = year_10;
        this.year_12 = year_12;
        this.backlogs = backlogs;
        this.percent_10 = percent_10;
        this.percent_12 = percent_12;
        this.spi = spi;
        this.cpi = cpi;
    }

    public String validate() {
        Field[] fields = StudentProfile.class.getDeclaredFields();
        try {
            for(Field field: fields) {
                if(field.get(this)==null && !isIgnoredField(field.getName()))
                    return (field.getName() + " required");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "validated";
    }

    public static boolean isIgnoredField(String key) {
        String[] IGNORED_FIELDS = {"status", "remarks", "credits", "spi"};
        for(String str: IGNORED_FIELDS) {
            if(key.equals(str))
                return true;
        }
        return false;
    }
}
