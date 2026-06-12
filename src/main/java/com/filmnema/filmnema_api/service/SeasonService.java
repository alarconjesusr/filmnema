package com.filmnema.filmnema_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.filmnema.filmnema_api.exception.FilmnemaException;
import com.filmnema.filmnema_api.model.Season;
import com.filmnema.filmnema_api.repository.ISeasonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeasonService implements ISeasonService {

    private final ISeasonRepository seasonRepository;

    @Override
    public String getSeasonTitleById(int seasonNumber) {
        return this.seasonRepository.findSeasonTitleById(seasonNumber);
    }

    @Override
    public Optional<Season> getSeasonById(int seasonNumber) {
        return this.seasonRepository.findSeasonById(seasonNumber);
    }

    @Override
    public List<Season> getAllSeasons() {
        return this.seasonRepository.findAllSeasons();
    }

    @Override
    public void saveSeason(Season season) {
        if(season.tvShowId() == null) {
            throw new FilmnemaException("Season must be associated with a TV show");
        }        

        this.seasonRepository.saveSeason(season);
    }
}
