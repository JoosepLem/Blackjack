package com.example.blackjackuus;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Mäng extends Application { //peamine klass, kus kõik action toimub :)
    private boolean mängijaKord; //määrab ära, kas on mängija või diileri kord
    private Button hit; //nupp mängijale uue kaardi võtmiseks
    private Button stand; //nupp mängijale, kui enam uusi kaarte ei taha
    private Button ok; //nupp, et liikuda edasi peale tulemuse teada saamist
    private Label tekst;
    private Kaardipakk kaardipakk;
    private Diiler diiler;
    private Mängija mängija;
    private boolean diileriTeineKaart; //väärtus selle kohta, kas diileri teine kaart on avatud või mitte
    private int voidud = 0; //loeb mängija võite
    private int kaotused = 0; //loeb mängija kaotusi
    private boolean mangLabi; //väärtus selle kohta, kas mäng on läbi
    private int mangu_count = 1; //loeb, mitu mängu on mängitud
    private String kasutajanimi;

    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage primaryStage) throws InterruptedException, IOException { //alguseaken, kus saab kasutajanime sisestada

        Label alguseSonum = new Label("Tere tulemast mängima! Sisesta oma kasutajanimi, et alustada");
        alguseSonum.setAlignment(Pos.CENTER);
        TextField sisend = new TextField();
        sisend.setAlignment(Pos.CENTER);
        Button nupp = new Button("Alusta");
        nupp.setAlignment(Pos.CENTER);

        nupp.setOnAction(e -> { //alustab mänguga
            kasutajanimi = sisend.getText();
            try {
                mang(primaryStage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(alguseSonum, sisend, nupp);

        Scene scene = new Scene(vbox, 500, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void mang(Stage lava) throws IOException, InterruptedException { //mängu peamine osa, kus alguses luuakse vajalikud isendid mängu alustamiseks ja GUI ning hiljem toimub mängimine
        kaardipakk = new Kaardipakk();
        diiler = new Diiler();
        mängija = new Mängija();

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        tekst = new Label();
        tekst.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane();

        hit = new Button("Hit!");
        hit.setOnAction(event -> {
            mängija.võtaKaart(kaardipakk); //võtab pakist uue kaardi
            try {
                uuendaTeksti(lava); //uuendab teksti kaartide info kohta
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        stand = new Button("Stand!");
        stand.setOnAction(event -> {
            mängijaKord = false; //nüüd diileri kord
            try {
                tekst.setText(tekst.getText() + "\n");
                uuendaTeksti(lava); //uuendab teksti diileri kaartide kohta
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        ok = new Button("OK"); //nupp, et liikuda edasi peale kätt menüüsse
        ok.setOnAction(event -> {
            menuu(lava);
        });

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(hit, stand, ok);

        scrollPane.setContent(tekst);
        vBox.getChildren().addAll(scrollPane, buttonBox);

        Scene scene = new Scene(vBox, 500, 350);
        lava.setScene(scene);
        lava.show();

        alustaRaundi(lava); //alustatakse uue raundiga
    }

    private void alustaRaundi(Stage lava) throws InterruptedException, IOException { //luuakse kõik uued meetodid, uuendatakse väärtusi ja tehakse tegevused, mis on vajalikud uue raundi alustamiseks
        tekst.setText("");
        hit.setDisable(false);
        stand.setDisable(false);
        diileriTeineKaart = false;
        mängijaKord = true;
        kaardipakk.teeKaardipakk();
        mängija.clear();
        diiler.clear();
        mängija.võtaKaart(kaardipakk);
        diiler.võtaKaart(kaardipakk);
        mängija.võtaKaart(kaardipakk);
        diiler.võtaKaart(kaardipakk);
        mangLabi = false;

        uuendaTeksti(lava); //uuendatakse teksti
    }

    private String info(){ //tagastab kas mängija või diileri info tema kaartide kohta olenevalt olukorrast
        String info = "";

        if(mängijaKord){ //kui on mängija kord
            if(mängija.getKäsi().size() == 2){
                info += "Diileri esimene kaart on tagurpidi ja teine kaart on: " + diiler.getKäsi().get(1).getMast() + " " + diiler.getKäsi().get(1).getNimi() + "\n";
                info += "(koguväärtusega " + diiler.getKäsi().get(1).getVäärtus() + ") " + "\n";
                info += "Sinu kaardid on: " + mängija.käeSeis(mängija) + " (koguväärtusega " + mängija.getKäevaartus() + ")" + "\n";
            }
            else {
                info += "Teie järgmine kaart on: " + mängija.getKäsi().get(mängija.getKäsi().size() - 1).getMast() + " " + mängija.getKäsi().get(mängija.getKäsi().size() - 1).getNimi() + "\n";
                info += "Teie käsi on nüüd: " + mängija.käeSeis(mängija) + "\n";
            }
            if(mängija.getKäevaartus() < 21) {
                info += "Kas soovite uut kaarti (Hit) või aitab (Stand)?";
            }
        }
        else{ //kui on diileri kord
            if(!diileriTeineKaart){
                info += "Diileri varjatud kaart oli: " + diiler.getKäsi().get(0).getMast() + " " + diiler.getKäsi().get(0).getNimi() + "\n";
                info += "Diileri käsi on: " + diiler.käeSeis(diiler);
                diileriTeineKaart = true; //nüüd oleme diileri varjatud kaardi avanud ja edasi läheb teistmoodi
            }
            else {
                info += "Diileri järgmine kaart on: " + diiler.getKäsi().get(diiler.getKäsi().size() - 1).getMast() + " " + diiler.getKäsi().get(diiler.getKäsi().size() - 1).getNimi() + "\n";
                info += "Diileri käsi on nüüd: " + diiler.käeSeis(diiler);
            }
        }

        return info;
    }

    private void uuendaTeksti(Stage lava) throws InterruptedException, IOException { //meetod, mis teeb vajalikud toimingud olenevalt mängija sisendist ja tema/diileri käeseisust
        hit.setDisable(!mängijaKord); //kui pole mängija kord, siis nuppe vajutada ei saa
        stand.setDisable(!mängijaKord);
        ok.setDisable(mängijaKord);

        if(mängijaKord) {
            if (mängija.getKäevaartus() < 21) tekst.setText(tekst.getText() + "\n" + info()); //kui mängija ei ole lõhki läinud aga tahab edasi mängida, väljastatakse info tema kaartide kohta
            else if (mängija.getKäevaartus() == 21) { //kui blackjack
                if(mängija.getKäsi().size() == 2) {
                    tekst.setText(info() + "\n" + "Blackjack! Sinu võit!");
                    voidud += 1;
                    kirjutaFaili(lava);
                }
                else{
                    tekst.setText(tekst.getText() + "\n" + info() + "\n" + "Blackjack! Sinu võit!");
                    voidud += 1;
                    kirjutaFaili(lava);
                }
                mängijaKord = false;
                ok.setDisable(mängijaKord);
            } else { //kui mängija läks lõhki
                tekst.setText(tekst.getText() + "\n" + info() + "\n" + "Läksid lõhki! Diiler võitis!");
                kaotused += 1;
                kirjutaFaili(lava);
                mängijaKord = false;
                ok.setDisable(mängijaKord);
                hit.setDisable(true);
                stand.setDisable(true);
            }
        }
        else{
            tekst.setText(tekst.getText() + "\n" + info());
            diileriKontroll(lava);

            while(!mangLabi){ //diiler võtab niikaua uusi kaarte, kui läheb lõhki või summa jääb 17 ja 21 vahele
                diiler.võtaKaart(kaardipakk);
                diileriKontroll(lava); //kontrollime, mis seisus diileri kaartide summa on ja käitume vastavalt olukorrale
            }
        }
    }

    private void diileriKontroll(Stage lava) throws InterruptedException, IOException { //kontrollime, mis seisus diileri kaartide summa on ja käitume vastavalt olukorrale
        if(diiler.getKäevaartus() < 17){ //kui summa väiksem kui 17, siis tagastame kaartide info ja võtame uue
            tekst.setText(tekst.getText() + "\n" + info());
        }
        else if(diiler.getKäevaartus() > 21){ //kui väärtus suurem, kui 21, siis diiler kaotas ja kirjutame tulemuse faili ning lähme tagasi menüüsse
            tekst.setText(tekst.getText() + "\n" + info() + "\n" + "\n" + "Diiler läks lõhki! Sinu võit!");
            voidud += 1;
            kirjutaFaili(lava);
            mangLabi = true;
        }
        else{ //kui diileri käe väärtus on 17 ja 21 vahel, siis väljastame vajaliku info ja naaseme menüüsse
            if(diiler.getKäevaartus() > mängija.getKäevaartus()){
                tekst.setText(tekst.getText() + "\n" + info() + "\n" + "\n" + "Sa kaotasid! Diileril on parem käsi.");
                kaotused += 1;
                kirjutaFaili(lava);
            }
            else if(diiler.getKäevaartus() < mängija.getKäevaartus()){
                tekst.setText(tekst.getText() + "\n" + info() + "\n" + "\n" + "Sa võitsid! Sul on parem käsi.");
                voidud += 1;
                kirjutaFaili(lava);
            }
            else{
                tekst.setText(tekst.getText() + "\n" + info() + "\n" + "\n" + "Keegi ei võitnud, jäite viiki!");
                kirjutaFaili(lava);
            }
            mangLabi = true;
        }
    }

    private void menuu(Stage lava){ //menüü, kust saab uut raundi alustada, lõpetada või vaadata eelmiste mängude statistikat
        BorderPane bp = new BorderPane();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        tekst = new Label("Kui tahad uuesti mängida, vajuta uuesti, kui ei, vajuta lõpp, kui tahad näha eelmiste mängude statistikat, vajuta statistika");
        tekst.setWrapText(true);

        Label voitudeArv = new Label("Võite: " + voidud); //kuvab võitude arvu
        voitudeArv.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(voitudeArv, Priority.ALWAYS);
        voitudeArv.setAlignment(Pos.CENTER_LEFT);

        Label kaotusteArv = new Label("Kaotusi: " + kaotused); //kuvab kaotuste arvu
        kaotusteArv.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(kaotusteArv, Priority.ALWAYS);
        kaotusteArv.setAlignment(Pos.CENTER_RIGHT);

        Label username = new Label("Kasutajanimi: " + kasutajanimi); //kuvab kasutajanime
        username.setAlignment(Pos.CENTER);

        HBox hbox = new HBox();
        hbox.getChildren().addAll(voitudeArv, username, kaotusteArv);

        Button uuesti = new Button("Uuesti"); //kui soovid uuesti mängida
        uuesti.setOnAction(event -> {
            try {
                mang(lava);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button lopp = new Button("Lõpp"); //kui soovid lõpetada
        lopp.setOnAction(event -> {
            try {
                failPuhtaks(); //pühib statistika faili puhtaks, et järgmine kord see oleks tühi
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Platform.exit();
        });

        Button statistika = new Button("Statistika"); //kui soovid näha statistikat eelmiste mängude kohta
        statistika.setOnAction(event -> {
            try {
                statistika(lava);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        HBox nupud = new HBox(20);
        nupud.setAlignment(Pos.CENTER);
        nupud.getChildren().addAll(uuesti, statistika, lopp);

        vBox.getChildren().addAll(tekst, nupud);

        bp.setCenter(vBox);
        bp.setTop(hbox);

        Scene scene = new Scene(bp, 500, 350);
        lava.setScene(scene);
        lava.show();
    }

    private void statistika(Stage lava) throws FileNotFoundException { //loeb statistika failist tulemused ja väljastab need mängijale

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);

        Label tekst = new Label("Eelmiste mängude statistika:");
        tekst.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane();

        Button tagasi = new Button("Tagasi");
        tagasi.setAlignment(Pos.CENTER);
        tagasi.setOnAction(event -> {
            menuu(lava);
        });

        String info = loeFailist();
        Label tulemused = new Label(info);
        tulemused.setWrapText(true);
        scrollPane.setContent(tulemused);
        vBox.getChildren().addAll(tekst, scrollPane, tagasi);

        Scene scene = new Scene(vBox, 500, 350);
        lava.setScene(scene);
        lava.show();
    }

    private String loeFailist() throws FileNotFoundException { //meetod mängude statistika lugemiseks failist
        File fail = new File("statistika.txt");
        Scanner sc = new Scanner(fail);
        String vastus = "";
        while(sc.hasNextLine()){
            String rida = sc.nextLine();
            vastus += rida + "\n";
        }
        sc.close();
        return vastus;
    }
    private void kirjutaFaili(Stage lava) throws IOException { //meetod mängude andmete kirjutamiseks faili
        FileWriter fileWriter = new FileWriter("statistika.txt", true);

        fileWriter.write("Mäng " + mangu_count + ":" + "\n");
        fileWriter.write( "Mängija: " + mängija.käeSeis(mängija) + "\n");
        fileWriter.write("Diiler: " + diiler.käeSeis(diiler) + "\n");
        fileWriter.close();
        mangu_count += 1;
    }

    private void failPuhtaks() throws IOException { //meetod, mis pühib faili andmetest puhtaks
        String fileName = "statistika.txt";

        FileWriter fileWriter = new FileWriter(fileName, false);
        fileWriter.write("");
        fileWriter.close();
    }

}
