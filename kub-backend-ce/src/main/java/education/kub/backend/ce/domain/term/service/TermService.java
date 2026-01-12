package education.kub.backend.ce.domain.term.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.educational_program.repository.EducationalProgramRepository;
import education.kub.backend.ce.domain.term.domain.TermEntity;
import education.kub.backend.ce.domain.term.mapper.TermMapper;
import education.kub.backend.ce.domain.term.model.TermCreateRequest;
import education.kub.backend.ce.domain.term.model.TermDetailsResponse;
import education.kub.backend.ce.domain.term.model.TermUpdateRequest;
import education.kub.backend.ce.domain.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;
    private final EducationalProgramRepository educationalProgramRepository;
    private final TermMapper termMapper;

    public TermDetailsResponse createTerm(TermCreateRequest request) {
        var educationalProgram = educationalProgramRepository.findById(request.educationalProgramId())
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        var term = new TermEntity();
        term.setEducationalProgram(educationalProgram);
        term.setNumber(request.number());

        termRepository.save(term);
        return termMapper.toDetailsResponse(term);
    }

    public List<TermDetailsResponse> getAllTerms() {
        var terms = termRepository.findAll();
        return termMapper.toDetailsResponseList(terms);
    }

    public TermDetailsResponse getTermById(Long id) {
        var term = termRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
        return termMapper.toDetailsResponse(term);
    }

    public TermDetailsResponse updateTerm(Long id, TermUpdateRequest request) {
        var term = termRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        var educationalProgram = educationalProgramRepository.findById(request.educationalProgramId())
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        term.setEducationalProgram(educationalProgram);
        term.setNumber(request.number());

        termRepository.save(term);
        return termMapper.toDetailsResponse(term);
    }

    public void deleteTerm(Long id) {
        if (!termRepository.existsById(id)) {
            throw new KubException(KubException.ErrorCode.NOT_FOUND);
        }
        termRepository.deleteById(id);
    }
}
