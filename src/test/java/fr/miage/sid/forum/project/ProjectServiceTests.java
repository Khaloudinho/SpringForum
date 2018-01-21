package fr.miage.sid.forum.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.ProjectRepository;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.exception.ProjectNotFoundException;
import fr.miage.sid.forum.service.ProjectService;
import fr.miage.sid.forum.service.ProjectServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
public class ProjectServiceTests {
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Before
    public void setUp() {
        initMocks(this);
        this.projectService = new ProjectServiceImpl(projectRepository);
    }

    @Test
    public void createProjectWithSuccess() {
        Project expected = new Project().setId(123L);
        given(this.projectRepository.save(new Project().setId(123L))).willReturn(expected);

        Project actual = this.projectService.save(new Project().setId(123L));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void inexistantProjectIsNull() {
        assertThat(this.projectService.getOne(anyLong())).isEqualTo(null);
    }

    @Test
    public void getOneTest() {
        Project expected = new Project().setId(1L);
        given(this.projectRepository.getOne(1L)).willReturn(expected);

        Project actual = projectService.getOne(1L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getAllTest() {
        Project projectA = new Project().setId(1L);
        Project projectB = new Project().setId(2L);
        List<Project> projects = Arrays.asList(projectA, projectB);
        given(this.projectRepository.findAll()).willReturn(projects);

        List<Project> actual = projectService.getAllAllowed();

        assertThat(actual.size()).isEqualTo(projects.size());
        assertThat(actual.get(0)).isEqualTo(projects.get(0));
        assertThat(actual.get(1)).isEqualTo(projects.get(1));
    }

    @Test
    public void countProjectsCreatedByUserTest() {
        given(projectRepository.countAllByCreatedBy(any(User.class))).willReturn(3);

        assertThat(projectService.countCreatedByUser(new User())).isEqualTo(3);
    }

}
