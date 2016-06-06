/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.structures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Třída pro prostor krychlí algoritmu DENCLUE.
 */
public class Space {

    /** Mapování řetězce/klíče a jeho krychle. */
    public Map<String, Cube> cubeMap = new HashMap<String, Cube>();
    /** Parametr sigma algoritmu. */
    private double sigma;
    /** Parametr xí algoritmu. */
    private int xi;
    /** Mapa pro krychle s body. */
    private Map<String, Cube> popCubes = new HashMap<String, Cube>();
    /** Množina krychlí s alespoň xí body. */
    private Set<Cube> highPopCubes = new HashSet<Cube>();

    /**
     * Konstruktor třídy prostoru.
     *
     * @param sigma parametr sigma algoritmu.
     */
    public Space(double sigma) {
        /* Uložení sigma. */
        this.sigma = sigma;
        /* Nastavení standardní hodnoty xí algoritmu. */
        this.xi = 4;
    }

    /**
     * Konstruktor třídy prostoru se zadaným xí.
     *
     * @param sigma parametr sigma algoritmu.
     * @param xi paramter xí algoritmu.
     */
    public Space(double sigma, int xi) {
        /* Volání se sigma. */
        this(sigma);
        /* Uložení xí. */
        this.xi = xi;
    }

    /**
     * Funkce přidávající bod do prostoru.
     *
     * @param p přidávaný bod
     */
    public void addPoint(Point p) {
        /* Pole hranic nové krychle s bodem. */
        double[] limits = new double[p.coords.length];

        /* Cyklus výpočtu hranic. */
        for (int index = 0; index < p.coords.length; index++) {
            /* Výpočet jedné hranice. */
            limits[index] = p.coords[index] - p.coords[index] % (2 * sigma) + (2 * sigma);
        }

        /* Klíč krychle. */
        String k = Cube.getKey(limits);

        /* Získání krychle podle klíče. */
        Cube c = popCubes.get(k);
        /* Pokud neexistuje, */
        if (c == null) {
            /* Vytvoří se nová. */
            c = new Cube(k, limits);
        }
        /* Přidání bodu do krychle. */
        c.addPoint(p);
        /* Přidání krychle do neprázdných krychlí. */
        popCubes.put(k, c);
    }

    /**
     * Metoda spojující prostor krychlí.
     */
    public void connectMap() {
        /* Pro každou krychli v neprázdných. */
        for (Cube c : popCubes.values()) {
            /* Pokud má alespoň xí bodů. */
            if (c.points.size() >= xi) {
                /* Najdi spojení krychlí. */
                getCubeConnections(c);
                /* Přidej krychli. */
                highPopCubes.add(c);
            }
        }
    }

    /**
     * Metoda vytvářející spojení krychlí.
     *
     * @param c krychle
     */
    public void getCubeConnections(Cube c) {
        /* Pokud prostor neobsahuje krychli, */
        if (!cubeMap.containsKey(c.key)) {
            /* tak ji tam přidá. */
            cubeMap.put(c.key, c);
        }

        /* Pro všechny neprázdné krychle: */
        for (Cube popC : popCubes.values()) {
            /* Pokud je vzdálenost menší, */
            if (c.getDistance(popC) < (4 * sigma)) {
                /* Přidej klíč do sousedících. */
                c.neighbors.add(popC.key);

                /* Pokud prostor neobsahuje krychli, */
                if (!cubeMap.containsKey(popC.key)) {
                    /* Přidej ji. */
                    cubeMap.put(popC.key, popC);
                }
            }
        }
    }

}

