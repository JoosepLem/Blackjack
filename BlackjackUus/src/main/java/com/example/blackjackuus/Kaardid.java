package com.example.blackjackuus;

public class Kaardid {
    private int väärtus;
    private String nimi;
    private String mast;

    public Kaardid(String nimi, String mast) {
        this.nimi = nimi;
        this.mast = mast;
    }

    public int getVäärtus() {
        return väärtus;
    } //arvulise väärtuse saamiseks

    public String getNimi() {
        return nimi;
    } //nime saamiseks (poiss, emand jne)

    public String getMast() {
        return mast;
    } //masti saamise meetod

    public void setVäärtus(int väärtus) {
        this.väärtus = väärtus;
    } //uue väärtuse omistamise meetod

    @Override
    public String toString() {
        return "Kaart " + nimi + " " + mast + " " + väärtus;
    }
}
