package org.codehaus.plexus.archiver.sevenz;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

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

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.codehaus.plexus.archiver.AbstractUnArchiver;
import org.codehaus.plexus.archiver.ArchiverException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import static org.codehaus.plexus.archiver.util.Streams.bufferedInputStream;
import static org.codehaus.plexus.archiver.util.Streams.bufferedOutputStream;
import static org.codehaus.plexus.archiver.util.Streams.copyFully;
import static org.codehaus.plexus.archiver.util.Streams.fileInputStream;
import static org.codehaus.plexus.archiver.util.Streams.fileOutputStream;

/**
 * @author <a href="mailto:evenisse@codehaus.org">Emmanuel Venisse</a>
 * @version $Revision$ $Date$
 */
public class SevenZUnArchiver
    extends AbstractUnArchiver
{

    public SevenZUnArchiver()
    {
    }

    public SevenZUnArchiver( File sourceFile )
    {
        super( sourceFile );
    }

    protected void execute()
        throws ArchiverException
    {
    	File source=getSourceFile();
    	File dest=this.getDestDirectory();
        if ( source.lastModified() > dest.lastModified() )
        {
        	try{
	        	SevenZFile sevenZFile = new SevenZFile(source);
	        	SevenZArchiveEntry entry;
	        	FileOutputStream fos;
	        	byte[] bytes=new byte[1024];
	        	int read;
	        	long offset=0;
	        	long size;
	        	while((entry= sevenZFile.getNextEntry())!=null){
	        		fos=new FileOutputStream(Paths.get(dest.getAbsolutePath(), entry.getName()).toFile());
	        		size=entry.getSize();
	        		do{
	        			read=sevenZFile.read(bytes);
	        			if(read>0)
	        				fos.write(bytes, 0,read);
	        		}while(size>offset && read>0);
	        		fos.close();
	        	}
	        	sevenZFile.close();
        	}catch(IOException ioe){
        		throw new ArchiverException( "Problem unzipping 7z file "+source.getAbsolutePath()+" to "+dest.getAbsolutePath(), ioe );
        	}
        }
    }

    
    protected void execute( String path, File outputDirectory )
    {
        throw new UnsupportedOperationException( "Targeted extraction not supported in 7Z format." );
    }
}
