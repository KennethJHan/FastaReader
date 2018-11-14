import java.io.*;
import java.util.zip.*;

class FastaReader {
	private static long baseCount = 0;
	private static String s;
	public static long readFasta(String fastaFile) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(fastaFile))));
			while((s = in.readLine())!=null) {
				if (!(s.startsWith(">"))) {
					baseCount += s.trim().length();
				}
			}
		} catch (IOException ex) {
			System.out.println(ex);
		}
		return baseCount;
	}
}
