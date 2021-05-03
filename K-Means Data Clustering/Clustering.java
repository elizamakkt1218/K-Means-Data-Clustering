/*K-means Clustering:
This program is comprised of several methods: "main", "runApp", "cluster", "strToIntArray", "strToInt", "assign",
"distanceOfTwoPoints", "RanCentroid" and "relocation", and each of them will perform different functions.
*/
//Mak Kwan Ting

import java.util.Scanner;
import java.io.File;
import java.io.*;
import java.util.Random;

public class Clustering {
    public static void main(String[] args) throws Exception {
        new Clustering().runApp(args);
    }

    void runApp(String[] args) throws Exception {
        //initialize the args
        int k = strToInt(args[0]);
        String input = args[1];
        String output = args[2];

        //Read the input file
        File inputFile = new File(input);
        Scanner fileScanner = new Scanner(inputFile);
        //Get total number of the lines
        int totalNum = strToInt(fileScanner.nextLine());
        //Sort the data into "data" Array
        String[] line = new String[totalNum];
        int[][] data = new int[totalNum][3];
        for (int i = 0; i < totalNum; i++) {
            line[i] = fileScanner.nextLine();
            data[i] = strToIntArray(line[i]);
        }

        //Generate random centroids
        int[] centroidX = new int[k];
        int[] centroidY = new int[k];
        int count = 0;
        while (count < k) {
            centroidX = RanCentroid(centroidX);
            centroidY = RanCentroid(centroidY);
            count++;
        }

        //Sort the data into X,Y Coordinates
        int[] dataX = new int[totalNum];
        int[] dataY = new int[totalNum];
        for (int i = 0; i < data.length; i++) {
            dataX[i] = data[i][0];
            dataY[i] = data[i][1];
        }

        //when "cluster" method return the value "true", keep executing the method
        while(cluster(centroidX, centroidY, dataX, dataY, data, k)){
        }

        //print the value to the output file
        PrintWriter out = new PrintWriter(output);
        out.println(totalNum);

        for (int i = 0; i < k; i++){
            out.println(centroidX[i] + ", " + centroidY[i] );
        }

        for (int i = 0; i < dataX.length; i++){
            out.print(dataX[i] + ", " + dataY[i] + ", " + data[i][2] + "\n");
        }

        fileScanner.close();
        out.close();
    }

    //Return the value "true" when the cluster assigned to the data points have changed, otherwise return "false" value
    //Parameters: X/Y coordinate of centroids and data points, value of k
    boolean cluster(int[] centroidX, int[] centroidY, int[] dataX, int[] dataY, int[][] data, int k){
        boolean clusterChange = false;

        //Calculate the distance between the data point and the centroids
        int[] cluster = assign(centroidX, centroidY, dataX, dataY);
        //if the cluster assigned to each data point is different from the previous assignment, change the cluster
        //and return the value "true"
        for (int i = 0; i < data.length; i++){
            if (data[i][2] != cluster[i]) {
                data[i][2] = cluster[i];
                clusterChange = true;
            }
        }

        relocation(data, k, dataX, centroidX);
        relocation(data, k, dataY, centroidY);

        return clusterChange;
    }

    //Converting String into Integer Array
    int[] strToIntArray(String line) {
        String token = "";
        int[] result = new int[3];
        int index = 0;
        for (int pos = 0; pos < line.length(); pos++) {
            char c = line.charAt(pos);

            if (c != ' ' && c != ',') {
                token += c;
            } else if (c == ','){
                //num[0] = 123
                int num = strToInt(token);
                token = "";
                result[index] = num;
                index++;
            }
        }
        result[index] = strToInt(token);
        return result;
    }

    //Converting String into Integer
    int strToInt(String token){
        int result = 0;
        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            result = result * 10 + (c - '0');
        }
        return result;
    }

    //Calculate the closest centroid for all the data points
    //Parameters: X/Y coordinate of centroids and data points
    int[] assign(int[] centroidX, int[] centroidY, int[] dataX, int[] dataY) {
        int[] mins = new int[dataX.length];

        for (int i = 0; i < dataX.length; i++) {
            int min = 0;
            for (int j = 1; j < centroidX.length; j++) {
                //pDistance refers to the previous calculated distance between the data point and centroids
                //cDistance refers to the current distance between the data point and centroids
                int pDistance = distanceOfTwoPoints(dataX[i], centroidX[min], dataY[i], centroidY[min]);
                int cDistance = distanceOfTwoPoints(dataX[i], centroidX[j], dataY[i], centroidY[j]);

                if (cDistance < pDistance) {
                    min = j;
                }
            }
            mins[i] = min;
        }

        return mins;
    }

    //Calculate the distance of two points
    //Parameters: X, Y coordinates of data points and X, Y coordinates of the centroids
    int distanceOfTwoPoints(int x1, int x2, int y1, int y2){
        int dx = x1 - x2;
        int dy = y1 - y2;

        return dx * dx + dy * dy;
    }

    //Generate random centroids for initial setup
    //Return the random value of the centroid
    int[] RanCentroid(int[] centroidXY) {
        Random centroid = new Random();

        for (int i = 0; i < centroidXY.length; i++) {
            centroidXY[i] = centroid.nextInt(760);
        }
        return centroidXY;
    }
    
    //relocate the centroids
    //parameters: data Array, dataX/Y, centroidX/Y
    void relocation(int[][] data, int k, int[] dataXY, int[] centroid) {
        int[] sum = new int[k];
        int[] count = new int[k];

        for (int j = 0; j < k; j++) {
            for (int i = 0; i < data.length; i++) {
                if (data[i][2] == j) {

                    sum[j] += dataXY[i];

                    count[j]++;
                }
            }
        }

        for (int i = 0; i < k; i++) {
            if (count[i] != 0) {
                centroid[i] = (sum[i] / count[i]);
            }
        }
    }
}
