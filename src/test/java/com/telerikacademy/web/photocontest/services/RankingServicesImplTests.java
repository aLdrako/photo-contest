package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.exceptions.EntityNotFoundException;
import com.telerikacademy.web.photocontest.models.Ranking;
import com.telerikacademy.web.photocontest.models.Ranks;
import com.telerikacademy.web.photocontest.repositories.contracts.RankingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static com.telerikacademy.web.photocontest.helpers.Helpers.createMockRanking;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class RankingServicesImplTests {
    @Mock
    RankingRepository mockRepository;
    @InjectMocks
    RankingServicesImpl services;



    @Test
    public void getJunkie_Should_ReturnRanking_When_RankingAlreadyExists() {
        // Arrange
        Ranking mockRanking = createMockRanking(Ranks.JUNKIE);

        Mockito.when(mockRepository.getByName(anyString()))
                .thenReturn(mockRanking);

        // Act
        Ranking ranking = services.getJunkie();

        // Assert
        Assertions.assertEquals(ranking, mockRanking);
    }
    @Test
    public void getJunkie_Should_ReturnRanking_When_RankingDoesNotExists() {
        // Arrange
        Ranking mockRanking = createMockRanking(Ranks.JUNKIE);

        Mockito.when(mockRepository.getByName(anyString()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.save(any())).thenReturn(mockRanking);
        // Act
        Ranking ranking = services.getJunkie();

        // Assert
        Assertions.assertEquals(ranking.getName(), mockRanking.getName());
    }

    @Test
    public void getEnthusiast_Should_ReturnRanking_When_RankingAlreadyExists() {
        // Arrange
        Ranking mockRanking = createMockRanking(Ranks.ENTHUSIAST);

        Mockito.when(mockRepository.getByName(anyString()))
                .thenReturn(mockRanking);

        // Act
        Ranking ranking = services.getEnthusiast();

        // Assert
        Assertions.assertEquals(ranking, mockRanking);
    }
    @Test
    public void getEnthusiast_Should_ReturnRanking_When_RankingDoesNotExists() {
        // Arrange
        Ranking mockRanking = createMockRanking(Ranks.ENTHUSIAST);

        Mockito.when(mockRepository.getByName(anyString()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.save(any())).thenReturn(mockRanking);
        // Act
        Ranking ranking = services.getEnthusiast();

        // Assert
        Assertions.assertEquals(ranking.getName(), mockRanking.getName());
    }
    @Test
    public void getMaster_Should_ReturnRanking_When_RankingAlreadyExists() {
        // Arrange
        Ranking mockRanking = createMockRanking(Ranks.MASTER);

        Mockito.when(mockRepository.getByName(anyString()))
                .thenReturn(mockRanking);

        // Act
        Ranking ranking = services.getMaster();

        // Assert
        Assertions.assertEquals(ranking, mockRanking);
    }
    @Test
    public void getMaster_Should_ReturnRanking_When_RankingDoesNotExists() {
        // Arrange
        Ranking mockRanking = createMockRanking(Ranks.MASTER);

        Mockito.when(mockRepository.getByName(anyString()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.save(any())).thenReturn(mockRanking);
        // Act
        Ranking ranking = services.getMaster();

        // Assert
        Assertions.assertEquals(ranking.getName(), mockRanking.getName());
    }
    @Test
    public void getWiseAndBenevolentPhotoDictator_Should_ReturnRanking_When_RankingAlreadyExists() {
        // Arrange
        Ranking mockRanking = createMockRanking(Ranks.WISE_AND_BENEVOLENT_PHOTO_DICTATOR);

        Mockito.when(mockRepository.getByName(anyString()))
                .thenReturn(mockRanking);

        // Act
        Ranking ranking = services.getWiseAndBenevolentPhotoDictator();

        // Assert
        Assertions.assertEquals(ranking, mockRanking);
    }
    @Test
    public void getWiseAndBenevolentPhotoDictator_Should_ReturnRanking_When_RankingDoesNotExists() {
        // Arrange
        Ranking mockRanking = createMockRanking(Ranks.WISE_AND_BENEVOLENT_PHOTO_DICTATOR);

        Mockito.when(mockRepository.getByName(anyString()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.save(any())).thenReturn(mockRanking);
        // Act
        Ranking ranking = services.getWiseAndBenevolentPhotoDictator();

        // Assert
        Assertions.assertEquals(ranking.getName(), mockRanking.getName());
    }


}
