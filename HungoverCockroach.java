import java.util.Scanner;
import java.util.List;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import java.time.Instant;
import java.time.Duration;

public class HungoverCockroach {
    private static final Random random = new Random();

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        String[][] tileSet = buildTileset();
        placeCockroach(tileSet);

        int[] aLoc = findItemLoc(tileSet, "A ");
        int[] wLoc = findItemLoc(tileSet, "W ");
        int[] cStart = findItemLoc(tileSet, "C ");

        printTileset(tileSet);

        int numSim = -1;
        boolean validInput = false;
        
        while (!validInput) {
            try {
                System.out.println("Enter number of simulations you would like to run: ");
                String input = s.nextLine();
                numSim = Integer.parseInt(input);
                
                if (numSim < 1) {
                    throw new IllegalArgumentException("Please enter a number greater than 0 for simulations.");
                }
                
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer for the number of simulations.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("\nEnter the name and location of the output file: ");
        String file = s.nextLine();

        Instant start = Instant.now();

        List<SimulationResult> results = runSimulations(tileSet, aLoc, wLoc, cStart, numSim);
        
        Instant end = Instant.now();
        Duration time = Duration.between(start, end);

        double averageMoves = results.stream().mapToInt(r -> r.moves.size()).average().orElse(0);

        try (PrintWriter w = new PrintWriter(new FileWriter(file))) {
            w.println("Simulation Results");
            w.println(" ");
            w.println("Number of Simulations: " + numSim);
            w.println(" ");
            w.println("Time taken: " + time.toMillis() + " milliseconds");
            w.println(" ");
            w.println("Asprin Location: " + Arrays.toString(aLoc));
            w.println(" ");
            w.println("Water Location: " + Arrays.toString(wLoc));
            w.println(" ");
            w.println("Avergae Moves: " + averageMoves);
            w.println(" ");
            
            for (int i = 0; i<results.size(); i++) {
                SimulationResult result = results.get(i);
                w.println("Simulation: " + (i + 1));
                w.println("Moves: " + result.moves.size());
                w.println("Path: " + result.moves);
                w.println("Found Aspirin: " + result.foundAspirin);
                w.println("Found Water: " + result.foundWater);
                w.println(" ");
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

        s.close();
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
        System.out.println(" ");
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

    public static int[] findItemLoc(String[][] tileSet, String item){
        for (int i = 0; i < tileSet.length; i++){
            for (int j = 0; j < tileSet[0].length; j++){
                if (tileSet[i][j].equals(item)){
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public static List<SimulationResult> runSimulations(String[][] tileSet, int[] aLoc, int[] wLoc, int[] cStart, int numSim){
        List<SimulationResult> results = new ArrayList<>();

        for (int i = 0; i < numSim; i++){
            SimulationResult result = runSim(tileSet, aLoc, wLoc, cStart);
            results.add(result);
        }

        return results;
    }

    public static SimulationResult runSim(String[][] tileSet, int[] aLoc, int[] wLoc, int[] cStart) {
        boolean[][] visit = new boolean[tileSet.length][tileSet[0].length];
        int[] cPos = Arrays.copyOf(cStart, 2);
        List<String> moves = new ArrayList<>();
        boolean gotAsprin = false;
        boolean gotWater = false;

        while (!(gotAsprin && gotWater) && !allTilesVisited(visit, tileSet)){
            visit[cPos[0]][cPos[1]] = true;

            if (Arrays.equals(cPos, aLoc)) {
                gotAsprin = true;
            } 
            if (Arrays.equals(cPos, wLoc)) {
                gotWater = true;
            }

            if (gotAsprin && gotWater) {
                break;
            }

            if (allTilesVisited(visit, tileSet)) {
                break;
            }

            int[] nextPos = getNextPos(cPos, tileSet.length, tileSet[0].length);
            String dir = getDir(cPos, nextPos);

            moves.add(dir);
            cPos = nextPos;

        }

        if (!gotAsprin || !gotWater) {
        }

        return new SimulationResult(moves, gotAsprin, gotWater);
    }

    public static int[] getNextPos(int[] cPos, int rows, int col){
        int[][] dirs = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        int[] nextPos;

        do{
            int[] dir = dirs[random.nextInt(dirs.length)];
            nextPos = new int[]{cPos[0] + dir[0], cPos[1] + dir[1]};
        } while (nextPos[0] < 0 || nextPos[0] >= rows || nextPos[1] < 0 || nextPos[1] >= col);

        return nextPos;
    }

    public static String getDir(int[] from, int[] to){
        int rowDiff = to[0] - from[0];
        int colDiff = to[1] - from[1];

        if (rowDiff == -1 && colDiff == -1){
            return "NW";
        } else if (rowDiff == -1 && colDiff == 0){
            return "N";
        } else if (rowDiff == -1 && colDiff == 1){
            return "NE";
        } else if (rowDiff == 0 && colDiff == -1){
            return "W";
        } else if (rowDiff == 0 && colDiff == 1){
            return "E";
        } else if (rowDiff == 1 && colDiff == -1){
            return "SW";
        } else if (rowDiff == 1 && colDiff == 0){
            return "S";
        } else if (rowDiff == 1 && colDiff == 1){
            return "SE";
        } else {
            return "";
        }
    }

    public static boolean allTilesVisited(boolean[][] visit, String[][] tileSet){
        for (int i = 0; i < visit.length; i++){
            for (int j = 0; j < visit[0].length; j++){
                if (!visit[i][j] && !tileSet[i][j].equals("A ") && !tileSet[i][j].equals("W ")){
                    return false;
                }
            }
        }
        return true;
    }

    private static class SimulationResult {
        List<String> moves;
        boolean foundAspirin;
        boolean foundWater;
        
        SimulationResult(List<String> moves, boolean foundAspirin, boolean foundWater) {
            this.moves = moves;
            this.foundAspirin = foundAspirin;
            this.foundWater = foundWater;
        }
    }

}