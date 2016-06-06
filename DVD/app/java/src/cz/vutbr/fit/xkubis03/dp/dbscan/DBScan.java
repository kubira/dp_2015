/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.dbscan;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.Collections;

import cz.vutbr.fit.xkubis03.dp.structures.Clusters;
import cz.vutbr.fit.xkubis03.dp.structures.Point;
import cz.vutbr.fit.xkubis03.dp.structures.PointSet;

/**
 * Třída pro algoritmus DBSCAN.
 */
public class DBScan {

    /** Množina bodů pro shlukování. */
    private PointSet set;
    /** Parametr epsilon pro okolí bodů. */
    private double eps;
    /** Minimální počet bodů. */
    private int minPts;
    /** Nezpracované body */
    private Set<Point> unprocess;
    /** Shluknuté body */
    private Set<Point> clustered;

    
    /**
     * Konstruktor třídy algoritmu DBSCAN.
     *
     * @param set množina bodů pro shlukování.
     * @param eps epsilon pro okolí.
     */
    public DBScan(PointSet set, double eps) {
        /* Inicializace z parametrů. */
        this.set = set;
        this.eps = eps;
        /* Implicitní hodnota dle dokumentu pro 2D databáze */
        this.minPts = 4;
        
        /* Inicializace struktur. */
        this.unprocess = Collections.newSetFromMap(new ConcurrentHashMap<Point, Boolean>());
        this.clustered = Collections.newSetFromMap(new ConcurrentHashMap<Point, Boolean>());
    }
    
    /**
     * Konstruktor třídy algoritmu DBSCAN.
     *
     * @param set množina bodů pro shlukování.
     * @param eps epsilon pro okolí.
     * @param minPts minimální počet bodů pro shluk.
     */
    public DBScan(PointSet set, double eps, int minPts) {
        /* Inicializace z parametrů. */
        this.set = set;
        this.eps = eps;
        this.minPts = minPts;
        
        /* Inicializace struktur. */
        this.unprocess = Collections.newSetFromMap(new ConcurrentHashMap<Point, Boolean>());
        this.clustered = Collections.newSetFromMap(new ConcurrentHashMap<Point, Boolean>());
    }

    /**
     * Metoda pro samotné shlukování algoritmem DBSCAN.
     *
     * @return Vrací výsledné shluky.
     */
    public Clusters getClusters() {
        /* Výsledné shluky. */
        Clusters cls = new Clusters();

        /* Přidej všechny body do nezpracovaných. */
        unprocess.addAll(set);

        /* Projdi všechny nezpracované body. */
        for (Point p : unprocess) {
            /* Odstraň aktuální bod z nezpracovaných. */
            unprocess.remove(p);

            /* Získání sousedů bodu. */
            PointSet neighbors = getNeighbors(p);

            /* Pokud má alespoň minPts sousedů. */
            if (neighbors.size() >= minPts) {
                /* Vytvoří se shluk. */
                PointSet clstr = new PointSet();

                /* Rozšíření shluku. */
                enlargeCluster(neighbors, clstr);

                /* Pokud má dostatek bodů. */
                if (clstr.size() >= minPts) {
                    /* Ulož shluk. */
                    cls.add(clstr);
                } else if (clstr.size() > 0) {
                    /* Pokud nemá dost bodů, tak ho taky ulož,
                     * aby se zobrazily shluky s méně body.
                     */
                    cls.add(clstr);
                }
            } else {
                /* Pokud nemá dost sousedů,
                 * tak to není shluk, ale
                 * chceme ho zobrazit taky.
                 */

                /* Vytvoř nový shluk. */
                PointSet out = new PointSet();

                /* Přidej do něj osamocený bod. */
                out.add(p);

                /* Přidej shluk s bodem do výsledných shluků. */
                cls.add(out);
                /* Přidej bod do shluknutých. */
                clustered.add(p);
            }
        }

        /* Navrácení výsledných shluků. */
        return cls;
    }

    /**
     * Funkce pro rozšíření shluku.
     *
     * @param neighbors sousedé shluku.
     * @param clstr shluk.
     */
    private void enlargeCluster(PointSet neighbors, PointSet clstr) {
        /* Projdi všechny sousedy. */
        for (int i = 0; i < neighbors.size(); i++) {
            /* Jeden soused. */
            Point p = neighbors.get(i);

            /* Pokud nebyl soused zpracován. */
            if (unprocess.contains(p)) {
                /* Odeber ho z množiny nezpracovaných. */
                unprocess.remove(p);

                /* Získej jeho sousedy. */
                PointSet neighs = getNeighbors(p);

                /* Pokud má alespoň minPts sousedů. */
                if (neighs.size() >= minPts) {
                    /* Přidej je do sousedů shluku. */
                    neighbors.addAll(neighs);
                }
            }

            /* Pokud není bod ve shluknutých. */
            if (!clustered.contains(p)) {
                /* Přidej ho do shluku. */
                clstr.add(p);
                /* Přidej ho do shluknutých. */
                clustered.add(p);
            }
        }
    }

    /**
     * Funkce pro získání sousedů bodu.
     *
     * @param p bod pro sousedy.
     *
     * @return Vrací sousedy bodu.
     */
    private PointSet getNeighbors(Point p) {
        /* Vytvoř množinu pro sousedy. */
        PointSet neighs = new PointSet();

        /* Pro všechny body. */
        for (Point pnt : set) {
            /* Zjisti vzdálenost k aktuálnímu bodu. */
            double dist = p.getDistance(pnt);

            /* Pokud je vzdálenost menší nebo rovna epsilon. */
            if (dist <= eps) {
                /* Je to soused - přidej do sousedů. */
                neighs.add(pnt);
            }
        }

        /* Vrať sousedy bodu. */
        return neighs;
    }
}

