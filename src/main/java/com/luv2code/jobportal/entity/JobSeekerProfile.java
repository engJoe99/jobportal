package com.luv2code.jobportal.entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "job_seeker_profile")
public class JobSeekerProfile {

    @Id
    private int userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users userId;

    private String firstName;
    private String city;
    private String state;
    private String Country;
    private String workAuthorization;
    private String resume;

    @Column(nullable = true, length = 64)
    private String profilePhoto;

    @OneToMany(targetEntity = Skills.class,
                cascade = CascadeType.ALL,
                mappedBy = "jobSeekerProfile")
    private List<Skills> skills;

    public JobSeekerProfile() {
    }

    public JobSeekerProfile(Users userId) {
        this.userId = userId;
    }

    public JobSeekerProfile(int userAccountId, Users userId, String firstName,
                            String city, String state, String country, String workAuthorization,
                            String resume, String profilePhoto, List<Skills> skills) {

        this.userAccountId = userAccountId;
        this.userId = userId;
        this.firstName = firstName;
        this.city = city;
        this.state = state;
        Country = country;
        this.workAuthorization = workAuthorization;
        this.resume = resume;
        this.profilePhoto = profilePhoto;
        this.skills = skills;
    }

    public int getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(int userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getWorkAuthorization() {
        return workAuthorization;
    }

    public void setWorkAuthorization(String workAuthorization) {
        this.workAuthorization = workAuthorization;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public List<Skills> getSkills() {
        return skills;
    }

    public void setSkills(List<Skills> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "JobSeekerProfile{" +
                "userAccountId=" + userAccountId +
                ", userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", Country='" + Country + '\'' +
                ", workAuthorization='" + workAuthorization + '\'' +
                ", resume='" + resume + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", skills=" + skills +
                '}';
    }


}
