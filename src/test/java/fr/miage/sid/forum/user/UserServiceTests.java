package fr.miage.sid.forum.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.codehaus.groovy.runtime.DefaultGroovyMethods.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;

import fr.miage.sid.forum.domain.*;
import fr.miage.sid.forum.service.PostService;
import fr.miage.sid.forum.service.PostServiceImpl;
import fr.miage.sid.forum.exception.TopicNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.miage.sid.forum.service.UserService;
import fr.miage.sid.forum.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserServiceTests {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setUp() {
        initMocks(this);
        this.userService = new UserServiceImpl(userRepository, roleRepository, bCryptPasswordEncoder);
    }

    @Test
    public void createUserWithSuccess(){
        User expected = new User().setId(1L).setPassword("toto");
        given(this.bCryptPasswordEncoder.encode(anyString())).willReturn("encoded");
        given(this.userRepository.save(Matchers.any(User.class))).willReturn(expected);
        given(this.roleRepository.findByRole(anyString())).willReturn(new Role());

        User actual = this.userService.save(new User().setId(1L));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void inexistantUserIsNull() {
        assertThat(this.userService.getOne(anyLong())).isEqualTo(null);
    }

    @Test
    public void getOneTest() {
        User expected = new User().setId(1L);
        given(this.userRepository.getOne(1L)).willReturn(expected);

        User actual = userService.getOne(1L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getAllTest() {
        List<User> users = Arrays.asList(new User().setId(1L));
        given(this.userRepository.findAll()).willReturn(users);
        List<User> actual = userService.getAll();

        assertThat(actual).hasSameSizeAs(users);
        assertThat(actual.get(0)).isEqualTo(users.get(0));
    }

    @Test
    public void findByMailTest() {
        User user = new User().setEmail("mail@gmail.com");
        given(this.userRepository.eagerFindByEmail("mail@gmail.com")).willReturn(user);
        User actual = userService.eagerFindByEmail("mail@gmail.com");

        assertThat(user).isEqualTo(actual);
    }

    @Test
    public void getAllProjectReadersTest() {
        Set<User> users = new HashSet<>();
        users.add(new User().setId(1L));
        given(this.userRepository.getAllProjectReaders(2L)).willReturn(users);
        Set<User> actual = userService.getAllProjectReaders(2L);

        assertThat(users).isEqualTo(actual);
    }

    @Test
    public void getAllProjectWritersTest() {
        Set<User> users = new HashSet<>();
        users.add(new User().setId(1L));
        given(this.userRepository.getAllProjectWriters(2L)).willReturn(users);
        Set<User> actual = userService.getAllProjectWriters(2L);

        assertThat(users).isEqualTo(actual);
    }

    @Test
    public void getAllTopicReaders() {
        Set<User> users = new HashSet<>();
        users.add(new User().setId(1L));
        given(this.userRepository.getAllTopicReaders(2L)).willReturn(users);
        Set<User> actual = userService.getAllTopicReaders(2L);

        assertThat(users).isEqualTo(actual);
    }

    @Test
    public void getAllTopicWriters() {
        Set<User> users = new HashSet<>();
        users.add(new User().setId(1L));
        given(this.userRepository.getAllTopicWriters(2L)).willReturn(users);
        Set<User> actual = userService.getAllTopicWriters(2L);

        assertThat(users).isEqualTo(actual);
    }
}
