/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.spring.samples.security.shared.operations;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.samples.security.shared.Sections;
import org.vaadin.spring.samples.security.shared.backend.MyBackend;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 * An operation that invokes a backend method that is available for admin users only. The operation is, however,
 * always visible in the side bar to demonstrate that the security checks on the backend layer are working.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringComponent
@SideBarItem(sectionId = Sections.OPERATIONS, caption = "Admin operation", order = 1)
@FontAwesomeIcon(FontAwesome.WRENCH)
public class AdminOperation implements Runnable {

    private final MyBackend backend;

    @Autowired
    public AdminOperation(MyBackend backend) {
        this.backend = backend;
    }

    @Override
    public void run() {
        Notification.show(backend.adminOnlyEcho("Hello Admin World"));
    }
}
