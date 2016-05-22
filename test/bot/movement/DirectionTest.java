package bot.movement; /**
 * Created by tj on 5/12/16.
 */

import org.junit.Test;
import static org.junit.Assert.*;

public class DirectionTest {
  @Test
  public void test(){
    Direction dir = Direction.EAST;
    assertEquals(dir, Direction.EAST);
  }
}
