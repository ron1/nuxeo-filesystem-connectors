/*
 * (C) Copyright 2006-2009 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Thierry Delprat
 */

package org.nuxeo.wss.spi.dws;

import org.nuxeo.wss.spi.WSSListItem;

public class DWSDocument extends AbstractDWSItem {

    protected WSSListItem doc;
    //protected String siteRootPath;

    public DWSDocument(WSSListItem doc, String siteRootPath) {
        super(doc.getEtag(),doc.getAuthor(), doc.getCreationDate(), doc.getModificationDate(),doc.getRelativeFilePath(siteRootPath));
        this.doc = doc;
        //this.siteRootPath = siteRootPath;
    }

    public String getTitle() {
        return doc.getDisplayName();
    }


    public void setId(String id) {
        this.id = id;
    }


    public boolean getFolderish() {
        return doc.isFolderish();
    }

}
