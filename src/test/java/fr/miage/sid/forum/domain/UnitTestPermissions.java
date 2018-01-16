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
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UnitTestPermissions {

  private User writeUser, readUser, allUser;
  private HasPermissions project;

  @Before
  public void create() {
    writeUser = new User().setId(1L);
    readUser = new User().setId(2L);
    allUser = new User().setId(3L);

    project = new Project().setName("M2-SID");
  }

  @Test
  public void readerCanRead() {
    project.givePermissionTo(readUser.getId(), Permission.READ);
    assertThat(project.canRead(readUser.getId())).isTrue();
  }

  @Test
  public void writerCanWrite() {
    project.givePermissionTo(writeUser.getId(), Permission.WRITE);
    assertThat(project.canWrite(writeUser.getId())).isTrue();
  }

  @Test
  public void allUserCanWriteAndRead() {
    project.givePermissionTo(allUser.getId(), Permission.ALL);
    assertThat(project.canRead(allUser.getId())).isTrue();
    assertThat(project.canWrite(allUser.getId())).isTrue();
  }

  @Test
  public void userCanNoLongerUsePermissionPrivilegesAfterRevoking() {
    project.givePermissionTo(writeUser.getId(), Permission.READ);
    project.givePermissionTo(readUser.getId(), Permission.READ);
    project.removePermissionOf(readUser.getId(), Permission.READ);
    assertThat(project.canRead(readUser.getId())).isFalse();
  }

  @Test
  public void revokingInexistantPermissionWorks() {
    project.removePermissionOf(readUser.getId(), Permission.READ);
    assertThat(project.getReaders().size()).isEqualTo(0);
  }
}
