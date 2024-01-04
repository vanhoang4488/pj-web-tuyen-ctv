package vanhoang.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vanhoang.project.convertor.TagConvertor;
import vanhoang.project.dto.TagDTO;
import vanhoang.project.entity.TagEntity;
import vanhoang.project.repository.TagRepository;
import vanhoang.project.service.base.AbstractService;
import vanhoang.project.service.base.BaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService extends AbstractService<TagDTO, TagEntity> implements BaseService {
    private final TagRepository tagRepository;

    public List<TagDTO> findAll() {
        List<TagEntity> tagEntities = tagRepository.findAll();
        List<TagDTO> tagDTOs = new ArrayList<>();
        for (TagEntity tagEntity : tagEntities) {
            tagDTOs.add(this.convertToDTO(tagEntity, TagConvertor.class));
        }
        return tagDTOs;
    }

    public TagDTO findTagById (String id) {
        Optional<TagEntity> optionalTagDTO = tagRepository.findById(id);
        return this.convertToDTO(optionalTagDTO, TagConvertor.class);
    }
}
