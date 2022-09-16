import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BlobTester {
	
	private static Index index;
	
	private static File f, fi, fil, file;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File f = new File("f.txt");
		FileWriter fw = new FileWriter(f);
		fw.write("content");
		fw.close();
		
		File fi = new File("fi.txt");
		FileWriter fwr = new FileWriter(fi);
		fwr.write("some content");
		fwr.close();
		
		File fil = new File("fil.txt");
		FileWriter fwri = new FileWriter(fil);
		fwri.write("othercontent");
		fwri.close();
		
		file = new File("file.txt");
		index = new Index();
		index.initialize();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		file.delete();
		f.delete();
		fi.delete();
		fil.delete();
	}

	@Test
	void testBlob() throws NoSuchAlgorithmException, IOException {
		Blob blob = new Blob("file.txt");
		String Sha = blob.getSha1();
		assertTrue(new File("tester/objects/" + Sha).exists());
	}
	
	@Test
	void testInit() {
		assertTrue(new File("tester/objects").exists() && new File("tester/index.txt").exists());
	}
	
	@Test
	void testIndex() throws Exception {
		index.addBlobs("f.txt");
		Blob b = new Blob("f.text");
		String s = b.getSha1();
		index.addBlobs("fi.txt");
		Blob bl = new Blob("fi.text");
		String sh = bl.getSha1();
		index.addBlobs("fil.txt");
		Blob blo = new Blob("fil.text");
		String sha = blo.getSha1();
		
		File folder = new File("tester/objects");
		ArrayList<String> fileNames = new ArrayList<String>(Arrays.asList(folder.list()));
		if (!fileNames.contains(s)||!fileNames.contains(sh)||!fileNames.contains(sha)) {
			fail("fail");
		}
		
		Scanner scan = new Scanner("tester/index.txt");
		int counter = 0;
		while (scan.hasNextLine()) {
			counter++;
			scan.nextLine();
		}
		scan.close();
		if (counter!=3) {
			fail("fail");
		}
		
		index.removeBlobs("f.txt");
		index.removeBlobs("fi.txt");
		index.removeBlobs("fil.txt");
		
		File fold = new File("tester/objects");
		ArrayList<String> files = new ArrayList<String>(Arrays.asList(fold.list()));
		if (files.contains(s)||files.contains(sh)||files.contains(sha)) {
			fail("fail");
		}
		
		Scanner scanner = new Scanner("tester/index.txt");
		int count = 0;
		while (scanner.hasNextLine()) {
			count++;
			scanner.nextLine();
		}
		scanner.close();
		assertTrue(count==0);
	}

}
