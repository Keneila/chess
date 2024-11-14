package model;

import java.util.Collection;
import java.util.HashMap;

public record ListGamesResponse(Collection<GameData> games) {
}
