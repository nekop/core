/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.resources;

import java.util.Hashtable;
import java.util.Map.Entry;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

import org.jboss.weld.Container;
import org.jboss.weld.bootstrap.api.helpers.RegistrySingletonProvider;
import org.jboss.weld.bootstrap.spi.BeanDeploymentArchive;
import org.jboss.weld.logging.BeanManagerLogger;
import org.jboss.weld.manager.BeanManagerImpl;

public class ManagerObjectFactory implements ObjectFactory {
    private final String contextId;

    public ManagerObjectFactory() {
        this(RegistrySingletonProvider.STATIC_INSTANCE);
    }

    public ManagerObjectFactory(String contextId) {
        this.contextId = contextId;
    }

    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        if (Container.available(contextId)) {
            for (Entry<BeanDeploymentArchive, BeanManagerImpl> entry : Container.instance(contextId).beanDeploymentArchives().entrySet()) {
                if (entry.getKey().getId().equals("flat")) {
                    return entry.getValue().getCurrent();
                }
            }
            throw BeanManagerLogger.LOG.cannotLocateBeanManager();
        } else {
            throw BeanManagerLogger.LOG.cannotLocateBeanManager();
        }
    }

}
