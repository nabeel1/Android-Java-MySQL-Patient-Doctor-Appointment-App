package com.example.shahidhussain.assignemnt2.classess;

/**
 * Created by Hassan Naseer on 8/24/2018.
 */

public class pendingAppointmentList {
    public pendingAppointmentList() {
    }

    public pendingAppointmentList(int id, String doctorId, String patientId, String patientName, String startmin, String starthour, String endmin, String endhour, String appointmentNumber) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.startmin = startmin;
        this.starthour = starthour;
        this.endmin = endmin;
        this.endhour = endhour;
        this.appointmentNumber = appointmentNumber;
    }

    private int id;
    private String doctorId;
    private String patientId;
    private String patientName;
    private String startmin;
    private String starthour;
    private String endmin;
    private String endhour;
    private String appointmentNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getStartmin() {
        return startmin;
    }

    public void setStartmin(String startmin) {
        this.startmin = startmin;
    }

    public String getStarthour() {
        return starthour;
    }

    public void setStarthour(String starthour) {
        this.starthour = starthour;
    }

    public String getEndmin() {
        return endmin;
    }

    public void setEndmin(String endmin) {
        this.endmin = endmin;
    }

    public String getEndhour() {
        return endhour;
    }

    public void setEndhour(String endhour) {
        this.endhour = endhour;
    }

    public String getAppointmentNumber() {
        return appointmentNumber;
    }

    public void setAppointmentNumber(String appointmentNumber) {
        this.appointmentNumber = appointmentNumber;
    }
}
