package com.filmnema.filmnema_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.filmnema.filmnema_api.exception.FilmnemaException;
import com.filmnema.filmnema_api.model.Season;
import com.filmnema.filmnema_api.repository.ISeasonRepository;

@ExtendWith(MockitoExtension.class)
class SeasonServiceTest {

    @Mock
    private ISeasonRepository seasonRepository;

    @InjectMocks
    private SeasonService seasonService;

    @Test
    void getSeasonTitleById_returnsTitleFromRepository() {
        int seasonNumber = 1;
        String expectedTitle = "Season 1";

        when(seasonRepository.findSeasonTitleById(seasonNumber)).thenReturn(expectedTitle);

        String result = seasonService.getSeasonTitleById(seasonNumber);

        assertEquals(expectedTitle, result);
        verify(seasonRepository).findSeasonTitleById(seasonNumber);
    }

    @Test
    void getSeasonById_returnsSeasonFromRepository() {
        int seasonNumber = 1;
        Season expectedSeason = new Season(1L, null, null, "Original Title", null, null, seasonNumber, "Season 1", 1L);

        when(seasonRepository.findSeasonById(seasonNumber)).thenReturn(Optional.of(expectedSeason));

        Optional<Season> result = seasonService.getSeasonById(seasonNumber);

        assertTrue(result.isPresent());
        assertEquals(Optional.of(expectedSeason), result);
        verify(seasonRepository).findSeasonById(seasonNumber);
    }

    @Test
    void getSeasonById_returnsSeasonEmptyWhenNotFound() {
        int seasonNumber = 1;

        when(seasonRepository.findSeasonById(seasonNumber)).thenReturn(Optional.empty());

        Optional<Season> result = seasonService.getSeasonById(seasonNumber);

        assertFalse(result.isPresent());
        assertEquals(Optional.empty(), result);
        verify(seasonRepository).findSeasonById(seasonNumber);
    }


    @Test
    void getAllSeasons_returnsSeasonsFromRepository() {
        List<Season> expectedSeasons = List.of(
            new Season(1L, null, null, "Original Title 1", null, null, 1, "Season 1", 1L),
            new Season(2L, null, null, "Original Title 2", null, null, 2, "Season 2", 1L)
        );

        when(seasonRepository.findAllSeasons()).thenReturn(expectedSeasons);

        List<Season> result = seasonService.getAllSeasons();
        
        assertEquals(expectedSeasons, result);
        verify(seasonRepository).findAllSeasons();
    }

    @Test
    void saveSeason_callsRepositorySave() {
        Season season = new Season(1L, null, null, "Original Title", null, null, 1, "Season 1", 1L);

        doNothing().when(seasonRepository).saveSeason(season);
        
        seasonService.saveSeason(season);

        verify(seasonRepository).saveSeason(season);
    }

    @Test
    void saveSeason_throwsExceptionWhenTvShowIdIsNull() {
        Season season = new Season(1L, null, null, "Original Title", null, null, 1, "Season 1", null);

        assertThrows(FilmnemaException.class, () -> seasonService.saveSeason(season));

        verify(seasonRepository, never()).saveSeason(season);
    }


    @Test
    void saveSeason_throwsExceptionWithWhenInvalid() {
        Season season = new Season(1L, null, null, "Original Title", null, null, 1, "Season 1", null);

        doAnswer(invocation -> {
            throw new FilmnemaException("Season must be associated with a TV show");
        }).when(seasonRepository).saveSeason(season);

        assertThrows(FilmnemaException.class, () -> seasonService.saveSeason(season));

        verify(seasonRepository, never()).saveSeason(season);
    }

}