package com.example.blackjackuus;

import java.util.ArrayList;

public class Mängija {
    private String nimi;
    private ArrayList<Kaardid> käsi;

    public Mängija() {
        this.nimi = "Mängija";
        this.käsi = new ArrayList<>();
    }
    public ArrayList<Kaardid> getKäsi() {
        return käsi;
    } //tagastab mängija käe
    public int getKäevaartus() { //meetod käe arvulise väärtuse saamise jaoks
        int vastus = 0;
        for(Kaardid kaart: käsi){
            vastus += kaart.getVäärtus();
        }
        return vastus;
    }

    public StringBuilder käeSeis(Mängija mängija){ //meetod käe kirjelduse saamiseks
        StringBuilder mängija_käsi = new StringBuilder();
        ArrayList<Kaardid> mängija_kaardid = mängija.getKäsi();
        for (Kaardid elem : mängija_kaardid) {
            mängija_käsi.append(elem.getMast() + " " + elem.getNimi() + ", ");
        }
        mängija_käsi.append("(koguväärtusega " + getKäevaartus() + ")");
        return mängija_käsi;
    }

    public void clear(){
        käsi.clear();
    } //meetod, mis puhastab käe ehk kustutab sealt kõik kaardid

    public void võtaKaart(Kaardipakk kaardipakk){käsi.add(kaardipakk.võtaKaart()); //meetod uue kaardi lisamiseks enda omade hulka
    }
}
