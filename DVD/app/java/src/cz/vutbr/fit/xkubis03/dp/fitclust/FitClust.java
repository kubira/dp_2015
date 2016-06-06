/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.fitclust;

import cz.vutbr.fit.xkubis03.dp.structures.FASTAItem;
import cz.vutbr.fit.xkubis03.dp.structures.Vector;
import cz.vutbr.fit.xkubis03.dp.structures.FitClustItem;
import cz.vutbr.fit.xkubis03.dp.structures.Clusters;
import cz.vutbr.fit.xkubis03.dp.structures.Point;
import cz.vutbr.fit.xkubis03.dp.structures.PointSet;
import cz.vutbr.fit.xkubis03.dp.structures.DistanceMatrix;
import cz.vutbr.fit.xkubis03.dp.structures.DistanceMatrixKey;
import cz.vutbr.fit.xkubis03.dp.structures.DistanceMatrixItem;
import cz.vutbr.fit.xkubis03.dp.readers.FASTAReader;
import cz.vutbr.fit.xkubis03.dp.readers.VectorsReader;
import cz.vutbr.fit.xkubis03.dp.readers.DistanceMatrixReader;
import cz.vutbr.fit.xkubis03.dp.denclue.DenClue;
import cz.vutbr.fit.xkubis03.dp.dbscan.DBScan;

import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * Hlavní třída nástroje využívající ostatní balíčky.
 */
public class FitClust {

    /** Práh podobnosti semínek. */
    private double threshold;
    /** Metoda zarovnání. */
    private String alignment;
    /** Metoda shlukování. */
    private String clustering;
    /** Semínka. */
    private ArrayList<FASTAItem> seeds;
    /** Matice vzdáleností. */
    private DistanceMatrix dm;
    /** Seznam FASTA záznamů. */
    private ArrayList<FASTAItem> sequences;
    /** Seznam vektorizovaných záznamů. */
    private ArrayList<FitClustItem> vectorized;
    /** Shluky. */
    private Clusters clusters;
    /** Parametr sigma. */
    private double sigma;
    /** Parametr xí. */
    private int xi;
    /** Parametr počtu iterací. */
    private int maxIterations;
    /** Parametr delta. */
    private double delta;
    /** Parametr epsilon. */
    private double eps;
    /** Parametr minPts. */
    private int minPts;

    /**
     * Konstruktor pro hlavní funkční třídu nástroje.
     *
     * @param threshold práh podobnosti pro semínka
     * @param alignment metoda zarovnání
     * @param clustering metoda shlukování
     * @param param parametr sigma nebo epsilon
     * @param number parametr xí nebo minPts
     * @param maxIterations maximální počet iterací hill-climbing
     * @param delta parametr delta pro hill-climbing
     */
    public FitClust(double threshold, String alignment, String clustering, double param, int number, int maxIterations, double delta) {
        /* Nastavení hodnot z parametrů konstruktoru. */
        System.out.println("Initializing FitClust...");
        this.threshold = threshold;
        System.out.println("  - seeds threshold: "+this.threshold);
        this.alignment = alignment;
        System.out.println("  - sequences alignment: "+this.alignment);
        this.clustering = clustering;
        System.out.println("  - clustering: "+this.clustering);
        if (this.clustering.equals("DENCLUE")) {
            this.sigma = param;
            System.out.println("  - sigma: "+this.sigma);
            this.xi = number;
            System.out.println("  - xi: "+this.xi);
            this.maxIterations = maxIterations;
            System.out.println("  - maxIterations: "+this.maxIterations);
            this.delta = delta;
            System.out.println("  - delta: "+this.delta);
        } else if (this.clustering.equals("DBSCAN")) {
            this.eps = param;
            System.out.println("  - epsilon: "+this.eps);
            this.minPts = number;
            System.out.println("  - minPts: "+this.minPts);
        } else {
            System.err.println("WARNING: Clustering algorithm "+clustering+" is not supported.");
        }
        System.out.println("FitClust initialized.");
    }

    /**
     * Metoda pro načtení FASTA záznamů ze souboru.
     *
     * @param path cesta k souboru
     */
    public void loadFasta(String path) {
        System.out.println("Loading FASTA file "+path+"...");
        try {
            FileInputStream fis = new FileInputStream(path);
            sequences = FASTAReader.getSequences(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File "+path+" not found.");
            System.err.println("Exiting...");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sequences.size()+" sequences loaded from FASTA file "+path+".");
    }

    /**
     * Metoda pro zápis shluků do souboru.
     *
     * @param path cesta k souboru
     */
    public void writeCls(String path) {
        try {
            PrintWriter writer = new PrintWriter(path, "UTF-8");

            System.out.println("Writing clusters to file "+path+"...");
            System.out.format("\rCompleted: %10d of %10d", 0, clusters.size());
            int i = 0;
            for (i = 0; i < clusters.size(); i++) {
                if (clusters.get(i).size() > 0) {
                    writer.println(clusters.get(i).getCls());
                }
                System.out.format("\rCompleted: %10d of %10d", i, clusters.size());
            }
            System.out.format("\rCompleted: %10d of %10d\n", i, clusters.size());
            System.out.println("Clusters written.");

            writer.close();
       } catch (FileNotFoundException e) {
            System.err.println("ERROR: Clusters file "+path+" not found.");
            System.err.println("Exiting...");
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            System.err.println("ERROR: Unsupported encoding at file "+path+".");
            System.err.println("Exiting...");
            System.exit(1);
        }
    }

    /**
     * Metoda pro zápis vzdálenostní matice do souboru.
     *
     * @param path cesta k souboru
     */
    public void writeDistanceMatrix(String path) {
        try {
            PrintWriter writer = new PrintWriter(path, "UTF-8");

            System.out.println("Writing distance matrix to file "+path+"...");
            System.out.format("\rCompleted: %10d of %10d", 0, dm.size());

            int i = 0;
            for (DistanceMatrixKey dmk : dm.keySet()) {
                System.out.format("\rCompleted: %10d of %10d", i, dm.size());
                writer.println(dmk+";"+dm.get(dmk));
                i++;
            }
            System.out.format("\rCompleted: %10d of %10d\n", i, dm.size());
            System.out.println("Distance matrix written.");

            writer.close();
       } catch (FileNotFoundException e) {
            System.err.println("ERROR: Distance matrix file "+path+" not found.");
            System.err.println("Exiting...");
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            System.err.println("ERROR: Unsupported encoding at file "+path+".");
            System.err.println("Exiting...");
            System.exit(1);
        }
    }

    /**
     * Metoda pro načtení vektorů ze souboru.
     *
     * @param path cesta k souboru
     */
    public void loadVectors(String path) {
        System.out.println("Loading Vector file "+path+"...");
        try {
            FileInputStream fis = new FileInputStream(path);
            ArrayList<Vector> vecs = VectorsReader.getVectors(fis);
            vectorized = new ArrayList<FitClustItem>();
            for (Vector v : vecs) {
                vectorized.add(new FitClustItem(null, v));
            }
            fis.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File "+path+" not found.");
            System.err.println("Exiting...");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(vectorized.size()+" vectors loaded from vector file "+path+".");
    }

    /**
     * Metoda pro načtení vzdálenostní matice ze souboru.
     *
     * @param path cesta k souboru
     */
    public void loadDistanceMatrix(String path) {
        System.out.println("Loading DistanceMatrix file "+path+"...");
        try {
            FileInputStream fis = new FileInputStream(path);
            dm = DistanceMatrixReader.getMatrix(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File "+path+" not found.");
            System.err.println("Exiting...");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(dm.size()+" rows loaded from matrix file "+path+".");
    }

    /**
     * Metoda pro výběr semínek ze vstupních sekvencí.
     */
    public void selectSeeds() {
        seeds = new ArrayList<FASTAItem>();
        
        int numberOfItems = sequences.size();
        double log2 = (Math.log(numberOfItems) / Math.log(2));
        int numberOfSeeds = (new Double((Math.pow(log2, 2)))).intValue();
        
        System.out.println(numberOfItems+" items => "+numberOfSeeds+" seeds.");

        int stride = (new Double(Math.log10(numberOfItems))).intValue();

        System.out.println("Selecting "+numberOfSeeds+" seeds...");

        for (int i = 0; i < numberOfSeeds; i++) {
            System.out.format("\rSelected: %10d of %10d", seeds.size(), numberOfSeeds);
            seeds.add(sequences.get(i * stride));
        }
        System.out.format("\rSelected: %10d of %10d\n", seeds.size(), numberOfSeeds);
        System.out.println(seeds.size()+" seeds selected.");
    }

    /**
     * Metoda pro vyloučení podobných si semínek.
     */
    public void excludeSeeds() {
        int size = seeds.size();
        System.out.println("Excluding seeds (threshold = "+threshold+")...");

        int actualIndex = 1;
        boolean found = false;

        System.out.format("\rProcessed: %10d of %10d", 0, size);

        while (actualIndex < seeds.size()) {
            for (int i = 0; i < actualIndex; i++) {
                if (align(seeds.get(actualIndex), seeds.get(i)) <= threshold) {
                    seeds.remove(seeds.get(actualIndex));
                    found = true;
                    break;
                }
            }

            System.out.format("\rProcessed: %10d of %10d", actualIndex, seeds.size());

            if (!found) {
                actualIndex++;
            } else {
                found = false;
            }
        }

        System.out.format("\rProcessed: %10d of %10d\n", actualIndex, seeds.size());

        System.out.println((size - seeds.size())+" seeds from "+size+" excluded.");
        System.out.println("Available seeds: "+seeds.size());
    }

    /**
     * Funkce pro zarovnání dvou FASTA sekvencí.
     */
    private double align(FASTAItem f1, FASTAItem f2) {
        if (alignment.equals("FASTA")) {
            try {
                return Alignment.alignFASTA(f1, f2);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        } else if(alignment.equals("BLAST")) {
            try {
                return Alignment.alignBLAST(f1, f2);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        System.err.println("ERROR: "+alignment+" alignment is not supported.");
        System.err.println("Exiting...");
        System.exit(1);

        return 0.0;
    }

    /**
     * Metoda pro analýzu potenciálních semínek - NENÍ IMPLEMENTOVANÁ.
     */
    public void analyzeSeeds() {
        int size = seeds.size();
        System.out.println("Analyzing potential seeds...");
        // TODO: Analyze
        System.out.println("WARNING: Analysis of potential seeds is not implemented.");
        size = seeds.size() - size;
        System.out.println(size+" seeds added.");
    }

    /**
     * Metoda pro vektorizaci vstupních sekvencí.
     */
    public void vectorize() {
        vectorized = new ArrayList<FitClustItem>();

        int size = sequences.size();
        System.out.println("Vectorizing "+size+" sequences...");

        for (int i = 0; i < size; i++) {
            Vector v = new Vector(sequences.get(i).getName());

            System.out.format("\rCompleted: %10d of %10d", i, size);
            
            for (int j = 0; j < seeds.size(); j++) {
                v.add(align(sequences.get(i), seeds.get(j)));
            }

            vectorized.add(new FitClustItem(sequences.get(i), v));
        }

        System.out.format("\rCompleted: %10d of %10d\n", size, size);

        System.out.println(sequences.size()+" sequences vectorized.");
    }

    /**
     * Metoda pro zápis seřazených sekvencí do FASTA souboru.
     *
     * @param path cesta k souboru
     */
    public void writeFasta(String path) {
        try {
            PrintWriter writer = new PrintWriter(path, "UTF-8");

            System.out.println("Writing FASTA to file "+path+"...");
            System.out.format("\rCompleted: %10d of %10d", 0, sequences.size());
            int i = 0;
            for (i = 0; i < sequences.size(); i++) {
                writer.println(sequences.get(i).toString());
                System.out.format("\rCompleted: %10d of %10d", i, sequences.size());
            }
            System.out.format("\rCompleted: %10d of %10d\n", i, sequences.size());
            System.out.println("FASTA written.");

            writer.close();
       } catch (FileNotFoundException e) {
            System.err.println("ERROR: FASTA file "+path+" not found.");
            System.err.println("Exiting...");
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            System.err.println("ERROR: Unsupported encoding at file "+path+".");
            System.err.println("Exiting...");
            System.exit(1);
        }
    }
    
    /**
     * Metoda pro zápis vektorů do souboru.
     *
     * @param path cesta k souboru
     */
    public void writeVectors(String path) {
        try {
            PrintWriter writer = new PrintWriter(path, "UTF-8");

            System.out.println("Writing vectors to file "+path+"...");
            System.out.format("\rCompleted: %10d of %10d", 0, vectorized.size());
            int i = 0;
            for (i = 0; i < vectorized.size(); i++) {
                writer.println(vectorized.get(i).getVector());
                System.out.format("\rCompleted: %10d of %10d", i, vectorized.size());
            }
            System.out.format("\rCompleted: %10d of %10d\n", i, vectorized.size());
            System.out.println("Vectors written.");

            writer.close();
       } catch (FileNotFoundException e) {
            System.err.println("ERROR: Vector file "+path+" not found.");
            System.err.println("Exiting...");
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            System.err.println("ERROR: Unsupported encoding at file "+path+".");
            System.err.println("Exiting...");
            System.exit(1);
        }
    }

    /**
     * Metoda pro výpočet vzdálenostní matice z vektorů.
     */
    public void makeMatrix() {
        if (clustering.equals("DENCLUE")) {
            System.out.println("INFO: Distance matrix is not necessary for DENCLUE algorithm.");
        } else if (clustering.equals("DBSCAN")) {
            System.out.println("INFO: Distance matrix is not necessary for DBSCAN algorithm.");
        } else {
            dm = new DistanceMatrix();
            System.out.println("Making distance matrix...");
            int wantSize = (vectorized.size() * (vectorized.size() - 1)) / 2;
            System.out.format("\rCompleted: %10d of %10d", dm.size(), wantSize);
            for (int i = 0; i < (vectorized.size() - 1); i++) {
                for (int j = (i + 1); j < vectorized.size(); j++) {
                   dm.put(
                       new DistanceMatrixKey(vectorized.get(i).getFASTAItem().getName(), vectorized.get(j).getFASTAItem().getName()),
                       vectorized.get(i).getDistance(vectorized.get(j))
                   ); 
                }
            }

            System.out.format("\rCompleted: %10d of %10d\n", dm.size(), wantSize);
            System.out.println("Distance matrix made.");
        }
    }

    /**
     * Metoda spouštějící proces shlukování.
     */
    public void doClustering() {
        System.out.println(clustering+" clustering...");
        if (clustering.equals("DENCLUE")) {
            doDENCLUE();
        } else if (clustering.equals("DBSCAN")) {
            doDBSCAN();
        } else {
            System.err.println("ERROR: "+clustering+" clustering is not supported.");
        }
        System.out.println("Sequences clustered.");
    }

    /**
     * Metoda provádějící DENCLUE shlukování.
     */
    private void doDENCLUE() {
        PointSet ps = new PointSet();

        for (FitClustItem fci : vectorized) {
            ps.add(new Point(fci.getVector()));
        }


        DenClue dc = new DenClue(ps, sigma, xi, maxIterations, delta);
        clusters = dc.getClusters();
        System.out.println(clusters);
    }

    /**
     * Metoda provádějící DBSCAN shlukování.
     */
    private void doDBSCAN() {
        PointSet ps = new PointSet();

        for (FitClustItem fci : vectorized) {
            ps.add(new Point(fci.getVector()));
        }

        DBScan dbs = new DBScan(ps, eps, minPts);
        clusters = dbs.getClusters();
        System.out.println(clusters);
    }

}

