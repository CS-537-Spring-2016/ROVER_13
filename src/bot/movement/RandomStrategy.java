package bot.movement;

import java.util.Random;

/**
 * Created by tj on 4/19/16.
 */
public class RandomStrategy implements Strategy {

  private Random rng;

  public RandomStrategy(){
    rng = new Random();
  }

  public Direction bestMove(){
    Direction[] directions = Direction.values();
    int choice = rng.nextInt(directions.length);
    return directions[choice];
  }
}
