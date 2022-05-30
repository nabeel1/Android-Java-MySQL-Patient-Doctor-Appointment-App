package com.example.shahidhussain.assignemnt2.classess;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class doctorJavaClass {
    private String id;
    private String name;
    private String email;
    private String city;
    private String num;
    private String hospitalname;
    private String starthour;
    private String startmin;
    private String endhour;
    private String endmin;
    private String timedifference;
    private String totalrate;
    private String getrate;
    private String latitude;
    private String longitude;
    private String fee;
    private String experienceYear;
    private String qualitfication;
    private String password;
    private String pmdc;
    private String about;
    private String specialization;
    private String experience;
    private String qualification;
    private String hospitaladdress;
    private String distance;
    private Bitmap image;
    private String imageName;
    public doctorJavaClass(){

    }

    public doctorJavaClass(String id,String name, String email, String city, String num, String hospitalname, String starthour, String startmin, String endhour, String endmin, String timedifference, String totalrate, String getrate, String latitude, String longitude, String fee, String experienceYear,double distance,String about,String specialization,String qualification,String experience,String hospitaladdress,String imageName) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
        this.setCity(city);
        this.setNum(num);
        this.setHospitalname(hospitalname);
        this.setStarthour(starthour);
        this.setStartmin(startmin);
        this.setEndhour(endhour);
        this.setEndmin(endmin);
        this.setTimedifference(timedifference);
        this.setTotalrate(totalrate);
        this.setGetrate(getrate);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setFee(fee);
        this.setExperienceYear(experienceYear);
        this.setDistance(String.valueOf(distance));
        this.setAbout(about);
        this.setQualitfication(qualification);
        this.setSpecialization(specialization);
        this.setExperienceYear(experienceYear);
        this.setHospitaladdress(hospitaladdress);
        this.setImageName(imageName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getHospitalname() {
        return hospitalname;
    }

    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }

    public String getStarthour() {
        return starthour;
    }

    public void setStarthour(String starthour) {
        this.starthour = starthour;
    }

    public String getStartmin() {
        return startmin;
    }

    public void setStartmin(String startmin) {
        this.startmin = startmin;
    }

    public String getEndhour() {
        return endhour;
    }

    public void setEndhour(String endhour) {
        this.endhour = endhour;
    }

    public String getEndmin() {
        return endmin;
    }

    public void setEndmin(String endmin) {
        this.endmin = endmin;
    }

    public String getTimedifference() {
        return timedifference;
    }

    public void setTimedifference(String timedifference) {
        this.timedifference = timedifference;
    }

    public String getTotalrate() {
        return totalrate;
    }

    public void setTotalrate(String totalrate) {
        this.totalrate = totalrate;
    }

    public String getGetrate() {
        return getrate;
    }

    public void setGetrate(String getrate) {
        this.getrate = getrate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getExperienceYear() {
        return experienceYear;
    }

    public void setExperienceYear(String experienceYear) {
        this.experienceYear = experienceYear;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPmdc() {
        return pmdc;
    }

    public void setPmdc(String pmdc) {
        this.pmdc = pmdc;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getQualitfication() {
        return qualitfication;
    }

    public void setQualitfication(String qualitfication) {
        this.qualitfication = qualitfication;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getHospitaladdress() {
        return hospitaladdress;
    }

    public void setHospitaladdress(String hospitaladdress) {
        this.hospitaladdress = hospitaladdress;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}