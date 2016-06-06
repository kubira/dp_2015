/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.structures;

/**
 * Třída pro složený klíč matice vzdáleností.
 */
public class DistanceMatrixKey {

    /** Levý klíč. */
    protected String left;
    /** Pravý klíč. */
    protected String right;

    /**
     * Konstruktor klíče.
     *
     * @param left levý klíč
     * @param right pravý klíč
     */
    public DistanceMatrixKey(String left, String right) {
      /* Uložení levého klíče. */
      this.left = left;
      /* Uložení pravého klíče. */
      this.right = right;
    }
    
    /**
     * Funkce pro porovnání dvou objektů třídy.
     *
     * @param o porovnávaný objekt
     */
    @Override
    public boolean equals(Object o) {
      /* Identita -> true. */
      if (this == o) return true;
      
      /* Různé třídy objektů -> false. */
      if (!(o instanceof DistanceMatrixKey)) {
          return false;
      }
      
      /* Přetypování objektu. */
      DistanceMatrixKey key = (DistanceMatrixKey) o;
      /* Porovnání klíčů a vrácení hodnoty. */
      return left.equals(key.left) && right.equals(key.right);
    }

    /**
     * Metoda pro hashování.
     *
     * @return Vrací hash objektu.
     */
    @Override
    public int hashCode() {
      /* Získání hashe levého klíče. */
      int result = left.hashCode();
      /* Výpočet výsledného hashe. */
      result = 31 * result + right.hashCode();
      /* Vrácení hashe objektu. */
      return result;
    }

    /**
     * Metoda pro textovou reprezentaci objektu.
     *
     * @return Vrací řetězec klíč1;klíč2.
     */
    @Override
    public String toString() {
      /** Vrácení řetězce klíčů. */
      return left+";"+right;
    }

}
