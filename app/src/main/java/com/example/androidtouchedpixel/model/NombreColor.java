package com.example.androidtouchedpixel.model;

import android.graphics.Color;

/**
 * Created by ezgonzalez on 5/29/17.
 */

public class NombreColor {
    private int r;
    private int g;
    private int b;
    private String nombre;

    public NombreColor(int r, int g, int b, String nombre) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.nombre = nombre;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public String getNombre() {
        return nombre;
    }

    public double calcularDistancia (int colorHex) {
        return Math.sqrt(Math.pow(this.r - Color.red(colorHex), 2) + Math.pow(this.g - Color.green(colorHex), 2) + Math.pow(this.b - Color.blue(colorHex), 2));
    }
}
