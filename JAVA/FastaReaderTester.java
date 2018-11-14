import java.io.*;

class FastaReaderTester {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("java FastaReaderTester <fasta.gz>");
			System.exit(0);
		}
		String fastaFile = args[0];
		long baseCnt = FastaReader.readFasta(fastaFile);
		System.out.println(baseCnt);
	}
}
