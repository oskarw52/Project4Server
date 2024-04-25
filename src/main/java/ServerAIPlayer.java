import java.util.Random;

public class ServerAIPlayer {
    private ShipBoard aiBoard;
    private Random rand = new Random();

    public ServerAIPlayer() {
        this.aiBoard = new ShipBoard();
        placeShipsRandomly();
    }

    private void placeShipsRandomly() {
        for (Ship ship : aiBoard.ships) {
            boolean placed = false;
            while (!placed) {
                // Random start point
                int rowStart = rand.nextInt(aiBoard.board.length);
                int colStart = rand.nextInt(aiBoard.board[0].length);

                // Random end point based on ship size
                boolean horizontal = rand.nextBoolean();
                int rowEnd = horizontal ? rowStart : rowStart + ship.size - 1;
                int colEnd = horizontal ? colStart + ship.size - 1 : colStart;

                // Ensure the ship doesn't go out of bounds
                if (isValidPlacement(rowStart, colStart, rowEnd, colEnd, horizontal)) {
                    int placementResult = aiBoard.placeShip(ship, rowStart, colStart, rowEnd, colEnd);
                    placed = (placementResult == 1);
                }
            }
        }
    }

    // Method to validate ship placement
    private boolean isValidPlacement(int rowStart, int colStart, int rowEnd, int colEnd, boolean horizontal) {
        if (horizontal) {
            if (colEnd >= aiBoard.board[0].length) return false;
        } else {
            if (rowEnd >= aiBoard.board.length) return false;
        }

        // Check if another ship is already placed in any of the intended positions
        for (int row = rowStart; horizontal ? row == rowStart : row <= rowEnd; row++) {
            for (int col = colStart; horizontal ? col <= colEnd : col == colStart; col++) {
                if (aiBoard.board[row][col] != null) return false;
            }
        }
        return true;
    }

    public GameInfo makeAIMove() {
        GameInfo aiMove = new GameInfo();
        int row, col;
        do {
            row = rand.nextInt(aiBoard.board.length);
            col = rand.nextInt(aiBoard.board[0].length);
        } while (aiBoard.board[row][col] != null && aiBoard.board[row][col].isDestroyed()); // Check if the tile is already destroyed

        // Determine the effect of the move
        if (aiBoard.board[row][col] != null) {
            aiMove.hitShip = true;
            aiMove.hitShipRow = row;
            aiMove.hitShipCol = col;
            // Further handling to check if the ship is destroyed etc.
        } else {
            aiMove.hitShip = false;
        }
        return aiMove;
    }
}

