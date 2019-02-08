package com.example.a11400703.projetarro;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {
    Handler hd;
    TextView tv;
    EditText edit;
    int valHum;
    int rand;
   private static int [] tab;
    ViewFlipper viewFlipper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//on crée l'instance
        setContentView(R.layout.activity_main); //texte view
        tv=(TextView) findViewById(R.id.textView);//récupérer l'élement
        edit   = (EditText)findViewById(R.id.editText);//on peut éditer le text
        viewFlipper=(ViewFlipper) findViewById(R.id.viewFlipper);
        hd=new Handler();//pour passer d'une view à l'autr
    }


   public class envoyer extends Thread implements Runnable {//class pour envoyer le chiffe
        public void run() {
            byte[] b= new byte[2];//pour le binaire
        /* convertir valHum entre 0 et 1024, 1023 correspondant à 0% */
            System.out.println(valHum);//on get le nombre
            valHum =(int) Math.floor(((1- (valHum*1.0/100))*1023));//pour convertir le nombre
            System.out.println(valHum);//on affiche le nombre pour voir si il s'est bien envoyé
           b[0]=(byte) (valHum/255);//le premier paquet
            b[1]=(byte )(valHum%255);//deuxième paquet
            try {


                Socket s = new Socket("192.168.0.39",3131);//On crée un socket sur mon ip

                OutputStream dop = s.getOutputStream();//On prépare le socket à être envoyé


                dop.write(b); //on écrit le socket
                System.out.println("envoyé"+ b[0]+" "+b[1]);//on voit si il s'envoie bien
                s.close();//on quitte le socoket


            } catch (IOException e) {//une  execption
                e.printStackTrace();
            }

        }
    }
    class recevoir extends Thread implements Runnable {
        public void run() {
            // byte b ;
            byte[] b = new byte[2];//on recoit les paquets par deux du coup on déclare une variale
            try {


                rand = (int) (Math.random() * (99 - 0));//on envoie des données aléatoires
                System.out.println("Le nombre qu'à été envoyé est : " + rand);
                b[0] = (byte) (rand);//on définit un bit aléatoire
                b[1] = (byte) 255;//bit sur 255
                tab = new int[rand];
                Socket s = new Socket("172.16.8.222", 3132);//on envoie les donnée aléatoire

                OutputStream dop = s.getOutputStream();//on recoit les données

                InputStream send = s.getInputStream();//on envoie les données
                dop.write(b);//on écrit les données
                for (int i = 0; i < rand; i++) {//on boucle sur les données aléatoires

                    int pfa = (send.read() & 255) * 255 + send.read() & 255;//convertion de données
                    if (pfa > 100) {//si c'est supérieur à 100
                        try {
                            Thread.sleep(150);//on fait dormir le serveur sockets si c'est supérieur à 100  de 150ms
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        pfa = (int) Math.floor(((1 - (pfa * 1.0 / 1023)) * 100));//conversion de doonées pour qua ça soit en poucentage
                        System.out.println(i + ":" + pfa);//on vérifie que c'est bien en pourcentage
                        tab[i]=pfa;//on le passe dans un tableau

                    }

                    if (i == (rand - 1)) {//si la boucle a terminer on y va à la view
                        System.out.println("Go");
                        hd.post(new Runnable() {
                            @Override
                            public void run() {
                                viewFlipper.setDisplayedChild(1);
                                surfaceView vv=(surfaceView) findViewById(R.id.surfaceview);//on passe à la vue suivante;
                                vv.tab1=tab;
                            }
                        });

                    }

                    /*else{
                        return;
                    }*/
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    void action(View v){
        try {
            valHum = Integer.parseInt(edit.getText().toString());
        }catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        (new envoyer()).start();

    }
    void action2(View v){


        (new recevoir()).start();//on démarre le serveur

    }
}

