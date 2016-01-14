package org.codehaus.plexus.archiver.sevenz;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

/**
 *
 * Copyright 2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.util.Compressor;
import org.codehaus.plexus.components.io.resources.PlexusIoResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @version $Revision$ $Date$
 */
public class SevenZCompressor
    extends Compressor
{
    private SevenZOutputFile zOut;
    
    /**
     * perform the BZip2 compression operation.
     */
    public void compress()
        throws ArchiverException
    {
        try
        {
            zOut = new SevenZOutputFile(getDestFile());
            PlexusIoResource source = getSource();
            SevenZArchiveEntry entry=new SevenZArchiveEntry();
            entry.setName(source.getName());
            entry.setDirectory(source.isDirectory());
            zOut.putArchiveEntry(entry);
            
            InputStream inputStream=source.getContents();
            int read = 0;
    		byte[] bytes = new byte[1024];
    		while ((read = inputStream.read(bytes)) != -1) {
    			zOut.write(bytes,0,read);
    		}
            zOut.closeArchiveEntry();
        }
        catch ( IOException ioe )
        {
            String msg = "Problem creating 7z " + ioe.getMessage();
            throw new ArchiverException( msg, ioe );
        }
    }

    public void close()
    {
    	try {
			zOut.close();
		} catch (IOException ioe) {
			String msg = "Problem closing 7z " + ioe.getMessage();
            throw new ArchiverException( msg, ioe );
		}
        zOut = null;
    }
}
