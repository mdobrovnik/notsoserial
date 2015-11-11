/*
 * Copyright 2015 Kantega AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kantega.notsoserial;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import org.junit.Test;

import javax.xml.transform.TransformerConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class WithoutAgentIT {

    @Test
    public void attachShouldWorkWithNoAgent() throws TransformerConfigurationException, IOException, ClassNotFoundException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {



        byte[] ser = Files.readAllBytes(Paths.get("target").resolve("bytes.ser"));

        try {

            System.setProperty("pwned", "false");
            // Deserializing should flip pwned to true
            deserialize(ser);
        } catch (ClassCastException e) {
            // Ignore this exception, happens after exploit has effect
        }

        assertThat(System.getProperty("pwned"), is("true"));

    }



    private Object deserialize(byte[] ser) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(ser));
        return stream.readObject();
    }

}
