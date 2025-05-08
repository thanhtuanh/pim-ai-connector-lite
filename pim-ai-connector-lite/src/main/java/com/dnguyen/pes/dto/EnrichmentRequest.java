package com.dnguyen.pes.dto;

public class EnrichmentRequest {
    private String marke;
    private String produktart;
    private String farbe;
    private String material;
    private String imageBase64;

    public String getMarke() {
        return marke;
    }

    public void setMarke(String marke) {
        this.marke = marke;
    }

    public String getProduktart() {
        return produktart;
    }

    public void setProduktart(String produktart) {
        this.produktart = produktart;
    }

    public String getFarbe() {
        return farbe;
    }

    public void setFarbe(String farbe) {
        this.farbe = farbe;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}