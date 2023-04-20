package ch.uzh.ifi.hase.soprafs23.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileLifespanEntity;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileLifespanRepository;
import ch.uzh.ifi.hase.soprafs23.repository.ProfileRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.Profile.ProfileLifespanDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;

@Service
@Primary
@Transactional
public class ProfileLifespanService {

    private final ProfileLifespanRepository profileLifespanRepository;
    private ProfileRepository profileRepository;

    public ProfileLifespanService(
            @Qualifier("profileLifespanRepository") ProfileLifespanRepository profileLifespanRepository,
            ProfileRepository profileRepository) {
        this.profileLifespanRepository = profileLifespanRepository;
        this.profileRepository = profileRepository;
    }

    public List<ProfileLifespanEntity> getAllLifespansByProfileId(UUID id) {
        return this.profileLifespanRepository.findByProfileId(id);
    }

    public void updateProfileLifespans(UUID profileId, List<ProfileLifespanDTO> lifespanDTOs) {

        List<ProfileLifespanEntity> entities = this.profileLifespanRepository.findByProfileId(profileId);
        entities.forEach((entity) -> this.profileLifespanRepository.deleteById(entity.getId()));

        for (ProfileLifespanDTO lifespanDTO : lifespanDTOs) {
            ProfileLifespanEntity profileLifespanEntity = DTOMapper.INSTANCE
                    .convertLifespanDTOToLifespanEntity(lifespanDTO);
            ProfileEntity profileEntity = profileRepository.findById(profileId).get();
            profileLifespanEntity.setProfile(profileEntity);
            profileLifespanRepository.save(profileLifespanEntity);
        }
    }
}
