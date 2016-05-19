package highscore;

public class Score {
    private long score = 0L;

    /**
     * Adds to the score
     * @param score The score that should be added
     */
    public void addScore(long score){
        if(score >= 0){
            setScore(getScore() + score);
        }
    }

    /**
     * Resets the score
     */
    public void resetScore(){
        score = 0L;
    }

    /**
     * @return The current score the player has
     */
    public long getScore() {
        return score;
    }

    /**
     * @param score The score that this.score should become
     */
    public void setScore(long score) {
        this.score = score;
    }
}
