package com.example.a11400703.projetarro;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.view.View;

import static android.R.attr.padding;

/**
 * Created by 11400703 on 11/05/2017.
 */
public class surfaceView extends SurfaceView implements SurfaceHolder.Callback ,View.OnTouchListener {
    int increX=100;
    int incretY=0;
    SurfaceHolder mSurfaceHolder;
    float angle=0;
    float x=400;
    float y=200;
    boolean app=false;
    float delx=0;
    float dely=0;
    int tab1[];
    int gg;
    public surfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        mSurfaceHolder = holder;

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.print("test");
        dessin(0,0);
        Rotate rt=new Rotate();
        rt.start();
        setOnTouchListener(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        System.out.println(x+":"+event.getX()+"-"+y+":"+event.getY());

        if((event.getX()>=x) &&(event.getX()<=(x+200))&&((event.getY()>=y)&&(event.getY()<=(y+200))) && (event.getAction()==MotionEvent.ACTION_DOWN)) {
            delx=x-event.getX();
            dely=y-event.getY();
            app=true;
        }
        if (app && (event.getAction()==MotionEvent.ACTION_MOVE))
        {
            dessin((int) event.getX()+(int)delx, (int) event.getY()+(int)dely);
            x=event.getX();
            y=event.getY();
        }
        if (app && (event.getAction()==MotionEvent.ACTION_UP))
            app=false;

        return true;
    }

    void dessin(int a, int b) {//la fonction dessin qui ne return rien
        Canvas c = null;
        angle+=40;
        angle=angle%360;

        try {
            c = mSurfaceHolder.lockCanvas();//on crée une surface
            Paint p = new Paint();//on crée un endorit où déssiner
            p.setStyle(Paint.Style.FILL);//on dessine
           // System.out.println(tab1[3]);
            /*

            c.drawRect(0,0,c.getWidth(),c.getHeight(),p);

            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.RED);
            c.clipRect(1,2,2,2,p);*/

            //  c.save();
            //c.rotate(angle,a+100,b+100);
            /*for(int i=0;i  < tab1.length;i++) {
                if(tab1[i]!=0) {

                    c.drawLine(tab1[1], 0, 1000, 100, p);
                    c.drawLine(0, tab1[2], 100, 1000, p);
                    c.drawLine(tab1[3], 0, 1500, 150, p);
                    System.out.println(i+": "+tab1[i]);
                    increX = increX + 1;
                    incretY += 30;
                    System.out.println(i+": "+tab1[i]);
                    c.drawText("Les données de l'arroseur", i+50, i+50,p);
                }
            }*/
            float canvasHeight = getHeight();//on prend les valeurs de la hauteur
            float canvasWidth = getWidth();//on prend les valeurs de la largueur


           p.setStrokeWidth(2);//on défini la largueur du trait
            int grey = 200;//on défini a couleur
            c.drawARGB(255, grey, grey, grey);//on dessine une couleur

            for (int i = 0; i < 5; i++) {

                    p.setColor(Color.RED);//on défini le rouge pour le déssine
                    c.drawLine(tab1[i], canvasHeight - tab1[i+10], tab1[i + 10], 100 - tab1[i + 1], p);//on déssine la ligne des donnée
                    c.drawLine(tab1[i], 1400 - tab1[i], tab1[i], 100 - tab1[i + 1], p);
            }

            p.setColor(Color.BLACK);//on défini la couleur
            c.drawLine(0, canvasHeight - 10, canvasWidth,
                    canvasHeight - 10, p);//on déssine l'axe
            c.drawLine(10, 0, 10, canvasHeight,
                    p);//axe y
            p.setTextSize(60);//on défini la hauteur du font size
            c.drawText("axe x", 500,1400,p);//on écrit le texte
            c.drawText("axe y", 0,200,p);

            synchronized (mSurfaceHolder) {// on synchronized
            }
        } finally {

            if (c != null) {
                mSurfaceHolder.unlockCanvasAndPost(c);//surfaceHolder
            }

        }
    }
    class Rotate extends Thread implements Runnable{
        public void run()
        {
            while (true)
            {
                try {
                    sleep(1);
                    if(!app)   dessin((int)x+(int)delx,(int)y+(int)dely);//on dessine
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

