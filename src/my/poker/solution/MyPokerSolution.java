/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.poker.solution;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author shrey
 */
public class MyPokerSolution {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            File file = new File(args[0]);    //creates a new file instance from the argument 
            FileReader fr = new FileReader(file);   //reads the file  
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
            StringBuffer sb = new StringBuffer();    //constructs a string buffer with no characters  
            String line;
            int player1Hands = 0, player2Hands = 0; //how many hands each player won 
            int winner = 0;//who is the winner of the each round.
            while ((line = br.readLine()) != null) {    //readd till null
                winner = checkWinner(line);    //each line will be considered as one round consisting 10 cards and winner will be decided based on that
                if (winner == 1) {
                    player1Hands++;
                } else {
                    player2Hands++;
                }
            }
            fr.close();    //closes the stream and release the resources 
            System.out.println("Player 1:" + player1Hands);
            System.out.println("Player 2:" + player2Hands);

        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO code application logic here
    }

    private static int checkWinner(String line) {
        int rankPlayer1 = 0, rankPlayer2 = 0;
        String[] cards = line.split(" ", 0);
        /*Test purpose in case you need to test your functionalities for each case
        List<String> player1 = new ArrayList<>(Arrays.asList("TH","JH","AH","QH","KH"));  
       List<String> player2 = new ArrayList<>(Arrays.asList("6C","3S","7S","QS","QD")); */
        List<String> player1 = new ArrayList<>();
        List<String> player2 = new ArrayList<>();
        int i = 0;
        for (String a : cards) {
            if (i < 5) {        //first 5 cards for Player 1
                player1.add(a);
            } else {
                player2.add(a);
            }
            i++;

        }
        player1 = Arrays.asList(sortCards(player1));   //Sorting the cards based on their value.
        player2 = Arrays.asList(sortCards(player2));    //For player 2
        rankPlayer1 = getRank(player1);                 //Get the score/Rank for player 1's hand

        rankPlayer2 = getRank(player2);                 //Get the score/Rank for player 1's hand

        if (rankPlayer1 > rankPlayer2) {                   //If score on of 1 is greater returning 1 else 2
            return 1;
        }
        if (rankPlayer1 < rankPlayer2) {
            return 2;
        }
        if (rankPlayer1 == rankPlayer2) {               //in case theres tie.
            return tieBreaker(player1, player2);        //The card with higher value will win.
        }
        return 0;
    }

    private static int getRank(List<String> player1) {
        int rank;           //giving rank/score
        /*
        Scoring is as follow:
        1*High card                     High card                  Highest value card
        2000+high card                  Pair                    Two cards of same value
        3000+pair with high card        Two pairs                  Two different pairs
        4000 + Value of the kind        Three of a kind            Three cards of the same value
        5000                            Straight                   All five cards in consecutive value order
        6000                            Flush                      All five cards having the same suit
        7000+value of 3 of a kind       Full house                 Three of a kind and a Pair
        8000 +value of 4 of a kind      Four of a kind             Four cards of the same value
        9000 Straight flush             All five cards in consecutive value order, with the same suit
        10000 Royal Flush               Ten, Jack, Queen, King and Ace in the same suit
         */

        if (StraightFlush(player1) && player1.get(0).charAt(0) == 'T') { //if its straight flush and first card is 10.
            return 10000;
        }
        if (StraightFlush(player1)) {                           //if its a straight flush
            return 9000;
        }
        if ((rank = fourOfKind(player1)) > 8000) {
            return rank;
        }
        if ((rank = fullHouse(player1)) > 7000) {
            return rank;
        }
        if (flush(player1)) {
            return 6000;
        }
        if (straight(player1)) {
            return 5000;
        }
        if ((rank = threeOfKind(player1)) > 4000) {
            return rank;
        }
        if ((rank = twoPairs(player1)) > 3000) {
            return rank;
        }
        if ((rank = twoOfKind(player1)) > 2000) {
            return rank;
        }

        return cardValue(player1.get(4));           //return the card value of the last card.

    }

    private static boolean flush(List<String> player1) {
        //if Cards are in same suit. Hence checking the second character of each hand.
        return player1.get(0).charAt(1) == player1.get(1).charAt(1) && player1.get(0).charAt(1) == player1.get(2).charAt(1) && player1.get(0).charAt(1) == player1.get(3).charAt(1) && player1.get(0).charAt(1) == player1.get(4).charAt(1);
    }

    private static boolean StraightFlush(List<String> player1) {
        return flush(player1) && straight(player1);
    }

    private static int cardValue(String get) {
        //Getting the card value for 10,Jack=11,Queen=12,King=13 and Ace=14.
        if (Character.isDigit(get.charAt(0))) {
            return Character.getNumericValue(get.charAt(0));
        } else {
            if (get.charAt(0) == 'T') {
                return 10;
            }
            if (get.charAt(0) == 'J') {
                return 11;
            }
            if (get.charAt(0) == 'Q') {
                return 12;
            }
            if (get.charAt(0) == 'K') {
                return 13;
            }
        }
        return 14;

    }
//Here i have applied inserstion sort to sort the array based on the card values.
    private static String[] sortCards(List<String> player1) {
        String[] arr = new String[player1.size()];
        arr = player1.toArray(arr);
        int n = arr.length;

        // One by one move boundary of unsorted subarray 
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in unsorted array 
            int min_idx = i;
            for (int j = i + 1; j < n; j++) {
                if (cardValue(arr[j]) < cardValue(arr[min_idx])) {
                    min_idx = j;
                }
            }

            // Swap the found minimum element with the first 
            // element 
            String temp = arr[min_idx];
            arr[min_idx] = arr[i];
            arr[i] = temp;
        }
        return arr;
    }

    
    private static int fourOfKind(List<String> player) {
        for (int i = 0; i < player.size(); i++) {
            int countOfKind = 0, value = 0;
            for (int j = 0; j < player.size(); j++) {
                if (cardValue(player.get(i)) == cardValue(player.get(j))) {
                    countOfKind++;
                    value = cardValue(player.get(j));
                }
            }
            if (countOfKind >= 4) { //if we get any 4 of a kind return with the value of the card +8000
                return 8000 + value;
            }
        }
        return 0;
    }

    private static boolean straight(List<String> player1) {
        return cardValue(player1.get(0)) == cardValue(player1.get(1)) - 1 && cardValue(player1.get(1)) == cardValue(player1.get(2)) - 1 && cardValue(player1.get(2)) == cardValue(player1.get(3)) - 1 && cardValue(player1.get(3)) == cardValue(player1.get(4)) - 1;
        //If all the five numbers are consecutive
    }

    private static int threeOfKind(List<String> player) {
        for (int i = 0; i < player.size(); i++) {
            int countOfKind = 0, value = 0;
            for (int j = 0; j < player.size(); j++) {
                if (cardValue(player.get(i)) == cardValue(player.get(j))) {
                    countOfKind++;
                    value = cardValue(player.get(j));
                }
            }
            if (countOfKind == 3) { //if we get three of a kind return 3000+the value
                return 4000 + value;
            }
        }
        return 0;
    }

    private static int twoOfKind(List<String> player) {
        for (int i = 0; i < player.size(); i++) {
            int countOfKind = 0, value = 0;
            for (int j = 0; j < player.size(); j++) {
                if (cardValue(player.get(i)) == cardValue(player.get(j))) {
                    countOfKind++;
                    value = cardValue(player.get(j));
                }
            }
            if (countOfKind == 2) {//if we get two of a kind return 3000+the value
                return 2000 + value;
            }
        }
        return 0;
    }

    private static int twoPairs(List<String> player) {
        int pair = 0, highValue = 0;
        for (int i = 0; i < player.size(); i++) {
            int countOfKind = 0, value = 0;
            for (int j = 0; j < player.size(); j++) {
                if (cardValue(player.get(i)) == cardValue(player.get(j))) {
                    countOfKind++;
                    value = cardValue(player.get(j));
                }
            }
            if (countOfKind == 2) {
                pair++;
                if (value > highValue) {
                    highValue = value;
                }
            }
        }
        if (pair == 4) {            //this is 4 because each pair will be iterated twice. 
            return 3000 + highValue;
        }
        return 0;
    }

    private static int fullHouse(List<String> player) {
        int pair = 0, highValue = 0, threePair = 0;
        for (int i = 0; i < player.size(); i++) {
            int countOfKind = 0, value = 0;
            for (int j = 0; j < player.size(); j++) {
                if (cardValue(player.get(i)) == cardValue(player.get(j))) {
                    countOfKind++;
                    value = cardValue(player.get(j));
                }
            }
            if (countOfKind == 2) { //if we get a pair of two we increment the  pair
                pair++;

            }
            if (countOfKind == 3) { //if we get a pair of three we increment the three pair
                threePair++;
                highValue = value;
            }
        }
        if (pair == 2 && threePair == 3) { //because if there are 3 elements the conidtion will be true for 3 times.
            return 7000 + highValue;
        }
        return 0;
        
    }

    private static int tieBreaker(List<String> player1, List<String> player2) {
        for (int i = 4; i >= 0; i--) { //loop in reverse and break the tie ( Higher card wins)
            if (cardValue(player1.get(i)) > cardValue(player2.get(i))) {
                return 1;
            }
            if (cardValue(player1.get(i)) < cardValue(player2.get(i))) {
                return 2;
            }

        }
        return 0;
    }
}
