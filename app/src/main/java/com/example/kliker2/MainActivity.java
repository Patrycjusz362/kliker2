package com.example.kliker2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    long points = 0;
    long kliki = 1;
    int combo= 1; // narazie nie wykorzystywane
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wczytaj();
        wyswietl();

    }

    protected void onDestroy()
    {
        zapisz();
        super.onDestroy();
    }
    protected void onPause()
    {
        zapisz();
        super.onPause();
    }
    
    // moje funkcje

    @SuppressLint("SetTextI18n")
    public void wyswietl()
    {
        //podstawy
        TextView punkty = findViewById(R.id.punkty);
        punkty.setText(points + " $");

        TextView przycisk = findViewById(R.id.button);
        przycisk.setText("+ " + kliki);

        //sklep
        TextView sklep = findViewById(R.id.sklep1);
        sklep.setText("+1$ / " + kup(1, true) + "$");
        sklep = findViewById(R.id.sklep5);
        sklep.setText("+5$ / " + kup(5, true) + "$");
        sklep = findViewById(R.id.sklep10);
        sklep.setText("+10$ / " + kup(10, true) + "$");
        sklep = findViewById(R.id.sklep50);
        sklep.setText("+50$ / " + kup(50, true) + "$");
        sklep = findViewById(R.id.sklep250);
        sklep.setText("+250$ / " + kup(250, true) + "$");
    }

    public void zapisz()
    {
        SharedPreferences sharedPref = getSharedPreferences("hajs", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("pieniondze1", points);
        editor.apply();

        sharedPref = getSharedPreferences("kliki", MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putLong("kliki", kliki);
        editor.apply();

    }
    public void wczytaj()
    {
        SharedPreferences sharedPref = getSharedPreferences("hajs", MODE_PRIVATE);
        points = sharedPref.getLong("pieniondze1", 0);

        sharedPref = getSharedPreferences("kliki", MODE_PRIVATE);
        kliki = sharedPref.getLong("kliki", 0);
        if (kliki == 0) kliki = 1;

    }

    public long kup(int ile, boolean wyswietl)
    {   long cena;
        SharedPreferences sharedPref = getSharedPreferences("sklep"+ile, MODE_PRIVATE);
        cena = sharedPref.getLong("cena"+ile, 0);
       double mnoznikceny = 1.25;
       // co jeśli bedzie pusto
        if (cena == 0)
        {
            if (ile == 1) cena = 100;
            if (ile == 5) cena = 1000;
            if (ile == 10) cena = 5000;
            if (ile == 50) cena = 10000;
            if (ile == 250) cena = 100000;
            if (ile == 500) cena = 1000000;
            if (ile == 10000) cena = 1000000;
        }


       // działanie
       if(wyswietl) return cena;
       else if(cena <= points)
       {
           points = points - cena;
           kliki = kliki + ile;
           cena = (long) (cena * mnoznikceny);
       }

       SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong("cena"+ile, cena);
        editor.apply();
        zapisz();
        wyswietl();
        return cena;

    }


    // przyciski
    public void klik(View view)
    {
    points = points + kliki * combo;
    wyswietl();

    }

    //tylko do testów
    ///**
    public void SaveButton(View view) {
        @SuppressLint("SdCardPath") File sharedPreferenceFile = new File("/data/data/"+ getPackageName()+ "/shared_prefs/");
        File[] listFiles = sharedPreferenceFile.listFiles();
        for (File file : listFiles) {
            file.delete();
        }
        points = 0;
        kliki = 1;
        zapisz();
        wyswietl();
    }
    //**/
    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    public void sklep(View view) {
        TextView sklep = findViewById(view.getId());;
        String numer = "sklep";
        int wartosc=0;
        switch (view.getId()){
            case R.id.sklep1:
                wartosc = 1;
                break;
            case R.id.sklep5:
                wartosc = 5;
                break;
                case R.id.sklep10:
                wartosc = 10;
                break;
            case R.id.sklep50:
                wartosc = 50;
                break;
            case R.id.sklep250:
                wartosc = 250;
                break;
            //case R.id.sklep500:
                //wartosc = 500;
                //break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
        sklep.setText("+"+wartosc+"$ / " + kup(wartosc, false) + "$");

    }

    public void inneMenu(View view) {
        Intent intent = new Intent(this, InneActivity.class);
        startActivity(intent);
    }
}