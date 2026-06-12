package com.filmnema.filmnema_api.repository;

import java.util.List;
import java.util.Optional;

import com.filmnema.filmnema_api.model.Season;

public interface ISeasonRepository {
    public String findSeasonTitleById(int seasonNumber);
    public Optional<Season> findSeasonById(int seasonNumber);
    public List<Season> findAllSeasons();
    void saveSeason(Season season);
}
