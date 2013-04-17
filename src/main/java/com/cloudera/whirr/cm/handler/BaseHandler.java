/**
 * Licensed to Cloudera, Inc. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Cloudera, Inc. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloudera.whirr.cm.handler;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.whirr.ClusterSpec;
import org.apache.whirr.service.ClusterActionEvent;
import org.apache.whirr.service.ClusterActionHandlerSupport;

import com.cloudera.whirr.cm.CmConstants;

public abstract class BaseHandler extends ClusterActionHandlerSupport implements CmConstants {

  // jclouds allows '-', CM does not, CM allows '_', jclouds does not, so lets restrict to alphanumeric
  private static final Pattern CM_CLUSTER_NAME_REGEX = Pattern.compile("[A-Za-z0-9]+");

  protected static final String CONFIG_IMPORT_PATH = "functions/cmf/";

  @Override
  protected void beforeBootstrap(ClusterActionEvent event) throws IOException, InterruptedException {
    super.beforeBootstrap(event);
    if (!CM_CLUSTER_NAME_REGEX.matcher(
        event.getClusterSpec().getConfiguration()
            .getString(ClusterSpec.Property.CLUSTER_NAME.getConfigName(), CONFIG_WHIRR_NAME_DEFAULT)).matches()) {
      throw new IOException("Illegal cluster name ["
          + event.getClusterSpec().getConfiguration()
              .getString(ClusterSpec.Property.CLUSTER_NAME.getConfigName(), CONFIG_WHIRR_NAME_DEFAULT)
          + "] passed in variable [" + ClusterSpec.Property.CLUSTER_NAME.getConfigName() + "] with default ["
          + CONFIG_WHIRR_NAME_DEFAULT + "]. Please use only alphanumeric characters.");
    }
  }

}