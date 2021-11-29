/*************************************************************************
 *  Compilation:  javac ArtCollage.java
 *  Execution:    java ArtCollage Flo2.jpeg
 *
 *  @author:J.T. McQuigg, jtm258@scarletmail.rutgers.edu, jtm258
 *
 *************************************************************************/

import java.awt.Color;

public class ArtCollage {

    // The orginal picture
    private Picture original;

    // The collage picture
    private Picture collage;

    // The collage Picture consists of collageDimension X collageDimension tiles
    private int collageDimension;

    // A tile consists of tileDimension X tileDimension pixels
    private int tileDimension;
    
    /*
     * One-argument Constructor
     * 1. set default values of collageDimension to 4 and tileDimension to 100
     * 2. initializes original with the filename image
     * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension, 
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage (String filename) {

	    // WRITE YOUR CODE HERE
        this.original = new Picture(filename);
        this.collageDimension = 4;
        this.tileDimension = 100;
        this.collage = new Picture((collageDimension * tileDimension),(collageDimension * tileDimension));
        
        for(int width = 0; width < (collageDimension * tileDimension); width++){
            for(int height = 0; height < (collageDimension * tileDimension); height++){
                int col = width * original.width() / (collageDimension * tileDimension);
                int row = height * original.height() / (tileDimension * collageDimension);
                Color color = original.get(col, row);
                collage.set(width, height, color);
            }
        }

    }

    /*
     * Three-arguments Constructor
     * 1. set default values of collageDimension to cd and tileDimension to td
     * 2. initializes original with the filename image
     * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension, 
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage (String filename, int td, int cd) {

	    // WRITE YOUR CODE HERE
        this.original = new Picture(filename);
        this.collageDimension = cd;
        this.tileDimension = td;
        this.collage = new Picture(collageDimension * tileDimension,collageDimension * tileDimension);
        
        for(int width = 0; width < (collageDimension * tileDimension); width++){
            for(int height = 0; height < (collageDimension * tileDimension); height++){
                int col = width * original.width() / (collageDimension * tileDimension);
                int row = height * original.height() / (tileDimension * collageDimension);
                Color color = original.get(col, row);
                collage.set(width, height, color);
            }
        }
    }

    /*
     * Returns the collageDimension instance variable
     *
     * @return collageDimension
     */
    public int getCollageDimension() {

	    // WRITE YOUR CODE HERE
        return collageDimension;
    }

    /*
     * Returns the tileDimension instance variable
     *
     * @return tileDimension
     */
    public int getTileDimension() {

	    // WRITE YOUR CODE HERE
        return tileDimension;
    }

    /*
     * Returns original instance variable
     *
     * @return original
     */
    public Picture getOriginalPicture() {

	    // WRITE YOUR CODE HERE
        return original;
    }

    /*
     * Returns collage instance variable
     *
     * @return collage
     */
    public Picture getCollagePicture() {

	    // WRITE YOUR CODE HERE
        return collage;
    }
    
    /*
     * Display the original image
     * Assumes that original has been initialized
     */
    public void showOriginalPicture() {

	    // WRITE YOUR CODE HERE
        original.show();
    }

    /*
     * Display the collage image
     * Assumes that collage has been initialized
     */
    public void showCollagePicture() {

	    // WRITE YOUR CODE HERE
        collage.show();
    }

    /*
     * Replaces the tile at collageCol,collageRow with the image from filename
     * Tile (0,0) is the upper leftmost tile
     *
     * @param filename image to replace tile
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void replaceTile (String filename,  int collageCol, int collageRow) {

	    // WRITE YOUR CODE HERE
        
        Picture file = new Picture(filename);
        int x = getTileDimension();
        Picture a = new Picture(x, x);

        for(int col = 0; col < x; col++ ){
            for(int row = 0; row < x; row++){
                int OriginalRow = row * file.height() / x;
                int OriginalCol = col * file.width() / x; 
                Color color = file.get(OriginalCol, OriginalRow);
                a.set(col, row, color); 
            }
        }

        for(int row = 0; row < x; row++){
            for(int col = 0; col < x; col++ ){
                Color color = a.get(col, row);
                collage.set(col + (x * collageCol), row + (x * collageRow), color);           
            }
        }



        

    }
    
    /*
     * Makes a collage of tiles from original Picture
     * original will have collageDimension x collageDimension tiles, each tile
     * has tileDimension X tileDimension pixels
     */
    public void makeCollage () {

	    // WRITE YOUR CODE HERE
        Picture file = getOriginalPicture();
        int x = getTileDimension();
        Picture a = new Picture(x, x);
        int collageCol = 0;
        int collageRow = 0;
        int cd = getCollageDimension();

        for(int width = 0; width < x; width++){
            for(int height = 0; height < x; height++){
                int col = width * file.width() / x;
                int row = height * file.height() / x;
                Color color = original.get(col, row);
                a.set(width, height, color);
            }
        }
        for(int width = 0; width < (cd * x); width++){

            if(x == collageCol){
                collageCol = 0;
            }
            for(int height = 0; height < (cd * x); height++){
                if(x == collageRow){
                    collageRow = 0;
                }
                Color color = a.get(collageCol, collageRow);
                collage.set(width, height, color);
                collageRow++;
            }
            collageCol++;
        }

    }

    /*
     * Colorizes the tile at (collageCol, collageRow) with component 
     * (see CS111 Week 9 slides, the code for color separation is at the 
     *  book's website)
     *
     * @param component is either red, blue or green
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void colorizeTile (String component,  int collageCol, int collageRow) {

	    // WRITE YOUR CODE HERE
        int tileresolution = getTileDimension();
        int x = collageCol * tileresolution;
        int y = collageRow * tileresolution;
        for(int i = 0 + x; i < x + tileresolution; i++){
            for(int j = 0 + y; j < y + tileresolution; j++){
                Color color = new Color(collage.getRGB(i, j));
                int R = 0, G = 0, B = 0;
                if(component.equalsIgnoreCase("red")){    
                    R = color.getRed();
                }
                else if(component.equalsIgnoreCase("green")){
                    G = color.getGreen();
                }
                else
                {
                    B = color.getBlue();          
                }
                Color spot1 = new Color(R, G, B);
                collage.set(i, j, spot1);
            }
        }

    }

    /*
     * Grayscale tile at (collageCol, collageRow)
     * (see CS111 Week 9 slides, the code for luminance is at the book's website)
     *
     * @param collageCol tile column
     * @param collageRow tile row
     */

    public void grayscaleTile (int collageCol, int collageRow) {

	    // WRITE YOUR CODE HERE
        int tileresolution = getTileDimension();
        int x = collageCol * tileresolution;
        int y = collageRow * tileresolution;
        for(int i = 0 + x; i < x + tileresolution; i++){
            for(int j = 0 + y; j < y + tileresolution; j++){
                Color color = new Color(collage.getRGB(i, j));
                int R = 0, G = 0, B = 0;
                    R = color.getRed();
                    G = color.getGreen();
                    B = color.getBlue(); 
                    double lum = (.299 * R) + (.587 * G) +(.114 * B);         
 
                Color spot1 = new Color((int)lum, (int)lum, (int)lum);
                collage.set(i, j, spot1);
            }
        }
    }


    /*
     *
     *  Test client: use the examples given on the assignment description to test your ArtCollage
     */
    public static void main (String[] args) {

        ArtCollage art = new ArtCollage(args[0]); 
        art.showCollagePicture();
    }
}
