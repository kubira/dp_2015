/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;

import cz.vutbr.fit.xkubis03.dp.structures.DistanceMatrixItem;
import cz.vutbr.fit.xkubis03.dp.readers.CSVReader;
import cz.vutbr.fit.xkubis03.dp.readers.FASTAReader;
import cz.vutbr.fit.xkubis03.dp.readers.VectorsReader;
import cz.vutbr.fit.xkubis03.dp.readers.DistanceMatrixReader;
import cz.vutbr.fit.xkubis03.dp.structures.DistanceMatrix;
import cz.vutbr.fit.xkubis03.dp.structures.FASTAItem;
import cz.vutbr.fit.xkubis03.dp.structures.CSVLine;
import cz.vutbr.fit.xkubis03.dp.structures.Vector;
import cz.vutbr.fit.xkubis03.dp.fitclust.Alignment;
import cz.vutbr.fit.xkubis03.dp.fitclust.FitClust;

/**
 * Třída spouštějící nástroj FitClust. Její použití viz diplomová práce.
 */
public class Main {

    /**
     * Hlavní funkce pro spuštění nástroje
     *
     * @param args argumenty příkazového řádku
     */
    public static void main(String[] args) {
        try {
            /**
             * Hlavní objekt nástroje, který je třeba nastavit. Buď ručně zde,
             * nebo jej spouštět skriptem 'run' s parametry na příkazové řádce.
             */
            FitClust fc = new FitClust(
                    Double.parseDouble(args[0]), /* Práh podobnosti semínek */
                    args[1], /* Metoda zarovnání sekvencí */
                    args[2], /* Metoda shlukování sekvencí */
                    Double.parseDouble(args[3]), /* Parametr sigma nebo eps */
                    Integer.parseInt(args[4]), /* Parametr xí nebo minPts */
                    Integer.parseInt(args[5]), /* Parametr maxIterations */
                    Double.parseDouble(args[6]) /* Parametr delta */
                );
            /**
             * Z následujících řádků je třeba odkomentovat ty,
             * které mají být použity nástrojem.
             */
            // fc.loadFasta("<cesta>"); /* Načítá FASTA záznamy ze souboru */
            // fc.writeFasta("<cesta>"); /* Zapisuje seřazené záznamy do souboru */
            // fc.loadVectors("<cesta>"); /* Načítá vektory ze souboru */
            // fc.selectSeeds(); /* Vybírá semínka ze vstupních FASTA záznamů */
            // fc.excludeSeeds(); /* Vylučuje podobná si semínka */
            // fc.vectorize(); /* Provádí vektorizaci se semínky */
            // fc.writeVectors("<cesta>"); /* Zapisuje vektory do souboru */
            // fc.doClustering(); /* Provádí shlukování */
            // fc.writeCls("<cesta>"); /* Zapisuje shluky do souboru */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
