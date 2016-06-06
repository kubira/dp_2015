/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.structures;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Třída pro krychly prostoru DENCLUE.
 */
public class Cube {

    /** Identifikátor krychle. */
    public String key;
    /** Hranice krychle. */
    public double[] limits;
    /** Body krychle. */
    public Set<Point> points = new HashSet<Point>();
    /** Sousedé krychle. */
    public ArrayList<String> neighbors = new ArrayList<String>();
    /** Součty pro prvky krychle. */
    private double[] itemsSum;

    /**
     * Konstruktor krychle.
     *
     * @param key identifikátor krychle.
     * @param limits hranice krychle.
     */
    public Cube(String key, double[] limits) {
        /* Uložení klíče. */
        this.key = key;
        /* Uložení hranic. */
        this.limits = limits;

        /* Inicializace součtů. */
        itemsSum = new double[limits.length];
    }

    /**
     * Funkce pro přidání bodu do krychle.
     *
     * @param p bod pro přidání.
     */
    public void addPoint(Point p) {
        /* Přidání bodu. */
        points.add(p);

        /* Přepočet sum s novým bodem. */
        for (int index = 0; index < p.coords.length; index++) {
            /* Přepočet jedné sumy s novým bodem. */
            itemsSum[index] += p.coords[index];
        }
    }

    /**
     * Metoda vracející průměry.
     *
     * @return Vrací průměry krychle.
     */
    public double [] getMeans() {
        /* Pole pro průměry. */
        double[] means = new double[itemsSum.length];
        /* Počet bodů v krychly. */
        int numberOfPoints = points.size();

        /* Výpočet průměrů. */
        for (int index = 0; index < itemsSum.length; index++) {
            /* Výpočet jednoho průměru. */
            means[index] = (itemsSum[index] / numberOfPoints);
        }
        
        /* Navrácení průměrů. */
        return means;
    }

    /**
     * Funkce pro vzdálenost dvou krychlí.
     *
     * @param c druhá krychle pro výpočet.
     *
     * @return Vrací vzdálenost krychlí.
     */
    public double getDistance(Cube c) {
        /* Počet dimenzí. */
        int dimensions = c.limits.length;
        /* Součet. */
        double sum = 0.0;
        /* Získání průměrů pro tuto krychli. */
        double[] myMeans = getMeans();
        /* Získání průměrů pro zadanou krychli. */
        double[] cMeans = c.getMeans();

        /* Cyklus výpočtu vzdálenosti. */
        for (int index = 0; index < dimensions; index++) {
            /* Rozdíl průměrů. */
            double diff = myMeans[index] - cMeans[index];
            /* Přičtení druhé mocniny. */
            sum += diff * diff;
        }

        /* Navrácení vzdálenosti. */
        return Math.sqrt(sum);
    }

    /**
     * Funkce pro vytvoření identifikátoru krychle z hranic.
     *
     * @param lims hranice krychle.
     *
     * @return Vrací identifikátor krychle.
     */
    public static String getKey(double[] lims) {
        /* Tvořič řetězce. */
        StringBuilder resultKey = new StringBuilder();
        /* Formátovač čísla. */
        DecimalFormat fmt = new DecimalFormat("0.#");

        /* Pro každou hranici, */
        for (double l : lims) {
            /* přidej hodnotu */
            resultKey.append(fmt.format(l));
            /* a přidej oddělovač. */
            resultKey.append(",");
        }

        /* Navrácení klíče krychle. */
        return resultKey.toString();
    }

}
