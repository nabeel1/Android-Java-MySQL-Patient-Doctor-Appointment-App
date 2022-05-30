package com.example.shahidhussain.assignemnt2.classess;

/**
 * Created by Hassan Naseer on 8/24/2018.
 */

public class medicationJavaClass {
    public medicationJavaClass(String disease, String symptoms, String medicines,String doze) {
        this.setDisease(disease);
        this.setSymptoms(symptoms);
        this.setMedicines(medicines);
        this.setDoze(doze);
    }

    public medicationJavaClass() {
    }

    private String disease;
    private String symptoms;
    private String medicines;
    private String doze;


    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public String getDoze() {
        return doze;
    }

    public void setDoze(String doze) {
        this.doze = doze;
    }
}
