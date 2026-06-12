package com.filmnema.filmnema_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.filmnema.filmnema_api.model.Season;

@Repository
public class SeasonRepository implements ISeasonRepository {

    @Override
    public String findSeasonTitleById(int seasonNumber) {        
        return null;
    }

    @Override
    public Optional<Season> findSeasonById(int seasonNumber) {
        return Optional.empty();
    }

    @Override
    public List<Season> findAllSeasons() {
        return List.of();
    }

    @Override
    public void saveSeason(Season season) {
        throw new UnsupportedOperationException("Unimplemented method 'saveSeason'");
    }
}
