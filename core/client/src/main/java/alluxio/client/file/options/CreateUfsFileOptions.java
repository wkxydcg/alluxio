/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0
 * (the "License"). You may not use this work except in compliance with the License, which is
 * available at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio.client.file.options;

import alluxio.annotation.PublicApi;
import alluxio.client.ClientContext;
import alluxio.security.authorization.PermissionStatus;
import alluxio.thrift.CreateUfsFileTOptions;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;

import java.io.IOException;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Options for creating a UFS file.
 */
@PublicApi
@NotThreadSafe
public final class CreateUfsFileOptions {
  /** The ufs user this file should be owned by. */
  private final String mUser;
  /** The ufs group this file should be owned by. */
  private final String mGroup;
  /** The ufs permission in Posix string format, such as 0777. */
  private String mPosixPerm;

  /**
   * @return the default {@link CreateUfsFileOptions}
   */
  public static CreateUfsFileOptions defaults() {
    PermissionStatus ps = PermissionStatus.defaults();
    try {
      // Set user and group from user login module.
      ps.setUserFromLoginModule(ClientContext.getConf());
      // TODO(chaomin): set permission depends on the alluxio file.
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
    return new CreateUfsFileOptions(ps.getUserName(), ps.getGroupName(),
        ps.getPermission().toString());
  }

  /**
   * Constructs a {@link CreateUfsFileOptions} with user, group and permission.
   *
   * @param user the user name
   * @param group the group name
   * @param posixPerm the permission in posix string format
   */
  public CreateUfsFileOptions(String user, String group, String posixPerm) {
    mUser = user;
    mGroup = group;
    mPosixPerm = posixPerm;
  }

  /**
   * @return the group which should own the file
   */
  public String getGroup() {
    return mGroup;
  }

  /**
   * @return the user who should own the file
   */
  public String getUser() {
    return mUser;
  }

  /**
   * @return the ufs permission in Posix string format
   */
  public String getPosixPerm() {
    return mPosixPerm;
  }

  /**
   * @return if the group has been set
   */
  public boolean hasGroup() {
    return mGroup != null;
  }

  /**
   * @return if the user has been set
   */
  public boolean hasUser() {
    return mUser != null;
  }

  /**
   * @return if the posixPerm has been set
   */
  public boolean hasPosixPerm() {
    return mPosixPerm != null;
  }

  /**
   * Sets the posix perm.
   *
   * @param posixPerm the perm to set in posix string format
   */
  public void setPosixPerm(String posixPerm) {
    mPosixPerm = posixPerm;
  }

  @Override
  public boolean equals(Object o) {
    return this == o || o instanceof CreateUfsFileOptions;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).toString();
  }

  /**
   * @return Thrift representation of the options
   */
  public CreateUfsFileTOptions toThrift() {
    CreateUfsFileTOptions options = new CreateUfsFileTOptions();
    if (hasGroup()) {
      options.setGroup(mGroup);
    }
    if (hasUser()) {
      options.setUser(mUser);
    }
    if (hasPosixPerm()) {
      options.setPosixPerm(mPosixPerm);
    }
    return options;
  }
}
