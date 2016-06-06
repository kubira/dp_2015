/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.structures;

import java.util.Arrays;

/**
 * Třída pro jeden bod/sekvenci.
 */
public class Point {

    /** Název bodu/sekvence. */
    public String name;
    /** Souřadnice. */
    public double[] coords;
    /** Hustota bodu. */
    public double density;

    /**
     * Konstruktor bodu bez názvu.
     *
     * @param coords souřadnice bodu.
     */
    public Point(double[] coords) {
        /* Uložení souřadnic bodu. */
        this.coords = coords;
        /* Zkopírování souřadnic. */
        System.arraycopy(coords, 0, this.coords, 0, coords.length);
    }

    /**
     * Konstruktor bodu s názvem.
     *
     * @param name jméno bodu
     * @param coords souřadnice bodu.
     */
    public Point(String name, double[] coords) {
        /* Uložení názvu bodu. */
        this.name = name;
        /* Uložení souřadnic bodu. */
        this.coords = coords;
        /* Zkopírování souřadnic. */
        System.arraycopy(coords, 0, this.coords, 0, coords.length);
    }

    /**
     * Konstruktor bodu z vektoru.
     *
     * @param v vektor pro vytvoření bodu.
     */
    public Point(Vector v) {
        /* Uložení názvu z vektoru. */
        this.name = v.name;
        /* Vytvoření pole souřadnic. */
        this.coords = new double[v.size()];

        /* Zkopírování souřadnic z vektoru do bodu. */
        for (int i = 0; i < v.size(); i++) {
            /* Uložení souřadnice na indexu i. */
            this.coords[i] = v.get(i);
        }
    }

    /**
     * Funkce vracející vzdálenost dvou bodů.
     *
     * @param p bod, od kterého se počítá vzdálenost.
     *
     * @return Vrací vzdálenost bodů.
     */
    public double getDistance(Point p) {
        /* Uložení počtu dimenzí bodu. */
        int dimensions = coords.length;
        /* Suma pro výpočet. */
        double sum = 0.0;

        /* Pokud nemají body stejný počet souřadnic. */
        if (dimensions != p.coords.length) {
            /* Vyhození výjimky. */
            throw new IllegalArgumentException("Number of dimensions is not same");
        }

        /* Výpočet vzdálenosti. */
        for (int index = 0; index < dimensions; index++) {
            /* Odečtení souřadnic. */
            double diff = coords[index] - p.coords[index];
            /* Mocnina rozdílu. */
            sum += diff * diff;
        }

        /* Vrácení vzdálenosti. */
        return Math.sqrt(sum);
    }

    /**
     * Funkce vracející euklidovskou normu bodu.
     *
     * @return euklidovská norma bodu.
     */
    public double getEuclidean() {
        /* Suma pro výpočet. */
        double sum = 0.0;

        /* Výpočet sumy mocnin souřadnic. */
        for (double c : coords) {
            /* Přičtení mocniny jedné souřadnice. */
            sum += c * c;
        }

        /* Vrácení euklidovské normy. */
        return Math.sqrt(sum);
    }

}

