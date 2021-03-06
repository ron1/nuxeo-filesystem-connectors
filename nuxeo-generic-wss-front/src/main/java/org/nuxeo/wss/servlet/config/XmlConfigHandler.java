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

package org.nuxeo.wss.servlet.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class XmlConfigHandler extends DefaultHandler {

    protected List<FilterBindingConfig> configEntries = null;

    protected FilterBindingConfig currentConfig = null;

    public static final String FILTERMAPPING_TAG = "filtermapping";

    public static final String URL_ATTRIBUTE = "url";

    public static final String REDIRECT_ATTRIBUTE = "redirect";

    public static final String SERVICE_ATTRIBUTE = "service";

    public static final String TYPE_ATTRIBUTE = "type";

    protected static String configName = "wssdefaultbindings-config.xml";

    protected static XmlConfigHandler instance;

    @Override
    public void startElement(String uri, String localName, String name,
            Attributes attributes) throws SAXException {
        if (FILTERMAPPING_TAG.equalsIgnoreCase(name)) {
            synchronized (this) {
            currentConfig = new FilterBindingConfig();

            if (attributes != null) {
                currentConfig.setUrl(getAttributeValue(attributes, URL_ATTRIBUTE));
                currentConfig.setRedirectURL(getAttributeValue(attributes, REDIRECT_ATTRIBUTE));
                currentConfig.setTargetService(getAttributeValue(attributes, SERVICE_ATTRIBUTE));
                currentConfig.setRequestType(getAttributeValue(attributes, TYPE_ATTRIBUTE));
            }
        }
        }
    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        if (FILTERMAPPING_TAG.equalsIgnoreCase(name)) {
            synchronized (this) {
            if (configEntries == null) {
                configEntries = new ArrayList<FilterBindingConfig>();
            }
            configEntries.add(currentConfig);
            currentConfig = null;
        }
    }
    }

    //Sometimes we got java.lang.NullPointerException during attribute value extraction
    private String getAttributeValue(Attributes attributes, String key){
        try {
            return attributes.getValue(key);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getConfigName() {
        return configName;
    }

    public static void setConfigName(String configName) {
        XmlConfigHandler.configName = configName;
    }

    public static void loadConfig() throws Exception {
        loadConfig(getConfigName());
    }

    protected static XmlConfigHandler getInstance() {
        if (instance == null) {
            instance = new XmlConfigHandler();
        }
        return instance;
    }

    public static void loadConfig(String configName) throws Exception {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                configName);

        XMLReader reader;
        reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(getInstance());
        reader.setFeature("http://xml.org/sax/features/namespaces", false);
        reader.setFeature("http://xml.org/sax/features/validation", false);
        reader.parse(new InputSource(in));
    }

    public static List<FilterBindingConfig> getConfigEntries() throws Exception {
        List<FilterBindingConfig> entries = getInstance().getParsedConfigEntries();

        if (entries == null) {
            loadConfig();
            entries = getInstance().getParsedConfigEntries();
        }
        return entries;
    }

    protected List<FilterBindingConfig> getParsedConfigEntries() {
        return configEntries;
    }

}
