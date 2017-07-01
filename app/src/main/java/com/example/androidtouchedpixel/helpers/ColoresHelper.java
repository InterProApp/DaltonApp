package com.example.androidtouchedpixel.helpers;

import android.content.res.Resources;

import com.example.androidtouchedpixel.R;
import com.example.androidtouchedpixel.model.NombreColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ezgonzalez on 5/29/17.
 */

public class ColoresHelper {
    private static ColoresHelper instance = null;

    //    aca voy a guardar una lista de todos los colores
    private List<NombreColor> colores = new ArrayList<NombreColor>();

    private ColoresHelper(Resources res) {
        int[] reds = res.getIntArray(R.array.Num1);
        int[] greens = res.getIntArray(R.array.Num2);
        int[] blues = res.getIntArray(R.array.Num3);
        String[] nombresColores = res.getStringArray(R.array.TiposDos);

        for (int i = 0; i < nombresColores.length; i++) {
            this.colores.add(new NombreColor(reds[i], greens[i], blues[i], nombresColores[i]));
        }
    }

    public static ColoresHelper getInstance (Resources res) {
        if (instance == null) {
            instance = new ColoresHelper(res);
        }

        return instance;
    }

    public String getNombreDelColorAproximado(int touchedRGB) {
        double minDistancia = Double.MAX_VALUE;
        NombreColor colorAproximado = null;

        for (NombreColor nombreColor : this.colores) {
            if (nombreColor.calcularDistancia(touchedRGB) < minDistancia) {
                minDistancia = nombreColor.calcularDistancia(touchedRGB);
                colorAproximado = nombreColor;
            }
        }

        return colorAproximado.getNombre();
    }
}
