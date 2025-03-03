package service;

import dataaccess.MemoryGameDAO;

public class GameService {

    MemoryGameDAO gameDB = new MemoryGameDAO();

    public void clear() {
    gameDB = new MemoryGameDAO();
    }
}
