import java.io.Serializable;

public class CoordsMsg implements Serializable {
    private static final long serialVersionUID = 8564362918537326616L;
    private Double[][] coords;

    public CoordsMsg(Double[][] coords) {
      this.coords = coords;
    }

    public Double[][] getCoords() {
      return coords;
    }

    @Override
    public String toString() {
      String meme = "";
      for(Double[]x : coords){
        for(Double y : x) {
          meme += y + ",";
        }
        meme += " ";
      }
      return meme;
    }
}
