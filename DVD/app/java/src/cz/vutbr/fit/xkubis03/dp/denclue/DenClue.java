/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.denclue;

import java.util.HashMap;
import java.util.Map;

import cz.vutbr.fit.xkubis03.dp.structures.Clusters;
import cz.vutbr.fit.xkubis03.dp.structures.Cube;
import cz.vutbr.fit.xkubis03.dp.structures.Point;
import cz.vutbr.fit.xkubis03.dp.structures.PointSet;
import cz.vutbr.fit.xkubis03.dp.structures.Space;

/**
 * Třída pro algoritmus DENCLUE.
 */
public class DenClue {

    /** Množina shlukovaných bodů. */
    private PointSet set;
    /** Vliv bodu v prostoru. */
    private double sigma;
    /** Parametr významnosti atraktoru. */
    private int xi;
    /** Prostor vektorů. */
    private Space space;
    /** Atraktory, mapuje bod na body v okolí. */
    private Map<Point, PointSet> attractors;
    /** Maximální počet iterací algoritmu */
    private int maxIterations;
    /** Parametr delta algoritmu */
    private double delta;

    /**
     * Konstruktor pro algoritmus DENCLUE.
     *
     * @param set množina shlukovaných bodů
     * @param sigma vliv bodu
     */
    public DenClue(PointSet set, double sigma) {
        /* Inicializace třídy z parametrů */
        this.set = set;
        this.sigma = sigma;
        /* Inicializace struktur */
        this.space = new Space(this.sigma, this.xi);
        this.attractors = new HashMap<Point, PointSet>();

        /* Inicializace implicitní */
        this.maxIterations = 5;
        this.delta = 1.0;
        this.xi = 4;
    }

    /**
     * Konstruktor pro algoritmus DENCLUE.
     *
     * @param set množina shlukovaných bodů
     * @param sigma vliv bodu
     * @param xi významnost atraktoru
     */
    public DenClue(PointSet set, double sigma, int xi) {
        /* Inicializace třídy z parametrů */
        this(set, sigma);
        this.xi = xi;
    }

    /**
     * Konstruktor pro algoritmus DENCLUE.
     *
     * @param set množina shlukovaných bodů
     * @param sigma vliv bodu
     * @param xi významnost atraktoru
     * @param maxIterations maximální počet iterací algoritmu
     */
    public DenClue(PointSet set, double sigma, int xi, int maxIterations) {
        /* Inicializace třídy z parametrů */
        this(set, sigma, xi);
        this.maxIterations = maxIterations;
    }

    /**
     * Konstruktor pro algoritmus DENCLUE.
     *
     * @param set množina shlukovaných bodů
     * @param sigma vliv bodu
     * @param xi významnost atraktoru
     * @param maxIterations maximální počet iterací algoritmu
     * @param delta parametr delta algoritmu
     */
    public DenClue(PointSet set, double sigma, int xi, int maxIterations, double delta) {
        /* Inicializace třídy z parametrů */
        this(set, sigma, xi, maxIterations);
        this.delta = delta;
    }

    /**
     * Metoda vracející výsledné shluky.
     * Její zavolání provádí samotný algoritmus. 
     *
     * @return Vrací výsledné shluky
     */
    public Clusters getClusters() {
        /* Inicializace shluků */
        Clusters cls = new Clusters();

        /* Přidání bodů do prostoru */
        for (Point p : set) {
            space.addPoint(p);
        }

        /* Vytvoření propojení v mapě */ 
        space.connectMap();

        /* Určení atraktorů */
        determineAttractors();

        /* Shlukování bodů */
        while (mergeClusters());

        /* Vkládání shluků do návratové proměnné */
        for (Point p : attractors.keySet()) {
            /* Pouze pokud mají dostatek bodů */
            //if (attractors.get(p).size() > xi) {
                cls.add(attractors.get(p));
            //}
        }

        /* Vrácení shluků */
        return cls;
    }

    /**
     * Metoda provádějící shlukování bodů
     *
     * @return Vrací logickou hodnotu, zda se provedlo shlukování.
     */
    private boolean mergeClusters() {
        /* Pro každý atraktor */
        for (Point p1 : attractors.keySet()) {
            /* a každý atraktor */
            for (Point p2 : attractors.keySet()) {
                /* Totožné atraktory vynecháme */
                if (p1.equals(p2)) {
                    continue;
                }
 
                /* Získání bodů pro atraktor 1 */
                PointSet p1set = attractors.get(p1);
                /* Získání bodů pro atraktor 2 */
                PointSet p2set = attractors.get(p2);

                /* Pokud existuje mezi shluky cesta */
                if (pathExist(p1, p1set, p2, p2set)) {
                    /* Nový shluk */
                    PointSet all = new PointSet();
                    /* Atraktor pro nový shluk */
                    Point allPoint = p1;

                    /* Přidání bodů z první množiny */
                    all.addAll(p1set);
                    /* Přidání bodů z druhé množiny */
                    all.addAll(p2set);

                    /* Odebrání bodů z atraktorů */
                    attractors.remove(p1);
                    attractors.remove(p2);

                    /* Vložení nového atraktoru a jeho bodů */
                    attractors.put(allPoint, all);

                    /* Bylo provedeno sloučení */
                    return true;
                }
            }
        }

        /* Sloučení neproběhlo = KONEC */
        return false;
    }

    /** Metoda pro určení atraktorů */
    private void determineAttractors() {
        /* Pro každou krychli v prostoru */
        for (Cube c : space.cubeMap.values()) {
            /* Pro každý bod v krychli */
            for (Point p : c.points) {
                /* Výpočet hustoty bodu */
                p.density = calcDensity(p);

                /* Nový atraktor hustoty */
                Point densPoint = getDensAttractor(p, maxIterations, delta);

                /* Pokud není atraktor v atraktorech */
                if (!attractors.containsValue(densPoint)) {
                    /* Vytvoří se mu nová množina */
                    PointSet set = new PointSet();

                    /* Přidá se do ní bod */
                    set.add(p);

                    /* Vloží se nový atraktor */
                    attractors.put(densPoint, set);
                }
            }
        }
    }

    /**
     * Funkce pro výpočet vlivu bodu na jiný bod.
     *
     * @param p1 první bod pro výpočet
     * @param p2 druhý bod pro výpočet
     *
     * @return Velikost vlivu mezi body.
     */
    private double calcInfluence(Point p1, Point p2) {
        /* Vzdálenost mezi body */
        double distance = p1.getDistance(p2);
        
        /* Pokud je vzdálenost nulová, je nulový i vliv */
        if (distance == 0.0) {
            return 0.0;
        }

        /* Výpočet exponentu */
        double exponent = - (distance * distance) / (2.0 * (sigma * sigma));
        /* Vrácení vlivu */
        return Math.exp(exponent);
    }

    /**
     * Funkce pro výpočet hustoty bodu.
     *
     * @param p bod pro výpočet
     *
     * @return Vrací hustotu bodu.
     */
    private double calcDensity(Point p) {
        /* Hustota */
        double dens = 0.0;

        /* Pro každou krychli v prostoru */
        for (Cube c : space.cubeMap.values()) {
            /* Pro každý bod v krychli */
            for (Point cubeP : c.points) {
                /* Součet vlivů */
                dens += calcInfluence(p, cubeP);
            }
        }

        /* Vrací hustotu */
        return dens;
    }

    /**
     * Funkce pro výpočet stoupání metodou hill-climbing.
     *
     * @param p bod, pro který se počítá stoupání
     *
     * @return Vrací stoupání pro bod.
     */
    private double[] calcGradient(Point p) {
        /* Stoupání bodu */
        double[] gradient = new double[p.coords.length];

        /* Pro každou krychli */
        for (Cube c : space.cubeMap.values()) {
            /* Pro každý bod krychle */
            for (Point cubeP : c.points) {
                /* Vypočítej vliv bodu na bod */
                double influence = calcInfluence(p, cubeP);

                /* Vypočítej stoupání */
                for (int index = 0; index < p.coords.length; index++) {
                    /* Součet souřadnic s vlivem */
                    gradient[index] += (cubeP.coords[index] - p.coords[index]) * influence;
                }
            }
        }

        /* Vrací nové stoupání */
        return gradient;
    }

    /**
     * Funkce pro získání nového atraktoru.
     *
     * @param p současný atraktor
     * @param maxIterations maximální počet iterací algoritmu
     * @param delta parametr delta algoritmu
     *
     * @return Vrací nový atraktor.
     */
    private Point getDensAttractor(Point p, int maxIterations, double delta) {
        /* Aktuální atraktor */
        Point curAttractor = new Point(p.coords);
        /* Předchozí atraktor */
        Point resAttractor = null;
        /* Pokud je vrchol */
        boolean isTop = false;

        /* Výpočet atraktoru */
        while (!isTop) {
            /* Pokud byl dosažen maximální počet iterací */
            if (--maxIterations <= 0) {
                /* Konči smyčku */
                break;
            }

            /* Uložení předchozího atraktoru */
            Point prevAttractor = curAttractor;

            /* Získání aktuálního stoupání */
            double[] curGradient = calcGradient(prevAttractor);

            /* Bod stoupání */
            Point gradientPoint = new Point(curGradient);

            /* Následující stoupání */
            double nextGradient = gradientPoint.getEuclidean();

            /* Předchozí atraktor se stane současným */
            curAttractor = prevAttractor;

            /* Výpočet souřadnic atraktoru */
            for (int index = 0; index < curGradient.length; index++) {
                curAttractor.coords[index] += (delta / nextGradient * gradientPoint.coords[index]);
            }

            /* Výpočet hustoty aktuálního atraktoru */
            curAttractor.density = calcDensity(curAttractor);

            /* Pokud byla překročena hustota */
            isTop = (curAttractor.density < prevAttractor.density);
            if (isTop) {
                /* Výsledný atraktor je ten předchozí */
                resAttractor = prevAttractor;
            }
        }

        /* Pokud byl překročen počet iterací */
        if (maxIterations <= 0) {
            /* Výsledný atraktor je ten aktuální */
            resAttractor = curAttractor;
        }

        /* Vrácení atraktoru */
        return resAttractor;
    }

    /**
     * Funkce zjišťující, zda existuje cesta mezi body.
     *
     * @param p1 první bod
     * @param p1set body prvního bodu
     * @param p2 druhý bod
     * @param p2set body druhého bodu
     * 
     * @return Vrací logickou hodnotu, zda existuje cesta.
     */
    private boolean pathExist(Point p1, PointSet p1set, Point p2, PointSet p2set) {
        /* Pokud je vzdálenost menší než sigma, tak existuje */
        if (p1.getDistance(p2) <= sigma) {
            /* Existuje */
            return true;
        }

        /* Kontrola, zda existuje cesta mezi nějakými body */
        for (Point depPoint1 : p1set) {
            for (Point depPoint2 : p2set) {
                /* Vzdálenost musí být menší než sigma */
                if (depPoint1.getDistance(depPoint2) <= sigma) {
                    /* Existuje */
                    return true;
                }
            }
        }

        /* Neexistuje */
        return false;
    }

}

