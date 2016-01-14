package org.codehaus.plexus.archiver.sevenz;



import java.io.File;
import java.nio.file.Files;

import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.BasePlexusArchiverTest;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
public class SevenZUnArchiverTest  extends BasePlexusArchiverTest{

	public void testUnArchive()
	        throws Exception
	    {
	        ZipArchiver zipArchiver = (ZipArchiver) lookup( Archiver.ROLE, "zip" );
	        zipArchiver.addDirectory( getTestFile( "src" ) );
	        zipArchiver.setDestFile( getTestFile( "target/output/archiveFor7z.zip" ) );
	        zipArchiver.createArchive();
	        SevenZArchiver archiver = (SevenZArchiver) lookup( Archiver.ROLE, "7z" );
	        String[] inputFiles = new String[ 1 ];
	        inputFiles[ 0 ] = "archiveFor7z.zip";
	        archiver.addDirectory( getTestFile( "target/output" ), inputFiles, null );
	        archiver.setDestFile( getTestFile( "target/output/archive.7z" ) );
	        archiver.createArchive();
	        
	        SevenZUnArchiver unArchiver =  new SevenZUnArchiver();
	        File dest = getTestFile("target/output/unarchive");
	        Files.createDirectories(dest.toPath());
	        unArchiver.setDestDirectory(dest);
	        unArchiver.setSourceFile(getTestFile( "target/output/archive.7z"));
	        unArchiver.execute();
	    }


}
