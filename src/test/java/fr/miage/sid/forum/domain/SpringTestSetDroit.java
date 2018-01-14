/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.sid.forum.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTestSetDroit {

  private User writeUser, readUser, allUser;
  private HasPermissions project;

  @Before
  public void create() {
    writeUser = new User().setId(1L);
    readUser = new User().setId(2L);
    allUser = new User().setId(3L);

    project = new Project().setName("M2-SID").setCreator(writeUser);
  }

  @Test
  public void readerCanRead() {
    project.givePermission(readUser, Permission.READ);
    assertThat(project.canRead(readUser)).isTrue();
  }

  @Test
  public void writerCanWrite() {
    project.givePermission(writeUser, Permission.WRITE);
    assertThat(project.canWrite(writeUser)).isTrue();
  }

  @Test
  public void allUserCanWriteAndRead() {
    project.givePermission(allUser, Permission.ALL);
    assertThat(project.canRead(allUser)).isTrue();
    assertThat(project.canWrite(allUser)).isTrue();
  }

  @Test
  public void userCanNoLongerUsePermissionPrivilegesAfterRevoking() {
    project.givePermission(readUser, Permission.READ);
    project.removePermission(readUser, Permission.READ);
    assertThat(project.canRead(readUser)).isFalse();
  }

  @Test
  public void revokingInexistantPermissionWorks() {
    project.removePermission(readUser, Permission.READ);
    assertThat(project.getReaders().size()).isEqualTo(0);
  }

//
//  @Test(expected = PermissionTopicException.class)
//  public void testCreateTopicWithNoPermission() throws Exception {
//    project.givePermission(writeUser, Permission.READ);
//    topic.setCreator(writeUser);
//    project.addTopic(topic);
//  }
//
//  @Test(expected = PermissionPostException.class)
//  public void testCreatePostWithNoPermission() throws Exception {
//    topic.givePermission(writeUser, Permission.READ);
//    Post tmp = new Post();
//    tmp.setUser(writeUser);
//    topic.addPost(tmp);
//  }
//
//  @Test(expected = Test.None.class)
//  public void testCreateTopicWithPermission() throws Exception {
//    project.givePermission(writeUser, Permission.WRITE);
//    topic.setCreator(writeUser);
//    project.addTopic(topic);
//  }
//
//  @Test(expected = Test.None.class)
//  public void testCreatePostWithPermission() throws Exception {
//    topic.givePermission(writeUser, Permission.WRITE);
//    Post tmp = new Post();
//    tmp.setUser(writeUser);
//    topic.addPost(tmp);
//  }
}
