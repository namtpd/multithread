package nthecoder.multithread.powerball;

import java.util.*;

public class OneThread {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        findWinOdd("02 05 13 17 25 11");
        long end = System.currentTimeMillis();
        System.out.println("How long: " + (end - start));
    }

    /**
     * @param targetNum This is for scenario in which a person
     *                  only selects fixed numbers all times
     */
    private static void findWinOdd(String targetNum) {
        long i = 0;
        while (true){
            String machinePick = genPowerBallNumbers();
            String winningNum = genPowerBallNumbers();
            System.out.println(i++ + ": Your selection: " + machinePick + " - Winning Numbers: "+ winningNum);
            if (winningNum.equals(machinePick)) {
                System.out.println("Found a win ticket with random selection!");
                return;
            }
            if (winningNum.equals(targetNum)) {
                System.out.println("Found a win ticket with fixed selection! - " + targetNum);
                return;
            }
        }
    }

    private static String genPowerBallNumbers() {
        Set<Integer> firstFive = new HashSet<>();
        int jackpot;
        String powerBallStr;
        Random r = new Random();

        //Select a jackbox number from 1 to 26
        jackpot = r.nextInt(26) + 1;

        //Select 5 numbers from 1 to 69
        while (firstFive.size() < 5) {
            firstFive.add(r.nextInt(69) + 1);
        }

        //Building PowerBall string
        StringBuilder pbBuilder = new StringBuilder();
        powerBallStr = firstFive.stream()
                .sorted()
                .map(e -> String.format("%02d", e))
                .reduce((a, b) -> a + " " + b).get();

        pbBuilder
                .append(powerBallStr)
                .append(" ")
                .append(String.format("%02d", jackpot));

        return pbBuilder.toString();
    }
}
