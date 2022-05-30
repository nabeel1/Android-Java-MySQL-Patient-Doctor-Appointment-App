package com.example.shahidhussain.assignemnt2.classess;

/**
 * Created by Hassan Naseer on 8/24/2018.
 */

public class appointmentDetail {

    public appointmentDetail(String doctorId, String appointmentId, String patientId, String patientName, String startmin, String starthour, String number) {
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.startmin = startmin;
        this.starthour = starthour;
        this.number = number;
    }

    public appointmentDetail() {
    }

    private String doctorId;
    private String appointmentId;
    private String patientId;
    private String patientName;
    private String startmin;
    private String starthour;
    private String number;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
