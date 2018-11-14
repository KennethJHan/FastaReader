#!/usr/bin/python

import sys
import gzip

class FastaReader:
    def __init__(self):
        self.fastaFile = ""
        self.count = 0

    def readFasta(self):
        with gzip.open(self.fastaFile,'rb') as fr:
            for line in fr:
                s = line.decode("utf-8").strip()
                if not s.startswith(">"):
                    self.count += len(s)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("#usage: python %s <fasta.gz>" %sys.argv[0])
        sys.exit()
    fastaFile = sys.argv[1]
    fastaReader = FastaReader()
    fastaReader.fastaFile = fastaFile
    fastaReader.readFasta()
    print(fastaReader.count)
