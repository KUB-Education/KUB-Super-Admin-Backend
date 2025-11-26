package education.kub.backend.ce.infrastructure.components.study_field;

import education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity;
import education.kub.backend.ce.domain.educational_program.mapper.EducationalProgramMapper;
import education.kub.backend.ce.domain.educational_program.repository.EducationalProgramRepository;
import education.kub.backend.ce.domain.educational_program.service.EducationalProgramService;
import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import education.kub.backend.ce.domain.specialty.mapper.SpecialtyMapper;
import education.kub.backend.ce.domain.specialty.repository.SpecialtyRepository;
import education.kub.backend.ce.domain.study_field.entity.StudyFieldEntity;
import education.kub.backend.ce.domain.study_field.mapper.StudyFieldMapper;
import education.kub.backend.ce.domain.study_field.repository.StudyFieldRepository;
import education.kub.backend.ce.infrastructure.properties.study_field.EducationalProgramProperties;
import education.kub.backend.ce.infrastructure.properties.study_field.SpecialtyProperties;
import education.kub.backend.ce.infrastructure.properties.study_field.StudyFieldProperties;
import education.kub.backend.ce.infrastructure.providers.mocks.repositories.StudyFieldRepositoryMockProvider;
import education.kub.backend.ce.infrastructure.providers.mocks.repositories.SpecialtyRepositoryMockProvider;
import education.kub.backend.ce.infrastructure.providers.mocks.repositories.EducationalProgramRepositoryMockProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudyFieldComponent {
    @Autowired
    public StudyFieldMapper studyFieldMapper;
    @Autowired
    public SpecialtyMapper specialtyMapper;
    @Autowired
    public EducationalProgramMapper educationalProgramMapper;

    @Autowired
    public StudyFieldRepository studyFieldRepo;
    @Autowired
    public SpecialtyRepository specialtyRepo;
    @Autowired
    public EducationalProgramRepository educationalProgramRepo;

    public void initRepos() {
        studyFieldRepo.deleteAll();
        specialtyRepo.deleteAll();
        educationalProgramRepo.deleteAll();
    }

    public void mockRepos() {
        studyFieldRepo = StudyFieldRepositoryMockProvider.createStudyFieldRepositoryMock();
        specialtyRepo = SpecialtyRepositoryMockProvider.createSpecialtyRepositoryMock();
        educationalProgramRepo = EducationalProgramRepositoryMockProvider.createEducationalProgramRepositoryMock();
    }

    public StudyFieldEntity saveStudyField(StudyFieldProperties studyFieldData) {
        StudyFieldEntity study_field = createStudyFieldEntity(studyFieldData);
        studyFieldRepo.save(study_field);
        return study_field;
    }

    public void saveSpecialty(StudyFieldProperties studyFieldData, SpecialtyProperties specialtyData) {
        saveSpecialty(studyFieldRepo.findById(studyFieldData.getId()).get(), specialtyData);
    }

    public void saveEducationalProgram(SpecialtyProperties specialtyData, EducationalProgramProperties educationalProgramData) {
        saveEducationalProgram(specialtyRepo.findById(specialtyData.getId()).get(), educationalProgramData);
    }

    private SpecialtyEntity saveSpecialty(StudyFieldEntity study_field, SpecialtyProperties specialtyData) {
        SpecialtyEntity specialty = createSpecialty(specialtyData);
        specialty.setStudyField(study_field);
        specialtyRepo.save(specialty);
        return specialty;
    }

    private EducationalProgramEntity saveEducationalProgram(SpecialtyEntity specialty, EducationalProgramProperties educationalProgramData) {
        EducationalProgramEntity program = createEducationalProgramEntity(educationalProgramData);
        program.setSpecialty(specialty);
        educationalProgramRepo.save(program);
        return program;
    }

    private StudyFieldEntity createStudyFieldEntity(StudyFieldProperties studyFieldData) {
        var study_field = new StudyFieldEntity();
        study_field.setCode(studyFieldData.getCode());
        study_field.setName(studyFieldData.getName());
        return study_field;
    }

    private SpecialtyEntity createSpecialty(SpecialtyProperties specialtyData) {
        var specialty = new SpecialtyEntity();
        specialty.setCode(specialtyData.getCode());
        specialty.setName(specialtyData.getName());
        return specialty;
    }

    private EducationalProgramEntity createEducationalProgramEntity(EducationalProgramProperties educationalProgramData) {
        var program = new EducationalProgramEntity();
        program.setName(educationalProgramData.getName());
        program.setDegreeType(educationalProgramData.getDegree_type());
        program.setStudyForm(educationalProgramData.getStudy_form());
        program.setDuration(educationalProgramData.getDuration());
        return program;
    }

    public EducationalProgramService BuildEducationalProgramService() {
        return new EducationalProgramService(educationalProgramRepo, educationalProgramMapper, specialtyRepo);
    }
}
