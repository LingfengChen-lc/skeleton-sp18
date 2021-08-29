package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.List;
import java.util.ArrayList;

import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 60;
    TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
    private static final int SEED = 23452;   // TOD: get user input/keyboard input
    private static final Random RANDOM = new Random(SEED);
    List<Room> ROOMS = new ArrayList<>();
    List<Hallway> HALLWAYS = new ArrayList<>();

    /**
     * Position on the map which contains a x coordinate and y coordinate
     */
    public static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean isValid() {
            return 0 <= x && x < WIDTH && 0 <= y && y < HEIGHT;
        }
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {

    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
//        if (input.length())
//        if (input.charAt(0) == '')
        ter.initialize(WIDTH, HEIGHT);
//        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                finalWorldFrame[i][j] = Tileset.NOTHING;
            }
        }
        int roomNum = RandomUtils.uniform(RANDOM, 1, 10);
        int hallwayNum = RandomUtils.uniform(RANDOM, roomNum - 1, 20);

        Room currentRoom = drawRandomRoom();
        while (ROOMS.size() < roomNum || HALLWAYS.size() < hallwayNum) {
            int roomPos = currentRoom.roomPosition();
            System.out.println(roomPos);
            int direction = switch (roomPos) {
                case 0: yield RandomUtils.bernoulli(RANDOM) ? 1 : 2;
                case 1: yield RandomUtils.bernoulli(RANDOM) ? 2 : 3;
                case 2: yield RandomUtils.bernoulli(RANDOM) ? 0 : 3;
                case 3: yield RandomUtils.bernoulli(RANDOM) ? 0 : 1;
                default: throw new IllegalArgumentException("Unexpected argument" + roomPos);
            };

            int len = RandomUtils.uniform(RANDOM, 4, 20);         // length of hallway
//            if (!hallwayValid(currentRoom, direction, len)) {
//                continue;
//            }
            int[] hallwayEndIndex = currentRoom.spawnHallway(direction, len);
            currentRoom = drawRandomRoom(hallwayEndIndex[0], hallwayEndIndex[1]);
        }

        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }


    /**
     * The room class defines a self-enclosed space which ensures to have at least one hallway connecting to other rooms
     */
    public class Room {
        public Position pos;
        public int width, height;

        public Room (Position pos, int width, int height) {
            this.pos = pos;
            this.width = width;
            this.height = height;
            drawRoom();
        }

        private void drawRoom() {
            int x = pos.x, y = pos.y;
            for (int i = x; i < x + width; i++) {
                for (int j = y; j < y + height; j++) {
                    if (i == x || j == y || i == x + width - 1 || j == y + height - 1) {
                        if (finalWorldFrame[i][j] == Tileset.WALL) {
                            finalWorldFrame[i][j] = Tileset.FLOOR;;
                        } else if (finalWorldFrame[i][j] != Tileset.FLOOR) {
                            finalWorldFrame[i][j] = Tileset.WALL;
                        }
                    } else {
                        if (finalWorldFrame[i][j] != Tileset.WALL) {
                            finalWorldFrame[i][j] = Tileset.FLOOR;
                        }
                    }
                }
            }
        }

        /**
         * tells if this room is valid (bounded by WIDTH and HEIGHT)
         * @return boolean valid or not valid
         */
        private boolean isValid() {
            return pos.isValid() && new Position(pos.x + width, pos.y + height).isValid();
        }

        /**
         * indicate the position of the room
         * @return 0: upper left, 1: upper right, 2: lower right, 3: lower left
         */
        public int roomPosition() {
            int x = pos.x, y = pos.y;
            int i = x + width / 2, j = y + height / 2;
            if (i <= WIDTH / 2) {
                if (j >= HEIGHT / 2) {
                    return 0;
                } else {
                    return 3;
                }
            } else {
                if (j >= HEIGHT / 2) {
                    return 1;
                } else {
                    return 2;
                }
            }
        }

        /**
         * spawn a hallway connect with this room with specified direction and hallway length
         * @param direction 0: up, 1: right, 2: down, 3: left
         * @param len is the hallway length
         * @return the end index of the hallway
         */
        private int[] spawnHallway(int direction, int len) {
            int i = 0, j = 0;
            int x = pos.x, y = pos.y;
            if (RandomUtils.bernoulli(RANDOM)) {   // hallway on the left/right side
                i = RandomUtils.bernoulli(RANDOM) ? x : x + width;
                j = RandomUtils.uniform(RANDOM, y, y + height);
            } else {  // hallway on the top or bottom side
                i = RandomUtils.uniform(RANDOM, x, x + width);
                j = RandomUtils.bernoulli(RANDOM) ? y : y + height;
            }
            assert(x <= i && i <= x + width);
            assert(y <= j && j <= y + height);
//            assert hallwayValid(i, j, direction, len);

            switch (direction) {
                case 0:
                    HALLWAYS.add(new Hallway(new Position(i, j), 3, len));
                    return new int[]{i, j + len};
                case 1:
                    HALLWAYS.add(new Hallway(new Position(i, j), len, 3));
                    return new int[]{i + len, j};
                case 2:
                    HALLWAYS.add(new Hallway(new Position(i, j - len), 3, len));
                    return new int[]{i, j - len};
                case 3:
                    HALLWAYS.add(new Hallway(new Position(i - len, j), len, 3));
                    return new int[]{i - len, j};
                default: return null;
            }
        }
    }

    /**
     * draw a random room in the world and return the room
     * @return the room object if valid, null if not valid
     */
    public Room drawRandomRoom() {
        int i = RandomUtils.uniform(RANDOM, 0, WIDTH);
        int j = RandomUtils.uniform(RANDOM, 0, HEIGHT);
        int width = RandomUtils.uniform(RANDOM, 1, 10);
        int height = RandomUtils.uniform(RANDOM, 1, 10);
        Room room = new Room(new Position(i, j), width, height);
        if (room.isValid()) {
            ROOMS.add(room);
            return room;
        }
        return null;
    }

    /**
     * draw a random room in the world x index i and y index j, with random width and length
     * @param i: x axis
     * @param j: y axis
     * @return room object if it is valid, null if it is not valid
     */
    public Room drawRandomRoom(int i, int j) {
        int width = RandomUtils.uniform(RANDOM, 0, WIDTH - i);
        int height = RandomUtils.uniform(RANDOM, 0, HEIGHT - j);
//        int offsetX = RandomUtils.uniform(RANDOM, 0, Math.min(width, i));  // I > offsetX
//        int offsetY = RandomUtils.uniform(RANDOM, 0, Math.min(height, j));  // j > offsetY
//        ROOMS.add(new Room(i - offsetX, j - offsetY, width, height));
        Room room = new Room(new Position(i, j), width, height);
        if (room.isValid()) {
            ROOMS.add(room);
            return room;
        }
        return null;
    }


//    public boolean hallwayValid (Room room, int direction, int len) {
//        int i = room.pos.x;
//        int j = room.pos.y;
//        switch (direction) {
//            case 0: return j + len <= HEIGHT;
//            case 1: return i + len <= WIDTH;
//            case 2: return j - len >= 0;
//            case 3: return i - len >= 0;
//            default: return false;
//        }
//    }

    /**
     * This Hallway class is designed to avoid building WALLS inside rooms
     */
    public class Hallway {
        Position pos;
        int width, height;

        public Hallway(Position pos, int width, int height) {
            assert(width == 3 || height == 3);
            this.pos = pos;
            this.width = width;
            this.height = height;
            addWay();
        }

        private void addWay() {
            int x = pos.x, y = pos.y;
            for (int i = x; i < x + width; i++) {
                for (int j = y; j < y + height; j++) {
                    if (width == 3) {
                        if (i == x + 1) {
                            finalWorldFrame[i][j] = finalWorldFrame[i][j] == Tileset.NOTHING && (j == y || j == y + height - 1) ? Tileset.WALL : Tileset.FLOOR;
                        } else if (finalWorldFrame[i][j] != Tileset.FLOOR) {
                            finalWorldFrame[i][j] = Tileset.WALL;
                        }
                    } else if (height == 3) {
                        if (j == y + 1) {
                            finalWorldFrame[i][j] = finalWorldFrame[i][j] == Tileset.NOTHING && (i == x || j == x + width - 1) ? Tileset.WALL : Tileset.FLOOR;
                        } else if (finalWorldFrame[i][j] != Tileset.FLOOR) {
                            finalWorldFrame[i][j] = Tileset.WALL;
                        }
                    }
                }
            }
        }

        /**
         * tells if the hallway is valid or not
         * @return true or false
         */
        public boolean isValid() {
            return pos.isValid() && pos.x + width < WIDTH && pos.y + height < HEIGHT;
        }
    }
}
