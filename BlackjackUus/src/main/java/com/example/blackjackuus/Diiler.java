package com.example.blackjackuus;

import java.util.ArrayList;

public class Diiler {
    private String nimi;
    private ArrayList<Kaardid> käsi;

    public Diiler() {
        this.nimi = "Diiler";
        this.käsi = new ArrayList<>();
    }

    public ArrayList<Kaardid> getKäsi() {
        return käsi;
    } //diileri käsi, mis koosneb kaartidest
    public int getKäevaartus(){ //meetod, millega saame käe arvulise väärtuse
        int vastus = 0;
        for(Kaardid kaart: käsi){
            vastus += kaart.getVäärtus();
        }
        return vastus;
    }

    public StringBuilder käeSeis(Diiler diiler) { //meetod, millega saame käe kirjelduse
        StringBuilder diileri_käsi = new StringBuilder();
        ArrayList<Kaardid> diileri_kaardid = diiler.getKäsi();
        for (Kaardid elem : diileri_kaardid) {
            diileri_käsi.append(elem.getMast() + " " + elem.getNimi() + ", ");
        }
        diileri_käsi.append("(koguväärtusega " + getKäevaartus() + ")");
        return diileri_käsi;
    }

    public void clear(){
        käsi.clear();
    } //meetod, mis kustutab kaardid diileri käest
    public void võtaKaart(Kaardipakk kaardipakk){ //meetod, millega diiler võtab pakist uue kaardi, mis lisandub tema kaartide hulka
        käsi.add(kaardipakk.võtaKaart());
    }

}
