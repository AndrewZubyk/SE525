import java.util.Scanner;

public class HungoverCockroach {

    public static void main(String[] args) {

        String[][] tileSet = buildTileset();

        System.out.println("test");

        printTileset(tileSet);

        System.out.println("Rows: " + tileSet.length);

        System.out.println("Columns: " + tileSet[0].length);

        tileSet = placeCockroach(tileSet);

        printTileset(tileSet);

        System.out.println(" ");

        moveCockroach(tileSet);
    }

    public static String[][] buildTileset(){
        /*
         * 
         * Function which builds tileset based on user input
         * All input is in "0 0" format and spliced properly to generate rows and columns
         * Takes in input from user for size of tileset, water location, and asprin location
         * Takes in: Void
         * Returns: tileSet in 2D Array format with strings
         * 
         */


        Scanner scanner = new Scanner(System.in);

        // Setup the tileset based on user parameters
        System.out.println("How many rows and columns would you like your room to have? (in format 'rows columns')");
        
        boolean validInput = false;
        int rows = -1;
        int columns = -1;
        
        while (!validInput) { // While to make sure input is done
            try {
                String input = scanner.nextLine();
                String[] split = input.split(" ");
                if (split.length != 2) { //Assure that the length when split is still 2 characters
                    throw new IllegalArgumentException("Please enter exactly two numbers separated by a space.");
                }
                rows = Integer.parseInt(split[0]);
                columns = Integer.parseInt(split[1]);
        
                if (rows < 2 || columns < 2) { // Catches to make sure the the setup tileset is in a 2x2 minimum
                    throw new IllegalArgumentException("Please enter numbers greater than 1 for both rows and columns.");
                }
        
                validInput = true;
            } catch (Exception e) { //Catch statement
                System.out.println(e.getMessage());
                System.out.println("Please enter valid row and column numbers.");
            }
        }
        
        String[][] tileSet = new String[rows][columns];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                tileSet[i][j] = "* ";
            }
        }
        
        printTileset(tileSet);

        System.out.println("What row and column to place the water (in format '1 1')");

        boolean validInputW = false;
        int wRow = -1;
        int wColumn = -1;

        while (!validInputW) { // While to make sure input is done
            try {
                String inputw = scanner.nextLine();
                String[] splitw = inputw.split(" ");
                if (splitw.length != 2) { //Assure that the length when split is still 2 characters
                    throw new IllegalArgumentException("Please enter exactly two numbers separated by a space.");
                }
                wRow = Integer.parseInt(splitw[0]);
                wColumn = Integer.parseInt(splitw[1]);

                if (wRow < 0 || wRow >= rows || wColumn < 0 || wColumn >= columns) { // Make sure that the value input is within the grid
                    throw new IllegalArgumentException("Please enter valid row and column numbers within the grid.");
                }

                validInputW = true;
            } catch (Exception e) { //Catch statement
                System.out.println(e.getMessage());
                System.out.println("Please enter valid row and column numbers.");
            }
        }

        tileSet[wRow][wColumn] = "W ";

        printTileset(tileSet);

        System.out.println("What row and column to place the Asprin (in format '1 1')");

        boolean validInputA = false;
        int aRow = -1;
        int aColumn = -1;

        while (!validInputA) { // While to make sure input is done
            try {
                String inputa = scanner.nextLine();
                String[] splita = inputa.split(" ");
                if (splita.length != 2) { // Assure that the length when split is still 2 characters
                    throw new IllegalArgumentException("Please enter exactly two numbers separated by a space.");
                }
                aRow = Integer.parseInt(splita[0]);
                aColumn = Integer.parseInt(splita[1]);

                if (aRow < 0 || aRow >= rows || aColumn < 0 || aColumn >= columns) { // Make sure that the value input is within the grid
                    throw new IllegalArgumentException("Please enter valid row and column numbers within the grid.");
                }
                if (aRow == wRow && aColumn == wColumn) { // Assures that the asprin is not the same location as the water
                    throw new IllegalArgumentException("Please enter a different row and column than the water.");
                }

                validInputA = true;
            } catch (Exception e) { //Catch statement
                System.out.println(e.getMessage());
                System.out.println("Please enter valid row and column numbers.");
            }
        }

        tileSet[aRow][aColumn] = "A ";

        printTileset(tileSet);

        scanner.close();
        
        return tileSet;
    }

    public static void printTileset(String[][] tiles){
        /*
         * 
         * Function for printing the tileset
         * Takes in: tileSet in 2D Array format with strings
         * Returns: void
         * 
         */


        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles[0].length; j++){
                System.out.print(tiles[i][j]);
            }
            System.out.println();
        }
    }

    public static String[][] placeCockroach(String[][] tileSet){
        /*
         * 
         * Function for placing cockroach in middle of tileset
         * Takes in: tileSet in 2D Array format with strings
         * Returns: tileSet in 2D Array format with strings with cockroach placed
         * 
         */

         /*
          * 
          * Still needs: Logic for if the water or the asprin is placed in the spot that the cockroach goes in
          * Should be basic if statement built into the loop
          * 
          */

        Double halfRow = Math.floor(tileSet.length / 2);
        Double halfCol = Math.floor(tileSet[0].length / 2);

        System.out.println("Half Row: " + halfRow);
        System.out.println("Half Col: " + halfCol);

        tileSet[halfRow.intValue()][halfCol.intValue()] = "C ";

        return tileSet;
    }

    public static void moveCockroach(String[][] tileSet){
        /*
         * 
         * Function for moving the cockroach in the tileset
         * Takes in: tileSet in 2D Array format with strings
         * Returns: 
         * 
         */

        for(int i = 0; i < tileSet.length; i++){
            for(int j = 0; j < tileSet[0].length; j++){
                if(tileSet[i][j].equals("C ")){
        
                    /*
                     * 
                     * Logic for moving the cockroach
                     * Random number generator done 1 --> 3
                     * 1 means move left or up
                     * 2 means stay in that row or column
                     * 3 means move right or down
                     * 
                     */

                    System.out.println("Cockroach found at: " + i + " " + j);
                }
            }
        }
    }

}