package com.filmnema.filmnema_api.service;

import java.util.List;
import java.util.Optional;

import com.filmnema.filmnema_api.model.Season;

public interface ISeasonService {
    public String getSeasonTitleById(int seasonNumber);
    public Optional<Season> getSeasonById(int seasonNumber);
    public List<Season> getAllSeasons();
    void saveSeason(Season season);
}
