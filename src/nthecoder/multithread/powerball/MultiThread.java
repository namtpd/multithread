package nthecoder.multithread.powerball;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MultiThread {
    public static void main(String[] args) throws InterruptedException {
        final int THREAD_NUM = 4;
        long start = System.currentTimeMillis();
        ThreadKiller threadKiller = new ThreadKiller();
        Thread[] threads = new LotoGenerator[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i++){
            threads[i] = new LotoGenerator("02 05 13 17 25 11", threadKiller);
            threads[i].start();;
        }
        for (int i = 0; i < THREAD_NUM; i++){
            threads[i].join();;
        }
        long end = System.currentTimeMillis();
        System.out.println("finished after " + (end - start) + " ms");
    }

    public static class ThreadKiller {
        private boolean killerStatus = false;

        //this will kill all the threads subscribed.
        public void killThread(LotoGenerator lotoGenerator){
            lotoGenerator.interrupt();
        }

        public void setKillerStatus(boolean killerStatus) {
            this.killerStatus = killerStatus;
        }

        public boolean getKillerStatus() {
            return killerStatus;
        }
    }

    public static class LotoGenerator extends Thread {
        private String targetNum;
        private ThreadKiller threadKiller;

        public LotoGenerator(String targetNum, ThreadKiller threadKiller){
            this.targetNum = targetNum;
            this.threadKiller = threadKiller;
        }

        @Override
        public void run() {
            long i = 0;
            long start = System.currentTimeMillis();
            while (!threadKiller.getKillerStatus()){
                String machinePick = genPowerBallNumbers();
                String winningNum = genPowerBallNumbers();
                System.out.println("Thread name: " + Thread.currentThread().getName() +
                        " - " + i++ + ": Your selection: " + machinePick +
                        " - Winning Numbers: "+ winningNum);
                if (winningNum.equals(machinePick)) {
                    System.out.println(Thread.currentThread().getName() +
                            " - Found a win ticket with random selection " + machinePick);
                    threadKiller.setKillerStatus(true);
                }
                if (winningNum.equals(targetNum)) {
                    System.out.println(Thread.currentThread().getName() +
                            " - Found a win ticket with fixed selection! - " + targetNum);
                    threadKiller.setKillerStatus(true);
                }
            }

            //kill all threads subscribed to ThreadKiller
            if (threadKiller.getKillerStatus()){
                threadKiller.killThread(this);
            }

            //this is to handle an interruption request.
            if (Thread.currentThread().isInterrupted()){
                long end = System.currentTimeMillis();
                System.out.println(String.format("Thread name: %s - Start %d, end %d - Total: %d",
                        Thread.currentThread().getName(), start, end, end - start));
                return;
            }
        }

        private String genPowerBallNumbers() {
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
}
