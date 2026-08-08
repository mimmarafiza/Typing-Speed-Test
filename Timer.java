    public class Timer {
    private long startTime;
    private long elapsedTime = 0;
    private boolean isRunning = false;
    public void start() {
    startTime = System.currentTimeMillis();
    isRunning = true;
    }
    public void stop() {
    if (isRunning) {
    elapsedTime = System.currentTimeMillis() - startTime;
    isRunning = false;
        }
    }
    public long getElapsedTime() {
    return isRunning ? System.currentTimeMillis() - startTime : elapsedTime;
    }
    public double getElapsedSeconds() {
    return getElapsedTime() / 1000.0;
    }
    public void reset() {
    elapsedTime = 0;
    isRunning = false;
    }
    public static void main(String[] args) {
    Timer timer = new Timer();
    timer.start();
        try {
        Thread.sleep(2000);
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        timer.stop();
        System.out.println("Elapsed time: " + timer.getElapsedSeconds() + " seconds");
    }
}